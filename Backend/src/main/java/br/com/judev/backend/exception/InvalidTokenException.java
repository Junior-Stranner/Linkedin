package br.com.judev.backend.exception;

public class InvalidTokenException extends Throwable {
    public InvalidTokenException(String message){
        super(message);
    }
}
