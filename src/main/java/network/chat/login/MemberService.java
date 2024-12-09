package network.chat.login;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import network.chat.exception.CustomException;
import network.chat.exception.ErrorCode;
import network.chat.jwt.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public TokenDto login(MemberLoginRequest requestDto) {
        // LoginId와 Password를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(requestDto.getLoginId(), requestDto.getPassword());

        /*
         실제 유저 검증이 일어나는 부분
         CustomUserDetailsService의 loadUserByUsername 메소드가 실행
         */
        Authentication authentication;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authToken);
        } catch (BadCredentialsException e) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        System.out.println("auth name : " + authentication.getName());
        System.out.println("auth principal : " + authentication.getPrincipal());

        String loginId = getLoginIdFromAuthentication(authentication);

        TokenDto token = jwtUtil.createToken(loginId);

        return token;
    }

    public MemberRespnseDto signUp(MemberSignupRequest requestDto) {
        checkDuplicatedLoginId(requestDto.getLoginId());

        return MemberRespnseDto.fromEntity(
                memberRepository.save(
                        requestDto.toEntity(
                                passwordEncoder.encode(requestDto.getPassword())
                        )
                ));
    }

    private void checkDuplicatedLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new CustomException(ErrorCode.ID_DUPLICATED);
        }
    }

    public TokenDto reissue(TokenRequest tokenRequest) {
        refreshTokenService.validateToken(tokenRequest.getLoginId(), tokenRequest.getRefreshToken());

        return jwtUtil.createToken(tokenRequest.getLoginId());
    }

    public void logout(String loginId) {
        refreshTokenService.deleteToken(loginId);
    }

    private String getLoginIdFromAuthentication(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof Member) {
            Member member = (Member) principal;
            return member.getLoginId();
        }

        throw new NullPointerException();
    }

}
