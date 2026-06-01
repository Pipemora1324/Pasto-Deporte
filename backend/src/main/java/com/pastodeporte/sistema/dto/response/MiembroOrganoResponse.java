package com.pastodeporte.sistema.dto.response;

import com.pastodeporte.sistema.model.enums.TipoOrgano;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para un miembro de un órgano del club.
 * Pilar POO: ABSTRACCION - expone datos sin revelar la entidad JPA.
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Data
public class MiembroOrganoResponse {

    private Long id;
    private Long clubId;
    private String clubNombre;
    private String nombre;
    private String cedula;
    private TipoOrgano tipoOrgano;
    private String tipoOrganoDescripcion;
    private String cargo;
    private LocalDateTime fechaCreacion;
}
