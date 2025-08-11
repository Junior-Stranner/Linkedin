package br.com.judev.backend.exception.handler;

import br.com.judev.backend.dto.ErrorDetails;
import br.com.judev.backend.exception.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(MethodArgumentNotValidException e, WebRequest request) {
        StringBuilder errorMessage = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );

        return ResponseEntity.badRequest().body(new ErrorDetails(
                new Date(),
                errorMessage.toString(),
                request.getDescription(false)
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> handleIllegalArgument(IllegalArgumentException e, WebRequest request) {
        return ResponseEntity.badRequest().body(new ErrorDetails(
                new Date(),
                e.getMessage(),
                request.getDescription(false)
        ));
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ErrorDetails> handleIncorrectPassword(IncorrectPasswordException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDetails(
                new Date(),
                e.getMessage(),
                request.getDescription(false)
        ));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleEmailAlreadyExists(EmailAlreadyExistsException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDetails(
                new Date(),
                e.getMessage(),
                request.getDescription(false)
        ));
    }

    @ExceptionHandler(EmailSendFailureException.class)
    public ResponseEntity<ErrorDetails> handleEmailSendFailure(EmailSendFailureException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDetails(
                new Date(),
                e.getMessage(),
                request.getDescription(false)
        ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrityViolation(DataIntegrityViolationException e, WebRequest request) {
        String message = e.getMessage().toLowerCase().contains("duplicate") || e.getMessage().contains("UK_email")
                ? "Email already exists. Please use another email or login."
                : "A database error occurred. Please try again later.";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDetails(
                new Date(),
                message,
                request.getDescription(false)
        ));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorDetails> handleInvalidToken(InvalidTokenException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDetails(
                new Date(),
                e.getMessage(),
                request.getDescription(false)
        ));
    }

    @ExceptionHandler(UserEmailNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleUserEmailNotFound(UserEmailNotFoundException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDetails(
                new Date(),
                e.getMessage(),
                request.getDescription(false)
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGeneralException(Exception e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDetails(
                new Date(),
                e.getMessage(),
                request.getDescription(false)
        ));
    }
}
