# Complex Mail Service

## Overview

This project consists of multiple microservices:

- **Core Service**: Processes messages and interacts with the database and RabbitMQ.
- **Answering Service**: Handles incoming requests and validates data.
- **SMTP Mail Service**: Sends emails based on processed messages.
- **PostgreSQL Database**: Stores messages, templates, and user data.

---

## Getting Started

### Prerequisites

- Docker & Docker Compose
- Java 17 or higher for Spring Boot

### Step 1: Clone the repository

```bash
git clone https://github.com/your-username/complex-mail-service.git
cd complex-mail-service
```

### Step 2: Build and Start Services

Run the following to build and start the services:

```bash
docker-compose up --build
```

This will start:
- **PostgreSQL** on port `5432`
- **RabbitMQ** on ports `5672` and `15672`
- **Core Service** on port `8082`
- **Answering Service** on port `8081`
- **SMTP Mail Service** on port `8083`

---

## Service Endpoints

### 1. **Core Service**

#### `POST /sent`

Creates a new message for processing.

### 2. **Answering Service**

#### `GET /status/{uniqueMessage}`

Fetches the status of a processed message.

### 3. **SMTP Mail Service**

#### `POST /send`

Sends an email to the users with the processed message.
