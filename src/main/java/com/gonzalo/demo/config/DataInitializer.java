package com.gonzalo.demo.config;

import com.gonzalo.demo.entities.Capacitacion;
import com.gonzalo.demo.entities.Profesor;
import com.gonzalo.demo.entities.Usuario;
import com.gonzalo.demo.repositories.CapacitacionRepository;
import com.gonzalo.demo.repositories.ProfesorRepository;
import com.gonzalo.demo.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CapacitacionRepository capacitacionRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // ── Crear usuario admin por defecto si no existe ──────────────────────
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario(
                    0,
                    "admin",
                    passwordEncoder.encode("admin123"),
                    "ROLE_ADMIN"
            );
            usuarioRepository.save(admin);
            System.out.println("====== USUARIO ADMIN CREADO (usuario: admin | contraseña: admin123) ======");
        }

        // ── Crear profesores y capacitaciones de muestra ──────────────────────
        if (profesorRepository.count() == 0 && capacitacionRepository.count() == 0) {

            // Crear profesores
            Profesor gonzalo = new Profesor(0, "Gonzalo", "Mascareño", "Desarrollo de Software");
            Profesor maria   = new Profesor(0, "María",   "Gómez",     "Sistemas Operativos");
            Profesor carlos  = new Profesor(0, "Carlos",  "Pérez",     "Matemáticas");

            profesorRepository.saveAll(Arrays.asList(gonzalo, maria, carlos));
            System.out.println("====== PROFESORES INICIALIZADOS ======");

            // Crear capacitaciones asociadas a los profesores
            Capacitacion prog  = new Capacitacion(0, "Programacion", gonzalo);
            Capacitacion linux = new Capacitacion(0, "Linux",        maria);
            Capacitacion alg   = new Capacitacion(0, "Algebra",      carlos);

            capacitacionRepository.saveAll(Arrays.asList(prog, linux, alg));
            System.out.println("====== CAPACITACIONES INICIALIZADAS Y ASOCIADAS ======");
        }
    }
}
