package com.pastodeporte.sistema.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO para creación y edición de clubes deportivos.
 * El estado se calcula automáticamente según fechaFinReconocimiento.
 *
 * Pilar POO: ENCAPSULAMIENTO - datos validados antes de llegar al servicio.
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Data
public class ClubRequest {

    @NotBlank(message = "El nombre del club es obligatorio")
    @Size(max = 200)
    private String nombre;

    @Size(max = 20)
    private String numeroClub;

    @NotBlank(message = "La disciplina deportiva es obligatoria")
    @Size(max = 100)
    private String disciplinaDeportiva;

    private String descripcionDeportiva;

    @Size(max = 300)
    private String direccion;

    @Size(max = 20)
    private String telefono;

    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150)
    private String email;

    @Size(max = 100)
    private String numeroResolucion;

    private LocalDate fechaExpedicionResolucion;

    private LocalDate fechaInicioReconocimiento;
    private LocalDate fechaFinReconocimiento;

    private LocalDate fechaInicioOrganoAdmon;
    private LocalDate fechaFinOrganoAdmon;

    @Size(max = 200)
    private String representanteLegalNombre;

    @Size(max = 30)
    private String representanteLegalCedula;

    @Size(max = 100)
    private String representanteLegalCargo;

}
