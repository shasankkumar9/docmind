package com.ssharma.docmind.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()

                .info(apiInfo())

                .externalDocs(externalDocumentation());

    }

    private Info apiInfo() {

        return new Info()

                .title("DocMind AI API")

                .description("""
                        AI-powered document question answering platform.
                        
                        Features:
                        • Multi-format document upload (TXT, PDF, DOCX, XLSX)
                        • Sentence-aware chunking
                        • Semantic search using pgvector
                        • Local LLM inference with Ollama
                        • AI-powered question answering
                        • Source citations with similarity scores
                        """)

                .version("1.0.0")

                .contact(contact())

                .license(license());

    }

    private Contact contact() {

        return new Contact()

                .name("Shasank Kumar Sharma")

                .email("shasank.kumar@example.com")

                .url("https://github.com/shasankkumar9");

    }

    private License license() {

        return new License()

                .name("MIT")

                .url("https://opensource.org/licenses/MIT");

    }

    private ExternalDocumentation externalDocumentation() {

        return new ExternalDocumentation()

                .description("Project Repository")

                .url("https://github.com/shasankkumar9/docmind");

    }

}