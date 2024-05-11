package com.alfheim.aflheim_community.repository;

import com.alfheim.aflheim_community.model.publication.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationRepo extends JpaRepository<Publication, Long> {
}
