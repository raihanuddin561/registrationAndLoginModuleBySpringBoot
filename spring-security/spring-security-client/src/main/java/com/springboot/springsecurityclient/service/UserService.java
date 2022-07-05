package com.springboot.springsecurityclient.service;

import com.springboot.springsecurityclient.entity.User;
import com.springboot.springsecurityclient.model.UserModel;

public interface UserService {
    User save(UserModel user);

    void saveVarificationToken(User user, String token);

    String validateVerificationToken(String token);
}
