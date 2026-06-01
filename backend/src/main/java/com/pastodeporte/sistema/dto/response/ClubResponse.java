package com.pastodeporte.sistema.dto.response;

import com.pastodeporte.sistema.model.enums.EstadoClub;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para información de un club deportivo.
 * Pilar POO: ABSTRACCION - expone solo los datos necesarios al cliente.
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Data
public class ClubResponse {

    private Long id;
    private String nombre;
    private String numeroClub;
    private String disciplinaDeportiva;
    private String descripcionDeportiva;
    private String direccion;
    private String telefono;
    private String email;
    private String numeroResolucion;
    private LocalDate fechaExpedicionResolucion;

    // Reconocimiento deportivo
    private LocalDate fechaInicioReconocimiento;
    private LocalDate fechaFinReconocimiento;

    // Órgano de administración
    private LocalDate fechaInicioOrganoAdmon;
    private LocalDate fechaFinOrganoAdmon;

    // Representante legal
    private String representanteLegalNombre;
    private String representanteLegalCedula;
    private String representanteLegalCargo;

    private EstadoClub estado;
    private String estadoDescripcion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}
