package com.memorynotfound.model;

import java.io.Serializable;

public class Message implements Serializable {
    private MessagingData data;
    private String to;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
    public MessagingData getData(){
        return data;
    }

    public void setData(MessagingData data) {
        this.data = data;
    }

}
