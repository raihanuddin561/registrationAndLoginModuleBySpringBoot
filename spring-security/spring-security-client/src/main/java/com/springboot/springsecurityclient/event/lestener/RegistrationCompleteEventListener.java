package com.springboot.springsecurityclient.event.lestener;

import com.springboot.springsecurityclient.entity.User;
import com.springboot.springsecurityclient.event.RegistrationCompleteEvent;
import com.springboot.springsecurityclient.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    @Autowired
    private UserService userService;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVarificationToken(user,token);
        String url = event.getApplicationUrl()+"/verifyRegistration?token="+token;
        log.info("Click the link to verify your account:{}",url);
    }
}
