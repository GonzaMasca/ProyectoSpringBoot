package com.gonzalo.demo.services;

import com.gonzalo.demo.entities.Alumno;
import java.util.List;

public interface AlumnoService {
    List<Alumno> findAll() throws Exception;
    Alumno findById(int id) throws Exception;
    Alumno save(Alumno alu) throws Exception;
    Alumno update(int id, Alumno alu) throws Exception;
    boolean delete(int id) throws Exception;
}
