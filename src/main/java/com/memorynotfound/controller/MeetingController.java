package com.memorynotfound.controller;

import com.memorynotfound.model.Meeting;
import com.memorynotfound.model.User;
import com.memorynotfound.service.MeetingService;
import com.memorynotfound.service.Mservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meeting")
public class MeetingController {

    private Mservice mservice =  new MeetingService();
    private final Logger LOG = LoggerFactory.getLogger(UserController.class);


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Meeting> createMeeting(@RequestBody Meeting meeting1){
        meeting1.setAdmin(ProfileController.uid);
        Meeting meeting = mservice.createMeeting(meeting1);
        if (meeting.getId() == 0){
            return new ResponseEntity<Meeting>(HttpStatus.CONFLICT);
        }
        return  new ResponseEntity<Meeting>(meeting,HttpStatus.CREATED);
    }

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public ResponseEntity<List<Meeting>> getAll(){
        List<Meeting> meeting= mservice.getAllMeetings();
        if (meeting == null || meeting.isEmpty()){
            return new ResponseEntity<List<Meeting>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Meeting>>(meeting,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Meeting>> getUsersMeetings(){
        List<Meeting> meeting= mservice.getUserMeetings(ProfileController.uid);
        if (meeting == null || meeting.isEmpty()){
            return new ResponseEntity<List<Meeting>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Meeting>>(meeting,HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<Meeting> getMeetById(@PathVariable("id") int id){
        Meeting meeting = mservice.getMettById(ProfileController.uid,id);
        if (meeting==null){
            return new ResponseEntity<Meeting>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<Meeting>(meeting,HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/add",method = RequestMethod.POST)
    public ResponseEntity<Void>addUser(@PathVariable("id") int id,@RequestBody User user){
        mservice.addUser(id,user.getMail());
            return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<Void>deleteUser(@PathVariable("id") int id,@RequestBody User user){
        LOG.info("meeting id:"+String.valueOf(id)+" user mail:"+user.getMail());
        if (mservice.deleteUser(id,user.getMail())){
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/updateName",method = RequestMethod.PUT)
    public ResponseEntity<Meeting>updateName(@RequestBody Meeting meeting){
        if (meeting!=null) {
            mservice.updateName(meeting);
        }else{
            return new ResponseEntity<Meeting>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<Meeting>(meeting,HttpStatus.OK);
    }

    @RequestMapping(value = "/updatePlace",method = RequestMethod.PUT)
    public ResponseEntity<Void>updatePoint(@RequestBody Meeting meeting){
        if (meeting!=null){
            mservice.updatePlace(meeting);
        }else{
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
