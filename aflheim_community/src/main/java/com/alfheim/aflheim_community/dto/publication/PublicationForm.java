package com.alfheim.aflheim_community.dto.publication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.mail.Multipart;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicationForm {

    private String title;
    private String content;
    private String categories;
    private String description;
    private Multipart coverImage;
}
