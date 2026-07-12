package com.ssharma.docmind;

import com.ssharma.docmind.config.OllamaProperties;
import com.ssharma.docmind.config.RagProperties;
import com.ssharma.docmind.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({
        OllamaProperties.class,
        RagProperties.class,
        StorageProperties.class
})
@SpringBootApplication
public class DocmindApplication {
    static void main(String[] args) {
        SpringApplication.run(DocmindApplication.class, args);
    }

}
