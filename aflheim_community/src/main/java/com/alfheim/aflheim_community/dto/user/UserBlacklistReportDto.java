package com.alfheim.aflheim_community.dto.user;

import com.alfheim.aflheim_community.model.user.BlacklistRecordState;
import com.alfheim.aflheim_community.model.user.UsersBlacklist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBlacklistReportDto {
    private Long id;
    private String username;
    private String userEmail;
    private String reason;
    private int reputationPtsDeducted;
    private BlacklistRecordState state;
    private LocalDateTime createdAt;

    public static UserBlacklistReportDto from(UsersBlacklist usersBlacklist) {
        return UserBlacklistReportDto.builder()
                .id(usersBlacklist.getId())
                .username(usersBlacklist.getUsername())
                .userEmail(usersBlacklist.getUserEmail())
                .reason(usersBlacklist.getReason())
                .state(usersBlacklist.getState())
                .reputationPtsDeducted(usersBlacklist.getReputationPtsDeducted())
                .createdAt(usersBlacklist.getCreatedAt())
                .build();
    }
}
