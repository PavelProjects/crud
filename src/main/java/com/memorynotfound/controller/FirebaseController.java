package com.memorynotfound.controller;


import com.memorynotfound.model.Message;
import com.memorynotfound.service.FbService;
import com.memorynotfound.service.FirebaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messaging")
public class FirebaseController {
    private FbService fbService =new FirebaseService();

    @RequestMapping(value = "/send",method = RequestMethod.POST)
    public ResponseEntity<Void> sendMessage(@RequestBody Message message) throws Exception {
        if (message== null){
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        fbService.sendMessage(message);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    @RequestMapping(value = "/token",method = RequestMethod.POST)
    public ResponseEntity<Void> updateToken(@RequestBody String token){
        fbService.TokenChanged(token,ProfileCotroller.uid);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
