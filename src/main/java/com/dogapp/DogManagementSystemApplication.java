package com.dogapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Clase principal de la aplicación Dog Management System
 * 
 * Esta aplicación implementa un sistema de microservicios para la gestión de perros
 * utilizando Spring Boot, JPA, HATEOAS y arquitectura REST.
 * 
 * Funcionalidades principales:
 * - CRUD completo de perros
 * - CRUD completo de propietarios
 * - Relaciones entre perros y propietarios
 * - API REST con HATEOAS
 * - Validación de datos
 * - Documentación OpenAPI
 * - Pruebas unitarias
 * 
 * @author Tu Nombre
 * @version 1.0.0
 * @since 2024
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.dogapp.repository")
@EnableTransactionManagement
public class DogManagementSystemApplication {

    /**
     * Método principal que inicia la aplicación Spring Boot
     * 
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(DogManagementSystemApplication.class, args);
        
        System.out.println("==============================================");
        System.out.println("🐕 Dog Management System iniciado exitosamente");
        System.out.println("==============================================");
        System.out.println("📱 Aplicación: http://localhost:8080/api");
        System.out.println("📊 H2 Console: http://localhost:8080/api/h2-console");
        System.out.println("📚 Swagger UI: http://localhost:8080/api/swagger-ui.html");
        System.out.println("🔍 API Docs: http://localhost:8080/api/api-docs");
        System.out.println("❤️  Health Check: http://localhost:8080/api/actuator/health");
        System.out.println("==============================================");
    }
} 