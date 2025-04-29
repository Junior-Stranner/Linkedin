package br.com.judev.backend.feature.authentication.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequestDTO(@NotBlank(message = "Email is mandatory") String email,
                                       @NotBlank(message = "Password is mandatory") String password) {

    @Override
    public @NotBlank(message = "Email is mandatory") String email() {
        return email;
    }

    @Override
    public @NotBlank(message = "Password is mandatory") String password() {
        return password;
    }
}
