package br.com.judev.backend.feature.authentication.dto;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        String company,
        String position,
        String location,
        String about
) {}

