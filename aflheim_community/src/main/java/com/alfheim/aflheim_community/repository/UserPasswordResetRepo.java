package com.alfheim.aflheim_community.repository;

import com.alfheim.aflheim_community.model.user.UserPasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPasswordResetRepo extends JpaRepository<UserPasswordReset, Long> {

    Optional<UserPasswordReset> findByResetCode(String resetCode);
}
