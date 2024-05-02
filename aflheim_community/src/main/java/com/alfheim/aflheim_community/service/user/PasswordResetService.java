package com.alfheim.aflheim_community.service.user;

public interface PasswordResetService {

    void sendConfirmationEmail(String email);

    int resetUserPassword(String code, String newPassword);

    String verifyResetToken(String verificationToken);
}
