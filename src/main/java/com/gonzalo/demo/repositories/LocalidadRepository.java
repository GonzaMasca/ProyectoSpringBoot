package com.gonzalo.demo.repositories;

import com.gonzalo.demo.entities.Localidad;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalidadRepository extends BaseRepository<Localidad, Long> {
}