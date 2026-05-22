package com.gonzalo.demo.repositories;

import com.gonzalo.demo.entities.Libro;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends BaseRepository<Libro, Long> {
}