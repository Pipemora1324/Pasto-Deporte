package com.pastodeporte.sistema.controller;

import com.pastodeporte.sistema.dto.response.OpcionMenuResponse;
import com.pastodeporte.sistema.service.IOpcionMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para exponer el árbol del menú de navegación.
 *
 * <p>Expone un único endpoint público ({@code GET /api/menu}) que retorna
 * el árbol completo del menú construido recursivamente por
 * {@link IOpcionMenuService#getMenuCompleto()}.</p>
 *
 * <p><b>Pilar POO — ABSTRACCIÓN:</b> el controlador solo conoce la interfaz
 * {@link IOpcionMenuService}, no la implementación concreta.</p>
 * <p><b>Pilar POO — MODULARIDAD:</b> delega completamente la construcción
 * del árbol al servicio; no contiene lógica de negocio propia.</p>
 * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> la complejidad recursiva queda
 * encapsulada en la capa de servicio, invisible para este controlador.</p>
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 * @see IOpcionMenuService
 */
@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
@Slf4j
public class OpcionMenuController {

    private final IOpcionMenuService opcionMenuService;

    /**
     * Retorna el árbol completo del menú de navegación del panel de administración.
     *
     * <p>Este endpoint es <strong>público</strong> (no requiere JWT) y es consumido
     * por el componente {@code MenuItemComponent} del frontend Angular para renderizar
     * el menú del sidebar de forma dinámica y recursiva.</p>
     *
     * @return {@link ResponseEntity} con la lista de ítems raíz del menú,
     *         cada uno con su árbol de hijos ya construido recursivamente
     */
    @GetMapping
    public ResponseEntity<List<OpcionMenuResponse>> getMenu() {
        log.debug("[Menu] Solicitud de menú recibida");
        return ResponseEntity.ok(opcionMenuService.getMenuCompleto());
    }
}
