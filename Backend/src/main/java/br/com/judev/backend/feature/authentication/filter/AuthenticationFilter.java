package br.com.judev.backend.feature.authentication.filter;

import br.com.judev.backend.feature.authentication.service.AuthenticationService;
import br.com.judev.backend.feature.authentication.utils.JwtToken;
import jakarta.servlet.http.HttpFilter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AuthenticationFilter  extends HttpFilter {
    private final List<String> unsecuredEndpoints = Arrays.asList(
            "/api/v1/authentication/login",
            "/api/v1/authentication/register",
            "/api/v1/authentication/send-password-reset-token",
            "/api/v1/authentication/reset-password");

    private final JwtToken jsonWebTokenService;
    private final AuthenticationService authenticationService;

    public AuthenticationFilter(JwtToken jsonWebTokenService, AuthenticationService authenticationService) {
        this.jsonWebTokenService = jsonWebTokenService;
        this.authenticationService = authenticationService;
    }


}
