package com.alfheim.aflheim_community.service.user;

import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.model.user.UserConfirmation;
import com.alfheim.aflheim_community.repository.UserConfirmationRepo;
import com.alfheim.aflheim_community.service.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountConfirmationServiceImpl implements AccountConfirmationService {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserConfirmationRepo userConfirmationRepo;

    @Override
    public void sendConfirmationEmail(String email) {

        // Generate the conformation code
        String confirmationCode = generateConformationCode();

        // Send mail
        mailService.sendConfirmationEmail(email, confirmationCode);

        // Save userEmail & code to the records
        addUserConfirmationRecord(email, confirmationCode);
    }

    @Override
    public String getEmailToConfirm(String code) {
        return userConfirmationRepo.findByCode(code).get().getUserEmail();
    }

    private void addUserConfirmationRecord(String email, String code) {
        UserConfirmation userConfirmationDetails = UserConfirmation.builder()
                .userEmail(email)
                .code(code)
                .build();

        userConfirmationRepo.save(userConfirmationDetails);
    }
    private String generateConformationCode() {
        return UUID.randomUUID().toString();
    }

}
