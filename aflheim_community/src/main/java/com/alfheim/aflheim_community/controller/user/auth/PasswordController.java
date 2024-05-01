package com.alfheim.aflheim_community.controller.user.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PasswordController {

    @GetMapping("/login/password/recover")
    public String getForgotPasswordPage(Model model) {

        model.addAttribute("isAuthenticated", false);
        return "user/auth/forgot_password_page";
    }

    @GetMapping("/password/reset/{reset-verification-code}")
    public String getResetPasswordPage(@PathVariable("reset-verification-token") String verificationToken, Model model) {

        model.addAttribute("isAuthenticated", false);
        // TODO : HANDLE THE LOGIC HERE
        return "user/auth/reset_password_page";
    }
}
