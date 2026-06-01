package com.pastodeporte.sistema.service.impl;

import com.pastodeporte.sistema.dto.request.ClubRequest;
import com.pastodeporte.sistema.dto.response.ClubResponse;
import com.pastodeporte.sistema.exception.ResourceNotFoundException;
import com.pastodeporte.sistema.model.Club;
import com.pastodeporte.sistema.model.enums.EstadoClub;
import com.pastodeporte.sistema.repository.ClubRepository;
import com.pastodeporte.sistema.service.IClubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementacion del servicio de gestion de clubes deportivos.
 *
 * <p><b>Pilar POO — POLIMORFISMO:</b> implementa {@link IClubService}
 * con {@code @Override} en cada metodo.</p>
 * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> la conversion entidad→DTO y la logica
 * de calculo de estado son metodos privados no expuestos al exterior.</p>
 * <p><b>Pilar POO — OCULTAMIENTO:</b> el soft delete y el calculo automatico
 * de estado de vigencia son detalles internos del servicio.</p>
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClubServiceImpl implements IClubService {

    private final ClubRepository clubRepository;

    /**
     * Obtiene todos los clubes activos.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IClubService}.</p>
     *
     * @return lista de {@link ClubResponse} de todos los clubes no eliminados
     */
    @Override
    @Transactional(readOnly = true)
    public List<ClubResponse> listarTodos() {
        return clubRepository.findByEliminadoFalse()
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Busca clubes con filtros combinados opcionales.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IClubService}.</p>
     *
     * @param nombre     fragmento del nombre (puede ser null para omitir el filtro)
     * @param disciplina fragmento de la disciplina (puede ser null)
     * @param estado     estado de vigencia a filtrar (puede ser null)
     * @return lista de {@link ClubResponse} que cumplen los filtros
     */
    @Override
    @Transactional(readOnly = true)
    public List<ClubResponse> buscarConFiltros(String nombre, String disciplina, EstadoClub estado) {
        return clubRepository.buscarConFiltros(nombre, disciplina, estado)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Obtiene un club por su identificador unico.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IClubService}.</p>
     *
     * @param id identificador del club
     * @return {@link ClubResponse} del club encontrado
     */
    @Override
    @Transactional(readOnly = true)
    public ClubResponse obtenerPorId(Long id) {
        return mapToResponse(clubRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Club no encontrado con id: " + id)));
    }

    /**
     * Crea un nuevo club y le asigna un numero correlativo automatico.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IClubService}.</p>
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> el calculo del estado y del numero
     * son detalles internos del servicio.</p>
     *
     * @param request DTO con los datos del nuevo club
     * @return {@link ClubResponse} del club recien persistido con su ID y numero asignados
     */
    @Override
    public ClubResponse crear(ClubRequest request) {
        Club club = new Club();
        mapRequestToClub(request, club);
        long total = clubRepository.countByEliminadoFalse();
        club.setNumeroClub(String.valueOf(total + 1));
        calcularEstadoAutomatico(club);
        Club guardado = clubRepository.save(club);
        log.info("Club creado: {} id: {} numero: {}", guardado.getNombre(), guardado.getId(), guardado.getNumeroClub());
        return mapToResponse(guardado);
    }

    /**
     * Actualiza los datos de un club existente y recalcula su estado de vigencia.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IClubService}.</p>
     *
     * @param id      identificador del club a actualizar
     * @param request DTO con los nuevos datos del club
     * @return {@link ClubResponse} con los datos actualizados
     */
    @Override
    public ClubResponse actualizar(Long id, ClubRequest request) {
        Club club = clubRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Club no encontrado con id: " + id));
        mapRequestToClub(request, club);
        calcularEstadoAutomatico(club);
        return mapToResponse(clubRepository.save(club));
    }

    /**
     * Elimina logicamente un club (soft delete).
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IClubService}.</p>
     * <p><b>Pilar POO — OCULTAMIENTO:</b> el borrado fisico nunca ocurre.</p>
     *
     * @param id identificador del club a eliminar
     */
    @Override
    public void eliminar(Long id) {
        Club club = clubRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Club no encontrado con id: " + id));
        club.setEliminado(true);
        clubRepository.save(club);
        log.info("Club eliminado lógicamente: {}", id);
    }

    /**
     * Calcula el estado de vigencia automaticamente segun la fecha fin del
     * reconocimiento deportivo. VIGENTE si {@code fechaFinReconocimiento >= hoy};
     * NO_VIGENTE en caso contrario.
     *
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> logica de negocio privada,
     * no expuesta al controlador.</p>
     *
     * @param club entidad {@link Club} cuyo estado se desea calcular y asignar
     */
    private void calcularEstadoAutomatico(Club club) {
        if (club.getFechaFinReconocimiento() != null) {
            boolean vigente = !club.getFechaFinReconocimiento().isBefore(LocalDate.now());
            club.setEstado(vigente ? EstadoClub.VIGENTE : EstadoClub.NO_VIGENTE);
        } else if (club.getEstado() == null) {
            club.setEstado(EstadoClub.NO_VIGENTE);
        }
    }

    /**
     * Copia los datos del DTO de solicitud hacia la entidad {@link Club}.
     *
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> mapeo privado que oculta los
     * detalles de conversion al resto de la clase.</p>
     *
     * @param request DTO con los datos a copiar
     * @param club    entidad destino donde se aplican los datos
     */
    private void mapRequestToClub(ClubRequest request, Club club) {
        club.setNombre(request.getNombre());
        club.setNumeroClub(request.getNumeroClub());
        club.setDisciplinaDeportiva(request.getDisciplinaDeportiva());
        club.setDescripcionDeportiva(request.getDescripcionDeportiva());
        club.setDireccion(request.getDireccion());
        club.setTelefono(request.getTelefono());
        club.setEmail(request.getEmail());
        club.setNumeroResolucion(request.getNumeroResolucion());
        club.setFechaExpedicionResolucion(request.getFechaExpedicionResolucion());
        club.setFechaInicioReconocimiento(request.getFechaInicioReconocimiento());
        club.setFechaFinReconocimiento(request.getFechaFinReconocimiento());
        club.setFechaInicioOrganoAdmon(request.getFechaInicioOrganoAdmon());
        club.setFechaFinOrganoAdmon(request.getFechaFinOrganoAdmon());
        club.setRepresentanteLegalNombre(request.getRepresentanteLegalNombre());
        club.setRepresentanteLegalCedula(request.getRepresentanteLegalCedula());
        club.setRepresentanteLegalCargo(request.getRepresentanteLegalCargo());
    }

    /**
     * Convierte una entidad {@link Club} en su DTO de respuesta {@link ClubResponse}.
     *
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> conversion privada que oculta la
     * estructura interna de la entidad JPA al resto del sistema.</p>
     *
     * @param club entidad {@link Club} a convertir
     * @return {@link ClubResponse} con los datos de presentacion del club
     */
    private ClubResponse mapToResponse(Club club) {
        ClubResponse r = new ClubResponse();
        r.setId(club.getId());
        r.setNombre(club.getNombre());
        r.setNumeroClub(club.getNumeroClub());
        r.setDisciplinaDeportiva(club.getDisciplinaDeportiva());
        r.setDescripcionDeportiva(club.getDescripcionDeportiva());
        r.setDireccion(club.getDireccion());
        r.setTelefono(club.getTelefono());
        r.setEmail(club.getEmail());
        r.setNumeroResolucion(club.getNumeroResolucion());
        r.setFechaExpedicionResolucion(club.getFechaExpedicionResolucion());
        r.setFechaInicioReconocimiento(club.getFechaInicioReconocimiento());
        r.setFechaFinReconocimiento(club.getFechaFinReconocimiento());
        r.setFechaInicioOrganoAdmon(club.getFechaInicioOrganoAdmon());
        r.setFechaFinOrganoAdmon(club.getFechaFinOrganoAdmon());
        r.setRepresentanteLegalNombre(club.getRepresentanteLegalNombre());
        r.setRepresentanteLegalCedula(club.getRepresentanteLegalCedula());
        r.setRepresentanteLegalCargo(club.getRepresentanteLegalCargo());
        r.setEstado(club.getEstado());
        r.setEstadoDescripcion(club.getEstado() == EstadoClub.VIGENTE ? "Vigente" : "No Vigente");
        r.setFechaCreacion(club.getFechaCreacion());
        r.setFechaModificacion(club.getFechaModificacion());
        return r;
    }
}
