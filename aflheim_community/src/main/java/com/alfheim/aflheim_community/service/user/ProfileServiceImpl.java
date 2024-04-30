package com.alfheim.aflheim_community.service.user;

import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.dto.user.UserUpdateForm;
import com.alfheim.aflheim_community.model.user.Gender;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDto updateAccount(UserUpdateForm userUpdateForm, String email) {

        User user = userRepo.findByEmail(email).get();
        user.setName(userUpdateForm.getName());
        user.setSurname(userUpdateForm.getSurname());
        user.setAddress(userUpdateForm.getAddress());
        user.setOccupation(userUpdateForm.getOccupation());
        user.setNumber(userUpdateForm.getNumber());
        user.setCountry(userUpdateForm.getCountry());
        user.setCity(userUpdateForm.getCity());
        user.setRegion(userUpdateForm.getRegion());
        user.setZip(userUpdateForm.getZip());
        user.setBirthdate(userUpdateForm.getBirthdate());

        if (userUpdateForm.getGender().equals("Male")) {
            user.setGender(String.valueOf(Gender.Male));
        } else if (userUpdateForm.getGender().equals("Female")) {
            user.setGender(String.valueOf(Gender.Female));
        }

        try {
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
}
