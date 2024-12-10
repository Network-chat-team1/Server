package network.chat.patient.repository;

import network.chat.doctor.entity.DoctorEntity;
import network.chat.patient.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientEntity,Long> {
    Optional<PatientEntity> findBySessionId(String sessionId);
}
