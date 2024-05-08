package com.alfheim.aflheim_community.controller.admin;

import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.dto.user.UserPaginationSearchFormatterDto;
import com.alfheim.aflheim_community.dto.user.UserUpdateForm;
import com.alfheim.aflheim_community.security.details.UserDetailsImpl;
import com.alfheim.aflheim_community.service.admin.UsersFetchingService;
import com.alfheim.aflheim_community.service.user.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminUserActionsController {

    @Autowired
    private UsersFetchingService usersFetchingService;

    @Autowired
    private ProfileService profileService;

    // Main Dashboard
    @GetMapping("/admin/dashboard")
    public String getAdminDashboard() {

        return "admins/test_admin_page";
    }

    // All Users Section
    @GetMapping("/admin/users/paginate")
    @ResponseBody
    public ResponseEntity<List<UserDto>> getAdminAllUsersPage(
            @RequestParam(value = "size", required = false) String size,
            @RequestParam(value = "page") String page,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "direction", required = false) String direction) {

        UserPaginationSearchFormatterDto paginationSearchFormatterDto =
                UserPaginationSearchFormatterDto.fromParams(page, size, query, sort, direction);


        List<UserDto> users = usersFetchingService.search(paginationSearchFormatterDto.getSize(),
                paginationSearchFormatterDto.getPage(),
                paginationSearchFormatterDto.getQuery(),
                paginationSearchFormatterDto.getSort(),
                paginationSearchFormatterDto.getDirection());

        System.out.println("CONTROLLER USERS PAGINATED : "+users.size()+"\nSAMPLE : "+users.toString());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/admin/users")
    public String getAdminUsersPage(Model model,
                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {

        // Getting Admin Details
        UserDto admin = profileService.getProfileDetails(userDetails.getUserEmail());

        // Fetching the first page of users.
        List<UserDto> firstFiveUsers = usersFetchingService.search(null, 0, null, null, null);

        // Passing attrs
        model.addAttribute("usersFirstPageList", firstFiveUsers);
        model.addAttribute("admin", admin);

        return "admins/admin_all_users_page";
    }

    @GetMapping("/admin/users/{username}")
    public String getAdminUserProfilePage(@PathVariable("username") String username,
                                          Model model,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        // Redirecting admin to thier profile
        if (username.equals(userDetails.getUsername())) {
            return "redirect:/profile";
        }

        // Getting Admin Details
        UserDto admin = profileService.getProfileDetails(userDetails.getUserEmail());
        UserDto userDto = profileService.getProfileDetailsByUsername(username);

        // Preparing the user update form
        UserUpdateForm updateForm = new UserUpdateForm();

        // Passing the models
        model.addAttribute("admin", admin);
        model.addAttribute("userDto", userDto);
        model.addAttribute("userUpdateForm", updateForm);

        return "admins/admin_user_profile_page";
    }
}
