package com.memorynotfound.service;


import com.memorynotfound.model.Meeting;
import com.memorynotfound.model.User;

import java.util.List;

public interface Mservice {
    List<Meeting> getAllMeeting();
    List<Meeting> getUserMeetings(int id);
    Meeting getMettById(int uid,int id);
    void addUser(int id, int uid);
    Meeting update(Meeting meeting);
}
