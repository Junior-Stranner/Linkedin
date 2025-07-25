package br.com.judev.backend.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message){
        super(message);
    }
}
