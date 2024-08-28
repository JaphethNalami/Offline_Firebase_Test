package com.example.offlinefirebasetest;

public class MessageClass {
    private String message, id, status;

    public MessageClass() {
    }

    public MessageClass(String message, String status, String id) {
        this.message = message;
        this.status = status;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
