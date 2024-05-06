package com.alfheim.aflheim_community.repository;

import com.alfheim.aflheim_community.model.user.RecordState;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.model.user.UserPasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserPasswordResetRepo extends JpaRepository<UserPasswordReset, Long> {
    @Query("SELECT upr FROM UserPasswordReset upr WHERE upr.resetCode = :code ORDER BY upr.createdAt DESC") // Getting the recent record
    Optional<UserPasswordReset> findByResetCode(@Param("code") String resetCode);

    @Query("SELECT upr FROM UserPasswordReset upr WHERE upr.userEmail = :email AND upr.state = 0 ORDER BY upr.createdAt DESC")
    Optional<UserPasswordReset> findByUserEmail(@Param("email") String email);
    List<UserPasswordReset> findByState(RecordState RecordState);
}
