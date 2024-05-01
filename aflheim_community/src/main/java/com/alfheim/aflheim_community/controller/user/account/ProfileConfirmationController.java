package com.alfheim.aflheim_community.controller.user.account;

import com.alfheim.aflheim_community.service.user.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}
