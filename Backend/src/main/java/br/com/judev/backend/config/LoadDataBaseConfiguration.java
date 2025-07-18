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

    private List<User> createUsers(UserRepository userRepository){
            List<String> firstNames = Arrays.asList("John", "Jane", "Michael", "Emily", "David", "Sarah", "James", "Emma",
                    "William", "Olivia", "Liam", "Ava", "Noah", "Isabella", "Ethan", "Sophia", "Mason", "Mia", "Lucas",
                    "Charlotte",
                    "Alexander", "Amelia", "Daniel", "Harper", "Joseph", "Evelyn", "Samuel", "Abigail", "Henry",
                    "Elizabeth",
                    "Sebastian", "Sofia", "Jack", "Avery", "Owen", "Ella", "Gabriel", "Madison", "Matthew", "Scarlett",
                    "Moussa", "Fatou", "Amadou", "Aisha", "Omar", "Aminata", "Ibrahim", "Mariam", "Abdul", "Zainab",
                    "Wei", "Xia", "Ming", "Lin", "Hui", "Yan", "Jie", "Ying", "Feng", "Hong",
                    "Mohammed", "Fatima", "Ahmed", "Aisha", "Ali", "Zainab", "Hassan", "Mariam", "Hussein", "Amira");


            List<String> lastNames = Arrays.asList("Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller",
                    "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas",
                    "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez",
                    "Clark", "Ramirez", "Lewis", "Robinson", "Walker", "Young", "Allen", "King", "Wright", "Scott",
                    "Diop", "Sow", "Fall", "Ndiaye", "Diallo", "Ba", "Sy", "Wade", "Gueye", "Mbaye",
                    "Wang", "Li", "Zhang", "Liu", "Chen", "Yang", "Huang", "Zhou", "Wu", "Xu",
                    "Al-Sayed", "Khan", "Ahmed", "Hassan", "Ali", "Ibrahim", "Rahman", "Sheikh", "Malik", "Qureshi");

            List<String> companies = Arrays.asList("Google", "Microsoft", "Apple", "Amazon", "Meta", "Netflix", "Tesla",
                    "Adobe", "Twitter", "LinkedIn", "Spotify", "Uber", "Airbnb", "Salesforce", "Oracle", "IBM", "Intel",
                    "Samsung", "Sony", "Docker", "Zoom", "Slack", "GitHub", "GitLab", "Redis", "MongoDB", "Orange",
                    "Thales", "Capgemini", "Botify", "Bwat", "EDF", "Algolia", "Zoho", "Shopopop", "Société Générale",
                    "BnpParibas", "Nexitis");

        }
    }
}
