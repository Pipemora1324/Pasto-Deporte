package com.pastodeporte.sistema.model.enums;

/**
 * Estados posibles de vigencia de un club deportivo ante Pasto Deporte.
 * Pilar POO: ABSTRACCION - limita los estados a los dos oficialmente reconocidos.
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
public enum EstadoClub {
    /** El club tiene reconocimiento vigente de Pasto Deporte */
    VIGENTE,
    /** El club no cuenta con reconocimiento vigente */
    NO_VIGENTE
}
