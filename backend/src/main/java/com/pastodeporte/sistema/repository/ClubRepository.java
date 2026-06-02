package com.pastodeporte.sistema.repository;

import com.pastodeporte.sistema.model.Club;
import com.pastodeporte.sistema.model.enums.EstadoClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para acceso a datos de clubes deportivos.
 *
 * Pilar POO aplicado: MODULARIDAD - capa de datos separada de la logica de negocio.
 * Pilar POO aplicado: ABSTRACCION - JpaRepository abstrae las operaciones CRUD.
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    /**
     * Busca todos los clubes que no han sido eliminados logicamente.
     * Pilar POO: ABSTRACCION - oculta la complejidad de la consulta soft-delete.
     */
    List<Club> findByEliminadoFalse();

    /**
     * Busca un club por id que no este eliminado.
     */
    Optional<Club> findByIdAndEliminadoFalse(Long id);

    /**
     * Busca clubes por estado de certificacion.
     */
    List<Club> findByEstadoAndEliminadoFalse(EstadoClub estado);

    /**
     * Busca clubes por nombre (sin importar mayusculas/minusculas).
     * Pilar POO: POLIMORFISMO - sobrecarga de busqueda por diferentes criterios.
     */
    List<Club> findByNombreContainingIgnoreCaseAndEliminadoFalse(String nombre);

    /**
     * Busca clubes con filtros combinados de nombre, disciplina y estado.
     */
    @Query("SELECT c FROM Club c WHERE c.eliminado = false " +
           "AND (:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
           "AND (:disciplina IS NULL OR LOWER(c.disciplinaDeportiva) LIKE LOWER(CONCAT('%', :disciplina, '%'))) " +
           "AND (:estado IS NULL OR c.estado = :estado)")
    List<Club> buscarConFiltros(@Param("nombre") String nombre,
                                @Param("disciplina") String disciplina,
                                @Param("estado") EstadoClub estado);

    /**
     * Cuenta clubes no eliminados — usado para generar número correlativo.
     */
    long countByEliminadoFalse();

    /**
     * Cuenta clubes agrupados por estado para el dashboard.
     */
    @Query("SELECT c.estado, COUNT(c) FROM Club c WHERE c.eliminado = false GROUP BY c.estado")
    List<Object[]> contarPorEstado();

    /**
     * Busca clubes cuya fecha fin de reconocimiento cae en el rango [desde, hasta].
     * Usado por el servicio de alertas para detectar vigencias próximas a vencer.
     *
     * Pilar POO: ABSTRACCION - oculta la consulta JPQL al servicio de alertas.
     */
    @Query("SELECT c FROM Club c WHERE c.eliminado = false " +
           "AND c.fechaFinReconocimiento IS NOT NULL " +
           "AND c.fechaFinReconocimiento BETWEEN :desde AND :hasta")
    List<Club> findClubsConVigenciaProxima(@Param("desde") java.time.LocalDate desde,
                                           @Param("hasta") java.time.LocalDate hasta);
}
