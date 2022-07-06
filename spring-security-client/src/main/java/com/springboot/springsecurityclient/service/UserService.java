package com.springboot.springsecurityclient.service;

import com.springboot.springsecurityclient.entity.User;
import com.springboot.springsecurityclient.entity.VerificationToken;
import com.springboot.springsecurityclient.model.UserModel;

import java.util.Optional;

public interface UserService {
    User save(UserModel user);

    void saveVarificationToken(User user, String token);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);

    User findUserByEmail(String email);

    void createPassowordResetTokenForUser(User user, String token);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);

    boolean checkIfOldPasswordIsValid(User user, String oldPassword);
}
