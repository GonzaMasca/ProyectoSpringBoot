package com.gonzalo.demo.controllers;

import com.gonzalo.demo.entities.Persona;
import com.gonzalo.demo.services.PersonaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/personas")
public class PersonaController extends BaseControllerImpl<Persona, Long, PersonaService> {

    public PersonaController(PersonaService personaService) {
        super(personaService);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String filtro) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(servicio.search(filtro));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}