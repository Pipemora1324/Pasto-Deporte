package com.pastodeporte.sistema.service.impl;

import com.pastodeporte.sistema.dto.response.OpcionMenuResponse;
import com.pastodeporte.sistema.model.OpcionMenu;
import com.pastodeporte.sistema.repository.OpcionMenuRepository;
import com.pastodeporte.sistema.service.IOpcionMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de construcción del menú de navegación recursivo.
 *
 * <p>El método central de esta clase, {@link #buildMenuRecursivo(Long)}, demuestra
 * el concepto de <strong>RECURSIVIDAD</strong>: se invoca a sí mismo para cada nivel
 * del árbol de menú hasta alcanzar nodos hoja (sin hijos), momento en que retorna
 * una lista vacía (caso base) y la pila de llamadas se desenrolla hacia arriba.</p>
 *
 * <p><b>Pilar POO — POLIMORFISMO:</b> implementa {@link IOpcionMenuService} con
 * {@code @Override} en cada método; el controlador trabaja contra la interfaz,
 * no contra esta clase concreta.</p>
 * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> el método privado {@code mapToResponse}
 * oculta la transformación entidad→DTO al resto de la clase.</p>
 * <p><b>Pilar POO — MODULARIDAD:</b> la lógica de árbol recursivo está
 * completamente aislada en este servicio.</p>
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 * @see IOpcionMenuService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OpcionMenuServiceImpl implements IOpcionMenuService {

    private final OpcionMenuRepository opcionMenuRepository;

    /**
     * Punto de entrada público: obtiene el árbol completo del menú
     * delegando a {@link #buildMenuRecursivo(Long)} con {@code null}
     * para indicar que se parte desde los nodos raíz.
     *
     * @return lista de {@link OpcionMenuResponse} de primer nivel,
     *         cada uno con su árbol de hijos ya construido
     */
    @Override
    public List<OpcionMenuResponse> getMenuCompleto() {
        log.debug("[Menu] Iniciando construcción del árbol de menú desde la raíz");
        return buildMenuRecursivo(null);
    }

    /**
     * Construye recursivamente el subárbol de menú a partir del nodo identificado
     * por {@code padreId}.
     *
     * <p><strong>Caso base:</strong> si el nivel consultado no tiene ítems, retorna
     * una lista vacía, deteniendo la recursión.</p>
     * <p><strong>Caso recursivo:</strong> por cada ítem del nivel actual se invoca
     * {@code buildMenuRecursivo(item.getId())} para poblar los hijos, y así
     * sucesivamente hasta alcanzar el caso base.</p>
     *
     * @param padreId ID del ítem padre cuyo subárbol se desea construir;
     *                {@code null} indica que se deben obtener los nodos raíz
     * @return lista de {@link OpcionMenuResponse} del nivel indicado,
     *         con su lista de {@code hijos} completamente poblada
     */
    @Override
    public List<OpcionMenuResponse> buildMenuRecursivo(Long padreId) {
        List<OpcionMenu> items = (padreId == null)
                ? opcionMenuRepository.findByPadreIsNullAndEliminadoFalseOrderByOrdenAsc()
                : opcionMenuRepository.findByPadreIdAndEliminadoFalseOrderByOrdenAsc(padreId);

        if (items.isEmpty()) {
            return new ArrayList<>(); // caso base: nodo hoja sin hijos
        }

        return items.stream().map(item -> {
            OpcionMenuResponse resp = mapToResponse(item);
            resp.setHijos(buildMenuRecursivo(item.getId())); // caso recursivo
            return resp;
        }).collect(Collectors.toList());
    }

    /**
     * Transforma una entidad {@link OpcionMenu} en su DTO de respuesta.
     *
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> método privado que oculta la lógica
     * de mapeo; solo {@link #buildMenuRecursivo(Long)} lo invoca.</p>
     *
     * @param item entidad {@link OpcionMenu} a transformar
     * @return {@link OpcionMenuResponse} con los datos de presentación del ítem
     */
    private OpcionMenuResponse mapToResponse(OpcionMenu item) {
        OpcionMenuResponse r = new OpcionMenuResponse();
        r.setId(item.getId());
        r.setNombre(item.getNombre());
        r.setRuta(item.getRuta());
        r.setIcono(item.getIcono());
        r.setOrden(item.getOrden());
        return r;
    }
}
