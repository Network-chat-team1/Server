package network.chat.websocket;
import network.chat.websocket.handler.ChatEmergencyHandler;
import network.chat.websocket.handler.NotificationHandler;
import network.chat.websocket.handler.SuggestionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final ChatEmergencyHandler chatEmergencyHandler;
    private final NotificationHandler notificationHandler;
    private final SuggestionHandler suggestionHandler;

    public WebSocketConfig(
            ChatEmergencyHandler chatEmergencyHandler,
            NotificationHandler notificationHandler,
            SuggestionHandler suggestionHandler
    ) {
        this.chatEmergencyHandler = chatEmergencyHandler;
        this.notificationHandler = notificationHandler;
        this.suggestionHandler = suggestionHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 의료진 채팅방
        registry.addHandler(chatEmergencyHandler, "/ws/doctorchat").setAllowedOrigins("*");

        // 공지방
        registry.addHandler(notificationHandler, "/ws/announcements").setAllowedOrigins("*");

        // 건의방
        registry.addHandler(suggestionHandler, "/ws/suggestions").setAllowedOrigins("*");
    }
}
