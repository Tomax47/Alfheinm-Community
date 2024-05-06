package com.alfheim.aflheim_community.dto.user;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateForm {

    private MultipartFile profilePicture;
    @Size(max = 20, message = "Too long name. 20 characters max.")
//    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name should only contain letters")
    private String name;
    @Size(max = 20, message = "Too long surname. 20 characters max.")
//    @Pattern(regexp = "^[a-zA-Z]+$", message = "Surname should only contain letters")
    private String surname;
    @Size(max = 100, message = "Too long Address. 100 characters max.")
    private String address;
    @NumberFormat(pattern = "###-###-####")
//    @Pattern(regexp = "^[0-9]+$", message = "Phone number should only contain digits")
    private String number;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    @Size(max = 60, message = "Too long occupation title. 60 characters max.")
    //    @Pattern(regexp = "^[a-zA-Z]+$", message = "Occupation should only contain letters")
    private String occupation;
    private String gender;
    //    @Pattern(regexp = "^[a-zA-Z]+$", message = "Country should only contain letters")
    private String country;
    @Size(max = 20, message = "Too long city name. 20 characters max.")
    //    @Pattern(regexp = "^[a-zA-Z]+$", message = "City name should only contain letters")
    private String city;
    @Size(max = 20, message = "Too long region name. 20 characters max.")
    //    @Pattern(regexp = "^[a-zA-Z]+$", message = "Region should only contain letters")
    private String region;
    //    @Pattern(regexp = "^[0-9]+$", message = "ZIP code should only contain digits")
    private String zip;

}