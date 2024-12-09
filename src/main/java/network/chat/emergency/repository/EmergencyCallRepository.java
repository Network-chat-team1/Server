package network.chat.emergency.repository;

import network.chat.emergency.entity.EmergencyCall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyCallRepository extends JpaRepository<EmergencyCall, Long> {
}
