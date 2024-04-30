package com.alfheim.aflheim_community.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private String address; // Optional
    private String number; // Optional
    private String occupation; // Optional

    private String gender; // [Male = 0, Female = 1, Not_Specified = 2]
    private String role; // [Visitor = 0, Member = 1, Admin = 3]
    private String state; // [CONFIRMED = 0, NOT_CONFIRMED = 1, SUSPENDED = 2, BANNED = 3, BLACKLISTED = 4]

}
