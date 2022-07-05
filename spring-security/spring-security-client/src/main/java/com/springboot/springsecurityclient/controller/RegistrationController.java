package com.springboot.springsecurityclient.controller;

import com.springboot.springsecurityclient.entity.User;
import com.springboot.springsecurityclient.event.RegistrationCompleteEvent;
import com.springboot.springsecurityclient.model.UserModel;
import com.springboot.springsecurityclient.model.UserResponse;
import com.springboot.springsecurityclient.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RegistrationController {
    @Autowired
    private UserService userService;
   @Autowired
   private ApplicationEventPublisher publisher;
    @PostMapping("/register")
    public String registration(@RequestBody UserModel userModel, final HttpServletRequest request){

        User user =userService.save(userModel);
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user,userResponse);
        publisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(request)
        ));
        return "success";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token){
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")){

            return "User Verifies Successfully";
        }
        return "Bad user";
    }
    private String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
