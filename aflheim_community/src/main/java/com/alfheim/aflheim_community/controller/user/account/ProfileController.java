package com.alfheim.aflheim_community.controller.user.account;

import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.dto.user.UserUpdateForm;
import com.alfheim.aflheim_community.exception.user.UserUnauthorizedRequestException;
import com.alfheim.aflheim_community.security.details.UserDetailsImpl;
import com.alfheim.aflheim_community.service.user.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.ws.RequestWrapper;
import java.security.Principal;


@Controller
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/profile")
    public String getUserProfilePage(Model model,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {

        // Getting the user
        UserDto userDto = profileService.getProfileDetails(userDetails.getUserEmail());

        // Passing in the dto and the form
        UserUpdateForm updateForm = new UserUpdateForm();
        model.addAttribute("userDto", userDto);
        model.addAttribute("user", updateForm);
        return "users/profile/profile_page";
    }

    @PostMapping("/profile/update")
    public String updateProfileInformation(@Valid @ModelAttribute("user") UserUpdateForm userUpdateForm,
                                           BindingResult result,
                                           Model model,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (result.hasErrors()) {
            model.addAttribute("user", userUpdateForm);
            model.addAttribute("userDto", profileService.getProfileDetails(userDetails.getUserEmail()));
            return "users/profile/profile_page";
        }

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
                                @AuthenticationPrincipal UserDetailsImpl userDetails,
                                Model model) {

        if (!username.equals(userDetails.getUsername())) {
            return "redirect:/profile";
        }

        String profilePictureUrl = profileService.getProfileDetailsByUsername(username).getProfilePicture();
        model.addAttribute("username", username);
        model.addAttribute("profilePicUrl", profilePictureUrl);
        model.addAttribute("isAuthenticated", true);
        return "users/profile/delete_profile_page";
    }

    @PostMapping("/profile/delete")
    @ResponseBody
    public ResponseEntity<Object> deleteUserProfile(@RequestParam("username") String username,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (!username.equals(userDetails.getUsername())) {
            // Unauthorized
            throw new UserUnauthorizedRequestException("unauthorized request");
        }

        // Success
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}
