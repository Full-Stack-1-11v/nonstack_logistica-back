package com.perfulandia.cl.logistica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Clase principal que inicia la aplicación de logística de Perfulandia.
 * <p>
 * Esta clase está anotada con {@link SpringBootApplication}, lo que habilita la
 * autoconfiguración de Spring Boot, el escaneo de componentes y la configuración.
 * {@link EnableFeignClients} activa la detección de clientes Feign para la comunicación
 * declarativa con otros microservicios.
 * {@link ComponentScan} especifica explícitamente el paquete base para buscar componentes de Spring.
 */
@SpringBootApplication
@EnableFeignClients
@ComponentScan("com.perfulandia.cl.logistica")
public class LogisticaApplication {

    /**
     * Punto de entrada principal para la aplicación Spring Boot.
     *
     * @param args Argumentos de la línea de comandos pasados al iniciar la aplicación.
     */
    public static void main(String[] args) {
        SpringApplication.run(LogisticaApplication.class, args);
    }

}
