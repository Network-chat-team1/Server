package network.chat.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
@AllArgsConstructor
public class MemberLoginRequest {

    private String loginId;
    private String password;
    private String username;
    private boolean role;

    public MemberLoginRequest() {}
}
