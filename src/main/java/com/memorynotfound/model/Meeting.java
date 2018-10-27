package com.memorynotfound.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Meeting implements Serializable{
    private int id;
    private String meetname;
    private String admin;
    private List<User> users = new ArrayList();
    private String date;
    private String time;
    private String adress;

    public Meeting(){}

    public Meeting(int id, String name, String admin,List<User> users, String date, String time, String adress){
        this.id=id;
        this.meetname=name;
        this.admin=admin;
        this.users=users;
        this.date = date;
        this.time = time;
        this.adress = adress;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getName(){
        return meetname;
    }
    public void setName(String name){
        this.meetname=name;
    }
    public String getAdmin(){
        return admin;
    }
    public void setAdmin(String admin){
        this.admin=admin;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public String getAdress() {
        return adress;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
