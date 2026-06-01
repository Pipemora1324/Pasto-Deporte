package com.pastodeporte.sistema.controller;

import com.pastodeporte.sistema.service.IArchivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Controlador REST para subida y descarga de archivos (imagenes y PDFs).
 *
 * <p><b>Pilar POO — MODULARIDAD:</b> delega el almacenamiento a
 * {@link com.pastodeporte.sistema.service.IArchivoService}.</p>
 * <p><b>Pilar POO — ABSTRACCION:</b> el controlador desconoce el mecanismo
 * de almacenamiento; solo conoce la interfaz del servicio.</p>
 *
 * <p>URLs de acceso:</p>
 * <ul>
 *   <li>{@code GET  /api/archivos/imagen/{nombre}} — muestra imagen en el navegador</li>
 *   <li>{@code GET  /api/archivos/pdf/{nombre}}    — muestra PDF inline en el navegador</li>
 *   <li>{@code POST /api/archivos/imagen}          — sube imagen, retorna {@code {url}}</li>
 *   <li>{@code POST /api/archivos/pdf}             — sube PDF, retorna {@code {url}}</li>
 * </ul>
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@RestController
@RequestMapping("/api/archivos")
@RequiredArgsConstructor
public class ArchivoController {

    private final IArchivoService archivoService;

    @Value("${archivos.upload-dir}")
    private String uploadDir;

    /**
     * Recibe y almacena una imagen del club.
     *
     * @param archivo archivo multipart de tipo imagen (JPEG, PNG, WebP o GIF)
     * @return {@link ResponseEntity} con mapa JSON {@code {"url": "/api/archivos/imagen/..."}}
     */
    @PostMapping("/imagen")
    public ResponseEntity<Map<String, String>> subirImagen(@RequestParam("file") MultipartFile archivo) {
        String url = archivoService.guardarImagen(archivo);
        return ResponseEntity.ok(Map.of("url", url));
    }

    /**
     * Recibe y almacena un documento PDF del club.
     *
     * @param archivo archivo multipart de tipo PDF
     * @return {@link ResponseEntity} con mapa JSON {@code {"url": "/api/archivos/pdf/..."}}
     */
    @PostMapping("/pdf")
    public ResponseEntity<Map<String, String>> subirPdf(@RequestParam("file") MultipartFile archivo) {
        String url = archivoService.guardarPdf(archivo);
        return ResponseEntity.ok(Map.of("url", url));
    }

    /**
     * Sirve una imagen directamente en el navegador (sin descarga forzada).
     *
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> el tipo MIME se detecta automaticamente.</p>
     *
     * @param nombreArchivo nombre del archivo de imagen a servir
     * @return {@link ResponseEntity} con el recurso de imagen o 404 si no existe
     * @throws IOException si ocurre un error al leer el archivo del disco
     */
    @GetMapping("/imagen/{nombreArchivo:.+}")
    public ResponseEntity<Resource> verImagen(
            @PathVariable String nombreArchivo) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(nombreArchivo);
        Resource resource = new FileSystemResource(filePath);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        String tipoDetectado = Files.probeContentType(filePath);
        MediaType mediaType = MediaType.parseMediaType(
                tipoDetectado != null ? tipoDetectado : "image/jpeg");
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }

    /**
     * Sirve un PDF inline en el navegador (no fuerza la descarga).
     *
     * @param nombreArchivo nombre del archivo PDF a servir
     * @return {@link ResponseEntity} con el recurso PDF inline o 404 si no existe
     * @throws IOException si ocurre un error al leer el archivo del disco
     */
    @GetMapping("/pdf/{nombreArchivo:.+}")
    public ResponseEntity<Resource> verPdf(
            @PathVariable String nombreArchivo) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(nombreArchivo);
        Resource resource = new FileSystemResource(filePath);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + nombreArchivo + "\"")
                .body(resource);
    }
}
