package com.alfheim.aflheim_community.controller.user_authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLoginForm() {
        return "/authentication/login_page";
    }
}
