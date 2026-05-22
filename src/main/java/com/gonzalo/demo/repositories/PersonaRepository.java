package com.gonzalo.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gonzalo.demo.entities.Persona;

@Repository
public interface PersonaRepository extends BaseRepository<Persona, Long> {

    List<Persona> findByNombreContainingOrApellidoContaining(String nombre, String apellido);

    Page<Persona> findByNombreContainingOrApellidoContaining(String nombre, String apellido, Pageable pageable);

    boolean existsByDni(int dni);

    // Consulta JPQL con parametro nombrado
    @Query("SELECT p FROM Persona p WHERE p.nombre LIKE CONCAT('%', :filtro, '%') OR p.apellido LIKE CONCAT('%', :filtro, '%')")
    List<Persona> search(@Param("filtro") String filtro);

    @Query(
            value = "SELECT p FROM Persona p WHERE p.nombre LIKE CONCAT('%', :filtro, '%') OR p.apellido LIKE CONCAT('%', :filtro, '%')",
            countQuery = "SELECT count(p) FROM Persona p WHERE p.nombre LIKE CONCAT('%', :filtro, '%') OR p.apellido LIKE CONCAT('%', :filtro, '%')"
    )
    Page<Persona> search(@Param("filtro") String filtro, Pageable pageable);

    // SQL nativo
    @Query(
            value = "SELECT * FROM persona WHERE nombre LIKE CONCAT('%', :filtro, '%') OR apellido LIKE CONCAT('%', :filtro, '%')",
            nativeQuery = true
    )
    List<Persona> searchNativo(@Param("filtro") String filtro);

    @Query(
            value = "SELECT * FROM persona WHERE nombre LIKE CONCAT('%', :filtro, '%') OR apellido LIKE CONCAT('%', :filtro, '%')",
            countQuery = "SELECT count(*) FROM persona WHERE nombre LIKE CONCAT('%', :filtro, '%') OR apellido LIKE CONCAT('%', :filtro, '%')",
            nativeQuery = true
    )
    Page<Persona> searchNativo(@Param("filtro") String filtro, Pageable pageable);
}