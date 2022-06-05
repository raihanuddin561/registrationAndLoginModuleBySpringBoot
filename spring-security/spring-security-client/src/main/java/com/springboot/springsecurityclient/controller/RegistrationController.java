package com.springboot.springsecurityclient.controller;

import com.springboot.springsecurityclient.entity.User;
import com.springboot.springsecurityclient.event.RegistrationCompleteEvent;
import com.springboot.springsecurityclient.model.UserModel;
import com.springboot.springsecurityclient.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    @Autowired
    private UserService userService;
   @Autowired
   private ApplicationEventPublisher publisher;
    @PostMapping("/register")
    public User registration(@RequestBody UserModel userModel){

        User returnedValue =userService.save(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(
                returnedValue,
                "url"
        ));
        return returnedValue;
    }
}
