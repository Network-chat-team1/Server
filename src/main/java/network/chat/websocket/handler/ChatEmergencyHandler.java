package network.chat.websocket.handler;
import com.fasterxml.jackson.databind.ObjectMapper;
import network.chat.doctor.entity.DoctorEntity;
import network.chat.doctor.repository.DoctorRepository;
import network.chat.doctorchat.entity.ChatMessage;
import network.chat.doctorchat.repository.ChatMessageRepository;
import network.chat.websocket.ChatSessionManager;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
@Component
public class ChatEmergencyHandler extends TextWebSocketHandler {
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final DoctorRepository doctorRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatSessionManager sessionManager;
    private final ObjectMapper objectMapper;

    public ChatEmergencyHandler(DoctorRepository doctorRepository, ChatMessageRepository chatMessageRepository, ChatSessionManager sessionManager, ObjectMapper objectMapper) {
        this.doctorRepository = doctorRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.sessionManager = sessionManager;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);

        String sessionId = session.getId();
        String query = session.getUri().getQuery();
        String uniqueIdentifier = query.split("=")[1];

        Optional<DoctorEntity> doctorOptional = doctorRepository.findByUniqueIdentifier(uniqueIdentifier);
        if (doctorOptional.isPresent()) {
            DoctorEntity doctor = doctorOptional.get();
            doctor.setSessionId(sessionId);
            doctorRepository.save(doctor);
            sessionManager.addSession(sessionId, doctor.getName());
            System.out.println("의료진 연결됨: " + doctor.getName() + " (" + sessionId + ")");
        } else {
            System.out.println("등록되지 않은 고유번호: " + uniqueIdentifier);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String sessionId = session.getId();
        String senderName = sessionManager.getUsername(sessionId);
        if (senderName == null) {
            senderName = "알 수 없는 사용자";
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(senderName);
        chatMessage.setMessage(message.getPayload());
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessageRepository.save(chatMessage);

        // JSON 형식 메시지 생성
        try {
            String jsonMessage = objectMapper.writeValueAsString(chatMessage);
            System.out.println("브로드캐스트 메시지: " + jsonMessage);

            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(jsonMessage));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        String sessionId = session.getId();
        String username = sessionManager.getUsername(sessionId);

        sessionManager.removeSession(sessionId);
        sessions.remove(session);

        System.out.println("사용자 연결 종료: " + (username != null ? username : "알 수 없는 사용자") + " (" + sessionId + ")");
    }

    public void sendNotification(String notificationMessage) {
        System.out.println("알림 메시지 전송: " + notificationMessage);
        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(notificationMessage));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
