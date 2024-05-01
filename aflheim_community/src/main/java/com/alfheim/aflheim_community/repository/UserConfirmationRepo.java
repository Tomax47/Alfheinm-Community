package com.alfheim.aflheim_community.repository;

import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.model.user.UserConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserConfirmationRepo extends JpaRepository<UserConfirmation, Long> {
    Optional<UserConfirmation> findByCode(String code);

}
