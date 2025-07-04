package br.com.judev.backend.feature.authentication.dto;

import br.com.judev.backend.feature.authentication.model.User;

public record UserResponse(
        Long id,
        String email,
        Boolean emailVerified,
        String emailVerificationToken,
        String firstName,
        String lastName,
        String company,
        String position,
        String location,
        String about
) {

    //2. Construtor que recebe um objeto User:
    //java
    //Copiar
    //Editar
    public UserResponse(User user) {
        this(
                user.getId(),
                user.getEmail(),
                user.getEmailVerified(),
                user.getEmailVerificationToken(),
                user.getFirstName(),
                user.getLastName(),
                user.getCompany(),
                user.getPosition(),
                user.getLocation(),
                user.getAbout()
        );
    }


}
