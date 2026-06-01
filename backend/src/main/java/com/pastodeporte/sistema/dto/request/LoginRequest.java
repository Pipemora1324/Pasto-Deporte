package com.pastodeporte.sistema.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para la solicitud de autenticacion.
 * Pilar POO aplicado: ENCAPSULAMIENTO - datos de login encapsulados en objeto inmutable.
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Data
public class LoginRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contrasena es obligatoria")
    private String password;
}
