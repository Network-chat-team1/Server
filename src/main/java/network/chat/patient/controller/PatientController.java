package network.chat.patient.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import network.chat.patient.entity.PatientEntity;
import network.chat.patient.repository.PatientRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patient API", description = "환자 관리 API")
public class PatientController {
    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // 모든 환자 목록 조회
    @Operation(summary = "모든 환자 목록 조회", description = "등록된 모든 환자의 데이터를 반환합니다.")
    @GetMapping("/api/patients/list")
    public List<PatientEntity> getAllPatients() {
        return patientRepository.findAll();
    }

    // 특정 의사의 환자 목록 조회
    @Operation(summary = "특정 의사의 환자 목록 조회", description = "특정 의사가 담당하는 환자 목록을 반환합니다.")
    @GetMapping("/api/patients/bydoctor")
    public List<PatientEntity> getPatientsByDoctor(@Parameter(description = "담당 의사의 이름", example = "김의사") @RequestParam String doctorName) {
        return patientRepository.findByDoctorAssigned(doctorName);
    }

    @Operation(summary = "특정 환자 차트 조회", description = "특정 환자의 차트를 조회합니다.")
    // 특정 환자 차트 열람
    @GetMapping("/api/patients/{id}")
    public PatientEntity getPatientById(@Parameter(description = "환자의 ID", example = "1") @PathVariable Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new RuntimeException("환자를 찾을 수 없습니다: " + id));
    }
}
