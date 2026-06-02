package com.pastodeporte.sistema.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Clase base para todas las entidades del sistema.
 *
 * Pilar POO aplicado: HERENCIA (@MappedSuperclass) - todas las entidades heredan
 * id, fechaCreacion, fechaModificacion y eliminado (soft delete).
 * Pilar POO aplicado: ENCAPSULAMIENTO - atributos private con acceso controlado via Lombok.
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    /**
     * Identificador unico auto-generado.
     * Pilar POO: ENCAPSULAMIENTO - solo accesible via getter.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fecha y hora de creacion del registro.
     * Se asigna automaticamente antes de persistir.
     */
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Fecha y hora de la ultima modificacion del registro.
     * Se actualiza automaticamente en cada modificacion.
     */
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    /**
     * Indicador de eliminacion logica (soft delete).
     * Pilar POO: ENCAPSULAMIENTO - el borrado fisico nunca ocurre.
     */
    @Column(name = "eliminado", nullable = false)
    private Boolean eliminado = false;

    /**
     * Callback ejecutado automaticamente antes de insertar.
     * Pilar POO: OCULTAMIENTO - la logica de timestamps es interna.
     */
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaModificacion = LocalDateTime.now();
        if (this.eliminado == null) {
            this.eliminado = false;
        }
    }

    /**
     * Callback ejecutado automaticamente antes de actualizar.
     * Pilar POO: OCULTAMIENTO - la logica de timestamps es interna.
     */
    @PreUpdate
    protected void onUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }
}
