# Context-Aware Personal Assistant

A full-stack task and reminder management app with Google OAuth2 login,
JWT-based authentication, and Kafka-driven push notifications.

## Tech Stack
- **Backend:** Spring Boot 4.1.1, PostgreSQL, Redis, Apache Kafka, Firebase Cloud Messaging
- **Frontend:** React (Vite), Axios, React Router
- **Auth:** Google OAuth2 + JWT

## Architecture

React (Login) → Google OAuth2 → Backend issues JWT → React stores JWT
→ every API call attaches JWT → Spring Security validates → Postgres


Reminders: `ReminderScheduler` (runs every 60s) → Kafka `reminder.due` topic
→ `ReminderEventConsumer` → Firebase Cloud Messaging → device push notification

## Running Locally

### Prerequisites
Docker Desktop, Node.js, JDK 21

### Backend
```bash
cd backend
docker compose up -d
# Set environment variables: GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET, JWT_SECRET
# Run BackendApplication via IntelliJ or: ./mvnw spring-boot:run
```

### Frontend
```bash
cd web
npm install
npm run dev
```
Visit `http://localhost:5173`