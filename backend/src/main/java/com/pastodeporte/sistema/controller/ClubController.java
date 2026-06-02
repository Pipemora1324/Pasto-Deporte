package com.pastodeporte.sistema.controller;

import com.pastodeporte.sistema.dto.request.ClubRequest;
import com.pastodeporte.sistema.dto.response.ClubResponse;
import com.pastodeporte.sistema.model.enums.EstadoClub;
import com.pastodeporte.sistema.service.IClubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/clubes")
@RequiredArgsConstructor
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
     * <p>Los errores son manejados centralmente por
     * {@link com.pastodeporte.sistema.exception.GlobalExceptionHandler}.</p>
     *
     * <p><b>Pilar POO — MODULARIDAD:</b> el controlador solo delega;
     * la logica de negocio vive en {@link IClubService}.</p>
     *
     * @param request DTO con los datos del nuevo club
     * @return {@link ResponseEntity} con el {@link ClubResponse} creado y HTTP 201
     */
    @PostMapping
    public ResponseEntity<ClubResponse> crear(@Valid @RequestBody ClubRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clubService.crear(request));
    }

    /**
     * Actualiza los datos de un club existente (protegido, requiere JWT).
     *
     * <p>Los errores son manejados centralmente por
     * {@link com.pastodeporte.sistema.exception.GlobalExceptionHandler}.</p>
     *
     * <p><b>Pilar POO — MODULARIDAD:</b> el controlador solo delega;
     * la logica de negocio vive en {@link IClubService}.</p>
     *
     * @param id      identificador del club a actualizar
     * @param request DTO con los nuevos datos del club
     * @return {@link ResponseEntity} con el {@link ClubResponse} actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClubResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClubRequest request) {
        return ResponseEntity.ok(clubService.actualizar(id, request));
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
