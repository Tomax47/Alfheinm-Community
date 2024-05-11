package com.alfheim.aflheim_community.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class UsersBlacklist {
    // This entity's error code starts with 4

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String userEmail;
    @Column(nullable = false)
    private String reason;
    @Column(nullable = false)
    private int reputationPtsDeducted;
    @Column(nullable = false)
    private BlacklistRecordState state;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
