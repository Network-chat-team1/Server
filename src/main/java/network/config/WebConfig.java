package network.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // 모든 URL 허용
			.allowedOrigins("*") // 모든 Origin 허용
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
			.allowedHeaders("*") // 모든 헤더 허용
			.allowCredentials(false); // 인증 정보 사용 안 함 (Postman에서는 보통 필요 없음)
	}
}
