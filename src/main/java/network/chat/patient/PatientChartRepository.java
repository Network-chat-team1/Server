package network.chat.patient;

import network.chat.login.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientChartRepository extends JpaRepository<PatientChart, Long> {
    PatientChart findByPatient(Member member);
}
