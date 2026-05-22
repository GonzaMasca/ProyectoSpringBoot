package com.gonzalo.demo.controllers;

import com.gonzalo.demo.entities.Alumno;
import com.gonzalo.demo.services.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(alumnoService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error al obtener alumnos: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable int id) {
        try {
            Alumno alu = alumnoService.findById(id);
            if (alu == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Alumno no encontrado.\"}");
            }
            return ResponseEntity.status(HttpStatus.OK).body(alu);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error al obtener alumno: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody Alumno entity) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(alumnoService.save(entity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error al guardar: " + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Alumno entity) {
        try {
            Alumno alu = alumnoService.update(id, entity);
            if (alu == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Alumno no encontrado.\"}");
            }
            return ResponseEntity.status(HttpStatus.OK).body(alu);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error al actualizar: " + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(alumnoService.delete(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error al eliminar: " + e.getMessage() + "\"}");
        }
    }
}
