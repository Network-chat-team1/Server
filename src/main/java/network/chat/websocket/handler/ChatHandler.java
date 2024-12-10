package network.chat.websocket.handler;
import network.chat.websocket.ChatSessionManager;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;

import java.util.concurrent.CopyOnWriteArrayList;
public class ChatHandler extends TextWebSocketHandler{
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final ChatSessionManager sessionManager;
    public ChatHandler(ChatSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        String sessionId = session.getId();

        // 세션 ID에 매핑된 사용자 이름 가져오기
        String username = sessionManager.getUsername(sessionId);
        if (username != null) {
            System.out.println("사용자 연결됨: " + username + " (" + sessionId + ")");
        } else {
            System.out.println("등록되지 않은 사용자 연결: " + sessionId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String sessionId = session.getId();

        // 사용자 이름 조회
        String username = sessionManager.getUsername(sessionId);
        if (username == null) {
            username = "알 수 없는 사용자";
        }

        // 메시지에 사용자 이름 포함
        String formattedMessage = username + ": " + message.getPayload();
        System.out.println("브로드캐스트 메시지: " + formattedMessage);

        // 모든 클라이언트에 메시지 브로드캐스트
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(formattedMessage));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
    }
}
