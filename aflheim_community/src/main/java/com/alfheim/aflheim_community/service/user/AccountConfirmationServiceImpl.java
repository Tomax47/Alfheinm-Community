package com.alfheim.aflheim_community.service.user;

import com.alfheim.aflheim_community.exception.profile.ConfirmationRecordNotFoundException;
import com.alfheim.aflheim_community.model.user.RecordState;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.model.user.UserConfirmation;
import com.alfheim.aflheim_community.repository.UserConfirmationRepo;
import com.alfheim.aflheim_community.service.mail.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
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
        Map<String, String> tokenDetails = new HashMap<>();
        tokenDetails.put("confirm_code", confirmationCode);
        System.out.println("111111111111111111111111111111111111");
        mailService.configureMailSettings("confirmation");
        System.out.println("22222222222222222222222222222222222");
        mailService.sendEmail(email, "confirmation", tokenDetails);

        System.out.println("CONFIRMATION CODE GENERATED -> "+tokenDetails.get("confirm_code")+"\n\n");

        // Save userEmail & code to the records
        addUserConfirmationRecord(email, confirmationCode);
    }

    @Override
    public void expireRecord(UserConfirmation confirmationRecord) {

        confirmationRecord.setState(RecordState.EXPIRED);
        userConfirmationRepo.save(confirmationRecord);
    }

    @Override
    public void deleteRecord(UserConfirmation userConfirmationRecord) {
        userConfirmationRepo.delete(userConfirmationRecord);
    }

    @Override
    public Optional<UserConfirmation> getEmailConfirmationRecord(String code) {

        Optional<UserConfirmation> record = userConfirmationRepo.findByCode(code);

        if (!record.isPresent()) {
            log.error("Active record has not been found (AccountConfirmationServiceImpl.deleteRecord)");
            throw new ConfirmationRecordNotFoundException("No active record been found");
        }

        return record;
    }

    private void addUserConfirmationRecord(String email, String code) {
        UserConfirmation userConfirmationDetails = UserConfirmation.builder()
                .userEmail(email)
                .code(code)
                .state(RecordState.ACTIVE)
                .build();

        userConfirmationRepo.save(userConfirmationDetails);
    }
    private String generateConformationCode() {
        return UUID.randomUUID().toString();
    }

}
