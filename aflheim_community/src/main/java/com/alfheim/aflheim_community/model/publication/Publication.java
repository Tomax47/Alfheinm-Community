package com.alfheim.aflheim_community.model.publication;

import com.alfheim.aflheim_community.model.File.FileInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    // Search related part
    @Column(nullable = false)
    private String categories;
    @Column(nullable = false)
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cover_image_id", referencedColumnName = "id")
    private FileInfo coverImage;

    // TODO : ADD THE LINK TO THE AUTHOR
}
