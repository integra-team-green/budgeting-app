# 💰 Budgeting App

A full-stack budgeting application built with Spring Boot and Angular.

---

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 24 (JDK)** - [Download & Install](https://www.oracle.com/java/technologies/downloads/)
- **Node.js** (v18+ recommended) - [Download & Install](https://nodejs.org/)
- **npm** (comes with Node.js)
- **Gradle** (optional, wrapper is included)
- **Git** - [Download & Install](https://git-scm.com/)
- **Docker** (or your chosen DB) - [Download & Install](https://www.docker.com/products/docker-desktop/)

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone git@github.com-personal:integra-team-green/budgeting-app.git
cd budgeting-app
```

---

## 🔧 Backend Setup (Spring Boot)

### Configure Database

Using Docker Compose (Recommended)

```bash
 docker-compose up -d
```

This will start a PostgreSQL container with the default configuration.

### Run Database Migrations

Flyway will run automatically on application startup if configured.

### Build and Run Backend

```bash
cd backend
./gradlew build
./gradlew bootRun
```

✅ The backend should now be running on **http://localhost:8080**

---

## 🎨 Frontend Setup (Angular)

```bash
cd ../frontend
npm install
npm start
```

✅ The Angular app should now be running on **http://localhost:4200**

---

## 📝 OpenAPI Code Generation

Since it is configured in `build.gradle`, running `./gradlew build` in the backend will:

- Generate the OpenAPI specification
- Create a TypeScript client for the frontend

**Generated files:**
- OpenAPI spec: `"build/generated/money-mind.yaml"` 
- TypeScript client: `"/frontend/src/app/api"` 

---

## 🌐 Access the Application

| Component | URL |
|-----------|-----|
| **Backend API** | http://localhost:8080/api/|
| **Frontend UI** | http://localhost:4200 |

---

## ⚠️ Common Issues

| Issue | Solution |
|-------|----------|
| **Port conflicts** | Ensure ports 8080 (backend) and 4200 (frontend) are available |
| **Database errors** | Verify PostgreSQL is running and credentials are correct |
| **Version issues** | Use the required Java and Node.js versions |

---

## 🛠️ Useful Commands

### Backend

```bash
# Run tests
./gradlew test

# Clean build
./gradlew clean build
```

### Frontend

```bash
# Run tests
npm test

# Build for production
npm run build
```

---

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Angular Documentation](https://angular.io/docs)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Flyway Documentation](https://flywaydb.org/documentation/)