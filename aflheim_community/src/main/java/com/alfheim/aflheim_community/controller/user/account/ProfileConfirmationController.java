package com.alfheim.aflheim_community.controller.user.account;

import com.alfheim.aflheim_community.service.user.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
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

        if (!confirmed) {
            return "redirect:/register?error";
        }

        return "redirect:/login";
    }

    @GetMapping("/confirm/process/{email}")
    public String getConfirmationProcessPage(@PathVariable("email") String email,
                                             Model model) {
        // This is the mapping for after registration confirmation
        model.addAttribute("email", email);
        return "users/auth/email_confirmation_processing_page";
    }

    @PostMapping("/confirm/process/resend/{email}")
    public String resendConfirmationEmail(@PathVariable("email") String email) {
        // This is the mapping for after registration confirmation

        int results = profileService.resendConfirmationEmail(email);

        if (results == 0) {
            // User ain't found
            return "redirect:/register";
        } else if (results == 1) {
            // Email has been resent
            return "redirect:/";
        } else {
            // User is already confirmed
            return "redirect:/login";
        }
    }

    @GetMapping("/account/confirm/resend")
    public String getAccountConfirmationProcessPage() {

        // This is the mapping for personal request to confirm email
        return "users/auth/email_confirmation_resend_page";
    }

    @PostMapping("/account/confirm/resend")
    public String resendAccountConfirmationEmail(@RequestParam("email") String email) {

        System.out.println("\n\n1111111111111111111111111\n\n"+email);
        int results = profileService.resendConfirmationEmail(email);

        if (results == 0) {
            // User ain't found
            return "redirect:/register";
        } else if (results == 1) {
            // Email has been resent
            return "redirect:/";
        } else {
            // User is already confirmed
            return "redirect:/login";
        }
    }
}
