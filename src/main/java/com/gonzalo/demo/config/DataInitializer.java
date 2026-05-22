package com.gonzalo.demo.config;

import com.gonzalo.demo.entities.Capacitacion;
import com.gonzalo.demo.repositories.CapacitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CapacitacionRepository capacitacionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (capacitacionRepository.count() == 0) {
            Capacitacion prog = new Capacitacion(0, "Programacion");
            Capacitacion linux = new Capacitacion(0, "Linux");
            Capacitacion alg = new Capacitacion(0, "Algebra");

            capacitacionRepository.saveAll(Arrays.asList(prog, linux, alg));
            System.out.println("====== DATOS DE PRUEBA INICIALIZADOS: Programacion, Linux, Algebra ======");
        }
    }
}
