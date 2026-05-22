package com.gonzalo.demo.services;

import com.gonzalo.demo.entities.Persona;

import java.util.List;

public interface PersonaService extends BaseService<Persona, Long> {
    List<Persona> search(String filtro) throws Exception;
}