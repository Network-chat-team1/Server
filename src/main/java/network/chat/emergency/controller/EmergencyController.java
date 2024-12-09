package network.chat.emergency.controller;
import network.chat.emergency.entity.EmergencyCall;
import network.chat.emergency.repository.EmergencyCallRepository;
import network.chat.websocket.ChatNotificationHandler;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/emergency")
public class EmergencyController {
    private final EmergencyCallRepository repository;
    private final ChatNotificationHandler notificationHandler;

    public EmergencyController(EmergencyCallRepository repository, ChatNotificationHandler notificationHandler) {
        this.repository = repository;
        this.notificationHandler = notificationHandler;
    }

    @PostMapping("/call")
    public String emergencyCall(@RequestParam String patientName) {
        // 비상 호출 데이터 저장
        EmergencyCall emergencyCall = new EmergencyCall(patientName, LocalDateTime.now());
        repository.save(emergencyCall);

        // 알림 메시지 전송
        String notificationMessage = "지금 " + patientName + " 환자가 비상 호출 버튼을 눌렀으니 확인해주세요!";
        notificationHandler.sendNotification(notificationMessage);

        return "Emergency call received and notification sent.";
    }
}
