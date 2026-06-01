package com.pastodeporte.sistema.repository;

import com.pastodeporte.sistema.model.OpcionMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para el acceso a datos de las opciones del menú de navegación.
 *
 * <p><b>Pilar POO — ABSTRACCIÓN:</b> {@link JpaRepository} abstrae todas las
 * operaciones CRUD, permitiendo consultas sin escribir SQL manualmente.</p>
 * <p><b>Pilar POO — MODULARIDAD:</b> la capa de datos queda aislada de la
 * lógica de negocio; el servicio solo conoce este contrato de métodos.</p>
 * <p><b>Pilar POO — OCULTAMIENTO:</b> la generación de queries JPQL es
 * interna a Spring Data y no se expone al resto del sistema.</p>
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Repository
public interface OpcionMenuRepository extends JpaRepository<OpcionMenu, Long> {

    /**
     * Obtiene todos los ítems raíz del menú (sin padre) ordenados por {@code orden}.
     * Son los nodos del primer nivel del árbol de navegación.
     *
     * @return lista de ítems raíz, ordenados de menor a mayor por {@code orden}
     */
    List<OpcionMenu> findByPadreIsNullAndEliminadoFalseOrderByOrdenAsc();

    /**
     * Obtiene todos los ítems hijos directos de un ítem padre dado,
     * ordenados por {@code orden}.
     *
     * @param padreId identificador del ítem padre cuyas opciones hijas se buscan
     * @return lista de hijos directos del padre, ordenados por {@code orden}
     */
    List<OpcionMenu> findByPadreIdAndEliminadoFalseOrderByOrdenAsc(Long padreId);
}
