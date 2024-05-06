package com.alfheim.aflheim_community.service.user;

public interface PasswordResetService {

    int sendPasswordResetEmail(String email);

    int resetUserPassword(String code, String newPassword);

    String verifyResetToken(String verificationToken);

    int authResetUserPassword(String username, String oldPassword, String newPassword);
}
