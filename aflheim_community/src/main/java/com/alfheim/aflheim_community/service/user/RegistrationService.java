package com.alfheim.aflheim_community.service.user;

import com.alfheim.aflheim_community.dto.user.UserRegistrationForm;

public interface RegistrationService {

    public int registerUser(UserRegistrationForm userForm);

    boolean isUsernameUnique(String username);
}
