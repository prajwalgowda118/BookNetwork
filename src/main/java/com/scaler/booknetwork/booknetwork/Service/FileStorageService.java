package com.scaler.booknetwork.booknetwork.Service;


import com.scaler.booknetwork.booknetwork.Models.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.io.File.separator;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    private final String uploadPath="/Users/prajwalgowdakr/Desktop/fullstack/LLD_DP/BookNetworkE2E/BookNetwork";
    public String saveFile(MultipartFile sourceFile,
                           Book book,
                           Long id) {
        final String FileUploadSubPath= "users"+ separator + id;
        return uploadFile( sourceFile,FileUploadSubPath);
    }

    private String uploadFile(MultipartFile sourceFile, String FileUploadSubPath) {
        //String fileName = sourceFile.getOriginalFilename();
        final String finalUploadPath = uploadPath + separator + FileUploadSubPath;
        File newFile = new File(finalUploadPath);
        if(!newFile.exists()) {
            boolean newFolder =newFile.mkdirs();
            if(!newFolder) {
                System.out.println("Error creating folder");
                return null;
                }
         }
        final String fileExtension=getFileExtension(sourceFile.getOriginalFilename());
        String targetFilePath = finalUploadPath + separator + System.currentTimeMillis() +"."+ fileExtension;
        Path targetPath= Paths.get(targetFilePath);
        try{
            Files.write(targetPath,sourceFile.getBytes());
            log.info("file saved to "+targetFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;

    }
    private String getFileExtension(String originalFilename) {

        if(originalFilename==null || originalFilename.length()==0){
            return null;
        }
        int index = originalFilename.lastIndexOf(".");
        if(index<0){
            return null;
        }
        return originalFilename.substring(index);
    }

}
