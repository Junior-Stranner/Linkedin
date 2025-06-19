package br.com.judev.backend.feature.authentication.controller;

import br.com.judev.backend.dto.Response;
import br.com.judev.backend.feature.authentication.dto.AuthenticationRequestDTO;
import br.com.judev.backend.feature.authentication.dto.AuthenticationResponseDTO;
import br.com.judev.backend.feature.authentication.dto.UpdateUserRequest;
import br.com.judev.backend.feature.authentication.dto.UpdateUserResponse;
import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.authentication.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @PutMapping("/reset-password")
    public Response resetPassword(@RequestParam String newPassword, @RequestParam String token,
                                  @RequestParam String email) {
        authenticationService.resetPassword(email, newPassword, token);
        return new Response("Password reset successfully.");
    }

    @PutMapping("/profile/{id}/info")
    public ResponseEntity<UpdateUserResponse> updateUserProfile(
            @RequestAttribute("authenticatedUser") User user,
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {

        if (!user.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "User does not have permission to update this profile.");
        }
        UpdateUserResponse response = authenticationService.updateUserProfile(id, request);
        return ResponseEntity.ok(response);
    }

}
