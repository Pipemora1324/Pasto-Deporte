package com.pastodeporte.sistema.repository;

import com.pastodeporte.sistema.model.DirectorioClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para acceso a datos del directorio de clubes.
 *
 * Pilar POO aplicado: MODULARIDAD - capa de acceso a datos separada.
 * Pilar POO aplicado: ABSTRACCION - operaciones CRUD via JpaRepository.
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Repository
public interface DirectorioClubRepository extends JpaRepository<DirectorioClub, Long> {

    /**
     * Obtiene todos los integrantes del directorio de un club no eliminados.
     */
    List<DirectorioClub> findByClubIdAndEliminadoFalse(Long clubId);

    /**
     * Busca un integrante del directorio por id no eliminado.
     */
    Optional<DirectorioClub> findByIdAndEliminadoFalse(Long id);
}
