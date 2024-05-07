package com.alfheim.aflheim_community.controller.admin;

import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.security.details.UserDetailsImpl;
import com.alfheim.aflheim_community.service.admin.UsersFetchingService;
import com.alfheim.aflheim_community.service.user.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @GetMapping("/admin/users/{page}")
    @ResponseBody
    public ResponseEntity<List<UserDto>> getAdminAllUsersPage(@PathVariable("page") String page) {

        // TODO: IMPLEMENT THE PAGINATION
        return ResponseEntity.ok(usersFetchingService.search(null, 0, null, null, null));
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

    @PostMapping("/admin/users")
    public String getUsersPagination() {

        return "admins/admin_all_users_page";
    }
}
