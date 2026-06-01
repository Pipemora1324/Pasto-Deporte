package com.pastodeporte.sistema.dto.response;

import com.pastodeporte.sistema.model.enums.CargoDirectorio;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para informacion del directorio del club.
 * Pilar POO aplicado: ABSTRACCION - datos del directivo expuestos sin la entidad JPA.
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Data
public class DirectorioResponse {

    private Long id;
    private Long clubId;
    private String clubNombre;
    private String nombre;
    private String apellido;
    private String cedula;
    private CargoDirectorio cargo;
    private String cargoDescripcion;
    private String telefono;
    private String email;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDateTime fechaCreacion;
}
