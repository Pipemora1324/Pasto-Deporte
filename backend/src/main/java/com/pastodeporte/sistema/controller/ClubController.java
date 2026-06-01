package com.pastodeporte.sistema.controller;

import com.pastodeporte.sistema.dto.request.ClubRequest;
import com.pastodeporte.sistema.dto.response.ClubResponse;
import com.pastodeporte.sistema.model.enums.EstadoClub;
import com.pastodeporte.sistema.service.IClubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestion de clubes deportivos.
 *
 * <p><b>Pilar POO — MODULARIDAD:</b> delega completamente a
 * {@link com.pastodeporte.sistema.service.IClubService}.</p>
 * <p><b>Pilar POO — ABSTRACCION:</b> el controlador no conoce la implementacion
 * concreta del servicio de clubes.</p>
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@RestController
@RequestMapping("/api/clubes")
@RequiredArgsConstructor
@Slf4j
public class ClubController {

    private final IClubService clubService;

    /**
     * Lista todos los clubes activos no eliminados (publico).
     *
     * @return {@link ResponseEntity} con lista de {@link ClubResponse}
     */
    @GetMapping
    public ResponseEntity<List<ClubResponse>> listarTodos() {
        return ResponseEntity.ok(clubService.listarTodos());
    }

    /**
     * Busca clubes con filtros combinados de nombre, disciplina y estado (publico).
     *
     * <p><b>Pilar POO — ABSTRACCION:</b> los filtros se pasan como parametros opcionales;
     * la logica de construccion de la consulta queda oculta en el servicio.</p>
     *
     * @param nombre      fragmento del nombre del club (opcional, ignorar mayusculas)
     * @param disciplina  fragmento de la disciplina deportiva (opcional)
     * @param estado      estado de vigencia {@link EstadoClub} a filtrar (opcional)
     * @return {@link ResponseEntity} con lista de {@link ClubResponse} que cumplen los filtros
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<ClubResponse>> buscar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String disciplina,
            @RequestParam(required = false) EstadoClub estado) {
        return ResponseEntity.ok(clubService.buscarConFiltros(nombre, disciplina, estado));
    }

    /**
     * Obtiene el detalle de un club por su identificador (publico).
     *
     * @param id identificador del club
     * @return {@link ResponseEntity} con el {@link ClubResponse} correspondiente
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClubResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clubService.obtenerPorId(id));
    }

    /**
     * Crea un nuevo club deportivo (protegido, requiere JWT).
     *
     * @param request DTO con los datos del nuevo club
     * @return {@link ResponseEntity} con el {@link ClubResponse} creado y HTTP 201,
     *         o HTTP 400 con mensaje de error si la validacion falla
     */
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody ClubRequest request) {
        try {
            log.info(">>> Recibiendo club: {}", request);
            ClubResponse response = clubService.crear(request);
            log.info(">>> Club creado con id: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error(">>> ERROR al crear club: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Actualiza los datos de un club existente (protegido, requiere JWT).
     *
     * @param id      identificador del club a actualizar
     * @param request DTO con los nuevos datos del club
     * @return {@link ResponseEntity} con el {@link ClubResponse} actualizado,
     *         o HTTP 400 con mensaje de error si la validacion falla
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClubRequest request) {
        try {
            log.info(">>> Actualizando club id={}: {}", id, request);
            ClubResponse response = clubService.actualizar(id, request);
            log.info(">>> Club id={} actualizado", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(">>> ERROR al actualizar club id={}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Elimina logicamente un club (protegido, requiere JWT).
     *
     * <p><b>Pilar POO — OCULTAMIENTO:</b> el borrado fisico nunca ocurre
     * (soft delete via flag {@code eliminado}).</p>
     *
     * @param id identificador del club a eliminar
     * @return {@link ResponseEntity} vacio con HTTP 204
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clubService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
