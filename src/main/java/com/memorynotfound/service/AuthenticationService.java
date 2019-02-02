package com.memorynotfound.service;


import com.memorynotfound.controller.ProfileController;

import java.io.IOException;
import java.util.*;

public class AuthenticationService {
    private Uservice us =  new UserService();
    public boolean authenticate(String authCredentials) {
        boolean flag = false;
        if (null == authCredentials)
            return false;
        final String encodedUserPassword = authCredentials.replaceFirst("Basic"
                + " ", "");
        String usernameAndPassword = null;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(
                    encodedUserPassword);
            usernameAndPassword = new String(decodedBytes, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final StringTokenizer tokenizer = new StringTokenizer(
                usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();
        String id = us.auth(username,password);
        if (id.length()>0){
            flag=true;
            ProfileController.uid=id;
        }
        return flag;
    }
}