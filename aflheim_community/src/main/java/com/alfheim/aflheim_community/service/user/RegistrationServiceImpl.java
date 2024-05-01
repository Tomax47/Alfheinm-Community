package com.alfheim.aflheim_community.service.user;

import com.alfheim.aflheim_community.dto.user.UserRegistrationForm;
import com.alfheim.aflheim_community.model.user.Gender;
import com.alfheim.aflheim_community.model.user.Role;
import com.alfheim.aflheim_community.model.user.State;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RegistrationServiceImpl implements RegistrationService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AccountConfirmationService accountConfirmationService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public int registerUser(UserRegistrationForm userForm) {

        System.out.println("REACHED REG");
        User user = User.builder()
                .username(userForm.getUsername())
                .email(userForm.getEmail())
                .password(passwordEncoder.encode(userForm.getPassword()))
                .state(String.valueOf(State.NOT_CONFIRMED))
                .role(String.valueOf(Role.Visitor))
                .gender(String.valueOf(Gender.NOT_SPECIFIED))
                .name(null)
                .surname(null)
                .address(null)
                .number(null)
                .occupation(null)
                .build();

        try {
            // TODO : WORK ON THE VARIOUS POSSIBLE ERRORS HANDLING
            userRepo.save(user);
            accountConfirmationService.sendConfirmationEmail(user.getEmail());
            return 1;
        } catch (Exception e) {
            // Fatal error
            System.out.println("FATAL ERROR");
            return 0;
        }
    }
}
