package com.pastodeporte.sistema.dto.request;

import com.pastodeporte.sistema.model.enums.TipoOrgano;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para agregar o editar un miembro de un órgano del club.
 * Pilar POO: ENCAPSULAMIENTO - datos validados antes de persistir.
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
@Data
public class MiembroOrganoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200)
    private String nombre;

    @Size(max = 20)
    private String cedula;

    @NotNull(message = "El tipo de órgano es obligatorio")
    private TipoOrgano tipoOrgano;

    /** Cargo dentro del órgano (ej: Presidente, Secretaria, Tesorera) */
    @Size(max = 100)
    private String cargo;
}
