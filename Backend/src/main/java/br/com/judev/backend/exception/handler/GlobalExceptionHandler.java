package br.com.judev.backend.exception.handler;

import br.com.judev.backend.dto.ErrorDetails;
import br.com.judev.backend.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorDetails> buildErrorResponse(Exception e, WebRequest request, HttpStatus status) {
        return ResponseEntity.status(status).body(new ErrorDetails(
                new Date(),
                e.getMessage(),
                request.getDescription(false),
                status.value()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(MethodArgumentNotValidException e, WebRequest request) {
        StringBuilder errorMessage = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );

        return ResponseEntity.badRequest().body(new ErrorDetails(
                new Date(),
                errorMessage.toString(),
                request.getDescription(false),
                HttpStatus.BAD_REQUEST.value()
        ));
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            EmailAlreadyExistsException.class,
            EmailSendFailureException.class
    })
    public ResponseEntity<ErrorDetails> handleBadRequest(Exception e, WebRequest request) {
        return buildErrorResponse(e, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ErrorDetails> handleIncorrectPassword(IncorrectPasswordException e, WebRequest request) {
        return buildErrorResponse(e, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrityViolation(DataIntegrityViolationException e, WebRequest request) {
        String message = e.getMessage().toLowerCase().contains("duplicate") || e.getMessage().contains("UK_email")
                ? "Email already exists. Please use another email or login."
                : "A database error occurred. Please try again later.";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDetails(
                new Date(),
                message,
                request.getDescription(false),
                HttpStatus.BAD_REQUEST.value()
        ));
    }

    @ExceptionHandler({InvalidTokenException.class, UserEmailNotFoundException.class})
    public ResponseEntity<ErrorDetails> handleNotFound(Exception e, WebRequest request) {
        return buildErrorResponse(e, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorDetails> handleMultipartException(MultipartException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDetails(
                new Date(),
                "Erro ao processar o upload. Verifique o formato do corpo ou o arquivo enviado.",
                request.getDescription(false),
                HttpStatus.BAD_REQUEST.value()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGeneralException(Exception e, WebRequest request) {
        return buildErrorResponse(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
