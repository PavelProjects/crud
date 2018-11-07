package com.memorynotfound.service;

import com.memorynotfound.model.Meeting;
import com.memorynotfound.model.Message;
import com.memorynotfound.model.MessagingData;

import java.util.List;

public interface MesService {
    void sendToMeeting(Message message, Meeting meeting) throws Exception;
    List<MessagingData> getMessages(String uid, int mid);
}
