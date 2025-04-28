package br.com.judev.backend.feature.authentication.utils;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class Encoder {

    // Método para codificar a senha usando SHA-256 e Base64.
    public String encode(CharSequence rawPassword) {
        try {
            // Cria uma instância do algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Gera o hash da senha (convertendo a senha em bytes e aplicando o algoritmo)
            byte[] hash = digest.digest(rawPassword.toString().getBytes());

            // Codifica o hash gerado em Base64 e retorna o resultado
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            // Caso o algoritmo SHA-256 não esteja disponível, lança uma exceção
            throw new RuntimeException("Error encoding password", e);
        }
    }

    // Método para verificar se uma senha informada corresponde à senha codificada
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // Codifica a senha informada e compara com a senha codificada
        return encode(rawPassword).equals(encodedPassword);
    }
}

/*Codificação da Senha: O processo de codificação da senha com SHA-256 ajuda a garantir que as
 senhas não sejam armazenadas em texto simples no banco de dados. Usar o Base64 permite que o
 hash seja armazenado em formato textual.

Verificação de Senha: O método matches é útil quando você precisa comparar a senha informada por
um usuário com a senha armazenada (já codificada) para autenticação.
*/

