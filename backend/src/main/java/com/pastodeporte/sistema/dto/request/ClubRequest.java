package com.pastodeporte.sistema.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO de entrada para creacion y edicion de clubes deportivos.
 *
 * <p>Solo nombre y disciplina son obligatorios. Las demas campos son opcionales
 * para permitir guardado parcial desde el formulario.</p>
 *
 * <p>Pilares POO aplicados:</p>
 * <ul>
 *   <li><b>ENCAPSULAMIENTO:</b> atributos privados, acceso solo via getters/setters de Lombok.</li>
 *   <li><b>ABSTRACCION:</b> expone solo los datos necesarios para crear/editar un club.</li>
 *   <li><b>MODULARIDAD:</b> clase dedicada exclusivamente a la validacion de entrada.</li>
 * </ul>
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
@Data
public class ClubRequest {

    @NotBlank(message = "El nombre del club es obligatorio")
    @Size(min = 3, max = 200, message = "El nombre debe tener entre 3 y 200 caracteres")
    private String nombre;

    @Size(max = 20)
    private String numeroClub;

    @NotBlank(message = "La disciplina deportiva es obligatoria")
    @Size(max = 100, message = "La disciplina no puede superar 100 caracteres")
    private String disciplinaDeportiva;

    private String descripcionDeportiva;

    @Size(max = 300)
    private String direccion;

    @Size(max = 20)
    private String telefono;

    @Email(message = "El email no tiene un formato valido")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "El numero de resolucion es obligatorio")
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
