package com.gonzalo.demo.services;

import com.gonzalo.demo.entities.Capacitacion;
import com.gonzalo.demo.repositories.CapacitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CapacitacionServiceImpl implements CapacitacionService {

    @Autowired
    private CapacitacionRepository capacitacionRepository;

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
            throw new Exception(e.getMessage());
        }
    }
}
