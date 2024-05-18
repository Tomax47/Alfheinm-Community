package com.alfheim.aflheim_community.service.user;

import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.dto.user.UserUpdateForm;
import com.alfheim.aflheim_community.exception.profile.AlreadyConfirmedException;
import com.alfheim.aflheim_community.exception.profile.ConfirmationRecordNotFoundException;
import com.alfheim.aflheim_community.exception.profile.RoleAlreadyExistException;
import com.alfheim.aflheim_community.exception.server.InternalServerErrorException;
import com.alfheim.aflheim_community.exception.user.UserNotFoundException;
import com.alfheim.aflheim_community.model.user.Gender;
import com.alfheim.aflheim_community.model.user.Role;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.model.user.UserConfirmation;
import com.alfheim.aflheim_community.repository.UserRepo;
import com.alfheim.aflheim_community.service.file.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private AccountConfirmationService accountConfirmationService;

    @Override
    public UserDto updateAccount(UserUpdateForm userUpdateForm, String email) {

        User user = userRepo.findByEmail(email).get();

        System.out.println("\n\nIS NAME NULL? " + userUpdateForm.getName() + ". Check -> "+ (userUpdateForm.getName() == null));
        if (!userUpdateForm.getName().isBlank()) {
            user.setName(userUpdateForm.getName());
        }

        if (!userUpdateForm.getSurname().isBlank()) {
            user.setSurname(userUpdateForm.getSurname());
        }
        if (!userUpdateForm.getAddress().isBlank()) {
            user.setAddress(userUpdateForm.getAddress());
        }
        if (!userUpdateForm.getNumber().isBlank()) {
            user.setNumber(userUpdateForm.getNumber());
        }
        if (!userUpdateForm.getCountry().isBlank()) {
            user.setCountry(userUpdateForm.getCountry());
        }
        if (!userUpdateForm.getCity().isBlank()) {
            user.setCity(userUpdateForm.getCity());
        }
        if (!userUpdateForm.getRegion().isBlank()) {
            user.setRegion(userUpdateForm.getRegion());
        }
        if (!userUpdateForm.getZip().isBlank()) {
            user.setZip(userUpdateForm.getZip());
        }

        if (userUpdateForm.getOccupation() != null) {
            user.setOccupation(userUpdateForm.getOccupation());
        }

        System.out.println("BIRTHDATE -> "+userUpdateForm.getBirthdate());
        if (userUpdateForm.getBirthdate() != null) {
            System.out.println("UPDATING BIRTHDATE...");
            user.setBirthdate(userUpdateForm.getBirthdate());
        }

        if (userUpdateForm.getGender().equals("Male")) {
            user.setGender(String.valueOf(Gender.Male));
        } else if (userUpdateForm.getGender().equals("Female")) {
            user.setGender(String.valueOf(Gender.Female));
        }

        try {

            System.out.println("\n\nUSER PROFILE PICTURE : \nIS NULL? "+
                    (userUpdateForm.getProfilePicture() == null));

            if (userUpdateForm.getProfilePicture() != null) {

                if (userUpdateForm.getProfilePicture().getContentType().equals("image/jpeg") ||
                        userUpdateForm.getProfilePicture().getContentType().equals("image/png") ||
                userUpdateForm.getProfilePicture().getContentType().equals("image/webp")) {

                    // New file submission

                    System.out.println("IMAGE ACCEPTED! SAVING A NEW IMAGE...");
                    // Saving the file
                    String imageStorageName = fileStorageService.saveFile(userUpdateForm.getProfilePicture());

                    System.out.println("\nIMAGE NAME : "+imageStorageName);
                    System.out.println("\nIS USER'S PROFILE IMAGE NULL? "+user.getProfilePicture());

                    // Setting the new profile picture
                    user.setProfilePicture(fileStorageService.findByStorageName(imageStorageName));
                }
            }

            // Update user's details
            User updatedUser = userRepo.save(user);
            return UserDto.from(updatedUser);

        } catch (Exception e) {
            log.error("Internal error (ProfileServiceImpl.updateAccount). Error message : " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isUsernameUnique(String username) {
        try {
            Optional<User> user = userRepo.findByUsername(username);

            if (!user.isPresent()) {
                return true;
            }

            return false;
        } catch (Exception e) {
            // Something went wrong
            log.error("Internal error (ProfileServiceImpl.deleteUserProfile). Error : " + e);
            throw new InternalServerErrorException("Something went wrong");
        }
    }

    @Override
    public UserDto getProfileDetails(String userEmail) {
        User user = userRepo.findByEmail(userEmail).get();

        return UserDto.from(user);
    }

    @Override
    public UserDto getProfileDetailsByUsername(String username) {
        Optional<User> user = userRepo.findByUsername(username);

        if (!user.isPresent()) {
            // User not found
            log.error("User not found (ProfileServiceImpl.getProfileDetailsByUsername)");
            throw new UserNotFoundException("User not found");
        }

        return UserDto.from(user.get());
    }

    @Override
    public int deleteUserProfile(String username) {
        User user = userRepo.findByUsername(username).get();

        if (user != null) {
            try {
                userRepo.delete(user);
                return 200;
            } catch (Exception e) {
                // Something went wrong
                log.error("Internal error (ProfileServiceImpl.deleteUserProfile)");
                throw new InternalServerErrorException("Something went wrong");
            }
        }
        // User not found
        log.error("User not found (ProfileServiceImpl.deleteUserProfile)");
        throw new UserNotFoundException("User not found");
    }

    @Override
    public boolean confirmAccount(String code) {

        UserConfirmation userConfirmationRecord = accountConfirmationService.getEmailConfirmationRecord(code).get();

        if (userConfirmationRecord.getUserEmail() != null &&
                userConfirmationRecord.getState().toString().equals("ACTIVE")) {

            // Record exist and isActive
            User user = userRepo.findByEmail(userConfirmationRecord.getUserEmail()).get();
            user.setState("CONFIRMED");

            // Activating user account
            userRepo.save(user);

            // Expiring the confirmation record
            accountConfirmationService.expireRecord(userConfirmationRecord);

            return true;

        } else {
            // No record found or not active
            log.error("Not active confirmation record been found (ProfileServiceImpl.confirmAccount)");
            throw new ConfirmationRecordNotFoundException("No active confirmation record been found");
        }
    }

    @Override
    public int resendConfirmationEmail(String email) {
        // Fetching the user by email
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isPresent()) {
            if (user.get().getState().toString().equals("NOT_CONFIRMED")) {

                // Resending confirmation email
                accountConfirmationService.sendConfirmationEmail(email);
                return 200;
            } else {
                // User is already confirmed
                log.error("UAccount is already confirmed (ProfileServiceImpl.resendConfirmationEmail)");
                throw new AlreadyConfirmedException("Account is already confirmed");
            }
        }
        // User ain't found
        log.error("User not found (ProfileServiceImpl.resendConfirmationEmail)");
        throw new UserNotFoundException("User not found");
    }

    @Override
    public int updateAccountRole(String username) {

        Optional<User> userOptional = userRepo.findByUsername(username);

        if (userOptional.isPresent()) {
            // User exist
            User user = userOptional.get();

            if (user.getRole().equals("MEMBER")) {
                // Already a member
                log.error("User is already a member (ProfileServiceImpl.updateAccountRole)");
                throw new RoleAlreadyExistException("User is already a Member");
            } else if (user.getRole().equals("ADMIN")) {
                // Already an Admin
                log.error("User is already an admin (ProfileServiceImpl.updateAccountRole)");
                throw new RoleAlreadyExistException("User is already an Admin");
            }

            // update user
            user.setRole(Role.MEMBER.toString());
            userRepo.save(user);
            return 200;
        }

        log.error("User not found (ProfileServiceImpl.updateAccountRole)");
        throw new UserNotFoundException("User not found (ProfileService.UpdateAccountRole)");
    }

    @Override
    public void addReputationPoints(String username, double pts) {

        Optional<User> userOptional = userRepo.findByUsername(username);

        if (userOptional.isPresent()) {
            try {
                // User exist. Updating reputation
                User user = userOptional.get();

                int newReputation = (int) (user.getReputation() + pts);
                user.setReputation(newReputation);

                // Updating user info
                userRepo.save(user);

                return;
            } catch (Exception e) {
                // Error
                log.error("Internal error (ProfileServiceImpl.addReputationPoints)");
                throw new InternalServerErrorException("Something went wrong while adding the reputation points");
            }
        }

        // User not found
        log.error("User not found (ProfileServiceImpl.addReputationPoints)");
        throw new UserNotFoundException("User not found (ProfileService.addReputationPoints)");
    }
}
