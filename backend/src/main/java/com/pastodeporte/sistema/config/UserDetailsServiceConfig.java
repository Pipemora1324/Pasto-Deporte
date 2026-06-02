package com.pastodeporte.sistema.config;

import com.pastodeporte.sistema.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuracion separada de {@link UserDetailsService} y {@link PasswordEncoder}.
 *
 * <p>Razon de existir: evitar la referencia circular:<br>
 * {@code JwtAuthFilter → UserDetailsService → SecurityConfig → JwtAuthFilter}</p>
 *
 * <p>Al definir estos beans aqui, el grafo de dependencias queda sin ciclos:</p>
 * <ul>
 *   <li>{@code UserDetailsServiceConfig → UsuarioRepository} (sin ciclo)</li>
 *   <li>{@code JwtAuthFilter → JwtUtil + UserDetailsService} (sin ciclo)</li>
 *   <li>{@code SecurityConfig → JwtAuthFilter + UserDetailsService} (sin ciclo)</li>
 * </ul>
 *
 * <p><b>Pilar POO — MODULARIDAD:</b> responsabilidad de autenticacion de usuario
 * separada de la configuracion general de seguridad.</p>
 * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> la logica de carga del usuario y el
 * algoritmo de cifrado estan encapsulados en sus respectivos beans.</p>
 * <p><b>Pilar POO — OCULTAMIENTO:</b> el algoritmo BCrypt y la consulta al
 * repositorio son detalles de implementacion ocultos al resto del sistema.</p>
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
@Configuration
public class UserDetailsServiceConfig {

    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor explicito para inyectar el repositorio de usuarios.
     *
     * @param usuarioRepository repositorio JPA de la entidad {@link com.pastodeporte.sistema.model.Usuario}
     */
    public UserDetailsServiceConfig(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Bean que carga los detalles del usuario desde la base de datos por nombre de usuario.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> implementa {@link UserDetailsService}
     * de Spring Security mediante una lambda anonima.</p>
     *
     * @return implementacion de {@link UserDetailsService} que consulta {@code UsuarioRepository}
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            var usuario = usuarioRepository.findByUsernameAndEliminadoFalse(username)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "Usuario no encontrado: " + username));

            return org.springframework.security.core.userdetails.User.builder()
                    .username(usuario.getUsername())
                    .password(usuario.getPassword())
                    .roles(usuario.getRol().name())
                    .build();
        };
    }

    /**
     * Bean de codificacion de contrasenas con algoritmo BCrypt.
     *
     * <p><b>Pilar POO — OCULTAMIENTO:</b> el algoritmo de hash y el factor de
     * costo estan ocultos detras de la interfaz {@link PasswordEncoder}.</p>
     *
     * @return instancia de {@link BCryptPasswordEncoder} lista para codificar contrasenas
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
