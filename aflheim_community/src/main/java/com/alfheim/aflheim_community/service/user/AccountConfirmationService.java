package com.alfheim.aflheim_community.service.user;

import com.alfheim.aflheim_community.model.user.UserConfirmation;

import java.util.Optional;

public interface AccountConfirmationService {

    void sendConfirmationEmail(String email);
    void expireRecord(UserConfirmation confirmationRecord);
    void deleteRecord(UserConfirmation userConfirmationRecord);
    Optional<UserConfirmation> getEmailConfirmationRecord(String code);
}
