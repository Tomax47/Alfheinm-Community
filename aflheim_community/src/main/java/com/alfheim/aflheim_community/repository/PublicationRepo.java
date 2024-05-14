package com.alfheim.aflheim_community.repository;

import com.alfheim.aflheim_community.model.publication.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublicationRepo extends JpaRepository<Publication, Long> {

    Optional<Publication> findById(Long id);
}
