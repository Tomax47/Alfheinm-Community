package com.alfheim.aflheim_community.service.user;

import com.alfheim.aflheim_community.model.user.User;

public interface PasswordResetService {

    int sendPasswordResetEmail(String email);

    int resetUserPassword(String code, String newPassword);

    String verifyResetToken(String verificationToken);

    int authResetUserPassword(String username, String oldPassword, String newPassword);

    int adminUserResetPassword(User user, String newPassword, String adminUsername);
}
