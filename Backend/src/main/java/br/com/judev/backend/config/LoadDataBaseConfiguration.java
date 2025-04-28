package br.com.judev.backend.config;


import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.authentication.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDataBaseConfiguration {

    @Bean
    public CommandLineRunner initDatabase(UserRepository UserRepository) {
        return args -> {
            if (UserRepository.findByEmail("jujubaprojects@gmail.com").isEmpty()) {
                User authenticationUser = new User("jujubaprojects@gmail.com", "jujuba");
                UserRepository.save(authenticationUser);
            }
        };
    }
}
