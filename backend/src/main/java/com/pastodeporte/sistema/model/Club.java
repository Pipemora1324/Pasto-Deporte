package com.pastodeporte.sistema.model;

import com.pastodeporte.sistema.model.enums.EstadoClub;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

/**
 * Entidad que representa un Club Deportivo registrado en Pasto Deporte.
 *
 * Pilar POO: HERENCIA - extiende BaseEntity obteniendo id, fechas y soft-delete.
 * Pilar POO: ENCAPSULAMIENTO - todos los atributos son private.
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Entity
@Table(name = "clubes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Club extends BaseEntity {

    /** Nombre oficial del club deportivo */
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    /** Número asignado por Pasto Deporte (ej: "068") */
    @Column(name = "numero_club", length = 20)
    private String numeroClub;

    /** Disciplina deportiva principal */
    @Column(name = "disciplina_deportiva", nullable = false, length = 100)
    private String disciplinaDeportiva;

    /** Descripción deportiva del club */
    @Column(name = "descripcion_deportiva", columnDefinition = "TEXT")
    private String descripcionDeportiva;

    /** Dirección física de la sede */
    @Column(name = "direccion", length = 300)
    private String direccion;

    /** Teléfono de contacto */
    @Column(name = "telefono", length = 20)
    private String telefono;

    /** Correo electrónico de contacto */
    @Column(name = "email", length = 150)
    private String email;

    /** Número de resolución oficial de Pasto Deporte (ej: "506") */
    @Column(name = "numero_resolucion", length = 100)
    private String numeroResolucion;

    /** Fecha en que se expidió la resolución */
    @Column(name = "fecha_expedicion_resolucion")
    private LocalDate fechaExpedicionResolucion;

    /** Fecha de inicio del reconocimiento deportivo */
    @Column(name = "fecha_inicio_reconocimiento")
    private LocalDate fechaInicioReconocimiento;

    /** Fecha de fin del reconocimiento deportivo — determina vigencia */
    @Column(name = "fecha_fin_reconocimiento")
    private LocalDate fechaFinReconocimiento;

    /** Fecha de inicio del período del órgano de administración */
    @Column(name = "fecha_inicio_organo_admon")
    private LocalDate fechaInicioOrganoAdmon;

    /** Fecha de fin del período del órgano de administración */
    @Column(name = "fecha_fin_organo_admon")
    private LocalDate fechaFinOrganoAdmon;

    /** Nombre completo del representante legal */
    @Column(name = "representante_legal_nombre", length = 200)
    private String representanteLegalNombre;

    /** Número de cédula del representante legal */
    @Column(name = "representante_legal_cedula", length = 30)
    private String representanteLegalCedula;

    /** Cargo del representante legal (ej: Presidente, Representante Legal) */
    @Column(name = "representante_legal_cargo", length = 100)
    private String representanteLegalCargo;

    /**
     * Estado de vigencia del club. Se calcula automáticamente según
     * fechaFinReconocimiento al guardar/actualizar.
     *
     * Pilar POO: ENCAPSULAMIENTO - cambio de estado solo vía servicios.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 15)
    private EstadoClub estado = EstadoClub.NO_VIGENTE;

}
