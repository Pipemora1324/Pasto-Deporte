package com.pastodeporte.sistema.service;

import com.pastodeporte.sistema.dto.response.OpcionMenuResponse;

import java.util.List;

/**
 * Contrato de operaciones para la construcción del menú de navegación recursivo.
 *
 * <p><b>Pilar POO — ABSTRACCIÓN:</b> define el <em>qué</em> hace el servicio de
 * menú sin revelar el <em>cómo</em>; el controlador solo conoce esta interfaz.</p>
 * <p><b>Pilar POO — POLIMORFISMO:</b> cualquier clase que implemente esta interfaz
 * puede sustituir a {@link com.pastodeporte.sistema.service.impl.OpcionMenuServiceImpl}
 * sin afectar al controlador ni a los clientes del servicio.</p>
 * <p><b>Pilar POO — MODULARIDAD:</b> separa la lógica recursiva de árbol del resto
 * del sistema, facilitando el mantenimiento y las pruebas.</p>
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
public interface IOpcionMenuService {

    /**
     * Construye el árbol completo del menú de navegación empezando desde
     * los nodos raíz (ítems sin padre).
     *
     * @return lista de {@link OpcionMenuResponse} de primer nivel, cada uno con
     *         su lista de hijos (y los hijos de los hijos, recursivamente)
     */
    List<OpcionMenuResponse> getMenuCompleto();

    /**
     * Método recursivo que construye el subárbol de menú a partir de un nodo padre.
     *
     * <p>Se llama a sí mismo por cada nivel de profundidad del árbol hasta
     * alcanzar nodos hoja (ítems sin hijos), demostrando así el concepto
     * de <strong>RECURSIVIDAD</strong>.</p>
     *
     * <p><b>Caso base:</b> cuando el nivel consultado no tiene hijos, retorna
     * una lista vacía deteniendo la recursión.</p>
     * <p><b>Caso recursivo:</b> para cada hijo encontrado se vuelve a invocar
     * {@code buildMenuRecursivo} con el ID de ese hijo como nuevo padre.</p>
     *
     * @param padreId ID del ítem padre del nivel a construir;
     *                {@code null} indica que se deben obtener los nodos raíz
     * @return lista de {@link OpcionMenuResponse} del nivel indicado,
     *         con sus respectivos hijos ya poblados
     */
    List<OpcionMenuResponse> buildMenuRecursivo(Long padreId);
}
