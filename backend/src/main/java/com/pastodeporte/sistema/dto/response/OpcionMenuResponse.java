package com.pastodeporte.sistema.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO de respuesta que representa un ítem del menú de navegación,
 * incluyendo la lista recursiva de sus ítems hijos.
 *
 * <p><b>Pilar POO — ABSTRACCIÓN:</b> expone solo los datos necesarios para
 * renderizar el menú en el frontend, ocultando detalles de persistencia.</p>
 * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> los atributos son private y se acceden
 * mediante getters/setters generados por Lombok {@code @Data}.</p>
 * <p><b>Pilar POO — MODULARIDAD:</b> la lista {@code hijos} permite representar
 * árboles de cualquier profundidad sin cambiar la estructura del DTO.</p>
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpcionMenuResponse {

    /** Identificador único del ítem de menú. */
    private Long id;

    /** Etiqueta visible del ítem (ej: "Dashboard"). */
    private String nombre;

    /** Ruta de navegación Angular (ej: "/admin/dashboard"). */
    private String ruta;

    /** Nombre del ícono de Material Icons. */
    private String icono;

    /** Posición del ítem dentro de su nivel en el árbol. */
    private Integer orden;

    /**
     * Lista de ítems hijos directos de este nodo.
     *
     * <p>Cada elemento de la lista puede a su vez contener su propia lista
     * de hijos, formando un árbol N-ario de profundidad arbitraria.
     * Esto es lo que permite la renderización recursiva en el frontend.</p>
     */
    private List<OpcionMenuResponse> hijos = new ArrayList<>();
}
