package br.com.judev.backend.feature.authentication.controller;

import br.com.judev.backend.dto.Response;
import br.com.judev.backend.feature.authentication.dto.AuthenticationRequestDTO;
import br.com.judev.backend.feature.authentication.dto.AuthenticationResponseDTO;
import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.authentication.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public User getUser(){
      return authenticationService.getUserByEmail("jujubaprojects@gmail.com");
    }

    @PostMapping("/login")
    public AuthenticationResponseDTO loginPage(@Valid @RequestBody AuthenticationRequestDTO loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PostMapping("/register")
    public AuthenticationResponseDTO registerPage(@Valid @RequestBody AuthenticationRequestDTO registerRequest) {
        return authenticationService.register(registerRequest);
    }

    @PutMapping("/validate-email-verification-token")
    public Response verifyEmail(@RequestParam String token, @RequestAttribute("authenticatedUser") User user) {
        authenticationService.validateEmailVerificationToken(token, user.getEmail());
        return new Response("Email verified successfully.");
    }

    @GetMapping("/send-email-verification-token")
    public Response sendEmailVerificationToken(@RequestAttribute("authenticatedUser") User user) {
        authenticationService.sendEmailVerificationToken(user.getEmail());
        return new Response("Email verification token sent successfully.");
    }

    @PutMapping("/send-password-reset-token")
    public Response sendPasswordResetToken(@RequestParam String email) {
        authenticationService.sendPasswordResetToken(email);
        return new Response("Password reset token sent successfully.");
    }
}
