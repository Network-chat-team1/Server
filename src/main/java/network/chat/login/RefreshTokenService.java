package network.chat.login;


import lombok.RequiredArgsConstructor;
import network.chat.exception.CustomException;
import network.chat.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 *  Refresh Token의 생성과 저장, 삭제 등을 책임지는 클래스
 *
 */
@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    @Value("#{'${jwt.refreshTokenValidTime}'.trim()}")
    private long refreshTokenValidTime;
    private final RefreshTokenRepository refreshTokenRepository;

    public String createRefreshToken(String loginId) {
        deleteTokenIfPresent(loginId);

        String newToken = UUID.randomUUID().toString();
        Date expirationDate = new Date(System.currentTimeMillis() + refreshTokenValidTime);

        RefreshToken refreshToken = RefreshToken.buildRefreshToken(newToken, loginId, expirationDate);

        saveRefreshToken(refreshToken);

        return newToken;
    }

    private void deleteTokenIfPresent(String loginId) {
        refreshTokenRepository.findByLoginId(loginId)
                .ifPresent(
                        token -> refreshTokenRepository.deleteByLoginId(loginId)
                );
    }

    private void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public void validateToken(String loginId, String requestToken) {
        RefreshToken refreshToken = loadByLoginId(loginId);

        if (refreshToken.isExpiredToken()) {
            throw new CustomException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        if (refreshToken.equals(requestToken)) {
            return;
        }

        throw new CustomException(ErrorCode.INVALID_TOKEN);
    }

    private RefreshToken loadByLoginId(String loginId) {
        return refreshTokenRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTHENTICATION_FAILED));
    }

    public void deleteToken(String loginId) {
        refreshTokenRepository.deleteByLoginId(loginId);
    }
}
