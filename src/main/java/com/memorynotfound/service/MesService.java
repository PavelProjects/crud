package com.memorynotfound.service;

import com.memorynotfound.model.Meeting;
import com.memorynotfound.model.Message;

public interface MesService {
    void sendToMeeting(Message message, Meeting meeting) throws Exception;

}
