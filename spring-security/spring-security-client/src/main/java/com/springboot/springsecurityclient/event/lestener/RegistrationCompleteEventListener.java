package com.springboot.springsecurityclient.event.lestener;

import com.springboot.springsecurityclient.entity.User;
import com.springboot.springsecurityclient.event.RegistrationCompleteEvent;
import org.springframework.context.ApplicationListener;

import java.util.UUID;

public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();

    }
}
