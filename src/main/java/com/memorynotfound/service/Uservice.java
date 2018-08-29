package com.memorynotfound.service;

import com.memorynotfound.model.User;
import com.memorynotfound.model.UserEvent;

import java.util.List;

public interface Uservice {
    List<User> getAll();
    List<User> getFriends(String id);
    void addFriend(String id, String fid);
    void deleteFriend(String id, String fid);
    List<UserEvent> getEvents(String id);
    void addEvent (String id, UserEvent userEvent);
    User findById(String id);
    User findByMail (String mail);
    List<User> findByName(String name);
    boolean create (User user1);
    void delete (String id);
    String auth (String username, String pass);
}
