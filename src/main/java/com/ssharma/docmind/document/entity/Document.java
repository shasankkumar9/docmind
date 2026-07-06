package com.ssharma.docmind.document.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "documents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    private UUID id;

    @Column(nullable = false, length = 500)
    private String originalFileName;

    @Column(nullable = false, length = 500)
    private String storedFileName;

    @Column(nullable = false)
    private String mimeType;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false, length = 1000)
    private String storagePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus status;

    @Column(nullable = false)
    private Instant uploadedAt;

    private Instant processedAt;

}