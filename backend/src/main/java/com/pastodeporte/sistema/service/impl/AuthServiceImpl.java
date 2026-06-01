package com.pastodeporte.sistema.service.impl;

import com.pastodeporte.sistema.config.JwtUtil;
import com.pastodeporte.sistema.dto.request.LoginRequest;
import com.pastodeporte.sistema.dto.response.AuthResponse;
import com.pastodeporte.sistema.exception.BusinessException;
import com.pastodeporte.sistema.model.Usuario;
import com.pastodeporte.sistema.repository.UsuarioRepository;
import com.pastodeporte.sistema.service.IAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * Implementacion del servicio de autenticacion con JWT.
 *
 * <p><b>Pilar POO — POLIMORFISMO:</b> implementa {@link IAuthService}
 * con {@code @Override} en cada metodo.</p>
 * <p><b>Pilar POO — HERENCIA:</b> la anotacion {@code @Service} integra
 * esta clase en el ciclo de vida de Spring (patron Template Method).</p>
 * <p><b>Pilar POO — OCULTAMIENTO:</b> los tokens y credenciales se procesan
 * internamente; nunca se expone la clave secreta ni el algoritmo de firma.</p>
 * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> todos los atributos son privados
 * e inyectados por el constructor generado por Lombok.</p>
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    /**
     * Autentica al usuario con sus credenciales y genera tokens JWT.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} del contrato
     * {@link IAuthService}.</p>
     * <p><b>Pilar POO — OCULTAMIENTO:</b> la verificacion de credenciales
     * y la firma del JWT son completamente internas.</p>
     *
     * @param request DTO con {@code username} y {@code password} del administrador
     * @return {@link AuthResponse} con access token, refresh token y datos del usuario
     */
    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new BusinessException("Credenciales invalidas");
        }

        Usuario usuario = usuarioRepository.findByUsernameAndEliminadoFalse(request.getUsername())
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));

        String accessToken = jwtUtil.generateAccessToken(usuario.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(usuario.getUsername());

        usuario.setRefreshToken(refreshToken);
        usuarioRepository.save(usuario);

        log.info("Login exitoso para usuario: {}", usuario.getUsername());

        return new AuthResponse(
                accessToken,
                refreshToken,
                jwtUtil.getExpiration(),
                usuario.getUsername(),
                usuario.getNombre(),
                usuario.getRol().name()
        );
    }

    /**
     * Renueva el access token usando un refresh token valido y persistido.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} del contrato
     * {@link IAuthService}.</p>
     * <p><b>Pilar POO — OCULTAMIENTO:</b> la rotacion del refresh token
     * y la validacion son internas.</p>
     *
     * @param refreshToken token de refresco previamente emitido al usuario
     * @return {@link AuthResponse} con el nuevo access token y el refresh token rotado
     */
    @Override
    public AuthResponse refreshToken(String refreshToken) {
        Usuario usuario = usuarioRepository.findByRefreshTokenAndEliminadoFalse(refreshToken)
                .orElseThrow(() -> new BusinessException("Refresh token invalido o expirado"));

        String username = jwtUtil.extractUsername(refreshToken);
        if (!username.equals(usuario.getUsername())) {
            throw new BusinessException("Refresh token no corresponde al usuario");
        }

        String newAccessToken = jwtUtil.generateAccessToken(usuario.getUsername());
        String newRefreshToken = jwtUtil.generateRefreshToken(usuario.getUsername());

        usuario.setRefreshToken(newRefreshToken);
        usuarioRepository.save(usuario);

        return new AuthResponse(
                newAccessToken,
                newRefreshToken,
                jwtUtil.getExpiration(),
                usuario.getUsername(),
                usuario.getNombre(),
                usuario.getRol().name()
        );
    }
}
