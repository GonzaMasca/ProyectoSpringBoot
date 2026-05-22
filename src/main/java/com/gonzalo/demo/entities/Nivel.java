package com.gonzalo.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nivel implements Serializable {
    protected String nombre;
    protected int ejerciciosNecesarios;
}
