package network.chat.login;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import network.chat.doctor.entity.DoctorEntity;
import network.chat.doctor.repository.DoctorRepository;
import network.chat.patient.entity.PatientEntity;
import network.chat.patient.repository.PatientRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/login")
@Tag(name = "Login API", description = "사용자 로그인 API")
public class LoginController {
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public LoginController(DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://hospitalchat.netlify.app","https://network-chat.store"})
    @Operation(summary = "사용자 로그인", description = "고유번호와 비밀번호를 사용하여 환자 또는 의료진을 로그인합니다.")
    @GetMapping
    public String login(
            @Parameter(description = "사용자의 고유번호 (의사: D001, 환자: P001)") @RequestParam String uniqueIdentifier,
            @Parameter(description = "사용자의 비밀번호") @RequestParam String password) {

        // 의사인지 확인
        Optional<DoctorEntity> doctor = doctorRepository.findByUniqueIdentifier(uniqueIdentifier);
        if (doctor.isPresent()) {
            if (doctor.get().getPassword().equals(password)) {
                return doctor.get().getName() + "님 환영합니다! (의료진)";
            } else {
                return "비밀번호가 올바르지 않습니다.";
            }
        }

        // 환자인지 확인
        Optional<PatientEntity> patient = patientRepository.findByUniqueIdentifier(uniqueIdentifier);
        if (patient.isPresent()) {
            if (patient.get().getPassword().equals(password)) {
                return patient.get().getName() + "님 환영합니다! (환자)";
            } else {
                return "비밀번호가 올바르지 않습니다.";
            }
        }

        // 둘 다 아니면 실패
        return "로그인 실패: 고유번호를 확인하세요.";
    }
}
