package br.com.judev.backend.feature.authentication.filter;

import br.com.judev.backend.feature.authentication.model.User;
import br.com.judev.backend.feature.authentication.service.AuthenticationService;
import br.com.judev.backend.feature.authentication.utils.JwtToken;
import jakarta.servlet.http.HttpFilter;
import org.springframework.stereotype.Component;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
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

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 1. CORS (libera requisições de outros domínios, útil para front-end separado)
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");


        // 2. Se for uma requisição OPTIONS (pré-flight do CORS), devolve 200 OK
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }


        // 3. Verifica se a URL é pública (não exige autenticação)
        String path = request.getRequestURI();
        if (unsecuredEndpoints.contains(path) || path.startsWith("/api/v1/authentication/oauth") || path.startsWith("/api/v1/storage")) {
            chain.doFilter(request, response); // segue a requisição normalmente
            return;
        }


        try {
            // 4. Requisições protegidas: busca o token no cabeçalho Authorization
            String authorization = request.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new ServletException("Token missing.");
            }


            // 5. Valida o token
            String token = authorization.substring(7); // Remove "Bearer "
            if (jsonWebTokenService.isTokenExpired(token)) {
                throw new ServletException("Invalid token");
            }

            // 6. Recupera email do token e busca o usuário
            String email = jsonWebTokenService.getEmailFromToken(token);
            User user = authenticationService.getUser(email);

            // 7. Anexa o usuário autenticado na requisição para uso posterior
            request.setAttribute("authenticatedUser", user);

            // 8. Passa a requisição adiante
            chain.doFilter(request, response);

          // 9. Em caso de erro, responde com status 401 (não autorizado)
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Invalid authentication token, or token missing.\"}");
        }
    }
}

/*Esse filtro faz o seguinte:
Permite requisições a rotas públicas
Bloqueia rotas protegidas se não tiver um token JWT válido
Valida o token
Anexa o usuário autenticado à requisição (request.setAttribute(...))
Passa a requisição adiante*/