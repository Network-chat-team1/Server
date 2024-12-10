package network.chat.notification;

import network.chat.websocket.handler.NotificationHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class NotificationConsoleInput implements CommandLineRunner{
    private final NotificationHandler notificationHandler;

    public NotificationConsoleInput(NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("공지 메시지를 입력하세요. 종료하려면 'exit'를 입력하십시오.");

        while (true) {
            System.out.print("공지> ");
            String input = scanner.nextLine();

            if ("exit".equalsIgnoreCase(input)) {
                System.out.println("공지 입력을 종료합니다.");
                break;
            }

            if (!input.trim().isEmpty()) {
                notificationHandler.sendNotification(input);
            } else {
                System.out.println("빈 메시지는 보낼 수 없습니다.");
            }
        }

        scanner.close();
    }
}
