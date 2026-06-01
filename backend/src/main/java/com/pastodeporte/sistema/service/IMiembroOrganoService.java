package com.pastodeporte.sistema.service;

import com.pastodeporte.sistema.dto.request.MiembroOrganoRequest;
import com.pastodeporte.sistema.dto.response.MiembroOrganoResponse;

import java.util.List;

/**
 * Contrato para la gestion de miembros de organos directivos de clubes.
 *
 * <p><b>Pilar POO — ABSTRACCION:</b> define las operaciones sin revelar
 * la implementacion concreta.</p>
 * <p><b>Pilar POO — POLIMORFISMO:</b> permite multiples implementaciones
 * del servicio sin afectar a los controladores.</p>
 * <p><b>Pilar POO — MODULARIDAD:</b> separa la logica de miembros de
 * otras responsabilidades del sistema.</p>
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
public interface IMiembroOrganoService {

    /**
     * Lista todos los miembros de todos los organos de un club.
     *
     * @param clubId identificador del club cuyos miembros se desean listar
     * @return lista de {@link MiembroOrganoResponse} del club indicado
     */
    List<MiembroOrganoResponse> listarPorClub(Long clubId);

    /**
     * Obtiene un miembro de organo por su identificador.
     *
     * @param id identificador unico del miembro
     * @return {@link MiembroOrganoResponse} con los datos del miembro
     */
    MiembroOrganoResponse obtenerPorId(Long id);

    /**
     * Agrega un nuevo miembro a un organo del club.
     *
     * @param clubId  identificador del club al que pertenece el organo
     * @param request DTO con los datos del nuevo miembro
     * @return {@link MiembroOrganoResponse} del miembro creado
     */
    MiembroOrganoResponse crear(Long clubId, MiembroOrganoRequest request);

    /**
     * Actualiza los datos de un miembro de organo existente.
     *
     * @param id      identificador del miembro a actualizar
     * @param request DTO con los nuevos datos del miembro
     * @return {@link MiembroOrganoResponse} con los datos actualizados
     */
    MiembroOrganoResponse actualizar(Long id, MiembroOrganoRequest request);

    /**
     * Eliminacion logica de un miembro (soft delete).
     *
     * <p><b>Pilar POO — OCULTAMIENTO:</b> el borrado fisico nunca ocurre.</p>
     *
     * @param id identificador del miembro a eliminar
     */
    void eliminar(Long id);
}
