package com.alfheim.aflheim_community.controller.user.auth;

import com.alfheim.aflheim_community.service.user.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordController {

    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping("/login/password/recover")
    public String getForgotPasswordPage(Model model) {

        model.addAttribute("isAuthenticated", false);
        return "user/auth/forgot_password_page";
    }

    @PostMapping("/login/password/recover")
    public String sendResetPasswordEmail(@RequestParam("email") String email) {

        passwordResetService.sendConfirmationEmail(email);
        return"redirect:/login";
    }

    @GetMapping("/password/reset/{reset-verification-code}")
    public String getResetPasswordPage(@PathVariable("reset-verification-code") String verificationToken, Model model) {

        model.addAttribute("isAuthenticated", false);

        String userEmail = passwordResetService.verifyResetToken(verificationToken);

        if (userEmail.isBlank()) {
            // Unauthorized or expired token
            return "redirect:/login";
        } else {
            // Authorized and correct token
            model.addAttribute("email", userEmail);
            model.addAttribute("resetToken", verificationToken);
            return "user/auth/reset_password_page";
        }
    }

    @PostMapping("/password/reset/{reset-verification-code}")
    public String resetUserPassowrd(@PathVariable("reset-verification-code") String verificationToken,
                                    @RequestParam("password") String password) {

        System.out.println("PASSWORD : " + password);
        int results = passwordResetService.resetUserPassword(verificationToken, password);

        if (results == 1) {
            return "redirect:/login";
        } else {
            return "user/auth/reset_password_page?error";
        }
    }
}
