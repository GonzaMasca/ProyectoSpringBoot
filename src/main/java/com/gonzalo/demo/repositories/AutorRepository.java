package com.gonzalo.demo.repositories;

import com.gonzalo.demo.entities.Autor;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends BaseRepository<Autor, Long> {
}