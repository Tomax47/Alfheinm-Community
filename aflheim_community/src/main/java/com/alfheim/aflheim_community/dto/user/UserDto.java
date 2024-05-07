package com.alfheim.aflheim_community.dto.user;

import com.alfheim.aflheim_community.model.user.Gender;
import com.alfheim.aflheim_community.model.user.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.print.DocFlavor;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String username;
    private String name;
    private String surname;
    private String address;
    private String number;
    private String occupation;
    private String gender;
    private String role;
    private String country;
    private String city;
    private String region;
    private String zip;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    private String profilePicture;
    private String accountState;

    public static UserDto from(User user) {

        String profilePictureUrl = "";

        if (user.getProfilePicture() != null) {
            profilePictureUrl += "http://localhost:8080/files/" + user.getProfilePicture().getFileStorageName();
        } else {
            profilePictureUrl = "/assets/img/default-avatar.jpg";
        }

//        LocalDate formattedBirthdate = LocalDate.ofInstant(
//                user.getBirthdate().toInstant(),
//                ZoneId.systemDefault());


        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .address(user.getAddress())
                .number(user.getNumber())
                .occupation(user.getOccupation())
                .gender(user.getGender())
                .role(user.getRole())
                .accountState(user.getState())
                .country(user.getCountry())
                .city(user.getCity())
                .region(user.getRegion())
                .zip(user.getZip())
                .birthdate(user.getBirthdate())
                .profilePicture(profilePictureUrl)
                .build();
    }

    public static List<UserDto> usersListFrom(List<User> users) {
        return users.stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }
}
