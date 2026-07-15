# API

## Upload

POST /api/documents

Request

multipart/form-data

Response

```json
{
  "id": "...",
  "originalFileName": "resume.pdf"
}
```

---

## Chat

POST /api/chat

```json
{
  "documentId": "...",
  "message": "Where do I work?"
}
```

Response

```json
{
  "response": "...",
  "sources": []
}
```