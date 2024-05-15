package com.alfheim.aflheim_community.model.publication;

import com.alfheim.aflheim_community.model.File.FileInfo;
import com.alfheim.aflheim_community.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // Search related part
    @Column(nullable = false)
    private String categories;
    @Column(nullable = false)
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cover_image_id", referencedColumnName = "id")
    private FileInfo coverImage;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @CreationTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinTable(name = "publication_author")
    private User author;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "publication_up_votes",
            joinColumns = @JoinColumn(name = "publication_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> upVotes;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "publication_down_votes",
            joinColumns = @JoinColumn(name = "publication_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> downVotes;

    private int totalUpVotes;
    private int totalDownVotes;
}
