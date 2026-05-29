package com.gonzalo.demo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Expone el token CSRF para que el frontend pueda incluirlo en el POST de logout.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/csrf")
    public Map<String, String> getCsrfToken(HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (token == null) {
            return Map.of("token", "", "headerName", "_csrf", "paramName", "_csrf");
        }
        return Map.of(
                "token",      token.getToken(),
                "headerName", token.getHeaderName(),
                "paramName",  token.getParameterName()
        );
    }
}
