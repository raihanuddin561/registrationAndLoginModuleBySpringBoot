package com.springboot.springsecurityclient.service.impl;

import com.springboot.springsecurityclient.entity.User;
import com.springboot.springsecurityclient.model.UserModel;
import com.springboot.springsecurityclient.repository.UserRepository;
import com.springboot.springsecurityclient.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public User save(UserModel userModel) {
        User user = new User();
        BeanUtils.copyProperties(userModel,user);
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        return userRepository.save(user);
    }
}
