## Create Database

- create finderai_db
- run below command to add pgvector externsion

```bash
CREATE EXTENSION IF NOT EXISTS vector;
```

## Run Application

```bash
mvn clean install

mvn spring-boot:run
```

## API Endpoints

| Method | Endpoint                            | Description               |
| ------ | ----------------------------------- | ------------------------- |
| `POST` | `/api/v1/vectors`                   | Store text with embedding |
| `GET`  | `/api/v1/vectors/search?query=text` | Search for similar text   |
