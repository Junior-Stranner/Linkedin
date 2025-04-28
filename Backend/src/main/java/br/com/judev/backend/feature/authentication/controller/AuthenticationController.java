package br.com.judev.backend.feature.authentication.controller;

import br.com.judev.backend.feature.authentication.dto.AuthenticationRequestDTO;
import br.com.judev.backend.feature.authentication.dto.AuthenticationResponseDTO;
import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.authentication.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/register")
    public AuthenticationResponseDTO registerPage(@Valid @RequestBody AuthenticationRequestDTO registerRequestBody) {
        return authenticationService.register(registerRequestBody);
    }
}
