package com.alfheim.aflheim_community.dto.user;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
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
//    private String password; TODO: ADD IT LATER ON
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name must contain only alphabetic characters")
    private String name;
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Surname must contain only alphabetic characters")
    private String surname;
    private String address;
    @NumberFormat(pattern = "###-###-####")
    private String number;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date birthdate;
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Occupation must contain only alphabetic characters")
    private String occupation;
    @Pattern(regexp = "^(Male|Female)$", message = "Gender must be either Male or Female")
    private String gender;
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Country must contain only alphabetic characters")
    private String country;
    @Pattern(regexp = "^[a-zA-Z]+$", message = "City must contain only alphabetic characters")
    private String city;
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Region must contain only alphabetic characters")
    private String region;
    @Pattern(regexp = "^[0-9]+$", message = "Zip code must contain only numbers")
    private String zip;

    public static UserUpdateForm getUpdateForm(UserDto userDto) {
        return UserUpdateForm.builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .address(userDto.getAddress())
                .number(userDto.getNumber())
                .occupation(userDto.getOccupation())
                .gender(userDto.getGender())
                .country(userDto.getCountry())
                .city(userDto.getCity())
                .region(userDto.getRegion())
                .zip(userDto.getZip())
                .birthdate(userDto.getBirthdate())
                .profilePicture(null)
                .build();
    }
}
