package com.springboot.springsecurityclient.repository;

import com.springboot.springsecurityclient.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    PasswordResetToken findByToken(String token);
}
