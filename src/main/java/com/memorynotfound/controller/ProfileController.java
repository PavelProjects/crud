package com.memorynotfound.controller;

import com.memorynotfound.model.User;
import com.memorynotfound.model.UserEvent;
import com.memorynotfound.service.UserService;
import com.memorynotfound.service.Uservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    public static String uid;

    private Uservice uservice = new UserService();

    @RequestMapping(value = "/friends",method = RequestMethod.GET)
    public ResponseEntity<List<User>> getFriends(){
        List<User> friends = uservice.getFriends(uid);
        if (friends.isEmpty()||friends==null){
            return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<User>>(friends,HttpStatus.OK);
    }
    @RequestMapping(value = "/friends/add", method = RequestMethod.POST)
    public ResponseEntity<User> addFriend(@RequestBody User user){
        uservice.addFriend(uid,user.getMail());
        return new ResponseEntity<User>(user,HttpStatus.OK);
    }
    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public ResponseEntity<User> authUser(){
        User user = uservice.findByMail(uid);
        user.setMail(uid);
        return new ResponseEntity<User>(user,HttpStatus.OK);
    }

}
