package com.pastodeporte.sistema.repository;

import com.pastodeporte.sistema.model.MiembroOrgano;
import com.pastodeporte.sistema.model.enums.TipoOrgano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para acceso a datos de miembros de órganos de clubes.
 *
 * Pilar POO: MODULARIDAD - capa de datos separada de la lógica.
 * Pilar POO: ABSTRACCION - JpaRepository abstrae las operaciones CRUD.
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
@Repository
public interface MiembroOrganoRepository extends JpaRepository<MiembroOrgano, Long> {

    /** Todos los miembros no eliminados de un club */
    List<MiembroOrgano> findByClubIdAndEliminadoFalse(Long clubId);

    /** Miembros de un tipo de órgano específico de un club */
    List<MiembroOrgano> findByClubIdAndTipoOrganoAndEliminadoFalse(Long clubId, TipoOrgano tipoOrgano);

    /** Miembro por id no eliminado */
    Optional<MiembroOrgano> findByIdAndEliminadoFalse(Long id);
}
