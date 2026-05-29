package com.gonzalo.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador MVC que sirve la página de login personalizada.
 * Spring Security maneja el POST de /login automáticamente.
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        // Sirve el archivo login.html desde src/main/resources/templates/
        return "login";
    }
}
