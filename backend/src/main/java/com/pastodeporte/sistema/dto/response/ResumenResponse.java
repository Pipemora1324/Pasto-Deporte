package com.pastodeporte.sistema.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para el resumen estadístico del dashboard.
 * Pilar POO: ABSTRACCION - agrupa estadísticas para el administrador.
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenResponse {

    private long totalClubes;
    private long vigentes;
    private long noVigentes;
}
