package com.gonzalo.demo.services;

import com.gonzalo.demo.entities.Profesor;
import java.util.List;

public interface ProfesorService {
    List<Profesor> findAll() throws Exception;
    Profesor findById(int id) throws Exception;
    Profesor save(Profesor prof) throws Exception;
    Profesor update(int id, Profesor prof) throws Exception;
    boolean delete(int id) throws Exception;
}
