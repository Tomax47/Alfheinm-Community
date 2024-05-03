package com.alfheim.aflheim_community.service.user;

import com.alfheim.aflheim_community.model.user.RecordState;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.model.user.UserConfirmation;
import com.alfheim.aflheim_community.model.user.UserPasswordReset;
import com.alfheim.aflheim_community.repository.UserPasswordResetRepo;
import com.alfheim.aflheim_community.repository.UserRepo;
import com.alfheim.aflheim_community.service.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
    public int sendPasswordResetEmail(String email) {
        System.out.println("SENDING PASSWORD RESET EMAIL TO: " + email + " | @COMPONENT");
        Optional<User> user = userRepo.findByEmail(email);

        if (user.isPresent()) {
            System.out.println("\n\n1212222222222222222\n\n");
            Optional<UserPasswordReset> userPasswordResetRecord = userPasswordResetRepo.findByUserEmail(email);

            if (userPasswordResetRecord.isPresent() &&
            userPasswordResetRecord.get().getState().toString().equals("ACTIVE")) {
                // There's an active password reset record. Refusing the request.
                System.out.println("USER ALREADY HAS AN ACTIVE RESET REQUEST. REFUSING NEW REQUEST.");
                return 2;
            }

            // No record, or active record found. Generate new reset code
            String resetCode = generatePasswordResetCode();

            // Send mail
            Map<String, String> tokenDetails = new HashMap<>();
            tokenDetails.put("pass_reset_code", resetCode);

            System.out.println("SENDING MAIL PASSWORD RESET CODE -> " + tokenDetails.get("pass_reset_code") + "\n\n");
            mailService.configureMailSettings("passwordReset");
            mailService.sendEmail(email, "passwordReset", tokenDetails);

            // Save userEmail & code to the records
            addUserPasswordResetRecord(email, resetCode);
            return 1;
        }

        // User ain't exist
        System.out.println("USER AIN'T EXIST! ABORTING SENDING RESET PASSWORD EMAIL...");
        return 0;
    }

    @Override
    public int resetUserPassword(String code, String newPassword) {

        // RESET USER'S PASSWORD
        Optional<UserPasswordReset> userPasswordResetRecord = userPasswordResetRepo.findByResetCode(code);

        if (userPasswordResetRecord.isPresent()) {
            System.out.println("RECORD EXIST. RESETTING PASSWORD...");
            if (userPasswordResetRecord.get().getState().toString().equals("ACTIVE")) {
                System.out.println("RECORD IS ACTIVE. RESETTING PASSWORD...");
                User user = userRepo.findByEmail(userPasswordResetRecord.get().getUserEmail()).get();

                if (user != null) {
                    System.out.println("USER HAS BEEN FOUND. RESETTING PASSWORD...");
                    // Updating user's password
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepo.save(user);

                    // Expiring the password reset record
                    UserPasswordReset userPasswordReset = userPasswordResetRecord.get();
                    userPasswordReset.setState(RecordState.EXPIRED);
                    userPasswordResetRepo.save(userPasswordReset);

                    return 1;
                } else {
                    // User can't be found, something went wrong
                    System.out.println("USER COULDN'T BE FOUND. ABORTING PASSWORD RESET...");
                    return 3;
                }

            } else {
                // Record exist but is expired
                System.out.println("RECORD EXIST BUT IS EXPIRED. ABORTING PASSWORD RESET...");
                return 2;
            }

        }

        // No request has been found
        System.out.println("RECORD HAS NOT BEEN FOUND. ABORTING PASSWORD RESET...");
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
                .state(RecordState.ACTIVE)
                .build();

        userPasswordResetRepo.save(userPasswordReset);
    }
    private String generatePasswordResetCode() {
        return UUID.randomUUID().toString();
    }
}
