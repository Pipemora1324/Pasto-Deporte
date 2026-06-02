package com.pastodeporte.sistema.controller;

import com.pastodeporte.sistema.dto.response.ResumenResponse;
import com.pastodeporte.sistema.service.IReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para generacion de reportes y estadisticas del sistema.
 *
 * <p><b>Pilar POO — MODULARIDAD:</b> delega la generacion de reportes a
 * {@link com.pastodeporte.sistema.service.IReporteService}.</p>
 * <p><b>Pilar POO — ABSTRACCION:</b> el controlador desconoce la implementacion
 * interna de los reportes (Apache POI, calculos estadisticos, etc.).</p>
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final IReporteService reporteService;

    /**
     * Genera el resumen estadistico de clubes para el dashboard (protegido).
     *
     * @return {@link ResponseEntity} con {@link ResumenResponse} que contiene
     *         totales de clubes vigentes y no vigentes
     */
    @GetMapping("/resumen")
    public ResponseEntity<ResumenResponse> resumen() {
        return ResponseEntity.ok(reporteService.generarResumen());
    }

    /**
     * Exporta la lista completa de clubes en formato Excel (protegido).
     *
     * <p><b>Pilar POO — ABSTRACCION:</b> la complejidad de Apache POI queda
     * encapsulada en el servicio; el controlador solo recibe los bytes.</p>
     *
     * @return {@link ResponseEntity} con bytes del archivo Excel como adjunto descargable
     */
    @GetMapping("/clubes-excel")
    public ResponseEntity<byte[]> exportarExcel() {
        byte[] excelBytes = reporteService.exportarClubsExcel();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clubes_pasto_deporte.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }
}
