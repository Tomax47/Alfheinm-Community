package com.alfheim.aflheim_community.service.newsletter;

import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.exception.newsletter.NewsletterAlreadySubscribedException;
import com.alfheim.aflheim_community.exception.server.InternalServerErrorException;
import com.alfheim.aflheim_community.exception.user.UserNotFoundException;
import com.alfheim.aflheim_community.model.newsLetter.Newsletter;
import com.alfheim.aflheim_community.repository.NewsletterRepo;
import com.alfheim.aflheim_community.service.sms.SmsService;
import com.alfheim.aflheim_community.service.user.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class NewsletterServiceImpl implements NewsletterService {

    @Autowired
    private NewsletterRepo newsletterRepo;

    @Autowired
    private SmsService smsService;

    @Autowired
    private ProfileService profileService;

    @Override
    public void subscribe(String phoneNumber, String userEmail) {

        UserDto user = profileService.getProfileDetails(userEmail);

        if (user != null) {

            if (findByPhoneNumber(phoneNumber).isPresent()) {
                // Already exist
                log.error("Already subscribed exception (NewsletterServiceImpl.subscribe)");
                throw new NewsletterAlreadySubscribedException("You are already subscribed to the newsletter");
            }

            try {

                // Not subscribed
                Newsletter newsletterRecord = Newsletter.builder()
                        .phoneNumber(phoneNumber)
                        .userEmail(userEmail)
                        .build();

                Newsletter newsletterCreatedRecord = newsletterRepo.save(newsletterRecord);

                if (newsletterCreatedRecord != null) {
                    // Success
                    smsService.sendWelcomeMsg(newsletterCreatedRecord.getPhoneNumber());
                    return;
                }

                log.error("No record saved (NewsletterServiceImpl.subscribe)");
                throw new InternalServerErrorException("Something went wrong");

            } catch (Exception e) {
                // Something went wrong
                log.error("Something went wrong (NewsletterServiceImpl.subscribe). Error : " + e);
                throw new InternalServerErrorException("Something went wrong");
            }
        }

        // User not found
        log.error("User not found (NewsletterServiceImpl.subscribe)");
        throw new UserNotFoundException("User not found");
    }

    @Override
    public Optional<Newsletter> findByPhoneNumber(String phoneNumber) {
        return newsletterRepo.findByPhoneNumber(phoneNumber);
    }
}
