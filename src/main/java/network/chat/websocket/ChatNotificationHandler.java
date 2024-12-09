package network.chat.websocket;
import network.chat.doctor.entity.DoctorEntity;
import network.chat.doctor.repository.DoctorRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CopyOnWriteArrayList;
@Component
public class ChatNotificationHandler extends TextWebSocketHandler {
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final DoctorRepository doctorRepository;

    public ChatNotificationHandler(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);

        // DB에서 이름 조회 및 설정
        String sessionId = session.getId();
        DoctorEntity doctor = doctorRepository.findBySessionId(sessionId).orElse(null);
        if (doctor == null) {
            System.out.println("등록되지 않은 세션: " + sessionId);
        } else {
            System.out.println("의료진 연결됨: " + doctor.getName() + " (" + sessionId + ")");
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String sessionId = session.getId();

        // DB에서 이름 조회
        DoctorEntity doctor = doctorRepository.findBySessionId(sessionId).orElse(null);
        String senderName = (doctor != null) ? doctor.getName() : "알 수 없는 사용자";

        // 이름을 포함한 메시지 생성
        String formattedMessage = senderName + ": " + message.getPayload();
        System.out.println("브로드캐스트 메시지: " + formattedMessage);

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
        sessions.remove(session);
        System.out.println("클라이언트 연결 종료: " + session.getId());
    }

    public void sendNotification(String notificationMessage) {
        System.out.println("알림 메시지 전송: " + notificationMessage);
        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(notificationMessage)); // 알림 메시지를 모든 클라이언트로 전송
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
