package com.pastodeporte.sistema.service.impl;

import com.pastodeporte.sistema.model.Club;
import com.pastodeporte.sistema.repository.ClubRepository;
import com.pastodeporte.sistema.service.IAlertaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementacion del servicio de alertas de vigencia proxima a vencer.
 *
 * <p><b>Pilar POO — POLIMORFISMO:</b> implementa {@link IAlertaService}
 * con {@code @Override} en cada metodo.</p>
 * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> la logica de consulta de clubes
 * proximos a vencer y el envio de la alerta son internos al servicio.</p>
 * <p><b>Pilar POO — OCULTAMIENTO:</b> las credenciales de WhatsApp y el
 * endpoint de la API de notificaciones estan ocultos en la configuracion.</p>
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AlertaServiceImpl implements IAlertaService {

    private final ClubRepository clubRepository;

    @Value("${alerta.whatsapp.phone:}")
    private String whatsappPhone;

    @Value("${alerta.whatsapp.apikey:}")
    private String whatsappApiKey;

    @Value("${alerta.whatsapp.enabled:false}")
    private boolean whatsappEnabled;

    /**
     * Revisa los clubes con reconocimiento deportivo proximo a vencer en los
     * proximos 30 dias y envia una alerta al canal configurado.
     *
     * <p>Ejecutado automaticamente por Spring Scheduling todos los dias a las 8:00 AM.</p>
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IAlertaService}.</p>
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> la logica de consulta y construccion
     * del mensaje es completamente interna al metodo.</p>
     */
    @Override
    @Scheduled(cron = "0 0 8 * * *")
    public void verificarCertificacionesProximasAVencer() {
        LocalDate hoy       = LocalDate.now();
        LocalDate en30Dias  = hoy.plusDays(30);

        List<Club> proximos = clubRepository.findClubsConVigenciaProxima(hoy, en30Dias);

        if (proximos.isEmpty()) {
            log.info("[Alertas] No hay clubs con vigencia próxima a vencer en 30 días.");
            return;
        }

        StringBuilder mensaje = new StringBuilder("⚠️ PASTO DEPORTE - Alertas de Vigencia:\n");
        for (Club club : proximos) {
            mensaje.append(String.format("• Club: %s | Vence: %s\n",
                    club.getNombre(),
                    club.getFechaFinReconocimiento()));
        }

        log.info("[Alertas] {} clubs con vigencia próxima a vencer.", proximos.size());
        enviarAlertaWhatsApp(mensaje.toString());
    }

    /**
     * Envia un mensaje de alerta por WhatsApp via la API de CallMeBot.
     * Si las credenciales no estan configuradas, solo registra el mensaje en el log.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> {@code @Override} de {@link IAlertaService}.</p>
     * <p><b>Pilar POO — OCULTAMIENTO:</b> la logica de codificacion de URL y la
     * llamada HTTP a la API externa son internas al metodo.</p>
     *
     * @param mensaje texto del mensaje de alerta a enviar por WhatsApp
     */
    @Override
    public void enviarAlertaWhatsApp(String mensaje) {
        if (!whatsappEnabled || whatsappPhone.isBlank() || whatsappApiKey.isBlank()) {
            log.warn("[WhatsApp ALERTA - desarrollo]: {}", mensaje);
            return;
        }
        try {
            String codificado = URLEncoder.encode(mensaje, StandardCharsets.UTF_8);
            String url = String.format(
                "https://api.callmebot.com/whatsapp.php?phone=%s&text=%s&apikey=%s",
                whatsappPhone, codificado, whatsappApiKey
            );
            String respuesta = new RestTemplate().getForObject(url, String.class);
            log.info("[WhatsApp] Alerta enviada. Respuesta: {}", respuesta);
        } catch (Exception e) {
            log.error("[WhatsApp] Error al enviar alerta: {}", e.getMessage());
        }
    }
}
