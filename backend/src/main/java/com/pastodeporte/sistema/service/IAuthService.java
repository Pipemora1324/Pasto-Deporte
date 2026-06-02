package com.pastodeporte.sistema.service;

import com.pastodeporte.sistema.dto.request.LoginRequest;
import com.pastodeporte.sistema.dto.response.AuthResponse;

/**
 * Contrato para la autenticacion de administradores y gestion de tokens JWT.
 *
 * <p><b>Pilar POO — ABSTRACCION:</b> define el contrato de autenticacion
 * sin exponer la implementacion interna.</p>
 * <p><b>Pilar POO — OCULTAMIENTO:</b> el proceso interno de firma y validacion
 * de JWT queda oculto al controlador.</p>
 * <p><b>Pilar POO — POLIMORFISMO:</b> cualquier implementacion de esta interfaz
 * puede sustituirse sin modificar los controladores.</p>
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
public interface IAuthService {

    /**
     * Autentica un usuario con sus credenciales y genera tokens JWT.
     *
     * <p><b>Pilar POO — OCULTAMIENTO:</b> la verificacion de credenciales
     * y la firma del token son internas a la implementacion.</p>
     *
     * @param request DTO con {@code username} y {@code password} del administrador
     * @return {@link AuthResponse} con access token, refresh token y datos del usuario
     */
    AuthResponse login(LoginRequest request);

    /**
     * Renueva el access token usando un refresh token valido y persistido.
     *
     * @param refreshToken token de refresco previamente emitido al usuario
     * @return {@link AuthResponse} con el nuevo access token y refresh token rotado
     */
    AuthResponse refreshToken(String refreshToken);
}
