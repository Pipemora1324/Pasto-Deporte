package com.pastodeporte.sistema.service;

import com.pastodeporte.sistema.dto.request.DirectorioRequest;
import com.pastodeporte.sistema.dto.response.DirectorioResponse;

import java.util.List;

/**
 * Contrato para la gestion del directorio (cargos directivos) de los clubes.
 *
 * <p><b>Pilar POO — ABSTRACCION:</b> define las operaciones del directorio
 * sin revelar la implementacion concreta.</p>
 * <p><b>Pilar POO — POLIMORFISMO:</b> la implementacion es intercambiable
 * sin afectar a los controladores.</p>
 * <p><b>Pilar POO — MODULARIDAD:</b> responsabilidad del directorio separada
 * de la gestion de clubes y miembros.</p>
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
public interface IDirectorioService {

    /**
     * Lista todos los integrantes del directorio de un club.
     *
     * @param clubId identificador del club cuyo directorio se desea listar
     * @return lista de {@link DirectorioResponse} del club indicado
     */
    List<DirectorioResponse> listarPorClub(Long clubId);

    /**
     * Obtiene un integrante del directorio por su identificador.
     *
     * @param id identificador unico del integrante del directorio
     * @return {@link DirectorioResponse} con los datos del integrante
     */
    DirectorioResponse obtenerPorId(Long id);

    /**
     * Agrega una persona al directorio de un club.
     *
     * @param clubId  identificador del club al que pertenece el directorio
     * @param request DTO con los datos del nuevo integrante
     * @return {@link DirectorioResponse} del integrante creado
     */
    DirectorioResponse crear(Long clubId, DirectorioRequest request);

    /**
     * Actualiza los datos de un integrante del directorio.
     *
     * @param id      identificador del integrante a actualizar
     * @param request DTO con los nuevos datos del integrante
     * @return {@link DirectorioResponse} con los datos actualizados
     */
    DirectorioResponse actualizar(Long id, DirectorioRequest request);

    /**
     * Eliminacion logica de un integrante del directorio (soft delete).
     *
     * <p><b>Pilar POO — OCULTAMIENTO:</b> el borrado fisico nunca ocurre.</p>
     *
     * @param id identificador del integrante a eliminar
     */
    void eliminar(Long id);
}
