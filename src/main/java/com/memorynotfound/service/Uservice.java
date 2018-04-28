package com.memorynotfound.service;

import com.memorynotfound.model.User;
import com.memorynotfound.model.UserEvent;

import java.util.List;

public interface Uservice {
    List<User> getAll();
    List<User> getFriends(int id);
    void addFriend(int id, int fid);
    void deleteFriend(int id, int fid);
    List<UserEvent> getEvents(int id);
    void addEvent (int id, UserEvent userEvent);
    User findById(int id);
    List<User> findByName(String name);
    void create (User user1);
    void delete (int id);
    int auth (String username, String pass);
}
