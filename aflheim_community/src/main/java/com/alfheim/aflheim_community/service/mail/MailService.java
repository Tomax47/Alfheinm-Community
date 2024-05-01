package com.alfheim.aflheim_community.service.mail;

public interface MailService {

    void sendConfirmationEmail(String email, String code);
}
