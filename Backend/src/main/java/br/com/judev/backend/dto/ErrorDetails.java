package br.com.judev.backend.dto;

import java.util.Date;

public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String path;
    private int status;

    public ErrorDetails(Date timestamp, String message, String path, int status) {
        this.timestamp = timestamp;
        this.message = message;
        this.path = path;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
}
