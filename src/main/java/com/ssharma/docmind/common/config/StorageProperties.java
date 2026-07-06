package com.ssharma.docmind.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.storage")
public class StorageProperties {

    /**
     * Root folder where uploaded documents are stored.
     */
    private String root;

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }
}