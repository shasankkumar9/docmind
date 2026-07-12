@echo off

echo =====================================
echo DocMind AI Setup
echo =====================================

echo.
echo Starting PostgreSQL...
docker compose up -d

echo.
echo Pulling Ollama Chat Model...
ollama pull qwen3:1.7b

echo.
echo Pulling Ollama Embedding Model...
ollama pull nomic-embed-text

echo.
echo =====================================
echo Setup Complete!
echo =====================================
echo.
echo Now start Spring Boot:
echo.
echo mvnw spring-boot:run
echo.
pause
