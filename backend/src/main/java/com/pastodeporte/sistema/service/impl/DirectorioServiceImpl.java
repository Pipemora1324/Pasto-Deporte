package com.pastodeporte.sistema.service.impl;

import com.pastodeporte.sistema.dto.request.DirectorioRequest;
import com.pastodeporte.sistema.dto.response.DirectorioResponse;
import com.pastodeporte.sistema.exception.ResourceNotFoundException;
import com.pastodeporte.sistema.model.Club;
import com.pastodeporte.sistema.model.DirectorioClub;
import com.pastodeporte.sistema.repository.ClubRepository;
import com.pastodeporte.sistema.repository.DirectorioClubRepository;
import com.pastodeporte.sistema.service.IDirectorioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementacion del servicio para gestion del directorio de cargos directivos de clubes.
 *
 * <p><b>Pilar POO — POLIMORFISMO:</b> implementa {@link IDirectorioService}
 * con {@code @Override} en cada metodo.</p>
 * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> el mapeo entidad→DTO y la copia de
 * datos del request estan encapsulados en metodos privados.</p>
 * <p><b>Pilar POO — OCULTAMIENTO:</b> el soft delete y la validacion de
 * existencia del club son detalles internos del servicio.</p>
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DirectorioServiceImpl implements IDirectorioService {

    private final DirectorioClubRepository directorioRepository;
    private final ClubRepository clubRepository;

    /**
     * Lista todos los integrantes del directorio de un club.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IDirectorioService}.</p>
     *
     * @param clubId identificador del club cuyo directorio se desea listar
     * @return lista de {@link DirectorioResponse} del club indicado
     */
    @Override
    @Transactional(readOnly = true)
    public List<DirectorioResponse> listarPorClub(Long clubId) {
        clubRepository.findByIdAndEliminadoFalse(clubId)
                .orElseThrow(() -> new ResourceNotFoundException("Club no encontrado con id: " + clubId));
        return directorioRepository.findByClubIdAndEliminadoFalse(clubId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Obtiene un integrante del directorio por su identificador.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IDirectorioService}.</p>
     *
     * @param id identificador del integrante del directorio
     * @return {@link DirectorioResponse} con los datos del integrante
     */
    @Override
    @Transactional(readOnly = true)
    public DirectorioResponse obtenerPorId(Long id) {
        DirectorioClub directorio = directorioRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Directorio no encontrado con id: " + id));
        return mapToResponse(directorio);
    }

    /**
     * Agrega una persona al directorio de un club.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IDirectorioService}.</p>
     *
     * @param clubId  identificador del club al que pertenece el directorio
     * @param request DTO con los datos del nuevo integrante
     * @return {@link DirectorioResponse} del integrante recien creado
     */
    @Override
    public DirectorioResponse crear(Long clubId, DirectorioRequest request) {
        Club club = clubRepository.findByIdAndEliminadoFalse(clubId)
                .orElseThrow(() -> new ResourceNotFoundException("Club no encontrado con id: " + clubId));
        DirectorioClub directorio = new DirectorioClub();
        directorio.setClub(club);
        mapRequestToDirectorio(request, directorio);
        return mapToResponse(directorioRepository.save(directorio));
    }

    /**
     * Actualiza los datos de un integrante del directorio.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IDirectorioService}.</p>
     *
     * @param id      identificador del integrante a actualizar
     * @param request DTO con los nuevos datos del integrante
     * @return {@link DirectorioResponse} con los datos actualizados
     */
    @Override
    public DirectorioResponse actualizar(Long id, DirectorioRequest request) {
        DirectorioClub directorio = directorioRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Directorio no encontrado con id: " + id));
        mapRequestToDirectorio(request, directorio);
        return mapToResponse(directorioRepository.save(directorio));
    }

    /**
     * Eliminacion logica de un integrante del directorio (soft delete).
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IDirectorioService}.</p>
     * <p><b>Pilar POO — OCULTAMIENTO:</b> el borrado fisico nunca ocurre.</p>
     *
     * @param id identificador del integrante a eliminar
     */
    @Override
    public void eliminar(Long id) {
        DirectorioClub directorio = directorioRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Directorio no encontrado con id: " + id));
        directorio.setEliminado(true);
        directorioRepository.save(directorio);
    }

    /**
     * Copia los datos del DTO de solicitud hacia la entidad {@link DirectorioClub}.
     *
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> mapeo privado que oculta los
     * detalles de conversion.</p>
     *
     * @param request    DTO con los datos a copiar
     * @param directorio entidad destino donde se aplican los datos
     */
    private void mapRequestToDirectorio(DirectorioRequest request, DirectorioClub directorio) {
        directorio.setNombre(request.getNombre());
        directorio.setApellido(request.getApellido());
        directorio.setCedula(request.getCedula());
        directorio.setCargo(request.getCargo());
        directorio.setTelefono(request.getTelefono());
        directorio.setEmail(request.getEmail());
        directorio.setFechaInicio(request.getFechaInicio());
        directorio.setFechaFin(request.getFechaFin());
    }

    /**
     * Convierte una entidad {@link DirectorioClub} en su DTO de respuesta.
     *
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> mapeo privado que oculta la
     * estructura interna de la entidad JPA.</p>
     *
     * @param d entidad {@link DirectorioClub} a convertir
     * @return {@link DirectorioResponse} con los datos de presentacion
     */
    private DirectorioResponse mapToResponse(DirectorioClub d) {
        DirectorioResponse response = new DirectorioResponse();
        response.setId(d.getId());
        response.setClubId(d.getClub().getId());
        response.setClubNombre(d.getClub().getNombre());
        response.setNombre(d.getNombre());
        response.setApellido(d.getApellido());
        response.setCedula(d.getCedula());
        response.setCargo(d.getCargo());
        response.setCargoDescripcion(d.getCargo() != null ? d.getCargo().name().replace("_", " ") : "");
        response.setTelefono(d.getTelefono());
        response.setEmail(d.getEmail());
        response.setFechaInicio(d.getFechaInicio());
        response.setFechaFin(d.getFechaFin());
        response.setFechaCreacion(d.getFechaCreacion());
        return response;
    }
}
