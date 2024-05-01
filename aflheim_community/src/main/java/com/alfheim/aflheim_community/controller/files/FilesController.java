package com.alfheim.aflheim_community.controller.files;

import com.alfheim.aflheim_community.service.file.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;

@Controller
public class FilesController {

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/files/{file-name:.+}")
    public void getFile(@PathVariable("file-name") String fileStorageName, HttpServletResponse response) {
        fileStorageService.writeFileToResponse(fileStorageName, response);
    }

}
