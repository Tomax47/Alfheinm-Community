package com.alfheim.aflheim_community.repository;

import com.alfheim.aflheim_community.model.newsLetter.Newsletter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsletterRepo extends JpaRepository<Newsletter, Long> {

    Optional<Newsletter> findByPhoneNumber(String phoneNumber);
}
