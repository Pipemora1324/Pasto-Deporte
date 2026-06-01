package com.pastodeporte.sistema.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuracion de seguridad HTTP, CORS y cadena de filtros JWT.
 *
 * NO define UserDetailsService ni PasswordEncoder — esos beans viven en
 * UserDetailsServiceConfig para evitar la referencia circular:
 *   SecurityConfig → JwtAuthFilter → UserDetailsService → SecurityConfig (CICLO)
 *
 * Con la separacion, el grafo queda limpio:
 *   UserDetailsServiceConfig → UsuarioRepository
 *   JwtAuthFilter            → JwtUtil, UserDetailsService
 *   SecurityConfig           → JwtAuthFilter, UserDetailsService, PasswordEncoder
 *
 * Pilar POO aplicado: MODULARIDAD - reglas de seguridad HTTP centralizadas aqui.
 * Pilar POO aplicado: ENCAPSULAMIENTO - configuracion de rutas encapsulada.
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor explicito — SIN @RequiredArgsConstructor.
     * Todos los beans inyectados vienen de clases distintas a esta, sin ciclo.
     */
    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                          UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cadena de filtros de seguridad HTTP.
     * Define rutas publicas y protegidas, y la politica sin estado (stateless).
     *
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> las reglas de acceso y la
     * configuracion de sesion estan centralizadas aqui.</p>
     *
     * @param http objeto de configuracion de seguridad HTTP de Spring
     * @return {@link SecurityFilterChain} completamente configurada
     * @throws Exception si ocurre un error durante la configuracion de seguridad
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(new AntPathRequestMatcher("/api/auth/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/archivos/**", "GET")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/uploads/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/clubes", "GET")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/clubes/**", "GET")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/miembros/**", "GET")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/menu/**")).permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuracion CORS que permite peticiones desde el frontend Angular
     * en desarrollo (localhost) y en produccion (Vercel).
     *
     * <p>Se usa {@code setAllowedOriginPatterns} en lugar de {@code setAllowedOrigins}
     * para poder usar el wildcard {@code *.vercel.app} con credenciales.</p>
     *
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> configuracion CORS encapsulada
     * en un bean aislado del resto de la configuracion.</p>
     *
     * @return {@link CorsConfigurationSource} configurada para los origenes del frontend
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(
            "http://localhost:4200",
            "https://pasto-deporte.vercel.app",
            "https://pasto-deporte-git-main-pipemora1324s-projects.vercel.app",
            "https://*.vercel.app"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Proveedor de autenticacion DAO que usa {@code UserDetailsService}
     * y {@code PasswordEncoder} para verificar credenciales.
     *
     * @return {@link AuthenticationProvider} configurado con DAO y BCrypt
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * Manager de autenticacion de Spring Security.
     *
     * @param config configuracion de autenticacion de Spring Security
     * @return {@link AuthenticationManager} obtenido de la configuracion de Spring
     * @throws Exception si ocurre un error al obtener el manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
