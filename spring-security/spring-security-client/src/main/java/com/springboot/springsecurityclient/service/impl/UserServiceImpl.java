package com.springboot.springsecurityclient.service.impl;

import com.springboot.springsecurityclient.entity.User;
import com.springboot.springsecurityclient.entity.VerificationToken;
import com.springboot.springsecurityclient.model.UserModel;
import com.springboot.springsecurityclient.repository.UserRepository;
import com.springboot.springsecurityclient.repository.VerificationTokenRepository;
import com.springboot.springsecurityclient.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Override
    public User save(UserModel userModel) {
        User user = new User();
        BeanUtils.copyProperties(userModel,user);
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void saveVarificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(user,token);
        verificationTokenRepository.save(verificationToken);

    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken==null){
            return "invalid";
        }
        User user =verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if((verificationToken.getExpirationTime().getTime()-calendar.getTime().getTime())<=0){
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }
}
