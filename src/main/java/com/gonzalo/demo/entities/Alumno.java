package com.gonzalo.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Alumno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alumno implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_alu;

    @Basic
    private String nombre;
    private String pseudonimo;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "alumno_capacitacion",
            joinColumns = @JoinColumn(name = "alumno_id"),
            inverseJoinColumns = @JoinColumn(name = "capacitacion_id")
    )
    @JsonIgnoreProperties("alumnos")
    private List<Capacitacion> capacitaciones = new ArrayList<>();

    public Alumno(int id_alu, String nombre, String pseudonimo) {
        this.id_alu = id_alu;
        this.nombre = nombre;
        this.pseudonimo = pseudonimo;
    }

    public void addCapacitaciones(Capacitacion capacitacion) {
        this.capacitaciones.add(capacitacion);
    }
}
