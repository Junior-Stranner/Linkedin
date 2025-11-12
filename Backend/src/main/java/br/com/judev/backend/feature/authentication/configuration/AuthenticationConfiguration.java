package br.com.judev.backend.feature.authentication.configuration;

import br.com.judev.backend.feature.authentication.filter.AuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration

public class AuthenticationConfiguration{

    /*
    Cria um bean do tipo FilterRegistrationBean que registra um filtro HTTP customizado (AuthenticationFilter).

Esse filtro será aplicado para URLs que correspondam ao padrão /api/*.

Isso é uma forma de adicionar um filtro para tratar autenticação nas requisições que chegarem nessas rotas.
     */
    @Bean
    public FilterRegistrationBean<AuthenticationFilter> customAuthenticationFilter(AuthenticationFilter filter) {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }

    /*
    Cria um bean do tipo RestTemplate, que é um cliente HTTP do Spring para fazer requisições REST externas.

Disponibiliza o RestTemplate para ser injetado em outras classes.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

/*
@Bean sim um mecanismo para criar e
registrar componentes (beans) no contexto do Spring.

Quando você marca um método com @Bean,
o Spring executa esse método uma vez no momento da inicialização do contexto
 e registra o objeto retornado como um bean gerenciado.
 */
