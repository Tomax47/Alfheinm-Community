package com.alfheim.aflheim_community.controller.user.account;

import com.alfheim.aflheim_community.security.details.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String getUserProfilePage(Model model,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {

        model.addAttribute("user", userDetails.getUserDto());
        return "user/profile_page";
    }
}
