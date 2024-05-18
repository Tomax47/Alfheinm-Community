package com.alfheim.aflheim_community.controller.user.auth;

import com.alfheim.aflheim_community.dto.user.UserRegistrationForm;
import com.alfheim.aflheim_community.exception.user.UserUnauthorizedRequestException;
import com.alfheim.aflheim_community.service.user.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.standard.processor.StandardHrefTagProcessor;

import javax.validation.Valid;

@Controller
public class RegisterController {

    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/register")
    public String getRegistrationForm(Model model) {

        // Auth. Filter
        UserRegistrationForm registrationForm = new UserRegistrationForm();
        model.addAttribute("registrationForm", registrationForm);
        return "/users/auth/register_page";
    }

    @PostMapping("/register")
    public ResponseEntity<Object> createAccount(@Valid @ModelAttribute("registrationForm") UserRegistrationForm registrationForm,
                                BindingResult result,
                                Model model) {

        if (result.hasErrors()) {
            // Fields input errors
            model.addAttribute("registrationForm", registrationForm);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You have made a bad request");
        }

        System.out.println("REGISTRATION PROCEEDING...");
        int responseStatus = registrationService.registerUser(registrationForm);

        return ResponseEntity.status(HttpStatus.OK).body("DONE");
    }

    @GetMapping("/register/usernameCheck")
    @ResponseBody
    public ResponseEntity<Object> doUsernameCheck(@RequestParam("username") String username) {

        boolean result = registrationService.isUsernameUnique(username);

        return ResponseEntity.ok(result);
    }

}
