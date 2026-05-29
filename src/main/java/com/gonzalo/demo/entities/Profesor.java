package com.gonzalo.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "Profesor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profesor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_prof;

    @Basic
    private String nombre;
    private String apellido;
    private String especialidad;

    public Profesor(int id_prof, String nombre, String apellido) {
        this.id_prof = id_prof;
        this.nombre = nombre;
        this.apellido = apellido;
    }
}
