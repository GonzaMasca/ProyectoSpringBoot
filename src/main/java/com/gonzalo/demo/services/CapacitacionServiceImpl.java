package com.gonzalo.demo.services;

import com.gonzalo.demo.entities.Capacitacion;
import com.gonzalo.demo.entities.Profesor;
import com.gonzalo.demo.repositories.CapacitacionRepository;
import com.gonzalo.demo.repositories.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CapacitacionServiceImpl implements CapacitacionService {

    @Autowired
    private CapacitacionRepository capacitacionRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Capacitacion> findAll() throws Exception {
        try {
            return capacitacionRepository.findAll();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Capacitacion findById(int id) throws Exception {
        try {
            Optional<Capacitacion> optional = capacitacionRepository.findById(id);
            return optional.orElse(null);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Capacitacion save(Capacitacion cap) throws Exception {
        try {
            if (cap.getProfesor() != null) {
                Optional<Profesor> dbProf = profesorRepository.findById(cap.getProfesor().getId_prof());
                if (dbProf.isPresent()) {
                    cap.setProfesor(dbProf.get());
                } else {
                    throw new Exception("El profesor asignado no existe.");
                }
            } else {
                throw new Exception("Es requerido asignar un profesor a la capacitación.");
            }
            return capacitacionRepository.save(cap);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Capacitacion update(int id, Capacitacion cap) throws Exception {
        try {
            Optional<Capacitacion> optional = capacitacionRepository.findById(id);
            if (optional.isPresent()) {
                Capacitacion capExistente = optional.get();
                capExistente.setNombre(cap.getNombre());
                
                if (cap.getProfesor() != null) {
                    Optional<Profesor> dbProf = profesorRepository.findById(cap.getProfesor().getId_prof());
                    if (dbProf.isPresent()) {
                        capExistente.setProfesor(dbProf.get());
                    } else {
                        throw new Exception("El profesor asignado no existe.");
                    }
                } else {
                    throw new Exception("Es requerido asignar un profesor a la capacitación.");
                }

                if (cap.getNiveles() != null) {
                    capExistente.setNiveles(cap.getNiveles());
                }
                return capacitacionRepository.save(capExistente);
            }
            return null;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean delete(int id) throws Exception {
        try {
            if (capacitacionRepository.existsById(id)) {
                capacitacionRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception("No se puede eliminar la capacitación porque tiene alumnos inscritos.");
        }
    }
}
