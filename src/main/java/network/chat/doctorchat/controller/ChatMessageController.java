package network.chat.doctorchat.controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import network.chat.doctorchat.entity.ChatMessage;
import network.chat.doctorchat.repository.ChatMessageRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "DoctorChat API", description = "의료진 채팅 메세지 조회")
public class ChatMessageController {
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageController(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    // 모든 메시지 조회
    @GetMapping
    public List<ChatMessage> getAllMessages() {
        return chatMessageRepository.findAll();
    }
}
