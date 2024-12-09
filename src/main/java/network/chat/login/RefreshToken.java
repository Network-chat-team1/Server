package network.chat.login;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private String loginId;
    private Date expirationDate;

    public static RefreshToken buildRefreshToken(String token, String loginId, Date expirationDate) {
        return RefreshToken.builder()
                .token(token)
                .loginId(loginId)
                .expirationDate(expirationDate)
                .build();
    }

    public boolean isExpiredToken() {
        if (expirationDate.before(new Date())) {
            return true;
        }

        return false;
    }
}
