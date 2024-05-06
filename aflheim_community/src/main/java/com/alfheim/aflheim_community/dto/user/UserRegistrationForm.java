package com.alfheim.aflheim_community.dto.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationForm {

    @NotBlank
//    @Pattern(regexp = "^[a-zA-Z0-9]{1,15}$", message = "Username can only contain numbers and alphabetic characters.")
    @Size(max = 15, message = "Username should be maximum 15 characters long.")
    private String username;

    @NotBlank
    @Email(message = "Email should have a valid email format.")
    @Size(max = 50, message = "Email should be maximum 50 characters long.")
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password should be at least 8 characters long.")
    private String password;
}
