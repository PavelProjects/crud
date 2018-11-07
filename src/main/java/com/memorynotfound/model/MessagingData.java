package com.memorynotfound.model;

public class MessagingData {
    private int id;
    private String message;
    private String f;
    private String event;
    private User user;
    private String info;
    private int mid;


    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String inf) {
        this.info = inf;
    }

    public String getEvent() {
        return event;
    }

    public User getUser() {
        return user;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }
}
