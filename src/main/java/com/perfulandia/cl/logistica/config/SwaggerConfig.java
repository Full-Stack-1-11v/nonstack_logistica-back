package com.perfulandia.cl.logistica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
            .info(new Info()
                .title("API Microservicio Logistica Perfulandia")
                .version("1.0")
                .description("Documentacion de la API de microservicios relacionados a la logistica."));
    }
}
