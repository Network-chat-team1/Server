package network.chat.websocket.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class NotificationHandler extends TextWebSocketHandler {
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public NotificationHandler() {
        // Background Thread for Console Input
        new Thread(this::handleConsoleInput).start();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        System.out.println("환자 공지방 연결됨: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        sessions.remove(session);
        System.out.println("환자 공지방 연결 종료: " + session.getId());
    }

    public void sendNotification(String message) {
        System.out.println("공지 전송: " + message);
        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage("공지: " + message));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleConsoleInput() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("공지 메시지를 입력하세요. 종료하려면 'exit'를 입력하십시오:");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String input = scanner.nextLine();
                if ("exit".equalsIgnoreCase(input)) {
                    System.out.println("콘솔 입력 종료");
                    break;
                }
                sendNotification(input); // 공지 메시지를 WebSocket으로 전송
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
