package network.chat.websocket.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/websockets")
@Tag(name = "WebSocket API", description = "WebSocket 연결 정보 제공 API")

public class WebSocketController {
    @Operation(
            summary = "의료진 채팅 WebSocket URL= ws://3.39.185.125:8080/ws/doctorchat",
            description = "의료진 간 실시간 채팅을 위한 WebSocket 경로입니다.\n"+
                    "프론트는 ws://3.39.185.125:8080/ws/doctorchat?uniqueIdentifier=${uniqueIdentifier} 이렇게 넘겨주시면 됩니다!"
    )
    @GetMapping("/doctorchat")
    public String getDoctorChatWebSocketInfo() {
        return "WebSocket URL: ws://3.39.185.125:8080/ws/doctorchat";
    }

    @Operation(
            summary = "공지방 WebSocket URL= ws://3.39.185.125:8080/ws/announcements",
            description = "모든 사용자에게 공지사항을 전달하기 위한 WebSocket 경로입니다.\n" +
                        "- 이 WebSocket은 클라이언트가 서버로 건의 메시지를 전송하는 데 사용됩니다.\n" +
                        "- 서버는 수신된 메시지를 처리하거나 저장합니다."
    )
    @GetMapping("/announcements")
    public String getAnnouncementsWebSocketInfo() {
        return "WebSocket URL: ws://3.39.185.125:8080/ws/announcements";
    }

    @Operation(
            summary = "건의방 WebSocket URL= ws://3.39.185.125:8080/ws/suggestions",
            description = "환자가 건의를 전송하기 위한 WebSocket 경로입니다.\n" +
                        "- 이 WebSocket은 클라이언트가 서버로 건의 메시지를 전송하는 데 사용됩니다.\n" +
                        "- 서버는 수신된 메시지를 처리하거나 저장합니다."
    )
    @GetMapping("/suggestions")
    public String getSuggestionsWebSocketInfo() {
        return "WebSocket URL: ws://3.39.185.125:8080/ws/suggestions";
    }
}
