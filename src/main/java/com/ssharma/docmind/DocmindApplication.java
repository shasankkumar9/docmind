package com.ssharma.docmind;

import com.ssharma.docmind.common.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		StorageProperties.class
})
public class DocmindApplication {

	static void main(String[] args) {
		SpringApplication.run(DocmindApplication.class, args);
	}

}