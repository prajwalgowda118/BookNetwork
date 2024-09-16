package com.scaler.booknetwork.booknetwork.Config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@RequiredArgsConstructor
public class FileUtils {

    public static byte[] readFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            log.warn("File URL is empty or null");
            return null;
        }
        try {
            Path path = new File(fileUrl).toPath();
            log.info("Reading file from path: {}", path);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            log.warn("Failed to read file at {}: {}", fileUrl, e.getMessage());
        }
        return null;
    }

}
