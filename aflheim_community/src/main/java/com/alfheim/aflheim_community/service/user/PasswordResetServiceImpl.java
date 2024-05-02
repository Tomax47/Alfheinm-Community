package com.alfheim.aflheim_community.service.user;

import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.model.user.UserConfirmation;
import com.alfheim.aflheim_community.model.user.UserPasswordReset;
import com.alfheim.aflheim_community.repository.UserPasswordResetRepo;
import com.alfheim.aflheim_community.repository.UserRepo;
import com.alfheim.aflheim_community.service.mail.MailService;
import com.alfheim.aflheim_community.service.mail.MailServiceImpl;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserPasswordResetRepo userPasswordResetRepo;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void sendConfirmationEmail(String email) {
        User user = userRepo.findByEmail(email).get();

        if (user != null) {
            String resetCode = generatePasswordResetCode();

            // Send mail
            Map<String, String> tokenDetails = new HashMap<>();
            tokenDetails.put("pass_reset_code", resetCode);

            System.out.println("SENDING MAIL PASSWORD RESET CODE -> " + tokenDetails.get("pass_reset_code") + "\n\n");
            mailService.configureMailSettings("passwordReset");
            mailService.sendEmail(email, "passwordReset", tokenDetails);

            // Save userEmail & code to the records
            addUserPasswordResetRecord(email, resetCode);
        }
    }

    @Override
    public int resetUserPassword(String code, String newPassword) {

        // RESET USER'S PASSWORD
        User user = userRepo.findByEmail(userPasswordResetRepo.findByResetCode(code).get().getUserEmail()).get();

        if (user != null) {
            // Update user's password
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);

            return 1;
        }

        return 0;
    }

    @Override
    public String verifyResetToken(String verificationToken) {
        return userPasswordResetRepo.findByResetCode(verificationToken).get().getUserEmail();
    }

    private void addUserPasswordResetRecord(String email, String code) {

        // SAVING THE PASSWD RESET REQUEST RECORD
        UserPasswordReset userPasswordReset = UserPasswordReset.builder()
                .userEmail(email)
                .resetCode(code)
                .build();

        userPasswordResetRepo.save(userPasswordReset);
    }
    private String generatePasswordResetCode() {
        return UUID.randomUUID().toString();
    }
}
