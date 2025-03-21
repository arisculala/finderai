# FinderAI

FinderAI is an AI-powered application that allows users to generate and store text embeddings using various AI providers such as OpenAI, DeepSeek, and HuggingFace. The application enables vector-based search to find the closest matching text data stored in a PostgreSQL database.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Setup & Installation](#setupinstallation)
- [Running the Application](#runningtheapplication)
- [API Documentation](#apidocumentation)
- [Future Enhancements](#futureenhancements)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Features

- Multi-AI Provider Support
  - FinderAI integrates with multiple AI models for generating text embeddings:
    - [OpenAI](https://platform.openai.com/ "OpenAI"), [DeepSeek](https://deepseek.com/ "Deepseek"), [HuggingFace](https://huggingface.co/ "HuggingFace")
- Text Embedding Generation
  - Convert text into numerical embeddings using AI models.
- Store Embeddings in PostgreSQL
  - Save generated embeddings along with metadata in PostgreSQL with pgvector.
- Vector Search for Similar Text
  - Perform a vector-based search to find similar text stored in the database.
- Pluggable AI Provider System
  - Easily switch between different AI providers without modifying the core logic.

## Prerequisites

Make sure you have the following installed:

- Java 21+
- Maven
- Docker
- PostgreSQL

## Setup & Installation

**1. Clone the Repository**

```bash
git clone https://github.com/arisculala/finderai.git
cd finderai
```

**2. Setup the Database**

- Ensure PostgreSQL is installed and create a database:

```bash
CREATE DATABASE finderai_db;
```

- Enable `pgvector` extension:

```bash
CREATE EXTENSION IF NOT EXISTS vector;
```

- Run the schema migration (TODO: integration to liquibase)

**3. Configure Environment Variables**
Set up `appliction.yml`: (update configuration)

```bash
cd finderai/src/main/resources (or directory location)
cp example.application.yml application.yml
```

## Running the Application

- Option 1: Run Locally

```bash
cd finderai
mvn clean install
mvn spring-boot:run
```

- Option 2: Run with Docker
  docker build -t finderai .
  docker run -p 3000:3000 finderai

## API Documentation

### 📌 Vector API

- **_/api/v1/vectors_** (Store a new vector embedding)

  - Request: `POST /api/v1/vectors`

  ```bash
  {
    "provider": "huggingface",
    "model": "test",
    "text": "Cool is summer cool",
    "metadata": {
      "author": "John Doe 1",
      "category": "Technology",
      "timestamp": "2025-03-19T12:00:00Z"
    }
  }
  ```

  - Response:

  ```bash
  {
    "id": 293658638168363008,
    "provider": "huggingface",
    "model": "test",
    "text": "Cool is summer cool",
    "embedding": [
      -0.054827493,
      -0.025799233,
      0.037845585,
      0.03024301,
      0.012639361,
      -0.077821776,
      0.11216135
    ],
    "metadata": {
        "author": "John Doe 1",
        "category": "Technology",
        "timestamp": "2025-03-19T12:00:00Z"
    },
    "createdAt": "2025-03-21T16:14:35.253674"
  }
  ```

- **_/api/v1/vectors/search_** (Search for closest vector embeddings)
  - Request: `GET /api/v1/vectors/search`
  ```bash
  {
    "provider": "huggingface",
    "model": "test",
    "query": "Cool summer",
    "limit": 10
  }
  ```
  - Response:
  ```bash
  [
    {
      "id": 293658638168363008,
      "provider": "huggingface",
      "model": "test",
      "text": "Cool is summer cool",
      "embedding": [
          -0.054827493,
          -0.025799233,
          0.037845585,
          0.03024301,
          0.012639361,
          -0.077821776,
          0.11216135
      ],
      "metadata": {
          "author": "John Doe 1",
          "category": "Technology",
          "timestamp": "2025-03-19T12:00:00Z"
      },
      "createdAt": "2025-03-21T16:14:35.253674"
    }
  ]
  ```

### 📌 EMBEDDING API

- **_/api/v1/vectors_** (Store a new vector embedding)
  - Request: `POST /api/v1/embeddings`
  ```bash
  {
    "provider": "huggingface",
    "model": "test",
    "text": "hello my old friend"
  }
  ```
  - Response:
  ```bash
  {
    "provider": "huggingface",
    "model": "test",
    "embedding": [
        -0.06657371,
        0.05152601,
        0.03301843,
        0.028689047,
        -0.042398233,
        -0.08200422,
        0.062944174
    ],
    "dimensions": 1536
  }
  ```

## 🏗 Future Enhancements

- Integration with Different Data Sources
  - Import data from Excel, databases, APIs, OCR, and other sources.
- More AI Models & Providers
  - Expand to additional AI services for embeddings.
- Advanced Analytics & Visualization
  - Monitor and analyze search trends with dashboards.

## Contributing

To contribute:

- Fork the Repository
- Crete a feature branch: `git checkout -b feat:new-feature`
- Commit your changes: `git commit -m 'added new feature'`
- Push to the branch: `git push origin feat/new-feature`
- Create a pull Request

## 📄 License

This project is licensed under the **MIT License**.

## Contact

- 📧 Email: arisculala@gmail.com
- 🐙 GitHub: [arisculala](https://github.com/arisculala "Visit MyGithub")
- Enjoy using **FinderAI**! 🚀 If you have any questions, feel free to reach out. 😊
