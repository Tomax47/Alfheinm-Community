package com.alfheim.aflheim_community.repository;

import com.alfheim.aflheim_community.model.File.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileInfoRepo extends JpaRepository<FileInfo, Long> {

    Optional<FileInfo> findByFileStorageName(String fileName);

}
