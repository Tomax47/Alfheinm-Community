package com.alfheim.aflheim_community.service.user;

import com.alfheim.aflheim_community.exception.password.PasswordActiveRecordExistException;
import com.alfheim.aflheim_community.exception.password.PasswordResetRequestExpiredException;
import com.alfheim.aflheim_community.exception.password.PasswordResetRequestNotFoundException;
import com.alfheim.aflheim_community.exception.server.InternalServerErrorException;
import com.alfheim.aflheim_community.exception.user.UserNotFoundException;
import com.alfheim.aflheim_community.model.user.RecordState;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.model.user.UserConfirmation;
import com.alfheim.aflheim_community.model.user.UserPasswordReset;
import com.alfheim.aflheim_community.repository.UserPasswordResetRepo;
import com.alfheim.aflheim_community.repository.UserRepo;
import com.alfheim.aflheim_community.service.mail.MailService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
                // There's an active password reset record. Refusing the request. 409
                log.error("Already exist an active password reset record (PasswordResetServiceImpl.sendPasswordResetEmail)");
                throw new PasswordActiveRecordExistException("An active password reset record already exist. Try again after 5 minutes");
            }

            // No record, or active record found. Generate new reset code
            String resetCode = generatePasswordResetCode();

            try {
                // Send mail
                Map<String, String> tokenDetails = new HashMap<>();
                tokenDetails.put("pass_reset_code", resetCode);

                mailService.configureMailSettings("passwordReset");
                mailService.sendEmail(email, "passwordReset", tokenDetails);

                // Save userEmail & code to the records
                addUserPasswordResetRecord(email, resetCode);
                return 200;
            } catch (Exception e) {
                // 500
                log.error("Internal error (PasswordResetServiceImpl.sendPasswordResetEmail). Error : " + e);
                throw new InternalServerErrorException("Something went wrong");
            }
        }

        // User ain't exist
        log.error("User not found (PasswordResetServiceImpl.sendPasswordResetEmail)");
        throw new UserNotFoundException("User not found");
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

                    return 200;
                } else {
                    // User can't be found, something went wrong
                    log.error("User not found (PasswordResetServiceImpl.resetUserPassword)");
                    throw new UserNotFoundException("User not found");
                }

            } else {
                // Record exist but is expired
                log.error("Expired password reset request (PasswordResetServiceImpl.resetUserPassword)");
                throw new PasswordResetRequestExpiredException("Password reset request has expired");
            }

        }

        // No request has been found
        log.error("Password reset request not found (PasswordResetServiceImpl.resetUserPassword)");
        throw new PasswordResetRequestNotFoundException("Password reset request not found");
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

            try {
                // Success
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepo.save(user);
                return 200;

            } catch (Exception e) {
                // Something went wrong
                log.error("Something went wrong (PasswordResetServiceImpl.authResetUserPassword). Error : " + e);
                throw new InternalServerErrorException("Something went wrong");
            }
        }

        // User not found
        throw new UserNotFoundException("user not found");
    }

    @Override
    public int adminUserResetPassword(User user, String newPassword, String adminUsername) {

        try {
            // Setting the new password
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);

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
