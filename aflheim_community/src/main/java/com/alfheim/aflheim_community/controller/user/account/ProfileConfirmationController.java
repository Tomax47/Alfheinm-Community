package com.alfheim.aflheim_community.controller.user.account;

import com.alfheim.aflheim_community.service.user.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileConfirmationController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/confirm/{code}")
    public String confirmUser(@PathVariable("code") String code) {

        boolean confirmed = profileService.confirmAccount(code);

        return "redirect:/login";
    }

    @GetMapping("/confirm/process/{email}")
    public String getConfirmationProcessPage(@PathVariable("email") String email,
                                             Model model) {
        // This is the mapping for after registration confirmation
        model.addAttribute("email", email);
        return "users/auth/email_confirmation_processing_page";
    }

    @PostMapping("/confirm/process/resend")
    public ResponseEntity<Object> resendConfirmationEmail(@RequestParam("email") String email) {
        // This is the mapping for after registration confirmation

        profileService.resendConfirmationEmail(email);

        return ResponseEntity.ok().body("OK");
    }

    @GetMapping("/account/confirm/resend")
    public String getAccountConfirmationProcessPage() {

        // This is the mapping for personal request to confirm email
        return "users/auth/email_confirmation_resend_page";
    }

    @PostMapping("/account/confirm/resend")
    public ResponseEntity<Object> resendAccountConfirmationEmail(@RequestParam("email") String email) {

        int results = profileService.resendConfirmationEmail(email);

        return ResponseEntity.ok().body("OK");
    }
}
