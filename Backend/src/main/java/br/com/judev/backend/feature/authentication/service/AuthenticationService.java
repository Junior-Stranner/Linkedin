package br.com.judev.backend.feature.authentication.service;

import br.com.judev.backend.feature.authentication.dto.AuthenticationRequestDTO;
import br.com.judev.backend.feature.authentication.dto.AuthenticationResponseDTO;
import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.authentication.repository.UserRepository;
import br.com.judev.backend.feature.authentication.utils.Encoder;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserRepository userRepository;
    private final int durationInMinutes = 1;
    private final Encoder encoder;
    private final TokenService tokenService;


    public AuthenticationService(UserRepository userRepository, Encoder encoder, TokenService tokenService){
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.tokenService = tokenService;
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User not found."));
    }

    public static String generateEmailVerificationToken() {
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            token.append(random.nextInt(10));
        }
        return token.toString();
    }

    public AuthenticationResponseDTO register(AuthenticationRequestDTO registerRequest) {
        User user = userRepository.save(new User(
                registerRequest.email(), encoder.encode(registerRequest.password())));

        String emailVerificationToken = generateEmailVerificationToken();
        String hashedToken = encoder.encode(emailVerificationToken);


        userRepository.save(user);

        String subject = "Email Verification";
        String body = String.format("""
                Only one step to take full advantage of LinkedIn.
                Enter this code to verify your email: %s. The code will expire in %s minutes.""",
                emailVerificationToken, durationInMinutes);
        String authToken = tokenService.generateToken(registerRequest.email());
        return new AuthenticationResponseDTO(authToken, "User registered successfully.");
    }
}
