package com.pastodeporte.sistema.repository;

import com.pastodeporte.sistema.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para acceso a datos de usuarios administradores.
 *
 * Pilar POO aplicado: MODULARIDAD - capa de datos separada.
 * Pilar POO aplicado: OCULTAMIENTO - las consultas de seguridad son internas.
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario activo por nombre de usuario para autenticacion.
     * Pilar POO: OCULTAMIENTO - busqueda de credenciales solo via este metodo.
     */
    Optional<Usuario> findByUsernameAndEliminadoFalse(String username);

    /**
     * Verifica si existe un usuario con el nombre dado.
     */
    boolean existsByUsername(String username);

    /**
     * Busca usuario por refresh token para renovacion de JWT.
     */
    Optional<Usuario> findByRefreshTokenAndEliminadoFalse(String refreshToken);
}
