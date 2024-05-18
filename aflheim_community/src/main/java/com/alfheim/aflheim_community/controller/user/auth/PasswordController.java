package com.alfheim.aflheim_community.controller.user.auth;

import com.alfheim.aflheim_community.dto.user.AuthPasswordResetForm;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.security.details.UserDetailsImpl;
import com.alfheim.aflheim_community.service.user.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import javax.validation.Valid;

@Controller
public class PasswordController {

    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping("/login/password/recover")
    public String getForgotPasswordPage(Model model) {

        return "users/auth/forgot_password_page";
    }

    @PostMapping("/login/password/recover")
    public ResponseEntity<Object> sendResetPasswordEmail(@RequestParam("email") String email) {

        System.out.println("SENDING PASSWORD RESET EMAIL TO: " + email + " | @CONTROLLER");
        int results = passwordResetService.sendPasswordResetEmail(email);

        return ResponseEntity.status(HttpStatus.OK).body("DONE");
    }

    @GetMapping("/password/reset/{reset-verification-code}")
    public String getResetPasswordPage(@PathVariable("reset-verification-code") String verificationToken, Model model) {

        String userEmail = passwordResetService.verifyResetToken(verificationToken);

        if (userEmail.isBlank()) {
            // Unauthorized or expired token
            return "redirect:/login";
        } else {
            // Authorized and correct token
            model.addAttribute("email", userEmail);
            model.addAttribute("resetToken", verificationToken);
            return "users/auth/reset_password_page";
        }
    }

    @PostMapping("/password/reset")
//    @PostMapping("/password/reset/{reset-verification-code}")
    public ResponseEntity<Object> resetUserPassword(@RequestParam("reset-verification-code") String verificationToken,
                                    @RequestParam("password") String password) {

        System.out.println("\n\nPASSWORD : " + password);
        System.out.println("TOKEN : " + verificationToken);
        int results = passwordResetService.resetUserPassword(verificationToken, password);

        return ResponseEntity.status(HttpStatus.OK).body("DONE");
    }

    @GetMapping("/profile/password/reset")
    public String getAuthPasswordResetPage(Model model,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        AuthPasswordResetForm passwordResetForm = new AuthPasswordResetForm();
        model.addAttribute("passwordResetForm", passwordResetForm);
        model.addAttribute("username", userDetails.getUsername());

        return "/users/profile/authenticated_password_reset_page";
    }

    @PostMapping("/profile/password/reset")
    @ResponseBody
    public ResponseEntity<Object> doAuthPasswordReset(@Valid @ModelAttribute("passwordResetForm") AuthPasswordResetForm authPasswordResetForm,
                                              BindingResult result,
                                              Model model,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String username = userDetails.getUsername();

        if (result.hasErrors()) {
            model.addAttribute("username", username);
            model.addAttribute("passwordResetForm", authPasswordResetForm);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You have mad a bad request");
        }

        int resp = passwordResetService.authResetUserPassword(username,
                authPasswordResetForm.getOldPassword(),
                authPasswordResetForm.getNewPassword());

        return ResponseEntity.status(HttpStatus.OK).body("Success!");
    }
}
