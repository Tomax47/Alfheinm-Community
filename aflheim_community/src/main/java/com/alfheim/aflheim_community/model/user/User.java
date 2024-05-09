package com.alfheim.aflheim_community.model.user;

import com.alfheim.aflheim_community.model.File.FileInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity(name = "account")
public class User {
    // This entity's error code starts with 1

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 15)
    private String username; // Mandatory & Unique
    @Column(unique = true, nullable = false, length = 50)
    private String email; // Mandatory & Unique
    @Column(nullable = false)
    private String password; // Mandatory & at least 8-char long
    @Column(nullable = true)
    private String name; // Optional
    @Column(nullable = true)
    private String surname; // Optional
    @Column(nullable = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate; // Optional
    @Column(nullable = true, length = 50)
    private String address; // Optional
    @Column(nullable = true, length = 15)
    private String number; // Optional
    @Column(nullable = true, length = 50)
    private String occupation; // Optional
    @Column(nullable = true, length = 50)
    private String country; // Optional
    @Column(nullable = true, length = 50)
    private String city; // Optional
    @Column(nullable = true, length = 50)
    private String region; // Optional
    @Column(nullable = true, length = 10)
    private String zip; // Optional
    @Column(nullable = false)
    private int reputation;

    @Column(nullable = false)
    private String gender; // [Male = 0, Female = 1, Not_Specified = 2]
    @Column(nullable = false)
    private String role; // [Visitor = 0, Member = 1, Admin = 3]
    @Column(nullable = false)
    private String state; // [CONFIRMED = 0, NOT_CONFIRMED = 1, SUSPENDED = 2, BANNED = 3, BLACKLISTED = 4]

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_picture_id", referencedColumnName = "id")
    private FileInfo profilePicture;
}
