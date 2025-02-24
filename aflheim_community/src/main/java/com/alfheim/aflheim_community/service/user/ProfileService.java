package com.alfheim.aflheim_community.service.user;

import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.dto.user.UserUpdateForm;

public interface ProfileService {

    public UserDto updateAccount(UserUpdateForm userUpdateForm, String email);

    boolean isUsernameUnique(String username);

    UserDto getProfileDetails(String userEmail);
    UserDto getProfileDetailsByUsername(String username);

    int deleteUserProfile(String username);

    boolean confirmAccount(String code);

    int resendConfirmationEmail(String email);

    int updateAccountRole(String username);

    void addReputationPoints(String username, double pts);
}
