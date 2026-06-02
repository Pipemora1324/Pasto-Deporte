package com.pastodeporte.sistema.service;

import com.pastodeporte.sistema.dto.response.ResumenResponse;

/**
 * Contrato para la generacion de reportes estadisticos del sistema.
 *
 * <p><b>Pilar POO — ABSTRACCION:</b> define el contrato de reportes sin revelar
 * la tecnologia utilizada (Apache POI, calculos internos, etc.).</p>
 * <p><b>Pilar POO — POLIMORFISMO:</b> permite intercambiar implementaciones
 * (PDF, CSV, Excel) sin modificar los controladores.</p>
 * <p><b>Pilar POO — MODULARIDAD:</b> centraliza la responsabilidad de reportes
 * en un unico contrato independiente del resto del sistema.</p>
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
public interface IReporteService {

    /**
     * Genera un resumen estadistico de clubes agrupados por estado de vigencia.
     *
     * @return {@link ResumenResponse} con totales de clubes vigentes y no vigentes
     */
    ResumenResponse generarResumen();

    /**
     * Genera un archivo Excel (.xlsx) con la lista completa de clubes activos.
     *
     * <p><b>Pilar POO — ABSTRACCION:</b> oculta la complejidad de Apache POI;
     * el consumidor solo recibe los bytes del archivo resultante.</p>
     *
     * @return arreglo de bytes del archivo Excel generado en memoria
     */
    byte[] exportarClubsExcel();
}
