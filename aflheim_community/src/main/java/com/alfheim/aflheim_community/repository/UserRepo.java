package com.alfheim.aflheim_community.repository;

import com.alfheim.aflheim_community.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    @Query("SELECT users FROM account users WHERE (:query = 'empty' OR UPPER(users.username) LIKE UPPER(CONCAT('%', :query, '%') ) )")
    Page<User> search(@Param("query") String query, Pageable pageable);
}
