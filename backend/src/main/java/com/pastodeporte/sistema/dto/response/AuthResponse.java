package com.pastodeporte.sistema.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para autenticacion exitosa.
 * Pilar POO aplicado: ENCAPSULAMIENTO - solo expone tokens, nunca la entidad Usuario.
 * Pilar POO aplicado: OCULTAMIENTO - el password nunca aparece en esta respuesta.
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private String username;
    private String nombre;
    private String rol;

    public AuthResponse(String accessToken, String refreshToken, Long expiresIn,
                        String username, String nombre, String rol) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = "Bearer";
        this.expiresIn = expiresIn;
        this.username = username;
        this.nombre = nombre;
        this.rol = rol;
    }
}
