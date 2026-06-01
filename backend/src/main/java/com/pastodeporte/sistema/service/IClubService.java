package com.pastodeporte.sistema.service;

import com.pastodeporte.sistema.dto.request.ClubRequest;
import com.pastodeporte.sistema.dto.response.ClubResponse;
import com.pastodeporte.sistema.model.enums.EstadoClub;

import java.util.List;

/**
 * Contrato de operaciones para la gestion de clubes deportivos.
 *
 * <p><b>Pilar POO — ABSTRACCION:</b> define las operaciones sin revelar
 * la implementacion concreta.</p>
 * <p><b>Pilar POO — POLIMORFISMO:</b> el controlador solo conoce esta interfaz;
 * la implementacion puede cambiarse sin afectar la capa superior.</p>
 * <p><b>Pilar POO — MODULARIDAD:</b> toda la logica de clubes esta encapsulada
 * en este contrato y su implementacion.</p>
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
public interface IClubService {

    /**
     * Obtiene todos los clubes activos (no eliminados logicamente).
     *
     * @return lista de {@link ClubResponse} de todos los clubes activos
     */
    List<ClubResponse> listarTodos();

    /**
     * Busca clubes con filtros combinados de nombre, disciplina y estado.
     *
     * @param nombre     fragmento del nombre del club a filtrar (puede ser null)
     * @param disciplina fragmento de la disciplina deportiva a filtrar (puede ser null)
     * @param estado     estado de vigencia {@link EstadoClub} a filtrar (puede ser null)
     * @return lista de {@link ClubResponse} que cumplen los filtros aplicados
     */
    List<ClubResponse> buscarConFiltros(String nombre, String disciplina, EstadoClub estado);

    /**
     * Obtiene un club por su identificador unico.
     *
     * @param id identificador del club
     * @return {@link ClubResponse} con los datos del club
     */
    ClubResponse obtenerPorId(Long id);

    /**
     * Crea un nuevo club deportivo en el sistema.
     *
     * @param request DTO con los datos del nuevo club
     * @return {@link ClubResponse} del club recien creado con su ID asignado
     */
    ClubResponse crear(ClubRequest request);

    /**
     * Actualiza los datos de un club existente.
     *
     * @param id      identificador del club a actualizar
     * @param request DTO con los nuevos datos del club
     * @return {@link ClubResponse} con los datos actualizados
     */
    ClubResponse actualizar(Long id, ClubRequest request);

    /**
     * Elimina logicamente un club (soft delete).
     *
     * <p><b>Pilar POO — OCULTAMIENTO:</b> el borrado fisico nunca ocurre;
     * solo se marca el flag {@code eliminado = true}.</p>
     *
     * @param id identificador del club a eliminar
     */
    void eliminar(Long id);
}
