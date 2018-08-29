package com.memorynotfound.service;


import com.memorynotfound.model.Message;

public interface FbService {
    String sendMessage(Message message) throws Exception;
    void TokenChanged (String token,String mail);
}
