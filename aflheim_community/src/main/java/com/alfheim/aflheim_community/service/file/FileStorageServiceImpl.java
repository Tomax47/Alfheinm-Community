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
        FileInfo fileInfo = fileInfoRepo.findByFileStorageName(fileStorageName);

        /**
         This line sets the content type of the HTTP response to the type of the file being written.
         The content type is a MIME type that tells the client what kind of data is being sent in the response
         */
        response.setContentType(fileInfo.getType());
        try {
            /**
             In this line, we write the content of the HTTP response we are sending to the client!
             The IOUtils.copy() method, writes content from an input stream "which is the new FileInputStream" with the content of "file url"
             in our case, to the HTTP response outputStream!
             */
            IOUtils.copy(new FileInputStream(fileInfo.getUrl()),
                    response.getOutputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public FileInfo findByStorageName(String storageName) {
        return fileInfoRepo.findByFileStorageName(storageName);
    }

    @Override
    public FileInfo findById(Long id) {
        return fileInfoRepo.findById(id).get();
    }

    @Override
    public int deleteFile(String storageName) {
        try {
            System.out.println("\n\n DELETING THE FILE \n\n");
            FileInfo fileInfo = fileInfoRepo.findByFileStorageName(storageName);
            fileInfoRepo.delete(fileInfo);

            return 1;
        } catch (Exception e) {
            return 0;
        }
    }
}
