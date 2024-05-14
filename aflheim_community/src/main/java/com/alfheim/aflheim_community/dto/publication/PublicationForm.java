package com.alfheim.aflheim_community.dto.publication;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PublicationForm {
    private String title;
    private String content;
    private String category;
    private String description;
    private MultipartFile coverImage;
}
