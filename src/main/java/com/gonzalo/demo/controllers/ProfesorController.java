package com.gonzalo.demo.controllers;

import com.gonzalo.demo.entities.Profesor;
import com.gonzalo.demo.services.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/profesores")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(profesorService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error al obtener profesores: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable int id) {
        try {
            Profesor prof = profesorService.findById(id);
            if (prof == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Profesor no encontrado.\"}");
            }
            return ResponseEntity.status(HttpStatus.OK).body(prof);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error al obtener profesor: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody Profesor entity) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(profesorService.save(entity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error al guardar: " + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Profesor entity) {
        try {
            Profesor prof = profesorService.update(id, entity);
            if (prof == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Profesor no encontrado.\"}");
            }
            return ResponseEntity.status(HttpStatus.OK).body(prof);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error al actualizar: " + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            boolean deleted = profesorService.delete(id);
            if (deleted) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(true);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Profesor no encontrado.\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
