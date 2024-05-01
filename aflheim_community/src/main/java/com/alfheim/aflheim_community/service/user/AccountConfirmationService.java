package com.alfheim.aflheim_community.service.user;

public interface AccountConfirmationService {

    void sendConfirmationEmail(String email);

    String getEmailToConfirm(String code);
}
