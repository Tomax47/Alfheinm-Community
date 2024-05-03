package com.alfheim.aflheim_community.service.user;

import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.dto.user.UserUpdateForm;
import com.alfheim.aflheim_community.model.user.Gender;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.model.user.UserConfirmation;
import com.alfheim.aflheim_community.repository.UserConfirmationRepo;
import com.alfheim.aflheim_community.repository.UserRepo;

import com.alfheim.aflheim_community.service.file.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
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

            if (userUpdateForm.getProfilePicture() != null &&
                    // TODO: TRANSFER THIS FUNCTIONALITY FOR JS ON THE FRONTEND
                    userUpdateForm.getProfilePicture().getContentType().equals("image/jpeg") ||
                    userUpdateForm.getProfilePicture().getContentType().equals("image/png")
            ) {

                String imageStorageName = fileStorageService.saveFile(userUpdateForm.getProfilePicture());

                // TODO: DELETE THE CURRENT IMG OF THE USER FROM THE TABLE IF HE HAS ONE AND REPLACE IT WITH THE NEW ONE
                user.setProfilePicture(fileStorageService.findByStorageName(imageStorageName));
            }
            User updatedUser = userRepo.save(user);
            return UserDto.from(updatedUser);

        } catch (Exception e) {
            // TODO: HANDLE IT BETTER
            return null;
        }
    }

    @Override
    public boolean isUsernameUnique(String username) {
        User user = userRepo.findByUsername(username).get();

        if (user.getUsername().isEmpty() || user.getUsername().isEmpty()) {
            return true;
        }

        return false;
    }

    @Override
    public UserDto getProfileDetails(String userEmail) {
        User user = userRepo.findByEmail(userEmail).get();

        return UserDto.from(user);
    }

    @Override
    public int deleteUserProfile(String username) {
        User user = userRepo.findByUsername(username).get();

        if (user != null) {
            try {
                userRepo.delete(user);
                return 1;
            } catch (Exception e) {
                // Something went wrong
                return 2;
            }
        }
        // User not found
        return 0;
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
            return false;
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
                return 1;
            } else {
                // User is already confirmed
                return 2;
            }
        }
        // User ain't found
        return 0;
    }
}
