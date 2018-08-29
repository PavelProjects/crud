package com.memorynotfound.service;


import com.memorynotfound.model.Meeting;
import com.memorynotfound.model.User;

import java.util.List;

public interface Mservice {
    Meeting createMeeting(Meeting meeting);
    List<Meeting> getAllMeeting();
    List<Meeting> getUserMeetings(String id);
    Meeting getMettById(String uid,int id);
    void addUser(int id, String uid);
    void deleteUser(int id, String uid);
    Meeting update(Meeting meeting);
}
