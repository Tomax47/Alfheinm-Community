package com.alfheim.aflheim_community.controller.admin;

import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.dto.user.UserPaginationSearchFormatterDto;
import com.alfheim.aflheim_community.dto.user.UserUpdateForm;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

        if (result == 0) {
            // User not found.
            System.out.println("ADMIN CONTROLLER : USER NOT FOUND!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomError(404,
                            "User couldn't be found!",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );

        } else if (result == 2) {
            // Refused. Can't delete an admin.
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new CustomError(403,
                            "Cannot delete an admin user",
                                    Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                            );

        } else if (result == 1) {
            // Result == 1. Accepted
            return ResponseEntity.status(HttpStatus.OK)
                    .body("OK");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CustomError(400,
                        "You have made a bad request.",
                        Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                );
    }

    // Admin user password reset
    @PostMapping("/admin/user/password/reset")
    public ResponseEntity<Object> adminResetUserPassword(@RequestParam("username") String username,
                                                         @RequestParam("newPassword") String newPassword,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {

        int result = adminUsersCRUDService.resetUserPassword(username, newPassword, userDetails.getUsername());

        if (result == 200) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("OK");
        } else if (result == 404) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomError(404,
                            "User can't be found!",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        } else if (result == 500) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomError(500,
                            "Password reset request has been refused.",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        } else if (result == 403) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new CustomError(403,
                            "Cannot change admins password.",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CustomError(400,
                        "You have made a bad request.",
                        Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                );
    }

    // Admin user account confirm
    @PostMapping("/admin/users/account/confirm")
    @ResponseBody
    public ResponseEntity<Object> adminConfirmUserAccount(@RequestParam("username") String username) {

        int result = adminUsersCRUDService.confirmUserAccount(username);

        if (result == 200) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("OK");
        } else if (result == 404) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomError(404,
                            "User can't be found!",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        } else if (result == 500) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomError(500,
                            "Account confirmation request has been refused.",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CustomError(400,
                        "You have made a bad request.",
                        Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                );
    }

    @PostMapping("/admin/users/account/banState")
    @ResponseBody
    public ResponseEntity<Object> adminChangeBanUserAccountState(@RequestParam("username") String username,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {

        int result = adminUsersCRUDService.changeBanUserAccountState(username, userDetails.getUsername());

        if (result == 200) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("OK");
        } else if (result == 403) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new CustomError(403,
                            "Request has been refused due to lack of authority.",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        } else if (result == 404) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomError(404,
                            "User can't be found!",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        } else if (result == 500) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomError(500,
                            "Account ban request has been refused.",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CustomError(400,
                        "You have made a bad request.",
                        Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                );
    }

    @PostMapping("/admin/users/account/suspensionState")
    @ResponseBody
    public ResponseEntity<Object> adminChangeSuspensionUserAccountState(@RequestParam("username") String username,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {

        int result = adminUsersCRUDService.changeSuspensionUserAccountState(username, userDetails.getUsername());

        if (result == 200) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("OK");
        } else if (result == 403) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new CustomError(403,
                            "Request has been refused due to lack of authority.",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        } else if (result == 404) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomError(404,
                            "User can't be found!",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        } else if (result == 500) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomError(500,
                            "Account suspension request has been refused.",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CustomError(400,
                        "You have made a bad request.",
                        Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                );
    }
}
