package com.pastodeporte.sistema.controller;

import com.pastodeporte.sistema.dto.request.MiembroOrganoRequest;
import com.pastodeporte.sistema.dto.response.MiembroOrganoResponse;
import com.pastodeporte.sistema.service.IMiembroOrganoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestion de miembros de organos de clubes.
 *
 * <p><b>Pilar POO — MODULARIDAD:</b> delega completamente a
 * {@link com.pastodeporte.sistema.service.IMiembroOrganoService}.</p>
 * <p><b>Pilar POO — ABSTRACCION:</b> el controlador trabaja contra la interfaz,
 * no contra la implementacion concreta.</p>
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
@RestController
@RequiredArgsConstructor
public class MiembroOrganoController {

    private final IMiembroOrganoService miembroService;

    /**
     * Lista todos los miembros de un club agrupables por tipoOrgano (publico).
     *
     * @param clubId identificador del club cuyos miembros se desean listar
     * @return {@link ResponseEntity} con lista de {@link MiembroOrganoResponse}
     */
    @GetMapping("/api/clubes/{clubId}/miembros")
    public ResponseEntity<List<MiembroOrganoResponse>> listar(@PathVariable Long clubId) {
        return ResponseEntity.ok(miembroService.listarPorClub(clubId));
    }

    /**
     * Agrega un miembro a un organo del club (protegido).
     *
     * @param clubId  identificador del club al que se agrega el miembro
     * @param request DTO con los datos del nuevo miembro
     * @return {@link ResponseEntity} con el {@link MiembroOrganoResponse} creado y HTTP 201
     */
    @PostMapping("/api/clubes/{clubId}/miembros")
    public ResponseEntity<MiembroOrganoResponse> crear(
            @PathVariable Long clubId,
            @Valid @RequestBody MiembroOrganoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(miembroService.crear(clubId, request));
    }

    /**
     * Actualiza los datos de un miembro (protegido).
     *
     * @param id      identificador del miembro a actualizar
     * @param request DTO con los nuevos datos del miembro
     * @return {@link ResponseEntity} con el {@link MiembroOrganoResponse} actualizado
     */
    @PutMapping("/api/miembros/{id}")
    public ResponseEntity<MiembroOrganoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MiembroOrganoRequest request) {
        return ResponseEntity.ok(miembroService.actualizar(id, request));
    }

    /**
     * Elimina logicamente un miembro del organo (protegido).
     *
     * <p><b>Pilar POO — OCULTAMIENTO:</b> el borrado fisico nunca ocurre.</p>
     *
     * @param id identificador del miembro a eliminar
     * @return {@link ResponseEntity} vacio con HTTP 204
     */
    @DeleteMapping("/api/miembros/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        miembroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
