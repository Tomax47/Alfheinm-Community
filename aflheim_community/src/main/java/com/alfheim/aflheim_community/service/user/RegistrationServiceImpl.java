package com.alfheim.aflheim_community.service.user;

import com.alfheim.aflheim_community.dto.user.UserRegistrationForm;
import com.alfheim.aflheim_community.exception.server.InternalServerErrorException;
import com.alfheim.aflheim_community.exception.user.EmailOrUsernameAlreadyExistException;
import com.alfheim.aflheim_community.model.user.Gender;
import com.alfheim.aflheim_community.model.user.Role;
import com.alfheim.aflheim_community.model.user.State;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.sql.Date;
import java.time.LocalDate;

@Component
@Slf4j
public class RegistrationServiceImpl implements RegistrationService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AccountConfirmationService accountConfirmationService;

    @Autowired
    private ProfileService profileService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public int registerUser(UserRegistrationForm userForm) {

        if (userRepo.findByEmail(userForm.getEmail()).isPresent()) {
            // User already exists "Faster than letting sql refuse the repeated email"
            System.out.println("EMAIL ALREADY EXIST -> " + userForm.getEmail());
            log.error("Email already exist (RegistrationServiceImpl.registerUser)");
            throw new EmailOrUsernameAlreadyExistException("Email already exist");

        } else if (!userRepo.findByEmail(userForm.getEmail()).isPresent() &&
        userRepo.findByUsername(userForm.getUsername()).isPresent()) {
            // Username is taken
            System.out.println("REGISTRATION FAILED - USERNAME TAKEN");
            log.error("Username already exist (RegistrationServiceImpl.registerUser)");
            throw new EmailOrUsernameAlreadyExistException("Username already exist");
        }

        System.out.println("REACHED REG");
        User user = User.builder()
                .username(userForm.getUsername())
                .email(userForm.getEmail())
                .password(passwordEncoder.encode(userForm.getPassword()))
                .state(String.valueOf(State.NOT_CONFIRMED))
                .role(String.valueOf(Role.VISITOR))
                .gender(String.valueOf(Gender.NOT_SPECIFIED))
                .birthdate(LocalDate.now())
                .name("Viking")
                .surname("Vikingson")
                .address("Valhalla")
                .number("000 000 0000")
                .occupation("Adventurer")
                .country("Midgard")
                .city("Valhalla")
                .region("Val")
                .zip("9")
                .reputation(0)
                .build();

        try {
            // TODO : WORK ON THE VARIOUS POSSIBLE ERRORS HANDLING
            userRepo.save(user);
            accountConfirmationService.sendConfirmationEmail(user.getEmail());
            return 200;
        } catch (Exception e) {
            // Fatal error
            log.error("Internal error (RegistrationServiceImpl.registerUser). Error : " + e);
            throw new InternalServerErrorException("Something went wrong");
        }
    }

    @Override
    public boolean isUsernameUnique(String username) {
        return profileService.isUsernameUnique(username);
    }
}
