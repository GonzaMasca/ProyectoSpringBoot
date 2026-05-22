package com.gonzalo.demo.controllers;

import com.gonzalo.demo.entities.Capacitacion;
import com.gonzalo.demo.services.CapacitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/capacitaciones")
public class CapacitacionController {

    @Autowired
    private CapacitacionService capacitacionService;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(capacitacionService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error al obtener capacitaciones: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable int id) {
        try {
            Capacitacion cap = capacitacionService.findById(id);
            if (cap == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Capacitacion no encontrada.\"}");
            }
            return ResponseEntity.status(HttpStatus.OK).body(cap);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error al obtener capacitacion: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody Capacitacion entity) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(capacitacionService.save(entity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error al guardar: " + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Capacitacion entity) {
        try {
            Capacitacion cap = capacitacionService.update(id, entity);
            if (cap == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Capacitacion no encontrada.\"}");
            }
            return ResponseEntity.status(HttpStatus.OK).body(cap);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error al actualizar: " + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(capacitacionService.delete(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error al eliminar: " + e.getMessage() + "\"}");
        }
    }
}
