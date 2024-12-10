package network.chat.websocket;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatSessionManager {
    private final ConcurrentHashMap<String, String> sessionUserMap = new ConcurrentHashMap<>();

    // WebSocket ID와 사용자 이름 매핑
    public void addSession(String sessionId, String username) {
        sessionUserMap.put(sessionId, username);
    }

    // WebSocket ID로 사용자 이름 조회
    public String getUsername(String sessionId) {
        return sessionUserMap.get(sessionId);
    }

    // 세션 종료 시 제거
    public void removeSession(String sessionId) {
        sessionUserMap.remove(sessionId);
    }
}
