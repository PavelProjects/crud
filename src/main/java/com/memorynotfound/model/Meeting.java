package com.memorynotfound.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Meeting implements Serializable{
    private int id;
    private String meetname;
    private String admin;
    private List<User> users = new ArrayList();

    public Meeting(){}

    public Meeting(int id, String name, String admin,List<User> users ){
        this.id=id;
        this.meetname=name;
        this.admin=admin;
        this.users=users;
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
}
