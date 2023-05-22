package com.HR.Blog.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
public class SwaggerConfig {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	private ApiKey apiKeys() {
		return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
	}

	private List<SecurityContext> securityContexts() {
		return Collections.singletonList(SecurityContext.builder().securityReferences(secRef()).build());
	}

	private List<SecurityReference> secRef() {

		AuthorizationScope scope = new AuthorizationScope("global", "accessEverything");

		return Arrays.asList(new SecurityReference("JWT", new AuthorizationScope[] { scope }));
	}

	@Bean
	public Docket api() {

		return new Docket(DocumentationType.SWAGGER_2).apiInfo(getInfo()).securityContexts(securityContexts())
				.securitySchemes(Arrays.asList(apiKeys())).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();

	}

	private ApiInfo getInfo() {

		return new ApiInfo("Blogging Application",
				"This project is developed by Hrishabh for Practise Purpose.", "1.0", "Terms of Service",
				new Contact("Hrishabh", "https://www.linkedin.com/in/hrishabh-raj-9929b1195/", "iamhrishabhraj@gmail.com"),
				"License of APIs", "API license URL", Collections.emptyList());
	};

}
