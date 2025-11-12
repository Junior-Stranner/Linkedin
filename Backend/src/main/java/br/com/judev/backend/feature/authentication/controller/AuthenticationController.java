package br.com.judev.backend.feature.authentication.controller;

import br.com.judev.backend.dto.Response;
import br.com.judev.backend.feature.authentication.dto.*;
import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.authentication.repository.UserRepository;
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
    private final UserRepository userRepository;

    public AuthenticationController(AuthenticationService authenticationService, UserRepository userRepository) {
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
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

    //1 Vem antes do verifyEmail
    @GetMapping("/send-email-verification-token")
    public Response sendEmailVerificationToken(@RequestAttribute("authenticatedUser") User user) {
        authenticationService.sendEmailVerificationToken(user.getEmail());
        return new Response("Email verification token sent successfully.");
    }

    @PutMapping("/validate-email-verification-token")
    public Response verifyEmail(@RequestParam String token, @RequestAttribute("authenticatedUser") User user) {
        authenticationService.validateEmailVerificationToken(token, user.getEmail());
        return new Response("Email verified successfully.");
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
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {
        UpdateUserResponse response = authenticationService.updateUserProfile(id, request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse userResponse = authenticationService.getUserById(id);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<User> getUser(@RequestAttribute("authenticatedUser") User user) {
        return ResponseEntity.ok(authenticationService.getUser(user.getEmail()));
    }

    @DeleteMapping("/delete")
    public Response deleteUser(@RequestAttribute("authenticatedUser") User user) {
        authenticationService.deleteUser(user.getEmail());
        return new Response("User deleted successfully.");
    }
}

/*
"authenticatedUser"
// Exemplo em um filtro ou interceptor
// usuário obtido via token JWT
Atributo da requisição, colocado por filtros/interceptors

 Quando usar @RequestAttribute?
Quando você injeta dados via filtro, como o User autenticado.
Para evitar buscar o usuário no banco de novo em cada controller.
Para reaproveitar dados compartilhados na requisição (ex: tenant, token, usuário).
 */
