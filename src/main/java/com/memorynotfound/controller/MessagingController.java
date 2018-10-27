package com.memorynotfound.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memorynotfound.model.Meeting;
import com.memorynotfound.model.Message;
import com.memorynotfound.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/messaging")
public class MessagingController {
    private MesService mesService = new MessageService();
    private Mservice mservice = new MeetingService();
    private FbService fb= new FirebaseService();
    private ObjectMapper objectMapper = new ObjectMapper();
    private final Logger LOG = LoggerFactory.getLogger(UserController.class);


    @RequestMapping(value = "/send",method = RequestMethod.POST)
    public ResponseEntity<Void> sendMessage(@RequestBody Message message) throws Exception {
        LOG.info("send message to "+message.getTo());
        if (message.getTo().equals("meeting")) {
            Meeting meeting = mservice.getMettById(ProfileCotroller.uid, message.getData().getMeeting().getId());
            if (meeting != null) {
                mesService.sendToMeeting(message, meeting);
            }
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    @RequestMapping(value = "/token",method = RequestMethod.POST)
    public ResponseEntity<Void> updateToken(@RequestBody String token){
        fb.TokenChanged(token,ProfileCotroller.uid);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
