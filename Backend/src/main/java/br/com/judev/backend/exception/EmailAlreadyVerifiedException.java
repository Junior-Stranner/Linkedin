package br.com.judev.backend.exception;

import org.springframework.web.context.request.WebRequest;

public class EmailAlreadyVerifiedException extends RuntimeException {
    public EmailAlreadyVerifiedException(String message) {
        super(message);
    }
}


