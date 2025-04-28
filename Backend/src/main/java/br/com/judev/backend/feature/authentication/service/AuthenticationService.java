package br.com.judev.backend.feature.authentication.service;

import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.authentication.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User not found."));
    }

}
