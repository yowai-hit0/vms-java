package com.rw.rra.vms.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
@OpenAPIDefinition(
        info = @Info(
                title       = "RRA Vehicle Tracking API",
                version     = "1.0",
                description = "APIs for user, owner, plates, vehicles & transfers"
        )
)

@Configuration
public class OpenApiConfig {

        private static final String SCHEME_NAME   = "bearerAuth";
        private static final String BEARER_FORMAT = "JWT";
        private static final String SCHEME        = "bearer";

        @Bean
        public OpenAPI baseOpenAPI() {
                return new OpenAPI()
                        // Apply the security scheme globally
                        .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
                        .components(new Components()
                                .addSecuritySchemes(SCHEME_NAME,
                                        new SecurityScheme()
                                                .name(SCHEME_NAME)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme(SCHEME)
                                                .bearerFormat(BEARER_FORMAT)
                                                .in(SecurityScheme.In.HEADER)
                                )
                        );
        }
}

