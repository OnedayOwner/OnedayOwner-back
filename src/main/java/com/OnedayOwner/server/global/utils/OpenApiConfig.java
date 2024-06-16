package com.OnedayOwner.server.global.utils;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

@Configuration
public class OpenApiConfig {

    private final Logger logger = LoggerFactory.getLogger(OpenApiConfig.class);
    private final StopWatch stopWatch = new StopWatch();


    @Bean
    public OpenAPI openAPI() {
        stopWatch.start();
        final String securitySchemeName = "Authorization";

        OpenAPI openAPI = new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("Bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(apiInfo());

        stopWatch.stop();
        logger.info("Started Swagger is {} ms", stopWatch.prettyPrint());
        return openAPI;
    }

    private Info apiInfo() {
        return new Info()
                .title("OnedayOwner back-end API")
                .version("0.0.0");
    }
}
