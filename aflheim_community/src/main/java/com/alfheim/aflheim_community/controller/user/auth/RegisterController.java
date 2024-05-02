package com.alfheim.aflheim_community.controller.user.auth;

import com.alfheim.aflheim_community.dto.user.UserRegistrationForm;
import com.alfheim.aflheim_community.service.user.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/register")
    public String getRegistrationForm(Model model) {

        // Auth. Filter
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("isAuthenticated", false);
            return "/user/auth/register_page";
        }

        return "redirect:/profile";
    }

    @PostMapping("/register")
    public String createAccount(UserRegistrationForm registrationForm) {

        // TODO : HANDLE THE ERRORS BETTER AND IN A SPECIFIC WAY

        int responseStatus = registrationService.registerUser(registrationForm);

        if (responseStatus == 1) {
            return "redirect:/confirm/process/" + registrationForm.getEmail();
        } else {
            return "redirect:/register?error";
        }

    }
}
