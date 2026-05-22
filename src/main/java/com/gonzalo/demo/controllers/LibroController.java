package com.gonzalo.demo.controllers;

import com.gonzalo.demo.entities.Libro;
import com.gonzalo.demo.services.LibroService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/libros")
public class LibroController extends BaseControllerImpl<Libro, Long, LibroService> {

    public LibroController(LibroService libroService) {
        super(libroService);
    }
}