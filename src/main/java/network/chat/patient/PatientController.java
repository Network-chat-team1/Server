package network.chat.patient;

import lombok.RequiredArgsConstructor;
import network.chat.login.Member;
import network.chat.login.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/patient")
public class PatientController {
    private final PatientService patientService;

    @GetMapping("/list")
    public Response<List<PatientListResponseDto>> getPatients() {

        return Response.success("환자 목록 조회 성공", patientService.getPatients());
    }

    @GetMapping("/{loginId}/info")
    public Response<PatientResponseDto> getPatientInfo(@PathVariable("loginId") String loginId) {

        return Response.success("특정환자 차트열람 성공", patientService.getPatientInfo(loginId));
    }

    @PostMapping("/{loginId}/chart")
    public Response<?> createChart(@PathVariable("loginId") String loginId,
                                   @RequestBody ChartRequestDto requestDto) {
        patientService.createChart(loginId, requestDto);
        return Response.success("차트기록 성공", null);
    }
}
