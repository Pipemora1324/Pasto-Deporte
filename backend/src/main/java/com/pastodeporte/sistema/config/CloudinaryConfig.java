package com.pastodeporte.sistema.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuracion del cliente Cloudinary para almacenamiento de archivos en produccion.
 *
 * <p>En desarrollo ({@code cloud-name} vacio) el bean se crea pero no se usa;
 * {@link com.pastodeporte.sistema.service.impl.ArchivoServiceImpl} guarda en disco local.</p>
 *
 * <p><b>Pilar POO — MODULARIDAD:</b> la configuracion de Cloudinary esta aislada
 * del servicio que lo utiliza.</p>
 * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> las credenciales se inyectan via
 * {@code @Value} y nunca se exponen fuera de este bean.</p>
 * <p><b>Pilar POO — ABSTRACCION:</b> el resto del sistema usa {@link Cloudinary}
 * sin conocer como se construyo.</p>
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-name:}")
    private String cloudName;

    @Value("${cloudinary.api-key:}")
    private String apiKey;

    @Value("${cloudinary.api-secret:}")
    private String apiSecret;

    /**
     * Crea el bean {@link Cloudinary} con las credenciales de entorno.
     *
     * @return instancia configurada de {@link Cloudinary}
     */
    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key",    apiKey);
        config.put("api_secret", apiSecret);
        return new Cloudinary(config);
    }
}
