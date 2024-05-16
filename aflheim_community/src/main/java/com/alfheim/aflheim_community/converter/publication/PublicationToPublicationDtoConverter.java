package com.alfheim.aflheim_community.converter.publication;

import com.alfheim.aflheim_community.dto.publication.PublicationDto;
import com.alfheim.aflheim_community.model.publication.Publication;
import com.alfheim.aflheim_community.model.user.User;
import lombok.Data;
import lombok.Setter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PublicationToPublicationDtoConverter implements Converter<Publication, PublicationDto> {

    @Override
    public PublicationDto convert(Publication publication) {
        String coverImageUrl = "";
        String authorProfilePic = "";

        if (publication.getCoverImage() != null) {
            coverImageUrl += "http://localhost:8080/files/" + publication.getCoverImage().getFileStorageName();
        } else {
            coverImageUrl = "/assets/img/default-publication-image.webp";
        }

        if (publication.getAuthor().getProfilePicture() != null) {
            authorProfilePic += "http://localhost:8080/files/" + publication.getAuthor().getProfilePicture().getFileStorageName();
        } else {
            authorProfilePic = "/assets/img/default-avatar.jpg";
        }

        Integer upVotesCount = 0;
        Integer downVotesCount = 0;
        List<String> upVoteUsernames = new ArrayList<>();
        List<String> downVoteUsernames = new ArrayList<>();


        if (publication.getUpVotes() != null) {
            for (User user : publication.getUpVotes()) {
                upVoteUsernames.add(user.getUsername());
            }

            upVotesCount = upVoteUsernames.size();
        }

        if (publication.getDownVotes() != null) {
            for (User user : publication.getDownVotes()) {
                downVoteUsernames.add(user.getUsername());
            }

            downVotesCount = downVoteUsernames.size();
        }

        // Returning the object
        return PublicationDto.builder()
                .id(publication.getId())
                .title(publication.getTitle())
                .content(publication.getContent())
                .categories(publication.getCategories())
                .description(publication.getDescription())
                .coverImageUrl(coverImageUrl)
                .authorUsername(publication.getAuthor().getUsername())
                .authorRole(publication.getAuthor().getRole())
                .authorReputation(publication.getAuthor().getReputation())
                .authorEmail(publication.getAuthor().getEmail())
                .authorProfilePictureUrl(authorProfilePic)
                .createdAt(publication.getCreatedAt())
                .updatedAt(publication.getUpdatedAt())
                .upVotesCount(upVotesCount)
                .downVotesCount(downVotesCount)
                .downVoteUsernames(downVoteUsernames)
                .upVoteUsernames(upVoteUsernames)
                .build();
    }
}
