package com.pastodeporte.sistema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Clase principal de la aplicacion Pasto Deporte.
 * Pilar POO aplicado: MODULARIDAD - punto de entrada unico al sistema.
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@SpringBootApplication
@EnableScheduling
public class PastoDeporteApplication {

    /**
     * Punto de entrada del sistema. Inicia Spring Boot y muestra las credenciales
     * de acceso en la consola.
     *
     * <p><b>Pilar POO — MODULARIDAD:</b> punto de entrada unico que delega
     * el arranque completo a {@link SpringApplication}.</p>
     *
     * @param args argumentos de linea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        SpringApplication.run(PastoDeporteApplication.class, args);
        System.out.println("===========================================");
        System.out.println("  PASTO DEPORTE - Sistema de Certificacion");
        System.out.println("  Puerto: 9797");
        System.out.println("  Admin: http://localhost:4200/admin/login");
        System.out.println("  Usuario: Erika1887 | Password: Erika1887");
        System.out.println("===========================================");
    }
}
