package com.pastodeporte.sistema.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pastodeporte.sistema.exception.BusinessException;
import com.pastodeporte.sistema.service.IArchivoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

/**
 * Implementacion del servicio de gestion de archivos.
 *
 * <p>Estrategia dual segun entorno:</p>
 * <ul>
 *   <li><b>Produccion</b> ({@code cloudinary.cloud-name} configurado):
 *       sube a Cloudinary y retorna la URL publica {@code secure_url}.</li>
 *   <li><b>Desarrollo</b> (cloud-name vacio):
 *       guarda en disco local y retorna URL relativa {@code /api/archivos/...}.</li>
 * </ul>
 *
 * <p><b>Pilar POO — POLIMORFISMO:</b> implementa {@link IArchivoService}
 * con {@code @Override} en cada metodo publico.</p>
 * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> la logica de guardado, validacion
 * de tipos MIME y generacion de nombres unicos son metodos privados.</p>
 * <p><b>Pilar POO — OCULTAMIENTO:</b> el mecanismo de almacenamiento
 * (local o nube) es transparente para los controladores.</p>
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Service
@Slf4j
public class ArchivoServiceImpl implements IArchivoService {

    @Value("${archivos.upload-dir}")
    private String uploadDir;

    @Value("${cloudinary.cloud-name:}")
    private String cloudinaryCloudName;

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Valida y guarda una imagen, retornando su URL de acceso HTTP.
     *
     * <p>Si Cloudinary esta configurado sube a la carpeta
     * {@code pasto-deporte/imagenes} y retorna la {@code secure_url}.
     * En caso contrario guarda en disco local.</p>
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IArchivoService}.</p>
     *
     * @param archivo archivo multipart de imagen (JPEG, PNG, WebP o GIF)
     * @return URL publica de Cloudinary o URL relativa local
     */
    @Override
    @SuppressWarnings("unchecked")
    public String guardarImagen(MultipartFile archivo) {
        validarArchivo(archivo, new String[]{"image/jpeg", "image/png", "image/webp", "image/gif"});

        if (!cloudinaryCloudName.isBlank()) {
            try {
                Map<String, Object> resultado = cloudinary.uploader().upload(
                    archivo.getBytes(),
                    ObjectUtils.asMap(
                        "folder",        "pasto-deporte/imagenes",
                        "resource_type", "image"
                    )
                );
                String url = (String) resultado.get("secure_url");
                log.info("Imagen subida a Cloudinary: {}", url);
                return url;
            } catch (IOException e) {
                throw new BusinessException("Error al subir imagen a Cloudinary: " + e.getMessage());
            }
        }

        String nombre = "img_" + UUID.randomUUID() + "_" + limpiarNombre(archivo.getOriginalFilename());
        guardarArchivo(archivo, nombre);
        return "/api/archivos/imagen/" + nombre;
    }

    /**
     * Valida y guarda un PDF, retornando su URL de acceso HTTP.
     *
     * <p>Si Cloudinary esta configurado sube a la carpeta
     * {@code pasto-deporte/pdfs} con {@code resource_type=raw} y retorna la
     * {@code secure_url}. En caso contrario guarda en disco local.</p>
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IArchivoService}.</p>
     *
     * @param archivo archivo multipart de tipo PDF
     * @return URL publica de Cloudinary o URL relativa local
     */
    @Override
    @SuppressWarnings("unchecked")
    public String guardarPdf(MultipartFile archivo) {
        validarPdf(archivo);

        if (!cloudinaryCloudName.isBlank()) {
            try {
                Map<String, Object> resultado = cloudinary.uploader().upload(
                    archivo.getBytes(),
                    ObjectUtils.asMap(
                        "folder",        "pasto-deporte/pdfs",
                        "resource_type", "raw"
                    )
                );
                String url = (String) resultado.get("secure_url");
                log.info("PDF subido a Cloudinary: {}", url);
                return url;
            } catch (IOException e) {
                throw new BusinessException("Error al subir PDF a Cloudinary: " + e.getMessage());
            }
        }

        String nombre = "pdf_" + UUID.randomUUID() + "_" + limpiarNombre(archivo.getOriginalFilename());
        guardarArchivo(archivo, nombre);
        return "/api/archivos/pdf/" + nombre;
    }

    /**
     * Retorna la ruta absoluta del PDF en el sistema de ficheros local.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IArchivoService}.</p>
     *
     * @param nombreArchivo nombre del archivo PDF almacenado
     * @return ruta absoluta del archivo en el servidor
     */
    @Override
    public String obtenerRutaPdf(String nombreArchivo) {
        return Paths.get(uploadDir, nombreArchivo).toAbsolutePath().toString();
    }

    /**
     * Valida que el archivo sea un PDF valido por tipo MIME o extension.
     *
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> logica de validacion PDF privada.</p>
     *
     * @param archivo archivo multipart a validar
     */
    private void validarPdf(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new BusinessException("El archivo no puede estar vacío");
        }
        String contentType   = archivo.getContentType();
        String originalName  = archivo.getOriginalFilename();
        boolean esPdf = "application/pdf".equals(contentType)
                || "application/x-pdf".equals(contentType)
                || "application/octet-stream".equals(contentType)
                || (originalName != null && originalName.toLowerCase().endsWith(".pdf"));
        if (!esPdf) {
            throw new BusinessException("Solo se permiten archivos PDF. Tipo recibido: " + contentType);
        }
    }

    /**
     * Copia el archivo multipart al directorio de uploads con el nombre indicado.
     *
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> logica de escritura en disco privada.</p>
     *
     * @param archivo archivo multipart a persistir en disco
     * @param nombre  nombre de destino del archivo en el directorio de uploads
     */
    private void guardarArchivo(MultipartFile archivo, String nombre) {
        try {
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);
            Files.copy(archivo.getInputStream(), dir.resolve(nombre), StandardCopyOption.REPLACE_EXISTING);
            log.info("Archivo guardado localmente: {}/{}", uploadDir, nombre);
        } catch (IOException e) {
            throw new BusinessException("Error al guardar el archivo: " + e.getMessage());
        }
    }

    /**
     * Valida que el archivo sea no nulo y que su tipo MIME este entre los permitidos.
     *
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> validacion generica reutilizable y privada.</p>
     *
     * @param archivo         archivo multipart a validar
     * @param tiposPermitidos arreglo de tipos MIME aceptados (e.g. {@code "image/jpeg"})
     */
    private void validarArchivo(MultipartFile archivo, String[] tiposPermitidos) {
        if (archivo == null || archivo.isEmpty()) {
            throw new BusinessException("El archivo no puede estar vacio");
        }
        String contentType = archivo.getContentType();
        for (String tipo : tiposPermitidos) {
            if (tipo.equals(contentType)) return;
        }
        throw new BusinessException("Tipo de archivo no permitido: " + contentType);
    }

    /**
     * Sanitiza el nombre original del archivo eliminando caracteres no seguros.
     *
     * <p><b>Pilar POO — OCULTAMIENTO:</b> logica de sanitizacion interna que
     * evita path traversal y caracteres especiales.</p>
     *
     * @param nombre nombre original del archivo (puede ser null)
     * @return nombre sanitizado apto para usar como nombre de fichero
     */
    private String limpiarNombre(String nombre) {
        if (nombre == null) return "archivo";
        return nombre.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
