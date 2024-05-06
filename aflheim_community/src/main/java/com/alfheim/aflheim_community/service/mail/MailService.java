package com.alfheim.aflheim_community.service.mail;

import java.util.Map;

public interface MailService {

    void sendEmail(String email, String emailType, Map<String, String> model);
    public void configureMailSettings(String emailType);
}
