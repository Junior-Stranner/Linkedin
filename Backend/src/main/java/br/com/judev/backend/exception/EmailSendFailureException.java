package br.com.judev.backend.exception;

public class EmailSendFailureException extends RuntimeException {
    public EmailSendFailureException(String message, Throwable cause) { super(message, cause); }
}
