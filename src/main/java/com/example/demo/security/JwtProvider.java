package com.example.demo.security;

import com.example.demo.entity.AuthUser;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    // Lista concurrente para almacenar tokens invalidados (segura en multi-thread)
    private Set<String> invalidTokens;

    // Duración del token (1 hora por defecto)
    private static final long TOKEN_VALIDITY = 3600000L; // 1 hora en milisegundos

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
        invalidTokens = ConcurrentHashMap.newKeySet();
    }

    /**
     * Genera un nuevo token JWT con datos del usuario.
     */
    public String createToken(AuthUser authUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", authUser.getId());
        claims.put("roles", authUser.getRoles().stream().map(r -> r.getName()).toList());

        Date now = new Date();
        Date expiration = new Date(now.getTime() + TOKEN_VALIDITY);

        return Jwts.builder()
                .setSubject(authUser.getUserName())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * Valida un token JWT, comprobando:
     * - Su firma
     * - Su expiración
     * - Que no esté en la lista negra (logout)
     */
    public boolean validate(String token) {
        if (token == null || token.trim().isEmpty()) {
            System.out.println("❌ Token vacío o nulo");
            return false;
        }

        // Quitar prefijo Bearer si existe
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Revisar si está en lista negra
        if (invalidTokens.contains(token)) {
            System.out.println("🚫 Token inválido: se encuentra en lista negra (logout realizado)");
            return false;
        }

        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token); // valida la firma y expiración

            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("⏰ Token expirado: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("⚠️ Token no soportado: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("❌ Token malformado: " + e.getMessage());
        } catch (SignatureException e) {
            System.out.println("🔒 Firma inválida del token: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ Token vacío o incorrecto: " + e.getMessage());
        }

        return false;
    }

    /**
     * Obtiene el nombre de usuario contenido en el token.
     */
    public String getUserNameFromToken(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Agrega el token a la lista negra (logout).
     */
    public void addToInvalidTokens(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        invalidTokens.add(token);
    }

    /**
     * Verifica si un token está en la lista negra.
     */
    public boolean isTokenInvalidated(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return invalidTokens.contains(token);
    }

    /**
     * Devuelve los tokens actualmente invalidados (opcional, para depurar).
     */
    public Set<String> getInvalidTokens() {
        return invalidTokens;
    }
}
