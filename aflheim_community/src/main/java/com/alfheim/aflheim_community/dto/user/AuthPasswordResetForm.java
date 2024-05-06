package com.alfheim.aflheim_community.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthPasswordResetForm {

    @NotBlank(message = "Password fields can't be empty!")
    private String oldPassword;
    @NotBlank(message = "Password fields can't be empty!")
    @Size(min = 8, message = "Password must be 8 characters long!")
    private String newPassword;
}
