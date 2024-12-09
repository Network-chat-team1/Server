package network.chat.doctorchat.controller;
import network.chat.doctorchat.entity.ChatMessage;
import network.chat.doctorchat.repository.ChatMessageRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatMessageRepository repository;

    public ChatController(ChatMessageRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ChatMessage sendMessage(@RequestBody ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        return repository.save(message);
    }

    @GetMapping
    public List<ChatMessage> getMessages() {
        return repository.findAll();
    }
}
