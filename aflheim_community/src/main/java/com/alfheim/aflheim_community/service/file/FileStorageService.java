package com.alfheim.aflheim_community.service.file;

import com.alfheim.aflheim_community.model.File.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface FileStorageService {

    String saveFile(MultipartFile file);
    void writeFileToResponse(String fileStorageName, HttpServletResponse response);
    FileInfo findByStorageName(String storageName);
    FileInfo findById(Long id);

    int deleteFile(String storageName);
}
