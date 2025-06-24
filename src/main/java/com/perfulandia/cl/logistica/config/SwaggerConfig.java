package com.perfulandia.cl.logistica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
/**
 * Configuración de Swagger (OpenAPI) para la documentación de la API.
 * Esta clase define los beans necesarios para generar la documentación
 * de la API REST del microservicio de Logística.
 */
@Configuration
public class SwaggerConfig {
    /**
     * Crea y configura el bean {@link OpenAPI} que proporciona la información
     * general de la API para la documentación de Swagger.
     *
     * @return una instancia de {@link OpenAPI} con la información de la API.
     */
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
            .info(new Info()
                .title("API Microservicio Logistica Perfulandia")
                .version("1.0")
                .description("Documentacion de la API de microservicios relacionados a la logistica."));
    }
}
