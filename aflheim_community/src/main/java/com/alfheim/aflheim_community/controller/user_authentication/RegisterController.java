package com.alfheim.aflheim_community.controller.user_authentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterController {

    @GetMapping("/register")
    public String getRegistrationForm() {
        return "/user_auth/register_page";
    }
}
