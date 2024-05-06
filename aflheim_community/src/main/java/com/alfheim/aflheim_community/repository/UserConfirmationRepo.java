package com.alfheim.aflheim_community.repository;

import com.alfheim.aflheim_community.model.user.RecordState;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.model.user.UserConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserConfirmationRepo extends JpaRepository<UserConfirmation, Long> {
    @Query("SELECT uc FROM UserConfirmation uc WHERE uc.code = :code ORDER BY uc.createdAt DESC") // Getting the recent record
    Optional<UserConfirmation> findByCode(@Param("code") String code);

    List<UserConfirmation> findByState(RecordState recordState);
}
