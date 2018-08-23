package com.memorynotfound.model;

import java.io.Serializable;

public class User implements Serializable {

    private String id;
    private String name;
    private String role;
    private String password;
    private String mail;


    public User() {
    }

    public User(String id, String name, String role, String password, String mail) {
        this.id = id;
        this.name = name;
        this.role=role;
        this.password=password;
        this.mail = mail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}