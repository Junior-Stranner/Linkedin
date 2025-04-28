package br.com.judev.backend.config;


import br.com.judev.backend.feature.authentication.model.AuthenticationUser;
import br.com.judev.backend.feature.authentication.repository.AuthenticationUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDataBaseConfiguration {

    @Bean
    public CommandLineRunner initDatabase(AuthenticationUserRepository authenticationUserRepository) {
        return args -> {
            if (authenticationUserRepository.findByEmail("jujubaprojects@gmail.com").isEmpty()) {
                AuthenticationUser authenticationUser = new AuthenticationUser("jujubaprojects@gmail.com", "jujuba");
                authenticationUserRepository.save(authenticationUser);
            }
        };
    }
}
