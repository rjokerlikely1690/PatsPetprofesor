package com.dogapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para la documentación de la API
 * 
 * Esta clase configura la documentación automática de la API REST
 * utilizando OpenAPI 3.0 y Swagger UI.
 * 
 * @author Tu Nombre
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configuración de OpenAPI para la documentación
     * 
     * @return configuración OpenAPI
     */
    @Bean
    public OpenAPI dogManagementOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Dog Management System API")
                .description("API REST para la gestión de perros y propietarios")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Tu Nombre")
                    .email("tu.email@ejemplo.com")
                    .url("https://github.com/tu-usuario"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8080")
                    .description("Servidor de desarrollo"),
                new Server()
                    .url("https://api.dogmanagement.com")
                    .description("Servidor de producción")
            ));
    }
} 