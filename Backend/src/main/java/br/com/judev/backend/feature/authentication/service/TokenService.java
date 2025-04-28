/*package br.com.judev.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class TokenService {


    // Geração do token JWT
    public String generateToken(UserDetails userDetails) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer(issuer) // Emissor do token
                    .withSubject(userDetails.getUsername()) // O assunto (geralmente o email do usuário)
                    .withAudience(audience) // Definir audiência do token (quem pode usar esse token)
                    .withExpiresAt(generateExpirationDate()) // Definir data de expiração
                    .sign(algorithm); // Assina com a chave secreta
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token", exception);
        }
    }

    // Método para gerar a data de expiração do token
    public Instant generateExpirationDate() {
        // Define a expiração do token para 12 horas a partir do momento atual
        return LocalDateTime.now().plusSeconds(expirationTime).toInstant(ZoneOffset.of("-3"));
    }

    // Método para validar o token JWT
    public String validateToken(String token) {
        try {
            // Algoritmo de assinatura HMAC com a chave secreta
            var algorithm = Algorithm.HMAC256(secret);

            // Verifica a validade do token com o algoritmo HMAC256 e o emissor esperado
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer(issuer) // Verifica se o emissor é o esperado
                    .withAudience(audience) // Verifica se a audiência é a esperada
                    .build() // Constrói a validação do token
                    .verify(token); // Verifica o token e decodifica

            return decodedJWT.getSubject(); // Retorna o assunto do token (geralmente o email do usuário)
        } catch (JWTVerificationException e) {
            // Caso o token não seja válido ou ocorra erro na verificação, retorna null
            return null;
        }
    }

    // Verifica se o token está expirado
    public boolean isTokenExpired(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getExpiresAt().before(new Date());
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    // Método para gerar o token de refresh (com expiração maior)
    public String generateRefreshToken(UserDetails userDetails) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer(issuer) // Emissor do token
                    .withSubject(userDetails.getUsername()) // O assunto (geralmente o email do usuário)
                    .withAudience(audience) // Definir audiência do token
                    .withExpiresAt(generateRefreshTokenExpirationDate()) // Data de expiração para o refresh token
                    .sign(algorithm); // Assina com a chave secreta
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o refresh token", exception);
        }
    }

    // Método para gerar a data de expiração do refresh token
    public Instant generateRefreshTokenExpirationDate() {
        // Define a expiração do refresh token para 30 dias a partir do momento atual
        return LocalDateTime.now().plusDays(30).toInstant(ZoneOffset.of("-3"));
    }

    // Método para validar o refresh token
    public String validateRefreshToken(String token) {
        return validateToken(token);
    }

    // Método para revogar um token (caso precise)
    // Para revogar, normalmente se usa uma lista de tokens inválidos ou blacklist
    public void revokeToken(String token) {
        // Adiciona o token revogado ao Redis com o tempo de expiração igual à expiração do token
        String tokenKey = "revoked_token:" + token;

        // A expiração do token revogado é a mesma do token original para garantir que não permaneça válido após expirar
        DecodedJWT decodedJWT = JWT.decode(token);
        long expirationTime = decodedJWT.getExpiresAt().getTime() - System.currentTimeMillis();

        // Armazenando o token revogado no Redis com o tempo de expiração
        redisTemplate.opsForValue().set(tokenKey, token, expirationTime, TimeUnit.MILLISECONDS);
    }

    // Método para verificar se um token foi revogado (na blacklist)
    public boolean isTokenRevoked(String token) {
        String tokenKey = "revoked_token:" + token;
        return redisTemplate.hasKey(tokenKey);
    }
}
*/