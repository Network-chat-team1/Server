package network.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI SwaggerAPI() {
		return new OpenAPI()
			.components(new Components())
			.info(apiInfo());

	}

	private Info apiInfo(){
		return new Info()
			.title("Network Swagger")
			.description("Network Server API 명세서")
			.version("1.0.0");
	}
}
