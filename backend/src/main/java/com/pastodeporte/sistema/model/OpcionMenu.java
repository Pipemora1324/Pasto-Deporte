package com.pastodeporte.sistema.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa una opción del menú de navegación del sistema.
 *
 * <p>Implementa una estructura de árbol auto-referencial: cada opción puede
 * tener un padre (OpcionMenu) y ser padre de otras opciones (hijos).
 * Esto permite construir menús de cualquier profundidad de forma recursiva.</p>
 *
 * <p><b>Pilar POO — HERENCIA:</b> extiende {@link BaseEntity} para reutilizar
 * id, timestamps y soft-delete sin duplicar código.</p>
 * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> todos los atributos son private;
 * el acceso se realiza exclusivamente a través de los getters/setters de Lombok.</p>
 * <p><b>Pilar POO — ABSTRACCIÓN:</b> la relación auto-referencial oculta la
 * complejidad de la estructura de árbol al resto del sistema.</p>
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 * @see BaseEntity
 * @see com.pastodeporte.sistema.service.IOpcionMenuService
 */
@Entity
@Table(name = "opciones_menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpcionMenu extends BaseEntity {

    /**
     * Etiqueta visible del ítem de menú (ej: "Dashboard", "Gestión de Clubes").
     * Es obligatorio y tiene un máximo de 100 caracteres.
     */
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /**
     * Ruta Angular a la que navega este ítem (ej: "/admin/dashboard").
     * Puede ser null si el ítem es solo un contenedor de hijos.
     */
    @Column(name = "ruta", length = 200)
    private String ruta;

    /**
     * Nombre del ícono de Material Icons que se muestra junto al ítem
     * (ej: "dashboard", "groups", "settings").
     */
    @Column(name = "icono", length = 50)
    private String icono;

    /**
     * Número de orden para renderizar los ítems de izquierda a derecha
     * o de arriba a abajo dentro del mismo nivel del árbol.
     */
    @Column(name = "orden", nullable = false)
    private Integer orden = 0;

    /**
     * Referencia al ítem padre en la jerarquía del menú.
     *
     * <p><b>Pilar POO — ABSTRACCIÓN:</b> la auto-referencia encapsula la
     * estructura de árbol dentro de la propia entidad, sin necesidad de
     * tablas intermedias.</p>
     *
     * <p>Si {@code padre} es {@code null} el ítem es un nodo raíz del menú.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "padre_id")
    private OpcionMenu padre;
}
