package br.com.judev.backend.feature.storage.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class StorageService {

    private final Path rootLocation = Path.of("uploads");

    public StorageService(){
        if(!rootLocation.toFile().exists()){
            rootLocation.toFile().mkdir();
        }
    }

    public String saveImage(MultipartFile file) throws IOException {
        if (!isImage(file.getContentType())) {
            throw new IllegalArgumentException("File is not an image");
        }

        if (isFileTooLarge(file)) {
            throw new IllegalArgumentException("File is too large");
        }

        String fileExtension = getFileExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + fileExtension;
        Files.copy(file.getInputStream(), this.rootLocation.resolve(fileName));
        return fileName;
    }

    private boolean isFileTooLarge(MultipartFile file) {
    return true;
    }
}
