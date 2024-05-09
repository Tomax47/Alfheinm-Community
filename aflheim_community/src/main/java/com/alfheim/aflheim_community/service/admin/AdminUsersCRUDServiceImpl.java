package com.alfheim.aflheim_community.service.admin;

import com.alfheim.aflheim_community.dto.user.UserBlacklistReportDto;
import com.alfheim.aflheim_community.dto.user.UserBlacklistReportForm;
import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.model.user.BlacklistRecordState;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.model.user.UsersBlacklist;
import com.alfheim.aflheim_community.repository.UserRepo;
import com.alfheim.aflheim_community.repository.UsersBlacklistRepo;
import com.alfheim.aflheim_community.service.user.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AdminUsersCRUDServiceImpl implements AdminUsersCRUDService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UsersBlacklistRepo usersBlacklistRepo;

    @Autowired
    private PasswordResetService passwordResetService;

    @Override
    public List<UserDto> search(Integer size, Integer page, String query, String sortParameter, String directionParameter) {

        Sort.Direction direction = Sort.Direction.ASC;
        Sort sort = Sort.by(direction, "id");

        if (directionParameter != null) {
            direction = Sort.Direction.fromString(directionParameter);
        }

        if (sortParameter != null) {
            sort = Sort.by(direction, sortParameter);
        }

        if (query == null) {
            // Setting the sql query part to be empty.
            query = "empty";
        }

        if (size == null) {
            size = 4;
        }

        if (page == null) {
            page = 0;
        }

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        // The page itself that we are looking for that holds the data we wanted to search it
        Page<User> papersPage = userRepo.search(query, pageRequest);

        return UserDto.usersListFrom(papersPage.getContent());
    }

    @Override
    public int deleteUser(String username) {
        Optional<User> userOptional = userRepo.findByUsername(username);

        System.out.println("\n\nADMIN SERVICE : 111111111111111111111");
        if (userOptional.isPresent()) {
            System.out.println("ADMIN SERVICE : 22222222222222222");
            User user = userOptional.get();

            if (user.getRole().equals("ADMIN")) {
                System.out.println("ADMIN SERVICE : REFUSED. IS ADMIN!");
                // Refusing request. Can't delete an admin!
                return 2;
            }

            System.out.println("ADMIN SERVICE : 33333333333333333333333");
            userRepo.delete(user);
            System.out.println("ADMIN SERVICE : DELETED!");
            return 1;
        }

        System.out.println("ADMIN SERVICE : NOT FOUND!");
        // User ain't found
        return 0;
    }

    @Override
    public int resetUserPassword(String username, String newPassword, String adminUsername) {

        Optional<User> userOptional = userRepo.findByUsername(username);

        if (userOptional.isPresent()) {

            if (userOptional.get().getRole().equals("ADMIN")) {
                // Forbidden. Can't change admins' passwords
                return 403;
            }

            int result = passwordResetService.adminUserResetPassword(userOptional.get(), newPassword, adminUsername);
            return result;
        }

        // User not found
        return 404;
    }

    @Override
    public int confirmUserAccount(String username) {

        Optional<User> userOptional = userRepo.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setState("CONFIRMED");

            try {
                userRepo.save(user);
                return 200;
            } catch (Exception e) {

                return 500;
            }
        }

        // User not found
        return 404;
    }

    @Override
    public int changeBanUserAccountState(String username, String adminUsername) {

        Optional<User> userOptional = userRepo.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (!user.getState().equals("BANNED")) {
                user.setState("BANNED");
            } else {
                user.setState("CONFIRMED");
            }

            if (userRepo.findByUsername(adminUsername).get().getRole().equals("ADMIN")) {
                try {
                    // Success
                    userRepo.save(user);
                    return 200;
                } catch (Exception e) {
                    // Some error
                    return 500;
                }
            }

            // Access forbidden. not an admin
            return 403;
        }

        // User not found
        return 404;
    }

    @Override
    public int changeSuspensionUserAccountState(String username, String adminUsername) {

        Optional<User> userOptional = userRepo.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (!user.getState().equals("SUSPENDED")) {
                // Suspend the account
                user.setState("SUSPENDED");
            } else {
                // Remove restrictions on the account
                user.setState("CONFIRMED");
            }

            if (userRepo.findByUsername(adminUsername).get().getRole().equals("ADMIN")) {
                try {
                    // Success
                    userRepo.save(user);
                    return 200;
                } catch (Exception e) {
                    // Some error
                    return 500;
                }
            }

            // Access forbidden. not an admin
            return 403;
        }

        // User not found
        return 404;
    }

    // Blacklisting user
    @Override
    public int addUserToBlacklist(UserBlacklistReportForm userBlacklistReportForm, String adminUsername) {
        System.out.println("\n\nADMIN SERVICE : ADDING USER TO THE BLACKLIST...");
        Optional<User> userOptional = userRepo.findByUsername(userBlacklistReportForm.getUsername());
        Optional<User> adminOptional = userRepo.findByUsername(adminUsername);

        if (adminOptional.isPresent()) {
            if (userOptional.isPresent()) {
                if (adminOptional.get().getRole().equals("ADMIN") && !userOptional.get().getRole().equals("ADMIN")) {
                    if (usersBlacklistRepo.findByUsernameAndState(userOptional.get().getUsername(), BlacklistRecordState.VALID).isPresent()) {
                        System.out.println("\n\n111111111111111111111\n\n");
                        return 4409;
                    }

                    // Preparing user and the blacklist report
                    User user = userOptional.get();

                    UsersBlacklist usersBlacklist = UsersBlacklist.builder()
                            .userEmail(user.getEmail())
                            .username(userBlacklistReportForm.getUsername())
                            .reason(userBlacklistReportForm.getReason())
                            .reputationPtsDeducted(userBlacklistReportForm.getReputationPtsDeducted())
                            .state(BlacklistRecordState.VALID)
                            .build();

                    try {
                        // Saving the Blacklist report
                        usersBlacklistRepo.save(usersBlacklist);

                        // Updating user's state
                        user.setState("BLACKLISTED");

                        // Updating user's reputation
                        int newUserReputation = user.getReputation() - usersBlacklist.getReputationPtsDeducted();
                        user.setReputation(newUserReputation);

                        userRepo.save(user);

                        // Success.
                        return 200;
                    } catch (Exception e) {
                        // Internal error
                        return 500;
                    }
                }
                // Forbidden
                return 403;
            }

            // User not found
            return 404;
        }

        // Admin not found
        return 1404;
    }

    @Override
    public int removeUserFromBlacklist(String username, boolean isRevertingErrorReq) {
        System.out.println("\n\nADMIN SERVICE : REMOVING USER FROM THE BLACKLIST");
        Optional<User> userOptional = userRepo.findByUsername(username);

        if (userOptional.isPresent()) {
            Optional<UsersBlacklist> usersBlacklistOptional = usersBlacklistRepo.findByUsernameAndState(username, BlacklistRecordState.VALID);

            if (usersBlacklistOptional.isPresent()) {
                // Record found
                System.out.println("\n\nRECORD BLACKLIST : "+usersBlacklistOptional.get().toString());
                UsersBlacklist usersBlacklistRecord = usersBlacklistOptional.get();
                User user = userOptional.get();

                // Invalidating the record
                usersBlacklistRecord.setState(BlacklistRecordState.INVALID);

                if (isRevertingErrorReq) {
                    // Reverting request. The blacklisting was an error, retrieving user's deducted reputation pts
                    int restoredReputation = user.getReputation() + usersBlacklistRecord.getReputationPtsDeducted();
                    user.setReputation(restoredReputation);
                }

                user.setState("CONFIRMED");

                try {
                    // Updating objects
                    userRepo.save(user);
                    usersBlacklistRepo.save(usersBlacklistRecord);

                    //Success
                    return 200;
                } catch (Exception e) {
                    // Internal error
                    return 500;
                }
            }

            // No record has been found by this username
            return 4404;
        }

        // User not found
        return 404;
    }

    // Getting user blacklist report's details
    @Override
    public UserBlacklistReportDto getUserBlacklistReportDetails(String username) {
        Optional<UsersBlacklist> usersBlacklist = usersBlacklistRepo.findByUsernameAndState(username, BlacklistRecordState.VALID);

        if (usersBlacklist.isPresent()) {
            // returning report's details
            return UserBlacklistReportDto.from(usersBlacklist.get());
        }

        // No report found
        return null;
    }
}
