package com.alfheim.aflheim_community.repository;

import com.alfheim.aflheim_community.model.File.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileInfoRepo extends JpaRepository<FileInfo, Long> {

    FileInfo findByFileStorageName(String fileName);
}
