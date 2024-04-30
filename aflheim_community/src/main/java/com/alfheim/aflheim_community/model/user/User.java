package com.alfheim.aflheim_community.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity(name = "account")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // Mandatory & Unique
    private String email; // Mandatory & Unique
    private String password; // Mandatory & at least 8-char long
    private String name; // Optional
    private String surname; // Optional
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date birthdate; // Optional
    private String address; // Optional
    private String number; // Optional
    private String occupation; // Optional
    private String country; // Optional
    private String city; // Optional
    private String region; // Optional
    private String zip; // Optional
    private String profilePicture;

    private String gender; // [Male = 0, Female = 1, Not_Specified = 2]
    private String role; // [Visitor = 0, Member = 1, Admin = 3]
    private String state; // [CONFIRMED = 0, NOT_CONFIRMED = 1, SUSPENDED = 2, BANNED = 3, BLACKLISTED = 4]

}
