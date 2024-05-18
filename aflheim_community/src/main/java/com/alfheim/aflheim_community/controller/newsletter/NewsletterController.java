package com.alfheim.aflheim_community.controller.newsletter;

import com.alfheim.aflheim_community.model.newsLetter.Newsletter;
import com.alfheim.aflheim_community.security.details.UserDetailsImpl;
import com.alfheim.aflheim_community.service.newsletter.NewsletterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/newsletter")
public class NewsletterController {

    @Autowired
    private NewsletterService newsletterService;

    @PostMapping("/subscribe")
    @ResponseBody
    public ResponseEntity<Object> subscribeToNewsletter(@RequestParam("phoneNumber") String phoneNumber,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        newsletterService.subscribe(phoneNumber, userDetails.getUserEmail());

        return ResponseEntity.status(HttpStatus.OK).body(200);
    }
}
