package com.dogapp.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controlador para el endpoint raíz y redirección a Swagger UI
 * 
 * @author Tu Nombre
 * @version 1.0.0
 */
@Controller
public class HomeController {

    /**
     * Endpoint raíz que redirije a Swagger UI
     * 
     * @return redirección a Swagger UI
     */
    @GetMapping("/")
    @Hidden
    public String home() {
        return "redirect:/swagger-ui.html";
    }

    /**
     * Endpoint de API raíz que proporciona información básica
     * 
     * @return información básica de la API
     */
    @GetMapping("/api")
    @ResponseBody
    @Hidden
    public String apiInfo() {
        return "{"
            + "\"name\": \"Dog Management System API\","
            + "\"version\": \"1.0.0\","
            + "\"description\": \"API REST para la gestión de perros y propietarios\","
            + "\"swagger-ui\": \"http://localhost:8080/swagger-ui.html\","
            + "\"api-docs\": \"http://localhost:8080/v3/api-docs\""
            + "}";
    }
} 