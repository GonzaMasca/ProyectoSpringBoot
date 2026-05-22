package com.gonzalo.demo.controllers;

import com.gonzalo.demo.entities.Autor;
import com.gonzalo.demo.services.AutorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/autores")
public class AutorController extends BaseControllerImpl<Autor, Long, AutorService> {

    public AutorController(AutorService autorService) {
        super(autorService);
    }
}