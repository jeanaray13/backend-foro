package com.backend.foro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket api() {
		//Visualización de la documentación de Swagger UI
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.backend.foro.controller"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(getApiInfo());
	}
	
	private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Forum Service API")
                .description("El backend Spring Boot para el foro permite interactuar con "
                		+ "comentarios en publicaciones mediante los métodos"
                		+ " GET (recuperar), POST (crear), PUT (actualizar) y DELETE (eliminar), "
                		+ "facilitando una gestión dinámica y segura.")
                .build();
    }
}

