package com.alfheim.aflheim_community.service.newsletter;

import com.alfheim.aflheim_community.model.newsLetter.Newsletter;

import java.util.Optional;

public interface NewsletterService {

    void subscribe(String phoneNumber, String userEmail);

    Optional<Newsletter> findByPhoneNumber(String phoneNumber);
}
