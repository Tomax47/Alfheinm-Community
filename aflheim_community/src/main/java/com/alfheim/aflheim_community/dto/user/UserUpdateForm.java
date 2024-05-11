package com.alfheim.aflheim_community.dto.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

    public static UserUpdateForm formatData(String dataString, MultipartFile profilePicture) {

        // JSON'ifying the data
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(dataString, JsonObject.class);

        // Getting the data from the JsonObject
        DataForm dataForm = gson.fromJson(jsonObject, DataForm.class);

        // Building the user update form object
        UserUpdateForm userUpdateForm = UserUpdateForm.builder()
                .profilePicture(profilePicture)
                .name(dataForm.getName())
                .surname(dataForm.getSurname())
                .address(dataForm.getAddress())
                .number(dataForm.getNumber())
                .occupation(null)
                .gender(dataForm.getGender())
                .country(dataForm.getCountry())
                .city(dataForm.getCity())
                .region(dataForm.getRegion())
                .zip(dataForm.getZip())
                .build();

        // Setting birthdate value
        if (dataForm.getBirthdate() != null && !dataForm.getBirthdate().isBlank()) {
            userUpdateForm.setBirthdate(LocalDate.parse(dataForm.getBirthdate()));
        } else {
            userUpdateForm.setBirthdate(null);
        }

        return userUpdateForm;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class DataForm {

        private String name;
        private String surname;
        private String address;
        private String number;
        private String birthdate;
        private String occupation;
        private String gender;
        private String country;
        private String city;
        private String region;
        private String zip;
    }
}