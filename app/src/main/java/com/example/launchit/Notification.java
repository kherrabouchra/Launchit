package com.example.launchit;


import java.util.Date;

public class Notification {
    private long id;
    private long userId;
    private String message;
    private Date time;

    // Constructor
    public Notification(long id, long userId, String message, Date time) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.time = time;
    }

    // Getters
    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public Date getTime() {
        return time;
    }
}
