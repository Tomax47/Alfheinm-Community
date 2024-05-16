package com.alfheim.aflheim_community.controller.admin;

import com.alfheim.aflheim_community.dto.user.*;
import com.alfheim.aflheim_community.model.CustomError;
import com.alfheim.aflheim_community.security.details.UserDetailsImpl;
import com.alfheim.aflheim_community.service.admin.AdminUsersCRUDService;
import com.alfheim.aflheim_community.service.user.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
public class AdminUserActionsController {

    @Autowired
    private AdminUsersCRUDService adminUsersCRUDService;

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


        List<UserDto> users = adminUsersCRUDService.search(paginationSearchFormatterDto.getSize(),
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
        List<UserDto> firstFiveUsers = adminUsersCRUDService.search(null, 0, null, null, null);

        // Passing attrs
        model.addAttribute("usersFirstPageList", firstFiveUsers);
        model.addAttribute("admin", admin);

        return "admins/admin_all_users_page";
    }

    // User profile
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
        System.out.println("\n\nADMINUSERACTIONSCONTRLLER : USERNAME -> "+username+"\n\n");
        UserDto userDto = profileService.getProfileDetailsByUsername(username);

        // Preparing the user update form
        UserUpdateForm updateForm = new UserUpdateForm();

        // Passing the models
        model.addAttribute("admin", admin);
        model.addAttribute("userDto", userDto);
        model.addAttribute("userUpdateForm", updateForm);

        return "admins/admin_user_profile_page";
    }

    // Delete user
    @PostMapping("/admin/users/delete")
    @ResponseBody
    public ResponseEntity<Object> adminDeleteUser(@RequestParam(value = "username") String username) {

        System.out.println("ADMIN CONTROLLER : DELETING USER!");
        int result = adminUsersCRUDService.deleteUser(username);

        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");
    }

    // Admin user password reset
    @PostMapping("/admin/user/password/reset")
    public ResponseEntity<Object> adminResetUserPassword(@RequestParam("username") String username,
                                                         @RequestParam("newPassword") String newPassword,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {

        int result = adminUsersCRUDService.resetUserPassword(username, newPassword, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");

    }

    // Admin user account confirm
    @PostMapping("/admin/users/account/confirm")
    @ResponseBody
    public ResponseEntity<Object> adminConfirmUserAccount(@RequestParam("username") String username) {

        int result = adminUsersCRUDService.confirmUserAccount(username);

        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");
    }

    // Admin user account change ban state
    @PostMapping("/admin/users/account/banState")
    @ResponseBody
    public ResponseEntity<Object> adminChangeBanUserAccountState(@RequestParam("username") String username,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {

        int result = adminUsersCRUDService.changeBanUserAccountState(username, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");
    }

    // Admin user account change suspension state
    @PostMapping("/admin/users/account/suspensionState")
    @ResponseBody
    public ResponseEntity<Object> adminChangeSuspensionUserAccountState(@RequestParam("username") String username,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {

        int result = adminUsersCRUDService.changeSuspensionUserAccountState(username, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");
    }

    // Admin file a user blacklist report
    @PostMapping("/admin/user/blacklist/add")
    @ResponseBody
    public ResponseEntity<Object> adminBlacklistUser(@RequestBody UserBlacklistReportForm userBlacklistReportForm,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {

        adminUsersCRUDService.addUserToBlacklist(
                userBlacklistReportForm,
                userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");
    }

    // Admin remove user from blacklist
    @PostMapping("/admin/user/blacklist/remove")
    @ResponseBody
    public ResponseEntity<Object> adminRemoveUserFromBlacklist(@RequestParam("username") String username,
                                                               @RequestParam("isErrorReport") String isErrorReport) {

        adminUsersCRUDService.removeUserFromBlacklist(username, Boolean.valueOf(isErrorReport));

        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");
    }

    // Get user blacklist report's details
    @GetMapping("/admin/user/blacklist/report")
    @ResponseBody
    public ResponseEntity<Object> adminGetUserBlacklistReportDetails(@RequestParam("username") String username) {

        UserBlacklistReportDto userBlacklistReportDto = adminUsersCRUDService.getUserBlacklistReportDetails(username);

        // Sending report details
        return ResponseEntity.status(HttpStatus.OK)
                .body(userBlacklistReportDto);
    }

    @PostMapping("/admin/user/profile/update")
    @ResponseBody
    public ResponseEntity<Object> updateUserProfileInfo(@RequestParam("username") String username,
                                                        @RequestParam(value = "profilePictureFile", required = false) MultipartFile profilePictureFile,
                                                        @RequestParam(value = "data", required = false) String data) {

        UserUpdateForm userUpdateForm = UserUpdateForm.formatData(data, profilePictureFile);

        UserDto userDto = adminUsersCRUDService.updateUserProfileInfo(username, userUpdateForm);

        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    // Admin user search page
    @GetMapping("/admin/users/search")
    public String getAdminUserSearchPage(Model model,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UserDto admin = profileService.getProfileDetails(userDetails.getUserEmail());
        model.addAttribute("admin", admin);

        return "admins/admin_users_publications_page";
    }
}
