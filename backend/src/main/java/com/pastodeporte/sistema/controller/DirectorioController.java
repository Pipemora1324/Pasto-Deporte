package com.pastodeporte.sistema.controller;

import com.pastodeporte.sistema.dto.request.DirectorioRequest;
import com.pastodeporte.sistema.dto.response.DirectorioResponse;
import com.pastodeporte.sistema.service.IDirectorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestion del directorio (cargos directivos) de los clubes.
 *
 * <p><b>Pilar POO — MODULARIDAD:</b> delega completamente a
 * {@link com.pastodeporte.sistema.service.IDirectorioService}.</p>
 * <p><b>Pilar POO — ABSTRACCION:</b> no depende de la implementacion concreta
 * del servicio de directorio.</p>
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class DirectorioController {

    private final IDirectorioService directorioService;

    /**
     * Lista todos los integrantes del directorio de un club (publico).
     *
     * @param clubId identificador del club cuyo directorio se desea listar
     * @return {@link ResponseEntity} con lista de {@link DirectorioResponse}
     */
    @GetMapping("/api/clubes/{clubId}/directorio")
    public ResponseEntity<List<DirectorioResponse>> listarPorClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(directorioService.listarPorClub(clubId));
    }

    /**
     * Agrega una persona al directorio de un club (protegido).
     *
     * @param clubId  identificador del club al que se agrega el integrante
     * @param request DTO con los datos del nuevo integrante del directorio
     * @return {@link ResponseEntity} con el {@link DirectorioResponse} creado y HTTP 201
     */
    @PostMapping("/api/clubes/{clubId}/directorio")
    public ResponseEntity<DirectorioResponse> crear(
            @PathVariable Long clubId,
            @Valid @RequestBody DirectorioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(directorioService.crear(clubId, request));
    }

    /**
     * Actualiza los datos de un integrante del directorio (protegido).
     *
     * @param id      identificador del integrante a actualizar
     * @param request DTO con los nuevos datos del integrante
     * @return {@link ResponseEntity} con el {@link DirectorioResponse} actualizado
     */
    @PutMapping("/api/directorio/{id}")
    public ResponseEntity<DirectorioResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DirectorioRequest request) {
        return ResponseEntity.ok(directorioService.actualizar(id, request));
    }

    /**
     * Elimina logicamente un integrante del directorio (protegido).
     *
     * <p><b>Pilar POO — OCULTAMIENTO:</b> el borrado fisico nunca ocurre.</p>
     *
     * @param id identificador del integrante a eliminar
     * @return {@link ResponseEntity} vacio con HTTP 204
     */
    @DeleteMapping("/api/directorio/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        directorioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
