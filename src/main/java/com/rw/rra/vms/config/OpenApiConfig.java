package com.rw.rra.vms.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;
import java.util.ArrayList;

@OpenAPIDefinition(
        info = @Info(
                title = "RRA Vehicle Tracking API",
                version = "1.0",
                description = "APIs for Employee, payroll, employement, deduction"
        )
)
@Configuration
public class OpenApiConfig {

        private static final String SCHEME_NAME = "bearerAuth";
        private static final String BEARER_FORMAT = "JWT";
        private static final String SCHEME = "bearer";

        @Bean
        public OpenAPI baseOpenAPI() {
                return new OpenAPI()
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

        @Bean
        public OpenApiCustomizer customizer() {
                return openApi -> {
                        // Iterate through all paths in the OpenAPI schema
                        openApi.getPaths().forEach((path, pathItem) -> {
                                // Only process GET endpoints under /api/
                                if (path.startsWith("/api/") && pathItem.getGet() != null) {
                                        var getOperation = pathItem.getGet();
                                        // Remove any incorrect 'pageable' parameter
                                        if (getOperation.getParameters() == null) {
                                                getOperation.setParameters(new ArrayList<>());
                                        }

                                        getOperation.getParameters().removeIf(param -> "pageable".equals(param.getName()));
                                        // Add page, size, and sort query parameters
                                        getOperation
                                                .addParametersItem(new Parameter()
                                                        .name("page")
                                                        .in("query")
                                                        .description("Page number (0-based)")
                                                        .required(false)
                                                        .schema(new io.swagger.v3.oas.models.media.IntegerSchema()
                                                                ._default(0)
                                                                .minimum(BigDecimal.ZERO)))
                                                .addParametersItem(new Parameter()
                                                        .name("size")
                                                        .in("query")
                                                        .description("Number of items per page")
                                                        .required(false)
                                                        .schema(new io.swagger.v3.oas.models.media.IntegerSchema()
                                                                ._default(20)
                                                                .minimum(BigDecimal.ONE)))
                                                .addParametersItem(new Parameter()
                                                        .name("sort")
                                                        .in("query")
                                                        .description("Sort criteria in the format: property,asc|desc (e.g., fieldName,asc)")
                                                        .required(false)
                                                        .schema(new io.swagger.v3.oas.models.media.ArraySchema()
                                                                .items(new io.swagger.v3.oas.models.media.StringSchema())));
                                }
                        });
                };
        }
}