package com.alfheim.aflheim_community.dto.user;

import com.alfheim.aflheim_community.model.user.Gender;
import com.alfheim.aflheim_community.model.user.User;
import lombok.*;

import javax.print.DocFlavor;
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

    public static UserDto from(User user) {
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
                .build();
    }

    public static List<UserDto> usersList(List<User> users) {
        return users.stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }
}
