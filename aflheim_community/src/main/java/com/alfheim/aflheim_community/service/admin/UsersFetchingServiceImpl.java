package com.alfheim.aflheim_community.service.admin;

import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsersFetchingServiceImpl implements UsersFetchingService {

    @Autowired
    private UserRepo userRepo;

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
            size = 5;
        }


        PageRequest pageRequest = PageRequest.of(page, size, sort);

        // The page itself that we are looking for that holds the data we wanted to search it
        Page<User> papersPage = userRepo.search(query, pageRequest);

        return UserDto.usersListFrom(papersPage.getContent());
    }
}
