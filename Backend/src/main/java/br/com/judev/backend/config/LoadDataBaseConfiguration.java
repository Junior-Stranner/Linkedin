package br.com.judev.backend.config;


import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.authentication.repository.UserRepository;
import br.com.judev.backend.feature.authentication.utils.Encoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class LoadDataBaseConfiguration {
    private static final int NUM_USERS = 500;
    private static final int MIN_POSTS_PER_USER = 1;
    private static final int MAX_POSTS_PER_USER = 3;
    private static final int MIN_CONNECTIONS_PER_USER = 0;
    private static final int MAX_CONNECTIONS_PER_USER = 3;
    private final Encoder encoder;

    public LoadDataBaseConfiguration(Encoder encoder) {
        this.encoder = encoder;
    }

    // Método que será executado ao subir a aplicação
    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            System.out.println("Iniciando o CommandLineRunner...");

            if (userRepository.findByEmail("jujubaprojects@gmail.com").isEmpty()) {
                String encodedPassword = encoder.encode("jujuba350");
                System.out.println("Senha codificada: " + encodedPassword);
                User authenticationUser = new User("jujubaprojects@gmail.com", encodedPassword);
                userRepository.save(authenticationUser);
            }
        };

    }
}
