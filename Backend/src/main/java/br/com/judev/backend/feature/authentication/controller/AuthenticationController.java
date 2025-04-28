package br.com.judev.backend.feature.authentication.controller;

import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.authentication.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
