package com.pastodeporte.sistema.service.impl;

import com.pastodeporte.sistema.dto.request.MiembroOrganoRequest;
import com.pastodeporte.sistema.dto.response.MiembroOrganoResponse;
import com.pastodeporte.sistema.exception.ResourceNotFoundException;
import com.pastodeporte.sistema.model.Club;
import com.pastodeporte.sistema.model.MiembroOrgano;
import com.pastodeporte.sistema.model.enums.TipoOrgano;
import com.pastodeporte.sistema.repository.ClubRepository;
import com.pastodeporte.sistema.repository.MiembroOrganoRepository;
import com.pastodeporte.sistema.service.IMiembroOrganoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementacion del servicio de miembros de organos directivos de clubes.
 *
 * <p><b>Pilar POO — POLIMORFISMO:</b> implementa {@link IMiembroOrganoService}
 * con {@code @Override} en cada metodo.</p>
 * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> el mapeo entidad→DTO y el formateo
 * de tipo de organo son metodos privados no expuestos al exterior.</p>
 * <p><b>Pilar POO — OCULTAMIENTO:</b> el soft delete y la validacion de
 * existencia del club son detalles internos del servicio.</p>
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MiembroOrganoServiceImpl implements IMiembroOrganoService {

    private final MiembroOrganoRepository miembroRepository;
    private final ClubRepository clubRepository;

    /**
     * Lista todos los miembros de todos los organos de un club.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IMiembroOrganoService}.</p>
     *
     * @param clubId identificador del club
     * @return lista de {@link MiembroOrganoResponse} del club indicado
     */
    @Override
    @Transactional(readOnly = true)
    public List<MiembroOrganoResponse> listarPorClub(Long clubId) {
        clubRepository.findByIdAndEliminadoFalse(clubId)
                .orElseThrow(() -> new ResourceNotFoundException("Club no encontrado con id: " + clubId));
        return miembroRepository.findByClubIdAndEliminadoFalse(clubId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Obtiene un miembro de organo por su identificador.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IMiembroOrganoService}.</p>
     *
     * @param id identificador del miembro
     * @return {@link MiembroOrganoResponse} con los datos del miembro
     */
    @Override
    @Transactional(readOnly = true)
    public MiembroOrganoResponse obtenerPorId(Long id) {
        return mapToResponse(miembroRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Miembro no encontrado con id: " + id)));
    }

    /**
     * Agrega un nuevo miembro a un organo del club.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IMiembroOrganoService}.</p>
     *
     * @param clubId  identificador del club al que pertenece el organo
     * @param request DTO con los datos del nuevo miembro
     * @return {@link MiembroOrganoResponse} del miembro recien creado
     */
    @Override
    public MiembroOrganoResponse crear(Long clubId, MiembroOrganoRequest request) {
        Club club = clubRepository.findByIdAndEliminadoFalse(clubId)
                .orElseThrow(() -> new ResourceNotFoundException("Club no encontrado con id: " + clubId));
        MiembroOrgano miembro = new MiembroOrgano();
        miembro.setClub(club);
        miembro.setNombre(request.getNombre());
        miembro.setCedula(request.getCedula());
        miembro.setTipoOrgano(request.getTipoOrgano());
        miembro.setCargo(request.getCargo());
        log.info("Miembro creado en club {}: {} - {}", clubId, request.getNombre(), request.getCargo());
        return mapToResponse(miembroRepository.save(miembro));
    }

    /**
     * Actualiza los datos de un miembro de organo existente.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IMiembroOrganoService}.</p>
     *
     * @param id      identificador del miembro a actualizar
     * @param request DTO con los nuevos datos
     * @return {@link MiembroOrganoResponse} con los datos actualizados
     */
    @Override
    public MiembroOrganoResponse actualizar(Long id, MiembroOrganoRequest request) {
        MiembroOrgano miembro = miembroRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Miembro no encontrado con id: " + id));
        miembro.setNombre(request.getNombre());
        miembro.setCedula(request.getCedula());
        miembro.setTipoOrgano(request.getTipoOrgano());
        miembro.setCargo(request.getCargo());
        return mapToResponse(miembroRepository.save(miembro));
    }

    /**
     * Eliminacion logica de un miembro (soft delete).
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IMiembroOrganoService}.</p>
     * <p><b>Pilar POO — OCULTAMIENTO:</b> el borrado fisico nunca ocurre.</p>
     *
     * @param id identificador del miembro a eliminar
     */
    @Override
    public void eliminar(Long id) {
        MiembroOrgano miembro = miembroRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Miembro no encontrado con id: " + id));
        miembro.setEliminado(true);
        miembroRepository.save(miembro);
    }

    /**
     * Convierte una entidad {@link MiembroOrgano} en su DTO de respuesta.
     *
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> mapeo privado que oculta la
     * estructura interna de la entidad JPA.</p>
     *
     * @param m entidad {@link MiembroOrgano} a convertir
     * @return {@link MiembroOrganoResponse} con los datos de presentacion
     */
    private MiembroOrganoResponse mapToResponse(MiembroOrgano m) {
        MiembroOrganoResponse r = new MiembroOrganoResponse();
        r.setId(m.getId());
        r.setClubId(m.getClub().getId());
        r.setClubNombre(m.getClub().getNombre());
        r.setNombre(m.getNombre());
        r.setCedula(m.getCedula());
        r.setTipoOrgano(m.getTipoOrgano());
        r.setTipoOrganoDescripcion(formatTipoOrgano(m.getTipoOrgano()));
        r.setCargo(m.getCargo());
        r.setFechaCreacion(m.getFechaCreacion());
        return r;
    }

    /**
     * Formatea el tipo de organo como texto legible en espanol.
     *
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> logica de formato privada e interna.</p>
     *
     * @param tipo valor del enum {@link TipoOrgano} a formatear
     * @return descripcion legible del tipo de organo
     */
    private String formatTipoOrgano(TipoOrgano tipo) {
        return switch (tipo) {
            case ORGANO_ADMINISTRACION -> "Órgano de Administración";
            case COMISION_DISCIPLINARIA -> "Comisión Disciplinaria";
            case ORGANO_CONTROL -> "Órgano de Control";
        };
    }
}
