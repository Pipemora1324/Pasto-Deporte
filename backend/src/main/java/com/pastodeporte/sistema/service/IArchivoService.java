package com.pastodeporte.sistema.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Contrato para la gestion de archivos multimedia del sistema (imagenes y PDFs).
 *
 * <p><b>Pilar POO — ABSTRACCION:</b> oculta el mecanismo de almacenamiento
 * (disco local, S3, etc.) al resto del sistema.</p>
 * <p><b>Pilar POO — POLIMORFISMO:</b> permite cambiar la implementacion a S3
 * o cualquier otro proveedor sin modificar los controladores.</p>
 * <p><b>Pilar POO — OCULTAMIENTO:</b> la validacion de tipos MIME y la
 * generacion de nombres unicos quedan ocultas al consumidor.</p>
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
public interface IArchivoService {

    /**
     * Guarda una imagen en el almacenamiento y retorna su URL de acceso HTTP.
     *
     * @param archivo archivo multipart de imagen (JPEG, PNG, WebP o GIF)
     * @return URL relativa de acceso a la imagen (e.g. {@code /api/archivos/imagen/img_...})
     */
    String guardarImagen(MultipartFile archivo);

    /**
     * Guarda un documento PDF en el almacenamiento y retorna su URL de acceso HTTP.
     *
     * @param archivo archivo multipart de tipo PDF
     * @return URL relativa de acceso al PDF (e.g. {@code /api/archivos/pdf/pdf_...})
     */
    String guardarPdf(MultipartFile archivo);

    /**
     * Retorna la ruta absoluta del archivo PDF en el sistema de ficheros.
     *
     * @param nombreArchivo nombre del archivo PDF almacenado
     * @return ruta absoluta del archivo en el servidor
     */
    String obtenerRutaPdf(String nombreArchivo);
}
