package com.ssharma.docmind.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.storage")
public record StorageProperties(
        String directory,
        boolean persistFiles
) {
}
