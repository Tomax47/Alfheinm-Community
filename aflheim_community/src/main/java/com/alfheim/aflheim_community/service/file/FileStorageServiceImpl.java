package com.alfheim.aflheim_community.service.file;

import com.alfheim.aflheim_community.model.File.FileInfo;
import com.alfheim.aflheim_community.repository.FileInfoRepo;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileStorageServiceImpl implements FileStorageService {

    @Autowired
    private FileInfoRepo fileInfoRepo;

    @Value("${storage.path}")
    private String storagePath;

    @Override
    public String saveFile(MultipartFile file) {

        // Formulating the file's storage name
        String storageName = UUID.randomUUID().toString() + "." +
                FilenameUtils.getExtension(file.getOriginalFilename());

        //Building the FileInfo object
        FileInfo fileInfo = FileInfo.builder()
                .fileName(file.getOriginalFilename())
                .fileStorageName(storageName)
                .type(file.getContentType())
                .size(file.getSize())
                .url(storagePath + "\\" + storageName)
                .build();

        // Saving the file into the storage path directory
        try {
            Files.copy(file.getInputStream(), Paths.get(storagePath, storageName));

        } catch (IOException e) {

            throw new IllegalStateException(e);
        }

        // Saving the file into the DB
        fileInfoRepo.save(fileInfo);
        return fileInfo.getFileStorageName();
    }

    @Override
    public void writeFileToResponse(String fileStorageName, HttpServletResponse response) {
        FileInfo fileInfo = fileInfoRepo.findByFileStorageName(fileStorageName).get();

        response.setContentType(fileInfo.getType());
        try {

            IOUtils.copy(new FileInputStream(fileInfo.getUrl()),
                    response.getOutputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public FileInfo findByStorageName(String storageName) {
        return fileInfoRepo.findByFileStorageName(storageName).get();
    }

    @Override
    public FileInfo findById(Long id) {
        return fileInfoRepo.findById(id).get();
    }

    @Override
    public int deleteFile(String storageName) {
        try {

            // TODO :
            //  1- FIX THE ISSUE OF THE FILE NOT BEING DELETED FROM THE DB.
            //  2- FIX THE ISSUE OF "FILE IS BEING USED" THAT'S PREVENTING THE DELETION OF THE FILE FROM THE DIRECTORY IT'S SAVED IN.

            // Deleting the file from the DB
            FileInfo fileInfo = fileInfoRepo.findByFileStorageName(storageName).get();

            fileInfoRepo.delete(fileInfo);

            // Deleting the file from the dir path
            Files.delete(Path.of(fileInfo.getUrl()));
            return 1;

        } catch (Exception e) {
            return 0;
        }
    }
}
