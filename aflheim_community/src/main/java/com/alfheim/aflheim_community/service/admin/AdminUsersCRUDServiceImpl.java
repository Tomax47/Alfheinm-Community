package com.alfheim.aflheim_community.service.admin;

import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class AdminUsersCRUDServiceImpl implements AdminUsersCRUDService {

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
                return 2;
            }

            System.out.println("ADMIN SERVICE : 33333333333333333333333");
            userRepo.delete(user);
            System.out.println("ADMIN SERVICE : DELETED!");
            return 1;
        }

        System.out.println("ADMIN SERVICE : NOT FOUND!");
        // User ain't found
        return 0;
    }
}
