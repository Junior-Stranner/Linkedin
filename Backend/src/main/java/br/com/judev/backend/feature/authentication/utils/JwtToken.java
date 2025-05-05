package br.com.judev.backend.feature.authentication.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtToken {
    // Chave secreta para assinar/verificar tokens (deve ser longa e segura)
    @Value("${jwt.secret.key}")
    private String secret;

    private final RestTemplate restTemplate = new RestTemplate();

    // Gera uma chave baseada na string `secret` para usar no algoritmo HMAC SHA-256
    public SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Gera um JWT com subject (email), data de emissão e expiração de 10 horas
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email) // payload: sub
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(getKey()) // assinatura
                .compact();
    }

    // Extrai o e-mail (subject) do token
    public String getEmailFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Método genérico para extrair qualquer claim do token
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // Decodifica o token JWT e extrai todos os claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey()) // verifica assinatura
                .build()
                .parseSignedClaims(token) // faz parse do token assinado
                .getPayload(); // retorna os dados
    }

    // Verifica se o token expirou
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extrai a data de expiração do token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Valida o token de login do Google (ID Token) usando as chaves públicas do Google
    public Claims getClaimsFromGoogleOauthIdToken(String idToken) {
        try {
            // Busca as chaves públicas (JWKs) do Google
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v3/certs",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new IllegalArgumentException("Falha ao buscar as chaves públicas do Google.");
            }

            // Extrai os pares de chave pública
            Map<String, Object> body = response.getBody();
            List<Map<String, Object>> keys = (List<Map<String, Object>>) body.get("keys");

            // Parser que usa uma função personalizada para localizar a chave correta via 'kid'
            JwtParser jwtParser = Jwts.parser().keyLocator(header -> {
                String kid = (String) header.get("kid");

                for (Map<String, Object> key : keys) {
                    if (kid.equals(key.get("kid"))) {
                        try {
                            // Monta uma chave RSA com modulus e exponent
                            BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode((String) key.get("n")));
                            BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode((String) key.get("e")));
                            RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
                            return KeyFactory.getInstance("RSA").generatePublic(rsaPublicKeySpec);
                        } catch (Exception e) {
                            throw new IllegalArgumentException("Erro ao criar chave pública RSA.", e);
                        }
                    }
                }

                throw new IllegalArgumentException("Chave pública com kid " + kid + " não encontrada.");
            }).build();

            // Faz o parse e retorna os dados contidos no token
            return jwtParser.parseSignedClaims(idToken).getPayload();

        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao validar o ID Token do Google.", e);
        }
    }
}
