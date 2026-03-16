package com.ayush.demo.error;


import java.time.LocalDateTime;

public class ErrorResponse {

    private int statusCode;
    private String message;
    private LocalDateTime timestamp;
    private String url;

    public ErrorResponse(int statusCode, String message, LocalDateTime timestamp, String url) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = timestamp;
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
