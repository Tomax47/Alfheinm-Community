package com.alfheim.aflheim_community.dto.publication;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@AllArgsConstructor
@Data
@Getter
@Setter
public class PublicationDto {
    private Long id;
    private String title;
    private String content;
    private String categories;
    private String description;
    private String coverImageUrl;
    private String authorUsername;
    private String authorRole;
    private String authorEmail;
    private String authorProfilePictureUrl;
    private Integer authorReputation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer upVotesCount;
    private Integer downVotesCount;
    private List<String> upVoteUsernames;
    private List<String> downVoteUsernames;
}
