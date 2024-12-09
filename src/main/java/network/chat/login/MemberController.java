package network.chat.login;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public Response<TokenDto> login(@RequestBody MemberLoginRequest requestDto) {
        return Response.success("로그인 성공", memberService.login(requestDto));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public Response<MemberRespnseDto> signup(@RequestBody MemberSignupRequest requestDto) {
        return Response.success("회원 가입 성공", memberService.signUp(requestDto));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reissue")
    public Response<TokenDto> reissue(@RequestBody TokenRequest tokenRequest) {
        return Response.success("토큰 재발급 성공", memberService.reissue(tokenRequest));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logout")
    public Response<Void> logout(@RequestBody LogoutRequest logoutRequest) {
        memberService.logout(logoutRequest.getLoginId());
        return Response.success("로그아웃 성공", null);
    }

    @GetMapping("/test")
    public String test() {
        return "통과";
    }



}
