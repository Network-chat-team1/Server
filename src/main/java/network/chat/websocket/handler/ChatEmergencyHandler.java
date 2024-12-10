package network.chat.websocket.handler;
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

    public ChatEmergencyHandler(DoctorRepository doctorRepository, ChatMessageRepository chatMessageRepository, ChatSessionManager sessionManager) {
        this.doctorRepository = doctorRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.sessionManager = sessionManager;
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);

        // 생성된 WebSocket 세션 ID 가져오기
        String sessionId = session.getId();

        // URL 쿼리 파라미터에서 고유번호 추출
        String query = session.getUri().getQuery();
        String uniqueIdentifier = query.split("=")[1];

        // 고유번호로 DB에서 사용자 조회
        Optional<DoctorEntity> doctorOptional = doctorRepository.findByUniqueIdentifier(uniqueIdentifier);
        if (doctorOptional.isPresent()) {
            DoctorEntity doctor = doctorOptional.get();
            doctor.setSessionId(sessionId); // DB에 세션 ID 업데이트
            doctorRepository.save(doctor); // 저장
            sessionManager.addSession(sessionId, doctor.getName());
            System.out.println("의료진 연결됨: " + doctor.getName() + " (" + sessionId + ")");
        } else {
            System.out.println("등록되지 않은 고유번호: " + uniqueIdentifier);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String sessionId = session.getId();

        // SessionManager에서 이름 조회
        String senderName = sessionManager.getUsername(sessionId);
        if (senderName == null) {
            senderName = "알 수 없는 사용자";
        }

        // 이름을 포함한 메시지 생성
        String formattedMessage = senderName + ": " + message.getPayload();
        System.out.println("브로드캐스트 메시지: " + formattedMessage);

        // DB에 메시지 저장
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(senderName);
        chatMessage.setMessage(message.getPayload());
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessageRepository.save(chatMessage);

        // 모든 클라이언트에게 브로드캐스트
        for (WebSocketSession s : sessions) {
            try {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(formattedMessage));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        String sessionId = session.getId();
        String username = sessionManager.getUsername(sessionId);

        // 세션 종료 시 SessionManager에서 제거
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
