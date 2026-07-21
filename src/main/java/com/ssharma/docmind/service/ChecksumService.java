package com.ssharma.docmind.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class ChecksumService {

    public String calculate(MultipartFile file) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            try (InputStream input =
                         file.getInputStream()) {

                byte[] buffer = new byte[8192];

                int read;

                while ((read = input.read(buffer)) != -1) {

                    digest.update(buffer, 0, read);

                }

            }

            byte[] hash = digest.digest();

            StringBuilder builder =
                    new StringBuilder();

            for (byte b : hash) {

                builder.append(
                        String.format("%02x", b)
                );

            }

            return builder.toString();

        } catch (IOException | NoSuchAlgorithmException ex) {

            throw new IllegalStateException(
                    "Unable to calculate checksum.",
                    ex
            );

        }

    }

}