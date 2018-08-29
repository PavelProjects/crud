package com.memorynotfound.model;

public class Message {
    private Data data;
    private String to;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
    public Data getData(){
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data{
        private String message;
        private String f;
        private String event;
        private User user;

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
}
