package com.ssharma.docmind.document.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface StorageService {

    Path store(MultipartFile file) throws IOException;

    Path load(String fileName);

    void delete(String fileName) throws IOException;

    boolean exists(String fileName);

}