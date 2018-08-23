package com.memorynotfound.controller;


import com.memorynotfound.model.User;
import com.memorynotfound.service.UserService;
import com.memorynotfound.service.Uservice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SignInController {
    private Uservice uservice = new UserService();

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@RequestBody User user){
        if (!uservice.create(user)){
            return new ResponseEntity<User>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<User>(user,HttpStatus.CREATED);
    }
}
