package com.alfheim.aflheim_community.controller.user.account;

import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.dto.user.UserUpdateForm;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.security.details.UserDetailsImpl;
import com.alfheim.aflheim_community.service.user.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static com.alfheim.aflheim_community.dto.user.UserUpdateForm.getUpdateForm;

@Controller
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/profile")
    public String getUserProfilePage(Model model,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails,
                                     Principal principal) {

        // Getting the user
        UserDto userDto = profileService.getProfileDetails(userDetails.getUserEmail());

        // Checking the aut state
        boolean isAuthenticated = principal != null;
        model.addAttribute("isAuthenticated", isAuthenticated);

        // Passing in the dto and the form
        model.addAttribute("userDto", userDto);
        model.addAttribute("user", getUpdateForm(userDto));
        return "user/profile/profile_page";
    }

    @PostMapping("/profile/update")
    public String updateProfileInformation(@Valid @ModelAttribute("user") UserUpdateForm userUpdateForm,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        System.out.println(userUpdateForm.getProfilePicture().getContentType());
        UserDto user = profileService.updateAccount(userUpdateForm, userDetails.getUserEmail());

        if (user != null) {
            return "redirect:/profile";
        } else {
            // TODO: HANDLE IT BETTER
            return "redirect:/profile?error";
        }
    }

    @GetMapping("/profile/{username}/deleteAccount")
    public String getDeletePage(@PathVariable("username") String username,
                                Model model) {
        model.addAttribute("username", username);
        model.addAttribute("isAuthenticated", true);
        return "user/profile/delete_profile_page";
    }

    @GetMapping("/profile/{username}/delete")
    public String deleteUserProfile(@PathVariable("username") String username,
                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {

        System.out.println("asfasdasdasdad");

        if (isSameUserAuth(username, userDetails)) {
            int deleteUser = profileService.deleteUserProfile(username);

            // TODO : HANDLE THE ERRORS IN A MORE SPECIFIC WAY
            if (deleteUser == 1) {
                // Deletion succeeded
                return "redirect:/logout";
            } else {
                // Something went wrong
                return "redirect:/profile";
            }
        } else {
            // UNAUTHORIZED REQUEST
            System.out.println("UNAUTHORIZED");
            return "redirect:/";
        }

    }

    private boolean isSameUserAuth(String username, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userDetails.getUsername().equals(username);
    }
}
