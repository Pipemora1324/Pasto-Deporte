package com.pastodeporte.sistema.service.impl;

import com.pastodeporte.sistema.dto.response.ResumenResponse;
import com.pastodeporte.sistema.model.Club;
import com.pastodeporte.sistema.model.enums.EstadoClub;
import com.pastodeporte.sistema.repository.ClubRepository;
import com.pastodeporte.sistema.service.IReporteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Implementacion del servicio de reportes y estadisticas del sistema.
 *
 * <p><b>Pilar POO — POLIMORFISMO:</b> implementa {@link IReporteService}
 * con {@code @Override} en cada metodo.</p>
 * <p><b>Pilar POO — ABSTRACCION:</b> la complejidad de Apache POI (estilos,
 * celdas, hojas) queda oculta al controlador y al resto del sistema.</p>
 * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> la logica de generacion de Excel
 * esta completamente encapsulada dentro del metodo de exportacion.</p>
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReporteServiceImpl implements IReporteService {

    private final ClubRepository clubRepository;

    /**
     * Genera el resumen estadistico de clubes agrupados por estado de vigencia.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IReporteService}.</p>
     *
     * @return {@link ResumenResponse} con total de clubes, vigentes y no vigentes
     */
    @Override
    public ResumenResponse generarResumen() {
        List<Club> todos = clubRepository.findByEliminadoFalse();
        long vigentes    = todos.stream().filter(c -> c.getEstado() == EstadoClub.VIGENTE).count();
        long noVigentes  = todos.stream().filter(c -> c.getEstado() == EstadoClub.NO_VIGENTE).count();
        return new ResumenResponse(todos.size(), vigentes, noVigentes);
    }

    /**
     * Genera un archivo Excel (.xlsx) con la lista completa de clubes activos
     * usando Apache POI con encabezados estilizados y columnas auto-dimensionadas.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IReporteService}.</p>
     * <p><b>Pilar POO — ABSTRACCION:</b> complejidad de POI completamente oculta
     * al controlador consumidor.</p>
     *
     * @return arreglo de bytes del archivo Excel generado en memoria
     */
    @Override
    public byte[] exportarClubsExcel() {
        List<Club> clubes = clubRepository.findByEliminadoFalse();

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Clubes Deportivos");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            String[] headers = {
                "N° Club", "Nombre del Club", "Disciplina Deportiva",
                "N° Resolución", "Fecha Expedición Resolución",
                "Fecha Inicio Reconocimiento", "Fecha Fin Reconocimiento",
                "Fecha Inicio Órgano Admon.", "Fecha Fin Órgano Admon.",
                "Representante Legal", "Cédula Representante", "Estado"
            };
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Club club : clubes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(club.getNumeroClub() != null ? club.getNumeroClub() : "");
                row.createCell(1).setCellValue(club.getNombre());
                row.createCell(2).setCellValue(club.getDisciplinaDeportiva());
                row.createCell(3).setCellValue(club.getNumeroResolucion() != null ? club.getNumeroResolucion() : "");
                row.createCell(4).setCellValue(club.getFechaExpedicionResolucion() != null ? club.getFechaExpedicionResolucion().toString() : "");
                row.createCell(5).setCellValue(club.getFechaInicioReconocimiento() != null ? club.getFechaInicioReconocimiento().toString() : "");
                row.createCell(6).setCellValue(club.getFechaFinReconocimiento() != null ? club.getFechaFinReconocimiento().toString() : "");
                row.createCell(7).setCellValue(club.getFechaInicioOrganoAdmon() != null ? club.getFechaInicioOrganoAdmon().toString() : "");
                row.createCell(8).setCellValue(club.getFechaFinOrganoAdmon() != null ? club.getFechaFinOrganoAdmon().toString() : "");
                row.createCell(9).setCellValue(club.getRepresentanteLegalNombre() != null ? club.getRepresentanteLegalNombre() : "");
                row.createCell(10).setCellValue(club.getRepresentanteLegalCedula() != null ? club.getRepresentanteLegalCedula() : "");
                row.createCell(11).setCellValue(club.getEstado() == EstadoClub.VIGENTE ? "VIGENTE" : "NO VIGENTE");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error al generar Excel: " + e.getMessage());
        }
    }
}
