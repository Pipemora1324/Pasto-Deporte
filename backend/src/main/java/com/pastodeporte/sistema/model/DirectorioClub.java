package com.pastodeporte.sistema.model;

import com.pastodeporte.sistema.model.enums.CargoDirectorio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

/**
 * Entidad que representa una persona del directorio (cargos directivos) de un club.
 *
 * Pilar POO aplicado: HERENCIA - extiende BaseEntity.
 * Pilar POO aplicado: ENCAPSULAMIENTO - atributos private, relacion ManyToOne controlada.
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Entity
@Table(name = "directorio_club")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DirectorioClub extends BaseEntity {

    /**
     * Club al que pertenece esta persona del directorio.
     * Pilar POO: ENCAPSULAMIENTO - relacion controlada, no expuesta directamente.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    /** Nombre de la persona en el cargo directivo */
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /** Apellido de la persona en el cargo directivo */
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    /** Numero de cedula de identidad */
    @Column(name = "cedula", length = 20)
    private String cedula;

    /**
     * Cargo que ocupa dentro del directorio del club.
     * Pilar POO: ABSTRACCION - usa enum para limitar valores posibles.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "cargo", nullable = false, length = 30)
    private CargoDirectorio cargo;

    /** Telefono de contacto */
    @Column(name = "telefono", length = 20)
    private String telefono;

    /** Correo electronico de contacto */
    @Column(name = "email", length = 150)
    private String email;

    /** Fecha en que inicio en el cargo */
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    /** Fecha en que termina o termino en el cargo */
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
}
