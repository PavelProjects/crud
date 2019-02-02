package com.memorynotfound.controller;

import com.memorynotfound.model.User;
import com.memorynotfound.service.UserService;
import com.memorynotfound.service.Uservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping(" /users")
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private Uservice uservice = new UserService();



    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAll() {
        List<User> users = uservice.getAll();
        if (users == null || users.isEmpty()){
            LOG.info("no users found");
            return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }


    @RequestMapping(value = "fid/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> get(@PathVariable("id") String id){
        LOG.info("getting user with id: {}", id);
        User user = uservice.findById(id);

        if (user == null){
            LOG.info("user with id {} not found", id);
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
    @RequestMapping(value = "fname/{name}",method = RequestMethod.GET)
    public ResponseEntity<List<User>> getByName(@PathVariable("name")String name){
        List<User> users = uservice.findByName(name);
        if (users == null || users.isEmpty()){
            LOG.info("user with id {} not found");
            return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> create(@RequestBody User user, UriComponentsBuilder ucBuilder){
        LOG.info("creating new user: {}", user);

        uservice.create(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<User>(user,headers, HttpStatus.CREATED);
    }


    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<User> update(@PathVariable String id, @RequestBody User user){
        LOG.info("updating user: {}", user);
        User currentUser = uservice.findById(id);

        if (currentUser == null){
            LOG.info("User with id {} not found", id);
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        currentUser.setId(user.getId());
        currentUser.setName(user.getName());
        currentUser.setRole(user.getRole());

        //userService.updateName(user);
        return new ResponseEntity<User>(currentUser, HttpStatus.OK);
    }


    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") String id){
        LOG.info("deleting user with id: {}", id);
        User user = uservice.findById(ProfileController.uid);
        if (user.getRole().contains("admin")) {
            uservice.delete(id);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }

}
