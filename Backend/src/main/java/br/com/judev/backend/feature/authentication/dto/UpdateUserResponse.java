package br.com.judev.backend.feature.authentication.dto;

import br.com.judev.backend.feature.authentication.model.User;

public record UpdateUserResponse(Long id,
                                 String firstName,
                                 String lastName,
                                 String company,
                                 String position,
                                 String location,
                                 String about) {

    public UpdateUserResponse(User user) {
        this(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getCompany(),
                user.getPosition(),
                user.getLocation(),
                user.getAbout()
        );
    }
}
