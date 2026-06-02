package com.pastodeporte.sistema.model.enums;

/**
 * Enum que define los roles de los usuarios administradores del sistema.
 * Pilar POO aplicado: ABSTRACCION - define los niveles de acceso al sistema.
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
public enum RolUsuario {
    /** Administrador con permisos sobre clubes y certificaciones */
    ADMIN,
    /** Super administrador con todos los permisos del sistema */
    SUPERADMIN
}
