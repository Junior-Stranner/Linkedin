package br.com.judev.backend.feature.authentication.service;

import br.com.judev.backend.exception.EmailAlreadyVerifiedException;
import br.com.judev.backend.exception.TokenExpiredException;
import br.com.judev.backend.exception.UserEmailNotFoundException;
import br.com.judev.backend.feature.authentication.dto.*;
import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.authentication.repository.UserRepository;
import br.com.judev.backend.feature.authentication.utils.Encoder;
import br.com.judev.backend.feature.authentication.utils.JwtToken;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserRepository userRepository;
    private final int durationInMinutes = 1;
    private final Encoder encoder;
    private final JwtToken jwtToken;
    private final EmailService emailService;

    public AuthenticationService(UserRepository userRepository, Encoder encoder, JwtToken jwtToken, EmailService emailService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtToken = jwtToken;
        this.emailService = emailService;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserEmailNotFoundException("User not found with email: " + email));
    }

    public static String generateEmailVerificationToken() {
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            token.append(random.nextInt(10));
        }
        return token.toString();
    }

    public void sendEmailVerificationToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException("User not found with email: " + email));

        if (user.getEmailVerified()) {
            throw new EmailAlreadyVerifiedException("Email already verified.");
        }

        String emailVerificationToken = generateEmailVerificationToken();
        String hashedToken = encoder.encode(emailVerificationToken);

        user.setEmailVerificationToken(hashedToken);
        user.setEmailVerificationTokenExpiryDate(LocalDateTime.now().plusMinutes(durationInMinutes));
        userRepository.save(user);

        String subject = "Email Verification";
        String body = String.format("""
                Only one step to take full advantage of LinkedIn.
                Enter this code to verify your email: %s. The code will expire in %s minutes.""",
                emailVerificationToken, durationInMinutes);
        try {
            emailService.sendEmail(email, subject, body);
        } catch (Exception e) {
            logger.info("Error while sending email: {}", e.getMessage());
        }
    }

    public void validateEmailVerificationToken(String token, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException("User not found with email: " + email));

        if (!encoder.matches(token, user.getEmailVerificationToken())) {
            throw new IllegalArgumentException("Email verification token is invalid.");
        }

        if (user.getEmailVerificationTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Email verification token expired.");
        }

        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationTokenExpiryDate(null);
        userRepository.save(user);
    }

    public AuthenticationResponseDTO register(AuthenticationRequestDTO registerRequest) {
        try {
            User user = new User(registerRequest.email(), encoder.encode(registerRequest.password()));
            String emailVerificationToken = generateEmailVerificationToken();
            String hashedToken = encoder.encode(emailVerificationToken);
            user.setEmailVerificationToken(hashedToken);
            user.setEmailVerificationTokenExpiryDate(LocalDateTime.now().plusMinutes(durationInMinutes));
            userRepository.save(user);

            String subject = "Email Verification";
            String body = String.format("""
                    Only one step to take full advantage of LinkedIn.
                    Enter this code to verify your email: %s. The code will expire in %s minutes.""",
                    emailVerificationToken, durationInMinutes);
            emailService.sendEmail(registerRequest.email(), subject, body);
            String authToken = jwtToken.generateToken(registerRequest.email());
            return new AuthenticationResponseDTO(authToken, "User registered successfully.");

        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("UK_email")) {
                throw new DataIntegrityViolationException("Email already exists. Please use another email or login.");
            }
            throw new DataIntegrityViolationException("Database error while registering user.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthenticationResponseDTO login(AuthenticationRequestDTO loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UserEmailNotFoundException("User not found."));

        if (!encoder.matches(loginRequest.password(), user.getPassword())) {
            throw new IllegalArgumentException("Password is incorrect.");
        }

        String token = jwtToken.generateToken(loginRequest.email());
        return new AuthenticationResponseDTO(token, "Authentication succeeded");
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException("User not found."));
    }

    public void sendPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException("User not found."));

        String passwordResetToken = generateEmailVerificationToken();
        String hashedToken = encoder.encode(passwordResetToken);
        user.setPasswordResetToken(hashedToken);
        user.setPasswordResetTokenExpiryDate(LocalDateTime.now().plusMinutes(durationInMinutes));
        userRepository.save(user);

        String subject = "Password Reset";
        String body = String.format("""
                You requested a password reset.

                Enter this code to reset your password: %s. The code will expire in %s minutes.""",
                passwordResetToken, durationInMinutes);
        try {
            emailService.sendEmail(email, subject, body);
        } catch (Exception e) {
            logger.info("Error while sending email: {}", e.getMessage());
        }
    }

    public void resetPassword(String email, String newPassword, String token) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException("User not found."));

        if (!encoder.matches(token, user.getPasswordResetToken())) {
            throw new IllegalArgumentException("Password reset token is invalid.");
        }

        if (user.getPasswordResetTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Password reset token expired.");
        }

        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiryDate(null);
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }

    public UpdateUserResponse updateUserProfile(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.updateProfile(request.firstName(), request.lastName(),
                request.company(), request.position(),
                request.location(), request.about());

        userRepository.save(user);
        return new UpdateUserResponse(user);
    }

    public UserResponse getUserById(Long receiverId) {
        User user = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
        return new UserResponse(user);
    }

    public void deleteUser(String email) {
        User deleteUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException("User not found, and cannot exclude!"));
        userRepository.delete(deleteUser);
    }
}
