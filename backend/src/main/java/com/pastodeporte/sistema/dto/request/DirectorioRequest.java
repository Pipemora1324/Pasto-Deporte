package com.pastodeporte.sistema.dto.request;

import com.pastodeporte.sistema.model.enums.CargoDirectorio;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO para creacion y edicion de integrantes del directorio.
 * Pilar POO aplicado: ENCAPSULAMIENTO - datos del directivo validados antes de procesar.
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Data
public class DirectorioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    private String cedula;

    @NotNull(message = "El cargo es obligatorio")
    private CargoDirectorio cargo;

    private String telefono;

    @Email(message = "El email debe tener un formato valido")
    private String email;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
