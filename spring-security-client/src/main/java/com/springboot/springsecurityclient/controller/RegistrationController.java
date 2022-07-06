package com.springboot.springsecurityclient.controller;

import com.springboot.springsecurityclient.entity.User;
import com.springboot.springsecurityclient.entity.VerificationToken;
import com.springboot.springsecurityclient.event.RegistrationCompleteEvent;
import com.springboot.springsecurityclient.model.PasswordModel;
import com.springboot.springsecurityclient.model.UserModel;
import com.springboot.springsecurityclient.model.UserResponse;
import com.springboot.springsecurityclient.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class RegistrationController {
    @Autowired
    private UserService userService;
   @Autowired
   private ApplicationEventPublisher publisher;
   @Autowired
   private PasswordEncoder passwordEncoder;
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
    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken,HttpServletRequest request){
        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
        User user = verificationToken.getUser();
        resendVerificationTokenMail(user,applicationUrl(request),verificationToken);
        return "Verification link sent";
    }

    @PostMapping("resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel,HttpServletRequest request){
        User user = userService.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if(user!=null){
            String token = UUID.randomUUID().toString();
            userService.createPassowordResetTokenForUser(user,token);
            url = passwordResetTokenl(user,applicationUrl(request),token);
        }
        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel){
        String result = userService.validatePasswordResetToken(token);
        if(!result.equalsIgnoreCase("valid")){
            return "Invalid Token";
        }
        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if(user.isPresent()){
            userService.changePassword(user.get(),passwordModel.getNewPassword());
            return "Password reset successfully";
        }else return "invalid token";
    }
    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel){
        User user = userService.findUserByEmail(passwordModel.getEmail());
        if(user == null){
            return "invalid user";
        }
        if(!userService.checkIfOldPasswordIsValid(user,passwordModel.getOldPassword())){
            return "invalid old password";
        }
        userService.changePassword(user,passwordModel.getNewPassword());
        return "Password Changed Successfully";
    }



    private String passwordResetTokenl(User user, String applicationUrl, String token) {
        String url = applicationUrl+"/savePassword?token="+token;
        log.info("Click the link to verify your account:{}",url);
        return url;
    }

    private void resendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {

        String url = applicationUrl+"/verifyRegistration?token="+verificationToken.getToken();
        log.info("Click the link to verify your account:{}",url);
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
