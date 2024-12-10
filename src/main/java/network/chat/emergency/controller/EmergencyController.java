package network.chat.emergency.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import network.chat.emergency.entity.EmergencyCall;
import network.chat.emergency.repository.EmergencyCallRepository;
import network.chat.websocket.handler.ChatEmergencyHandler;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/emergency")
@Tag(name = "Emergency API", description = "비상 호출 관리 API")
public class EmergencyController {
    private final EmergencyCallRepository repository;
    private final ChatEmergencyHandler notificationHandler;

    public EmergencyController(EmergencyCallRepository repository, ChatEmergencyHandler notificationHandler) {
        this.repository = repository;
        this.notificationHandler = notificationHandler;
    }

    @Operation(summary = "비상 호출 처리", description = "환자가 비상 호출 버튼을 눌렀을 때 이를 처리하고 알림을 전송합니다.")
    @PostMapping("/api/call")
    public String emergencyCall(@Parameter(description = "환자의 이름", example = "장지효") @RequestParam String patientName) {
        // 비상 호출 데이터 저장
        EmergencyCall emergencyCall = new EmergencyCall(patientName, LocalDateTime.now());
        repository.save(emergencyCall);

        // 알림 메시지 전송
        String notificationMessage = "지금 " + patientName + " 환자가 비상 호출 버튼을 눌렀으니 확인해주세요!";
        notificationHandler.sendNotification(notificationMessage);

        return notificationMessage;
    }
}
