package network.chat.websocket;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final ChatNotificationHandler chatNotificationHandler;

    public WebSocketConfig(ChatNotificationHandler chatNotificationHandler) {
        this.chatNotificationHandler = chatNotificationHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatNotificationHandler, "/ws/notifications").setAllowedOrigins("*");
    }

}
