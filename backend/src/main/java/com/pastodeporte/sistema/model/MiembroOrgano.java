package com.pastodeporte.sistema.model;

import com.pastodeporte.sistema.model.enums.TipoOrgano;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entidad que representa un miembro de un órgano directivo de un club.
 *
 * Pilar POO: HERENCIA - extiende BaseEntity.
 * Pilar POO: ENCAPSULAMIENTO - datos del miembro protegidos con acceso vía getters.
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Entity
@Table(name = "miembros_organo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MiembroOrgano extends BaseEntity {

    /**
     * Club al que pertenece el miembro del órgano.
     * Pilar POO: ENCAPSULAMIENTO - relación encapsulada.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    /** Nombre completo del miembro */
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    /** Número de cédula del miembro */
    @Column(name = "cedula", length = 20)
    private String cedula;

    /**
     * Tipo de órgano al que pertenece el miembro.
     * Pilar POO: ABSTRACCION - enum limita los valores posibles.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_organo", nullable = false, length = 30)
    private TipoOrgano tipoOrgano;

    /** Cargo dentro del órgano (ej: Presidente, Secretaria, Tesorera) */
    @Column(name = "cargo", length = 100)
    private String cargo;
}
