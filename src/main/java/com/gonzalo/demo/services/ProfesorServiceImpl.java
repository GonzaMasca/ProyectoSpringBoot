package com.gonzalo.demo.services;

import com.gonzalo.demo.entities.Profesor;
import com.gonzalo.demo.repositories.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProfesorServiceImpl implements ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Profesor> findAll() throws Exception {
        try {
            return profesorRepository.findAll();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Profesor findById(int id) throws Exception {
        try {
            Optional<Profesor> optional = profesorRepository.findById(id);
            return optional.orElse(null);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Profesor save(Profesor prof) throws Exception {
        try {
            return profesorRepository.save(prof);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Profesor update(int id, Profesor prof) throws Exception {
        try {
            Optional<Profesor> optional = profesorRepository.findById(id);
            if (optional.isPresent()) {
                Profesor profExistente = optional.get();
                profExistente.setNombre(prof.getNombre());
                profExistente.setApellido(prof.getApellido());
                profExistente.setEspecialidad(prof.getEspecialidad());
                return profesorRepository.save(profExistente);
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
            if (profesorRepository.existsById(id)) {
                profesorRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception("No se puede eliminar el profesor porque tiene capacitaciones asignadas.");
        }
    }
}
