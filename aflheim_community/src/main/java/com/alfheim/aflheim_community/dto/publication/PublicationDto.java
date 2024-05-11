package com.alfheim.aflheim_community.dto.publication;

import com.alfheim.aflheim_community.model.publication.Publication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicationDto {


    private Long id;
    private String title;
    private String content;
    private String categories;
    private String description;
    private String coverImageurl;

    public static PublicationDto from(Publication publication) {

        String coverImageUrl = "";

        if (publication.getCoverImage() != null) {
            coverImageUrl += "http://localhost:8080/files/" + publication.getCoverImage().getFileStorageName();
        } else {
            coverImageUrl = "/assets/img/default-avatar.jpg";
        }

        return PublicationDto.builder()
                .id(publication.getId())
                .title(publication.getTitle())
                .content(publication.getContent())
                .categories(publication.getCategories())
                .description(publication.getDescription())
                .coverImageurl(coverImageUrl)
                .build();
    }

    public static List<PublicationDto> publicationsListFrom(List<Publication> publications) {
        return publications.stream()
                .map(PublicationDto::from)
                .collect(Collectors.toList());
    }
}
