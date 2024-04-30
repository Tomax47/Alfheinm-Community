package com.alfheim.aflheim_community.service.user;

import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.dto.user.UserUpdateForm;

public interface ProfileService {

    public UserDto updateAccount(UserUpdateForm userUpdateForm, String email);

    boolean isUsernameUnique(String username);
}
