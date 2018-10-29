package com.memorynotfound.service;

import com.memorynotfound.model.Meeting;
import com.memorynotfound.model.Message;

import java.util.List;

public interface MesService {
    void sendToMeeting(Message message, Meeting meeting) throws Exception;
    List<Message> getMessages(String uid,int mid);
}
