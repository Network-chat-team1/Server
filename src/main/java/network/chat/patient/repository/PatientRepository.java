package network.chat.patient.repository;
import network.chat.patient.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientEntity, Long>{
    List<PatientEntity> findByDoctorAssigned(String doctorName); // 특정 의사의 환자 목록 조회
    Optional<PatientEntity> findByUniqueIdentifier(String uniqueIdentifier);
}
