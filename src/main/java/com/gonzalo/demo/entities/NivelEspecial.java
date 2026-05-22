package com.gonzalo.demo.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class NivelEspecial extends Nivel {
    
    private int minutosDeVideoNecesarios;

    public NivelEspecial(int minutosDeVideoNecesarios) {
        this.minutosDeVideoNecesarios = minutosDeVideoNecesarios;
    }

    public NivelEspecial(int minutosDeVideoNecesarios, String nombre, int ejerciciosNecesarios) {
        super(nombre, ejerciciosNecesarios);
        this.minutosDeVideoNecesarios = minutosDeVideoNecesarios;
    }
}
