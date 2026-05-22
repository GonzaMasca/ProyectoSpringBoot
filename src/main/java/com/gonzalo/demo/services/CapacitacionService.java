package com.gonzalo.demo.services;

import com.gonzalo.demo.entities.Capacitacion;
import java.util.List;

public interface CapacitacionService {
    List<Capacitacion> findAll() throws Exception;
    Capacitacion findById(int id) throws Exception;
    Capacitacion save(Capacitacion cap) throws Exception;
    Capacitacion update(int id, Capacitacion cap) throws Exception;
    boolean delete(int id) throws Exception;
}
