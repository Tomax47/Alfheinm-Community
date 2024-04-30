package com.alfheim.aflheim_community.controller.user.account;

import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.dto.user.UserUpdateForm;
import com.alfheim.aflheim_community.security.details.UserDetailsImpl;
import com.alfheim.aflheim_community.service.user.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Controller
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/profile")
    public String getUserProfilePage(Model model,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {

        model.addAttribute("user", userDetails.getUserDto());

        return "user/profile_page";
    }

    @PostMapping("/profile/update")
    public String updateProfileInformation(@Valid @ModelAttribute("user") UserUpdateForm userUpdateForm,
                                           Model model,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UserDto user = profileService.updateAccount(userUpdateForm, userDetails.getUserDto().getEmail());

        if (user != null) {
            model.addAttribute("user", user);
            return "user/profile_page";
        } else {
            // TODO: HANDLE IT BETTER
            return "redirect:/profile?error";
        }
    }
}
