package com.gonzalo.demo.services;

import com.gonzalo.demo.entities.Alumno;
import com.gonzalo.demo.entities.Capacitacion;
import com.gonzalo.demo.repositories.AlumnoRepository;
import com.gonzalo.demo.repositories.CapacitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private CapacitacionRepository capacitacionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Alumno> findAll() throws Exception {
        try {
            return alumnoRepository.findAll();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Alumno findById(int id) throws Exception {
        try {
            Optional<Alumno> optional = alumnoRepository.findById(id);
            return optional.orElse(null);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Alumno save(Alumno alu) throws Exception {
        try {
            if (alu.getCapacitaciones() != null && !alu.getCapacitaciones().isEmpty()) {
                List<Capacitacion> capPersistentes = new ArrayList<>();
                for (Capacitacion c : alu.getCapacitaciones()) {
                    Optional<Capacitacion> dbCap = capacitacionRepository.findById(c.getId_cap());
                    if (dbCap.isPresent()) {
                        capPersistentes.add(dbCap.get());
                    } else if (c.getNombre() != null) {
                        capPersistentes.add(capacitacionRepository.save(c));
                    }
                }
                alu.setCapacitaciones(capPersistentes);
            }
            return alumnoRepository.save(alu);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Alumno update(int id, Alumno alu) throws Exception {
        try {
            Optional<Alumno> optional = alumnoRepository.findById(id);
            if (optional.isPresent()) {
                Alumno aluExistente = optional.get();
                aluExistente.setNombre(alu.getNombre());
                aluExistente.setPseudonimo(alu.getPseudonimo());
                if (alu.getCapacitaciones() != null) {
                    List<Capacitacion> capPersistentes = new ArrayList<>();
                    for (Capacitacion c : alu.getCapacitaciones()) {
                        Optional<Capacitacion> dbCap = capacitacionRepository.findById(c.getId_cap());
                        dbCap.ifPresent(capPersistentes::add);
                    }
                    aluExistente.setCapacitaciones(capPersistentes);
                }
                return alumnoRepository.save(aluExistente);
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
            if (alumnoRepository.existsById(id)) {
                alumnoRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
