package com.alfheim.aflheim_community.service.admin;

import com.alfheim.aflheim_community.dto.user.UserDto;

import java.util.List;

public interface AdminUsersCRUDService {

    List<UserDto> search(Integer size, Integer page, String query, String sort, String direction);

    int deleteUser(String username);

    int resetUserPassword(String username, String newPassword, String adminUsername);
}
