package com.pastodeporte.sistema.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utilidad para generacion, firmado y validacion de tokens JWT.
 *
 * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> toda la logica JWT esta encapsulada
 * en esta clase utilitaria; ningun otro componente necesita conocer los detalles.</p>
 * <p><b>Pilar POO — OCULTAMIENTO:</b> la clave secreta de firma y el algoritmo
 * HMAC-SHA nunca se exponen fuera de esta clase.</p>
 * <p><b>Pilar POO — MODULARIDAD:</b> separa la responsabilidad de tokens del
 * resto de la logica de autenticacion.</p>
 *
 * @author Felipe Mora — Universidad Cooperativa de Colombia
 * @version 1.0
 * @since 2024
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    /**
     * Deriva la clave de firma HMAC-SHA a partir del secret configurado en properties.
     *
     * <p><b>Pilar POO — OCULTAMIENTO:</b> metodo privado; la clave nunca sale
     * de esta clase.</p>
     *
     * @return {@link SecretKey} lista para firmar o verificar tokens JWT
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Genera un access token JWT de corta duracion para el usuario dado.
     *
     * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> la creacion y firma del token
     * es interna a este metodo.</p>
     *
     * @param username nombre de usuario para incluir como subject del token
     * @return token JWT firmado con tiempo de expiracion configurado en properties
     */
    public String generateAccessToken(String username) {
        return buildToken(new HashMap<>(), username, expiration);
    }

    /**
     * Genera un refresh token JWT de larga duracion para el usuario dado.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> variante de generacion de token
     * para ciclo de vida diferente al access token.</p>
     *
     * @param username nombre de usuario para incluir como subject del token
     * @return refresh token JWT firmado con tiempo de expiracion extendido
     */
    public String generateRefreshToken(String username) {
        return buildToken(new HashMap<>(), username, refreshExpiration);
    }

    /**
     * Construye y firma el token JWT con los claims, subject y tiempo de expiracion dados.
     *
     * <p><b>Pilar POO — OCULTAMIENTO:</b> metodo privado que centraliza la
     * construccion de todos los tipos de tokens.</p>
     *
     * @param extraClaims claims adicionales a incluir en el payload del token
     * @param username    subject del token (nombre de usuario)
     * @param expirationMs tiempo de vida del token en milisegundos
     * @return token JWT compacto y firmado
     */
    private String buildToken(Map<String, Object> extraClaims, String username, Long expirationMs) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrae el nombre de usuario (subject) del token JWT.
     *
     * @param token token JWT del que se extrae el subject
     * @return nombre de usuario contenido en el token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae la fecha de expiracion del token JWT.
     *
     * @param token token JWT del que se extrae la expiracion
     * @return {@link Date} de expiracion del token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae un claim especifico del token usando la funcion de resolucion indicada.
     *
     * <p><b>Pilar POO — POLIMORFISMO:</b> acepta cualquier funcion de extraccion
     * de claims mediante generics.</p>
     *
     * @param <T>            tipo del claim a extraer
     * @param token          token JWT del que se extrae el claim
     * @param claimsResolver funcion que transforma los claims en el tipo deseado
     * @return valor del claim extraido del token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parsea y retorna todos los claims del payload del token JWT.
     *
     * <p><b>Pilar POO — OCULTAMIENTO:</b> metodo privado; el parsing y la
     * verificacion de la firma son completamente internos.</p>
     *
     * @param token token JWT a parsear y verificar
     * @return {@link Claims} con todos los claims del payload
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Verifica si el token JWT es valido para el usuario dado.
     * Un token es valido si el subject coincide con el username y no ha expirado.
     *
     * @param token    token JWT a verificar
     * @param username nombre de usuario esperado como subject del token
     * @return {@code true} si el token es valido y corresponde al usuario; {@code false} en caso contrario
     */
    public boolean isTokenValid(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username)) && !isTokenExpired(token);
    }

    /**
     * Verifica si el token JWT ha superado su fecha de expiracion.
     *
     * @param token token JWT a verificar
     * @return {@code true} si el token ha expirado; {@code false} si aun es valido
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Retorna el tiempo de expiracion configurado para el access token.
     *
     * @return tiempo de vida del access token en milisegundos
     */
    public Long getExpiration() {
        return expiration;
    }
}
