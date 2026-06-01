package com.pastodeporte.sistema.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para toda la API REST.
 *
 * <p><b>Pilar POO — POLIMORFISMO:</b> cada metodo {@code @ExceptionHandler} reemplaza
 * el comportamiento por defecto de Spring para cada tipo de excepcion.</p>
 * <p><b>Pilar POO — MODULARIDAD:</b> el manejo de errores esta centralizado en una
 * sola clase, sin dispersarse entre los controladores.</p>
 * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> el formato estandarizado de la respuesta
 * de error esta encapsulado en el metodo privado {@code buildErrorResponse}.</p>
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones de recurso no encontrado retornando HTTP 404.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> sobreescritura implicita del manejo
     * por defecto de Spring para {@link ResourceNotFoundException}.</p>
     *
     * @param ex excepcion con el mensaje del recurso no encontrado
     * @return {@link ResponseEntity} con cuerpo de error estandarizado y HTTP 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Maneja excepciones de logica de negocio retornando HTTP 400.
     *
     * @param ex excepcion con el mensaje de la regla de negocio violada
     * @return {@link ResponseEntity} con cuerpo de error estandarizado y HTTP 400
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        log.warn("Error de negocio: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Maneja errores de validacion de DTOs anotados con {@code @Valid}, retornando HTTP 400
     * con un mapa de campo→mensaje de error para cada campo invalido.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> manejo especializado para errores de
     * validacion de Bean Validation.</p>
     *
     * @param ex excepcion que contiene los errores de validacion por campo
     * @return {@link ResponseEntity} con mapa de errores por campo y HTTP 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
        });

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Errores de validacion");
        body.put("errores", errores);

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Maneja cualquier otra excepcion no controlada retornando HTTP 500.
     *
     * @param ex excepcion no manejada capturada como ultimo recurso
     * @return {@link ResponseEntity} con mensaje generico de error interno y HTTP 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Error interno del servidor: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor. Por favor contacte al administrador.");
    }

    /**
     * Construye la respuesta de error estandarizada con timestamp, status y mensaje.
     *
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> formato de error encapsulado en
     * metodo privado reutilizado por todos los handlers.</p>
     *
     * @param status  codigo de estado HTTP de la respuesta
     * @param message mensaje descriptivo del error
     * @return {@link ResponseEntity} con cuerpo de error estandarizado
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("mensaje", message);
        return ResponseEntity.status(status).body(body);
    }
}
