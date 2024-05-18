package com.alfheim.aflheim_community.service.admin;

import com.alfheim.aflheim_community.dto.user.UserBlacklistReportDto;
import com.alfheim.aflheim_community.dto.user.UserBlacklistReportForm;
import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.dto.user.UserUpdateForm;
import com.alfheim.aflheim_community.exception.blacklist_record.UserBlacklistActiveRecordException;
import com.alfheim.aflheim_community.exception.server.InternalServerErrorException;
import com.alfheim.aflheim_community.exception.blacklist_record.UserBlacklistRecordNotFoundException;
import com.alfheim.aflheim_community.exception.user.UserNotFoundException;
import com.alfheim.aflheim_community.exception.user.UserUnauthorizedRequestException;
import com.alfheim.aflheim_community.model.user.BlacklistRecordState;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.model.user.UsersBlacklist;
import com.alfheim.aflheim_community.repository.UserRepo;
import com.alfheim.aflheim_community.repository.UsersBlacklistRepo;
import com.alfheim.aflheim_community.service.user.PasswordResetService;
import com.alfheim.aflheim_community.service.user.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class AdminUsersCRUDServiceImpl implements AdminUsersCRUDService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UsersBlacklistRepo usersBlacklistRepo;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private ProfileService profileService;

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
                log.error("Unauthorized request (AdminUsersCRUDService.deleteUser)");
                throw new UserUnauthorizedRequestException("You lack the authority required for this action");
            }

            System.out.println("ADMIN SERVICE : 33333333333333333333333");

            try {
                userRepo.delete(user);
            } catch (Exception e) {
                // Something went wrong
                log.error("Internal error (AdminUsersCRUDService.deleteUser)");
                throw new InternalServerErrorException("Something went wrong");
            }

            System.out.println("ADMIN SERVICE : DELETED!");
            // Success
            return 200;
        }

        System.out.println("ADMIN SERVICE : NOT FOUND!");
        // User ain't found
        log.error("User not found (AdminUsersCRUDService.deleteUser)");
        throw new UserNotFoundException("User not found");
    }

    @Override
    public int resetUserPassword(String username, String newPassword, String adminUsername) {

        Optional<User> userOptional = userRepo.findByUsername(username);

        if (userOptional.isPresent()) {

            if (userOptional.get().getRole().equals("ADMIN")) {
                // Forbidden. Can't change admins' passwords
                log.error("Unauthorized request (AdminUsersCRUDService.resetUserPassword)");
                throw new UserUnauthorizedRequestException("You lack the authority required for this action");
            }

            int result = passwordResetService.adminUserResetPassword(userOptional.get(), newPassword, adminUsername);

            if (result != 200) {
                log.error("INternal error (AdminUsersCRUDService.resetUserPassword)");
                throw new InternalServerErrorException("Something went wrong");
            }

            return result;
        }

        // User not found
        log.error("User not found (AdminUsersCRUDService.resetUserPassword)");
        throw new UserNotFoundException("User not found");
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
                log.error("Internal error (AdminUsersCRUDService.confirmUserAccount)");
                throw new InternalServerErrorException("Something went wrong");
            }
        }

        // User not found
        log.error("User not found (AdminUsersCRUDService.confirmUserAccount)");
        throw new UserNotFoundException("User not found");
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
                    log.error("Internal error (AdminUsersCRUDService.changeBanUserAccountState)");
                    throw new InternalServerErrorException("Something went wrong");
                }
            }

            // Access forbidden. not an admin
            log.error("Unauthorized request (AdminUsersCRUDService.changeBanUserAccountState)");
            throw new UserUnauthorizedRequestException("You lack the authority required for this action");
        }

        // User not found
        log.error("User not found (AdminUsersCRUDService.changeBanUserAccountState)");
        throw new UserNotFoundException("User not found");
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
                    log.error("Internal error (AdminUsersCRUDService.changeBanUserAccountState)");
                    throw new InternalServerErrorException("Something went wrong");
                }
            }

            // Access forbidden. not an admin
            log.error("unauthorized request (AdminUsersCRUDService.changeBanUserAccountState)");
            throw new UserUnauthorizedRequestException("You lack authority required for this action");
        }

        // User not found
        log.error("User not found (AdminUsersCRUDService.changeBanUserAccountState)");
        throw new UserNotFoundException("User not found");
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
                        // Already existing active record error
                        log.error("409 User is already blacklisted (AdminUsersCRUDService.addUserToBlacklist)");
                        throw new UserBlacklistActiveRecordException("User is already blacklisted");
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
                        log.error("Internal error (AdminUsersCRUDService.addUserToBlacklist)");
                        throw new InternalServerErrorException("Something went wrong");
                    }
                }
                // Forbidden
                log.error("Unauthorized request (AdminUsersCRUDService.addUserToBlacklist)");
                throw new UserUnauthorizedRequestException("You lack the authority required for this action");
            }

            // User not found
            log.error("User not found (AdminUsersCRUDService.addUserToBlacklist)");
            throw new UserNotFoundException("User not found");
        }

        // Admin not found
        log.error("Admin not found (AdminUsersCRUDService.addUserToBlacklist)");
        throw new UserNotFoundException("Admin not found");
    }

    @Override
    public void removeUserFromBlacklist(String username, boolean isRevertingErrorReq) {
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
                    // Success... Updating objects
                    userRepo.save(user);
                    usersBlacklistRepo.save(usersBlacklistRecord);

                    return;
                } catch (Exception e) {
                    // Internal error
                    log.error("Internal error (AdminUsersCRUDService.removeUserFromBlacklist)");
                    throw new InternalServerErrorException("Something went wrong!");
                }
            }

            // No record has been found by this username
            String errorMsg = "No record been found for "+username;
            log.error("Blacklist record not found (AdminUsersCRUDService.removeUserFromBlacklist)");
            throw new UserBlacklistRecordNotFoundException(errorMsg);
        }

        // User not found
        log.error("User not found (AdminUsersCRUDService.removeUserFromBlacklist)");
        throw new UserNotFoundException("User not found");
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
        log.error("User not found (AdminUsersCRUDService.getUserBlacklistReportDetails)");
        throw new UserNotFoundException("User not found");
    }

    @Override
    public UserDto updateUserProfileInfo(String username, UserUpdateForm userUpdateForm) {

        Optional<User> userOptional = userRepo.findByUsername(username);

        if (userOptional.isPresent()) {

            if (userOptional.get().getRole().equals("ADMIN")) {
                log.error("Unauthorized request (AdminUsersCRUDService.updateUserProfileInfo)");
                throw new UserUnauthorizedRequestException("Cannot change admins' details");
            }

            // Success
            UserDto userDto = profileService.updateAccount(userUpdateForm, userOptional.get().getEmail());

            return userDto;
        }

        log.error("User not found (AdminUsersCRUDService.updateUserProfileInfo)");
        throw new UserNotFoundException("User not found");
    }
}
