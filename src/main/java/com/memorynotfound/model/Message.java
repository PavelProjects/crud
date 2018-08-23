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

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getFrom() {
            return f;
        }

        public void setFrom(String from) {
            this.f = from;
        }
    }
}
