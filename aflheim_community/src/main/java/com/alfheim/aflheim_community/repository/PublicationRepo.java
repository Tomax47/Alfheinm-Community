package com.alfheim.aflheim_community.repository;

import com.alfheim.aflheim_community.model.publication.Publication;
import com.alfheim.aflheim_community.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PublicationRepo extends JpaRepository<Publication, Long> {

    Optional<Publication> findById(Long id);

    // Search based on description
    @Query("SELECT publications FROM Publication publications WHERE ((:query = 'empty' OR UPPER(publications.title) LIKE UPPER(CONCAT('%', :query, '%') ) ) OR (:query = 'empty' OR UPPER(publications.description) LIKE UPPER(CONCAT('%', :query, '%') ) ))")
    Page<Publication> search(@Param("query") String query, Pageable pageable);

    // Search by category
    @Query("SELECT publications FROM Publication publications WHERE UPPER(publications.categories) LIKE UPPER(:category)")
    Page<Publication> searchByCategory(@Param("category") String category, Pageable pageable);
}
