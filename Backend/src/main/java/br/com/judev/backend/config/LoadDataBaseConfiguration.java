package br.com.judev.backend.config;


import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.authentication.repository.UserRepository;
import br.com.judev.backend.feature.authentication.utils.Encoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDataBaseConfiguration {

    private final Encoder encoder;

    public LoadDataBaseConfiguration(Encoder encoder) {
        this.encoder = encoder;
    }


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
