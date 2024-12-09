package network.chat.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

@Getter
@Builder
@AllArgsConstructor
public class MemberSignupRequest {
    private String loginId;
    private String password;
    private String username;
    private boolean role;

    public MemberSignupRequest() {}

    @Builder
    public Member toEntity(String password) {
        return Member.builder()
                .loginId(this.loginId)
                .password(password)
                .username(this.username)
                .role(this.role)
                .build();
    }

}
