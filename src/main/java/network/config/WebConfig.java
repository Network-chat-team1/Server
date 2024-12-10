package network.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // 모든 URL에 대해 CORS 허용
			.allowedOrigins("http://localhost:3000", "http://3.39.185.125", "https://hospitalchat.netlify.app", "https://network-chat.store") // 모든 도메인 허용
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
			.allowedHeaders("*") // 모든 헤더 허용
			.allowCredentials(true); // 인증 정보 허용 (필요시 true, 기본은 false)
	}
}
