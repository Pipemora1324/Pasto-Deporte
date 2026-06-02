package com.pastodeporte.sistema.model;

import com.pastodeporte.sistema.model.enums.RolUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entidad que representa un usuario administrador del sistema.
 *
 * Pilar POO aplicado: HERENCIA - extiende BaseEntity.
 * Pilar POO aplicado: ENCAPSULAMIENTO - password NUNCA expuesto en respuestas.
 * Pilar POO aplicado: OCULTAMIENTO - el campo password es privado y va cifrado con BCrypt.
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario extends BaseEntity {

    /** Nombre de usuario para autenticacion */
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    /**
     * Contrasena cifrada con BCrypt.
     * Pilar POO: OCULTAMIENTO - NUNCA se retorna en DTOs ni respuestas HTTP.
     */
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /** Nombre completo del administrador */
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /** Correo electronico del administrador */
    @Column(name = "email", length = 150)
    private String email;

    /**
     * Rol del usuario en el sistema.
     * Pilar POO: ABSTRACCION - usa enum para definir permisos.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 15)
    private RolUsuario rol;

    /**
     * Token de refresco para renovacion de JWT.
     * Pilar POO: OCULTAMIENTO - no expuesto en respuestas publicas.
     */
    @Column(name = "refresh_token", length = 500)
    private String refreshToken;
}
