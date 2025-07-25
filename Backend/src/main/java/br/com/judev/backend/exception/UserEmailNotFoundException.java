package br.com.judev.backend.exception;

public class UserEmailNotFoundException extends Throwable {
    public UserEmailNotFoundException(String message){
        super(message);
    }
}
