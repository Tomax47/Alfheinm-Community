package com.alfheim.aflheim_community.controller.user.auth;

import com.alfheim.aflheim_community.dto.user.UserRegistrationForm;
import com.alfheim.aflheim_community.service.user.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegisterController {

    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/register")
    public String getRegistrationForm(Model model) {

        // Auth. Filter
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {

            UserRegistrationForm registrationForm = new UserRegistrationForm();
            model.addAttribute("registrationForm", registrationForm);
            return "/users/auth/register_page";
        }

        return "redirect:/profile";
    }

    @PostMapping("/register")
    public String createAccount(@Valid @ModelAttribute("registrationForm") UserRegistrationForm registrationForm,
                                BindingResult result,
                                Model model) {

        if (result.hasErrors()) {
            // Fields input errors
            model.addAttribute("registrationForm", registrationForm);
            return "/users/auth/register_page";
        }

        System.out.println("REGISTRATION PROCEEDING...");
        int responseStatus = registrationService.registerUser(registrationForm);

        if (responseStatus == 1) {
            // Successful registration
            return "redirect:/confirm/process/" + registrationForm.getEmail();
        } else if (responseStatus == 2) {
            // Email already exists
            return "redirect:/register?error=email_exists";
        } else if (responseStatus == 3) {
            // Username is taken
            return "redirect:/register?error=username_is_taken";
        } else {
            // Other internal error
            return "redirect:/register?error=unknown";
        }

    }
}
