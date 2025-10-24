package com.ofss.kyc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI kycServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("KYC Service API")
                        .description("Customer Onboarding - REST API Documentation")
                        .version("1.0.0"));
    }
}
