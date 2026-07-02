
# Getting Started

## Prerequisites

Before running the application, make sure you have:

- Docker installed and running
- IntelliJ IDEA (Community or Ultimate)
- Java 25
- Maven

The application relies on a PostgreSQL database managed through Docker Compose. Follow the steps below to configure Docker, start the required containers, and run the application locally.
## Configure Environment Variables

Create a `.env` file at the root of the project and add the required environment variables.

## Configure Docker in IntelliJ

1. Open **Settings** → **Build, Execution, Deployment** → **Docker**
2. Click **+** and add a Docker connection:
    - Docker Desktop (Windows/Mac)
    - Unix Socket (`unix:///var/run/docker.sock`) for Linux
3. Click **Test Connection** to ensure Docker is accessible.


If you are using **Ubuntu/WSL**, ensure Docker is running:
```bash
docker ps
```

## Start Database Containers

From the project root:

```bash
docker compose up -d
```
## Stop project Containers

```bash
docker compose down
```

## Access PgAdmin

Open:

http://localhost:5000

Create a new PostgreSQL server using the values defined in your environment variables.
