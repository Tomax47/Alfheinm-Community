package com.alfheim.aflheim_community.dto.user;

import lombok.*;
@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationForm {

    private String username;
    private String email;
    private String password;
}
