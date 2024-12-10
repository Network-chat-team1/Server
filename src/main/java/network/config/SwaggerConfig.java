package network.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI SwaggerAPI() {
		return new OpenAPI()
			.components(new Components())
			.servers(servers()) // 서버 정보를 추가
			.info(apiInfo());
	}

	private Info apiInfo() {
		return new Info()
			.title("Network Swagger")
			.description("Network Server API 명세서")
			.version("1.0.0");
	}

	private List<Server> servers() {
		return List.of(
			new Server()
				.url("https://network-chat.store") // HTTPS URL 설정
				.description("Production Server") // 설명 추가
		);
	}
}
