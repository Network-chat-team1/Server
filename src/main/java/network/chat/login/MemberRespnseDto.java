package network.chat.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class MemberRespnseDto {

    private String loginId;
    private String username;

    public static MemberRespnseDto fromEntity(Member member) {
        return new MemberRespnseDto(member.getLoginId(), member.getUsername());
    }
}
