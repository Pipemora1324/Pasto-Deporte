package com.pastodeporte.sistema.exception;

/**
 * Excepcion lanzada por violaciones de reglas de negocio.
 *
 * Pilar POO aplicado: HERENCIA - extiende RuntimeException.
 * Pilar POO aplicado: ABSTRACCION - diferencia errores de negocio de errores tecnicos.
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
public class BusinessException extends RuntimeException {

    /**
     * Crea la excepcion con el mensaje de la regla de negocio violada.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> invoca el constructor de {@link RuntimeException}
     * propagando el mensaje hacia la cadena de herencia.</p>
     *
     * @param message descripcion de la regla de negocio que fue violada
     */
    public BusinessException(String message) {
        super(message);
    }
}
