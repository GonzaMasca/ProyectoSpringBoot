package com.gonzalo.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Capacitacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Capacitacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_cap;

    @Basic
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profesor_id", nullable = true) // Hacemos nullable en DB para facilidad de migración
    private Profesor profesor;

    @Transient // No se persiste en base de datos ya que Nivel no es una entidad JPA en el parcial original
    private List<Nivel> niveles = new ArrayList<>();

    @ManyToMany(mappedBy = "capacitaciones")
    @JsonIgnoreProperties("capacitaciones")
    private List<Alumno> alumnos = new ArrayList<>();

    public Capacitacion(int id_cap, String nombre) {
        this.id_cap = id_cap;
        this.nombre = nombre;
    }

    public Capacitacion(int id_cap, String nombre, Profesor profesor) {
        this.id_cap = id_cap;
        this.nombre = nombre;
        this.profesor = profesor;
    }

    public void addAlumnos(Alumno alumno) {
        this.alumnos.add(alumno);
    }
}
