package com.ssharma.docmind.document.storage;

import com.ssharma.docmind.common.config.StorageProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class LocalStorageService implements StorageService {

    private final Path root;

    public LocalStorageService(StorageProperties properties) {
        this.root = Path.of(properties.getRoot());
    }

    @PostConstruct
    public void initialize() throws IOException {
        Files.createDirectories(root);
    }

    @Override
    public Path store(MultipartFile file) throws IOException {

        LocalDate today = LocalDate.now();

        Path directory = root
                .resolve(String.valueOf(today.getYear()))
                .resolve(String.format("%02d", today.getMonthValue()));

        Files.createDirectories(directory);

        String extension = "";

        String original = file.getOriginalFilename();

        if (original != null && original.contains(".")) {
            extension = original.substring(original.lastIndexOf('.'));
        }

        String fileName = UUID.randomUUID() + extension;

        Path destination = directory.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                destination,
                StandardCopyOption.REPLACE_EXISTING
        );

        return destination;
    }

    @Override
    public Path load(String fileName) {
        return root.resolve(fileName);
    }

    @Override
    public void delete(String fileName) throws IOException {
        Files.deleteIfExists(load(fileName));
    }

    @Override
    public boolean exists(String fileName) {
        return Files.exists(load(fileName));
    }

}