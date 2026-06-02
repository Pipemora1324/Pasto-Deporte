package com.pastodeporte.sistema.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro JWT que intercepta cada peticion HTTP y valida el token de autenticacion.
 *
 * <p>Sus dependencias ({@link JwtUtil} y {@link UserDetailsService}) vienen definidas
 * fuera de {@link SecurityConfig}, garantizando que NO haya referencia circular.</p>
 *
 * <p><b>Pilar POO — HERENCIA:</b> extiende {@link OncePerRequestFilter} de Spring,
 * garantizando que el filtro se ejecute una sola vez por peticion.</p>
 * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> la logica de extraccion y validacion
 * del token esta completamente encapsulada en {@code doFilterInternal}.</p>
 * <p><b>Pilar POO — POLIMORFISMO:</b> sobreescribe {@code doFilterInternal}
 * con {@code @Override} para proporcionar la validacion JWT.</p>
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor explicito para inyectar dependencias sin {@code @RequiredArgsConstructor}.
     * {@code UserDetailsService} viene de {@link UserDetailsServiceConfig}, no de
     * {@link SecurityConfig}, evitando la referencia circular.
     *
     * @param jwtUtil            utilidad para operaciones JWT (firma, extraccion, validacion)
     * @param userDetailsService servicio de carga de detalles de usuario para Spring Security
     */
    public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Intercepta cada peticion HTTP, extrae el Bearer token del header Authorization
     * y, si es valido, establece la autenticacion en el {@link SecurityContextHolder}.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} del metodo abstracto
     * de {@link OncePerRequestFilter}.</p>
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> la logica de validacion del token y
     * la autenticacion del usuario estan completamente encapsuladas aqui.</p>
     *
     * @param request     peticion HTTP entrante
     * @param response    respuesta HTTP saliente
     * @param filterChain cadena de filtros de Spring Security
     * @throws ServletException si ocurre un error de servlet durante el filtrado
     * @throws IOException      si ocurre un error de E/S durante el filtrado
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String username;

        try {
            username = jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.isTokenValid(jwt, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
