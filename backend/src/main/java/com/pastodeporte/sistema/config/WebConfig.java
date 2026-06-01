package com.pastodeporte.sistema.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuracion de recursos estaticos para servir archivos subidos (imagenes y PDFs).
 *
 * <p><b>Pilar POO — MODULARIDAD:</b> la configuracion de recursos estaticos
 * esta separada de la configuracion de seguridad y de la aplicacion principal.</p>
 * <p><b>Pilar POO — POLIMORFISMO:</b> implementa {@link WebMvcConfigurer}
 * sobreescribiendo {@code addResourceHandlers} con {@code @Override}.</p>
 * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> el directorio de uploads y la URL
 * de acceso son detalles de configuracion internos a esta clase.</p>
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${archivos.upload-dir}")
    private String uploadDir;

    /**
     * Registra el directorio de uploads como recurso estatico accesible via HTTP
     * bajo la ruta {@code /uploads/**}.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} del metodo de
     * {@link WebMvcConfigurer}.</p>
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> la resolucion de la ruta absoluta
     * del directorio es interna a este metodo.</p>
     *
     * @param registry registro de manejadores de recursos de Spring MVC
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
        String uploadLocation = "file:" + uploadPath.toString() + "/";

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadLocation);
    }
}
