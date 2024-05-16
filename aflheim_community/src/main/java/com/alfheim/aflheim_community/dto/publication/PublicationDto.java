package com.alfheim.aflheim_community.dto.publication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@AllArgsConstructor
@Data
public record PublicationDto(Long id,
                             String title,
                             String content,
                             String categories,
                             String description,
                             String coverImageUrl,
                             String authorUsername,
                             String authorRole,
                             String authorEmail,
                             String authorProfilePictureUrl,
                             Integer authorReputation,
                             LocalDateTime createdAt,
                             LocalDateTime updatedAt,
                             Integer upVotesCount,
                             Integer downVotesCount,
                             List<String> upVoteUsernames,
                             List<String> downVoteUsernames) {
}
