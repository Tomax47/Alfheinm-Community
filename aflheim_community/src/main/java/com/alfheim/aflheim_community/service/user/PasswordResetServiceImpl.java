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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public int sendPasswordResetEmail(String email) {
        Optional<User> user = userRepo.findByEmail(email);

        if (user.isPresent()) {
            Optional<UserPasswordReset> userPasswordResetRecord = userPasswordResetRepo.findByUserEmail(email);

            if (userPasswordResetRecord.isPresent() &&
            userPasswordResetRecord.get().getState().toString().equals("ACTIVE")) {
                // There's an active password reset record. Refusing the request.
                return 2;
            }

            // No record, or active record found. Generate new reset code
            String resetCode = generatePasswordResetCode();

            // Send mail
            Map<String, String> tokenDetails = new HashMap<>();
            tokenDetails.put("pass_reset_code", resetCode);

            mailService.configureMailSettings("passwordReset");
            mailService.sendEmail(email, "passwordReset", tokenDetails);

            // Save userEmail & code to the records
            addUserPasswordResetRecord(email, resetCode);
            return 1;
        }

        // User ain't exist
        return 0;
    }

    @Override
    public int resetUserPassword(String code, String newPassword) {

        // RESET USER'S PASSWORD
        Optional<UserPasswordReset> userPasswordResetRecord = userPasswordResetRepo.findByResetCode(code);

        if (userPasswordResetRecord.isPresent()) {
            if (userPasswordResetRecord.get().getState().toString().equals("ACTIVE")) {
                User user = userRepo.findByEmail(userPasswordResetRecord.get().getUserEmail()).get();

                if (user != null) {
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
                    return 3;
                }

            } else {
                // Record exist but is expired
                return 2;
            }

        }

        // No request has been found
        return 0;
    }

    @Override
    public String verifyResetToken(String verificationToken) {
        return userPasswordResetRepo.findByResetCode(verificationToken).get().getUserEmail();
    }

    @Override
    public int authResetUserPassword(String username, String oldPassword, String newPassword) {

        Optional<User> userOptional = userRepo.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // TODO : FIX THE ENCODED STRING AIN'T LOOK LIKE BCRYPT ISSUE
            if (passwordEncoder.matches(user.getPassword(), oldPassword)) {

                try {
                    // Success
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepo.save(user);
                    return 1;

                } catch (Exception e) {
                    // Something went wrong
                    return 3;
                }
            }

            // Incorrect old password
            return 2;
        }

        // User not found
        return 0;
    }

    @Override
    public int adminUserResetPassword(User user, String newPassword, String adminUsername) {

        try {
            // Setting the new password
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);

            // TODO: SEND AN EMAIL TO THE USER INFORMING OF THE PASSWORD CHANGE!

            return 200;
        } catch (Exception e) {
            return 500;
        }
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
