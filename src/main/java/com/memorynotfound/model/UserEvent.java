package com.memorynotfound.model;

import java.io.Serializable;

public class UserEvent implements Serializable {
    private String date;
    private String event;

    public UserEvent(){

    }
    public UserEvent(String date,String event){
        this.date=date;
        this.event=event;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "Event{date="+date+",event="+event+'\''+"}";
    }
}
