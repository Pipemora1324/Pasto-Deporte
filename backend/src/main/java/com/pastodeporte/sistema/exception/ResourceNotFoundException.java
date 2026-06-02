package com.pastodeporte.sistema.exception;

/**
 * Excepcion lanzada cuando un recurso no se encuentra en la base de datos.
 *
 * Pilar POO aplicado: HERENCIA - extiende RuntimeException.
 * Pilar POO aplicado: ENCAPSULAMIENTO - el mensaje de error encapsula el detalle.
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Crea la excepcion con el mensaje descriptivo del recurso no encontrado.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> invoca el constructor de {@link RuntimeException}
     * propagando el mensaje hacia la cadena de herencia.</p>
     *
     * @param message descripcion del recurso que no fue encontrado
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
