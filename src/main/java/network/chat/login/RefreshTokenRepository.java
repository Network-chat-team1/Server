package network.chat.login;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByLoginId(String loginId);
    @Transactional
    void deleteByLoginId(String loginId);
}
