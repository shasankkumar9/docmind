package com.ssharma.docmind.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.rag")
public class RagProperties {

    private int topK;

    private double similarityThreshold;

    private int chunkSize;

    private int chunkOverlap;

    private boolean queryExpansionEnabled;

    public boolean isQueryExpansionEnabled() {
        return queryExpansionEnabled;
    }

    public void setQueryExpansionEnabled(boolean queryExpansionEnabled) {
        this.queryExpansionEnabled = queryExpansionEnabled;
    }

    public int getTopK() {
        return topK;
    }

    public void setTopK(int topK) {
        this.topK = topK;
    }

    public double getSimilarityThreshold() {
        return similarityThreshold;
    }

    public void setSimilarityThreshold(double similarityThreshold) {
        this.similarityThreshold = similarityThreshold;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public int getChunkOverlap() {
        return chunkOverlap;
    }

    public void setChunkOverlap(int chunkOverlap) {
        this.chunkOverlap = chunkOverlap;
    }
}
