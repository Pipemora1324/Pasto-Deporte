package com.pastodeporte.sistema.controller;

import com.pastodeporte.sistema.dto.request.LoginRequest;
import com.pastodeporte.sistema.dto.response.AuthResponse;
import com.pastodeporte.sistema.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST para autenticacion de administradores.
 *
 * <p><b>Pilar POO — MODULARIDAD:</b> delega completamente al servicio,
 * sin logica de negocio propia.</p>
 * <p><b>Pilar POO — ABSTRACCION:</b> interactua con {@link IAuthService},
 * no con la implementacion concreta.</p>
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    /**
     * Autentica un usuario y retorna tokens JWT de acceso y refresco.
     *
     * <p><b>Pilar POO — MODULARIDAD:</b> delega la autenticacion a {@link IAuthService}.</p>
     *
     * @param request DTO con username y password del administrador
     * @return {@link ResponseEntity} con {@link AuthResponse} que contiene los tokens JWT
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Renueva el access token usando un refresh token valido.
     *
     * <p><b>Pilar POO — OCULTAMIENTO:</b> la logica de renovacion de tokens
     * queda encapsulada en el servicio.</p>
     *
     * @param body mapa JSON con la clave {@code "refreshToken"}
     * @return {@link ResponseEntity} con {@link AuthResponse} que contiene los nuevos tokens
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }
}
