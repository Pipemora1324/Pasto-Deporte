package com.pastodeporte.sistema.service;

/**
 * Contrato para el servicio de alertas de vigencia proxima a vencer.
 *
 * <p><b>Pilar POO — ABSTRACCION:</b> define el contrato de alertas sin
 * exponer la implementacion concreta (canal de notificacion).</p>
 * <p><b>Pilar POO — POLIMORFISMO:</b> permite cambiar el canal de notificacion
 * (WhatsApp, email, SMS) sin modificar los clientes del servicio.</p>
 * <p><b>Pilar POO — MODULARIDAD:</b> separa la responsabilidad de alertas
 * del resto de la logica de negocio.</p>
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
public interface IAlertaService {

    /**
     * Revisa los clubes con reconocimiento deportivo proximo a vencer
     * y envia alertas al canal configurado.
     *
     * <p>Se ejecuta automaticamente via {@code @Scheduled} todos los dias
     * a las 8:00 AM.</p>
     *
     * <p><b>Pilar POO — OCULTAMIENTO:</b> la logica de consulta y el canal
     * de envio quedan ocultos al resto del sistema.</p>
     */
    void verificarCertificacionesProximasAVencer();

    /**
     * Envia un mensaje de alerta por WhatsApp al numero configurado.
     *
     * <p><b>Pilar POO — ABSTRACCION:</b> oculta el mecanismo de envio
     * (API de CallMeBot).</p>
     *
     * @param mensaje texto del mensaje de alerta a enviar
     */
    void enviarAlertaWhatsApp(String mensaje);
}
