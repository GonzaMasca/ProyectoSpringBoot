package com.gonzalo.demo.repositories;

import com.gonzalo.demo.entities.Domicilio;
import org.springframework.stereotype.Repository;

@Repository
public interface DomicilioRepository extends BaseRepository<Domicilio, Long> {
}