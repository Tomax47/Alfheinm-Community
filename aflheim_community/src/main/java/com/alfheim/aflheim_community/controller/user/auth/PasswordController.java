package com.alfheim.aflheim_community.controller.user.auth;

import com.alfheim.aflheim_community.dto.user.AuthPasswordResetForm;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.security.details.UserDetailsImpl;
import com.alfheim.aflheim_community.service.user.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import javax.validation.Valid;

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

        System.out.println("SENDING PASSWORD RESET EMAIL TO: " + email + " | @CONTROLLER");
        int results = passwordResetService.sendPasswordResetEmail(email);

        if (results == 0) {
            // USER AIN'T EXIST, CREATE A NEW ACCOUNT
            return "redirect:/register";
        } else if (results == 2) {
            // USER ALREADY HAS AN ACTIVE RESET REQUEST "CHECK YOUR EMAIL OR TRY AGAIN AFTER 5 MINUTES"
            return "redirect:/login?error=already_requested";
        }

        // ELSE, SUCCESSFULLY SENT RESET EMAIL
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

        // TODO: HANDLE THE ERRORS IN A BETTER WAY
        if (results == 0) {
            // NO REQUEST HAS BEEN FOUND
            return "redirect:/login/password/recover";
        } else if (results == 2) {
            // EXPIRED REQUEST
            return "redirect:/login/password/recover";
        } else if (results == 3) {
            // USER CAN'T BE FOUND
            return "redirect:/register";
        }
        // PASSWORD RESET SUCCESSFULLY
        return "redirect:/login";
    }

    @GetMapping("/profile/password/reset")
    public String getAuthPasswordResetPage(Model model,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        AuthPasswordResetForm passwordResetForm = new AuthPasswordResetForm();
        model.addAttribute("passwordResetForm", passwordResetForm);
        model.addAttribute("username", userDetails.getUsername());

        return "/user/profile/authenticated_password_reset_page";
    }

    @PostMapping("/profile/password/reset")
    public String doAuthPasswordReset(@Valid @ModelAttribute("passwordResetForm") AuthPasswordResetForm authPasswordResetForm,
                                      BindingResult result,
                                      Model model,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String username = userDetails.getUsername();
        AuthPasswordResetForm passwordResetForm = new AuthPasswordResetForm();

        if (result.hasErrors()) {
            System.out.println("CONTROLLER CAUGHT FILED ERRORS! PAS. RESET");
            System.out.println(result.getAllErrors());
            model.addAttribute("passwordResetForm", passwordResetForm);
            model.addAttribute("username", username);

            return "/user/profile/authenticated_password_reset_page";
        }

        int resp = passwordResetService.authResetUserPassword(username,
                authPasswordResetForm.getOldPassword(),
                authPasswordResetForm.getNewPassword());

        if (resp == 1) {
            return "redirect:/profile";
        } else if (resp == 2){
            return "redirect:/profile/password/reset?error=IncorrectCredentials";
        } else if (resp == 3) {
            return "redirect:/profile/password/reset?error=SWW";
        } else {
            return "redirect:/profile/password/reset?error=UserNotFound";
        }

    }
}
