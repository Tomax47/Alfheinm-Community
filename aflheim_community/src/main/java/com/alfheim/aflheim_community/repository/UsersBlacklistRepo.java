package com.alfheim.aflheim_community.repository;

import com.alfheim.aflheim_community.model.user.BlacklistRecordState;
import com.alfheim.aflheim_community.model.user.UsersBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UsersBlacklistRepo extends JpaRepository<UsersBlacklist, Long> {

    Optional<UsersBlacklist> findByUsernameAndState(String username, BlacklistRecordState state);

    Optional<UsersBlacklist> findById(Long reportId);
}
