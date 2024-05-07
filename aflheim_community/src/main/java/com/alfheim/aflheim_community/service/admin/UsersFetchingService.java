package com.alfheim.aflheim_community.service.admin;

import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.model.user.User;

import java.util.List;

public interface UsersFetchingService {

    List<UserDto> search(Integer size, Integer page, String query, String sort, String direction);
}
