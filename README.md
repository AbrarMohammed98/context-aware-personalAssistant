# Context-Aware Personal Assistant — Backend

A context-aware personal assistant backend built with Spring Boot, focused on secure, event-driven task and reminder management with real-time push notifications.

> This is a project built to learn enterprise-grade backend patterns (auth, event streaming, notification delivery) hands-on, not just to ship a demo.

---

## What's Been Built So Far

### Core CRUD & Data Layer
- Full **Entity → Repository → Service → Controller** pattern implemented and tested for `Task` and `Reminder`
- `@ManyToOne` foreign key relationship between `Reminder` and `Task`
- PostgreSQL as the persistence layer

### Authentication & Security
- **Google OAuth2 login** via `spring-security-oauth2-client`, with a custom `OAuth2LoginSuccessHandler` that finds or creates a `User` record on first login
- **JWT-based session auth** using `jjwt` 0.12.6 (`jjwt-api`, `jjwt-impl`, `jjwt-jackson`)
- `JwtAuthFilter` validates the token on every request and populates `SecurityContextHolder`
- Session policy set to `STATELESS` — no server-side session state
- **User-scoped data access**: `SecurityUtil.getCurrentUserId()` + ownership checks in the service layer prevent IDOR (users can't read/modify each other's tasks or reminders)

### Event-Driven Reminders (Kafka)
- `ReminderScheduler` (`@Scheduled(fixedRate = 60000)`) polls for due reminders every 60 seconds
- Due reminders are published as a `ReminderDueEvent` to the `reminder.due` Kafka topic via `ReminderEventProducer`
- Manual `KafkaConfig` bean
- Uses `JacksonJsonSerializer`


- Kafka **consumer** for `reminder.due` (manual consumer factory beans)
- `DeviceToken` entity + registration endpoint
- Firebase Admin SDK integration (`FcmService`) for push delivery
- `GlobalExceptionHandler` (`@RestControllerAdvice`)

### Deferred / Stretch Goals
- Redis (caching)
- Prometheus (monitoring)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language / Framework | Java, Spring Boot 4.1.1 |
| Database | PostgreSQL |
| Messaging | Apache Kafka |
| Auth | Spring Security, OAuth2 (Google), JWT (`jjwt` 0.12.6) |
| Push Notifications | Firebase Cloud Messaging *(planned)* |
| API Docs | Swagger |
| Testing | JUnit, Mockito |
| Containerization | Docker |
| Web Frontend | React (PWA, plain JavaScript/JSX — no TypeScript) |
| Mobile Frontend | React Native (FCM background notifications) |
| IDEs | IntelliJ Community Edition (backend), VS Code (frontend) |

**Explicitly evaluated and dropped/deferred:** MongoDB, RabbitMQ, Kubernetes, Electron, New Relic, Aerospike, TypeScript — assessed as unjustifiable overhead for this project's scope.

---

## Architecture Flow

End-to-end path for a reminder notification, from due-time detection to device delivery:

```
┌─────────────────┐
│ ReminderScheduler│  @Scheduled(fixedRate = 60000)
│  (polls DB every │  Finds reminders that are now due
│    60 seconds)   │
└────────┬─────────┘
         │ builds ReminderDueEvent
         ▼
┌─────────────────┐
│ ReminderEvent    │  Serializes event with JacksonJsonSerializer
│   Producer       │  Publishes to Kafka
└────────┬─────────┘
         │ topic: reminder.due
         ▼
┌─────────────────┐
│  Kafka Broker    │  Decouples detection from delivery;
│ (topic:reminder. │  durable, replayable event log
│      due)        │
└────────┬─────────┘
         │
         ▼
┌─────────────────┐
│  Kafka Consumer  │ 
│  (reminder.due)  │  Consumes event, looks up user's
│                  │  registered DeviceToken(s)
└────────┬─────────┘
         │
         ▼
┌─────────────────┐
│   FcmService     │  
│ (Firebase Admin  │  Sends push notification payload
│      SDK)        │  to the user's device(s)
└────────┬─────────┘
         │
         ▼
┌─────────────────┐
│  User's Device   │  Web (PWA) or Mobile (React Native)
│ (Web / Mobile)   │  receives and displays notification
└──────────────────┘
```

**Why this shape:** the scheduler only decides *what's due*; it never talks to notification infra directly. Kafka sits in between so the "detect due reminder" and "deliver notification" concerns are decoupled — the consumer/FCM side can be scaled, restarted, or reworked independently, and events aren't lost if the notification layer is temporarily down.

---

## Setup Instructions

### Prerequisites
- Java (JDK compatible with Spring Boot 4.1.1)
- Maven
- Docker & Docker Compose

### 1. Start Infrastructure (PostgreSQL + Kafka)

```bash
docker compose up -d
```

This should bring up:
- PostgreSQL (database for `User`, `Task`, `Reminder`, etc.)
- Kafka + Zookeeper (or KRaft-mode broker) for the `reminder.due` topic

> If you don't yet have a `docker-compose.yml` in `backend/`, add services for `postgres` and `kafka` before running this command.

### 2. Set Environment Variables

The app reads sensitive config via `${...}` placeholders in `application.yaml`, sourced from environment variables — **never commit these values**.

| Variable | Purpose |
|---|---|
| `GOOGLE_CLIENT_ID` | OAuth2 client ID from Google Cloud Console |
| `GOOGLE_CLIENT_SECRET` | OAuth2 client secret from Google Cloud Console |
| `JWT_SECRET` | Secret key used to sign/verify JWTs |

On Windows, these are set as **System Environment Variables** (Control Panel → System → Advanced → Environment Variables), so IntelliJ and any terminal session pick them up automatically.

On macOS/Linux, export them in your shell profile or set them per-run:

```bash
export GOOGLE_CLIENT_ID=your_client_id
export GOOGLE_CLIENT_SECRET=your_client_secret
export JWT_SECRET=your_jwt_secret
```

### 3. Timezone Configuration

The JVM must not default to a local timezone (e.g. `Asia/Calcutta`) that PostgreSQL won't recognize. Set explicitly:

- **IntelliJ Run Config → VM options:** `-Duser.timezone=UTC`
- **Maven Surefire (for tests), in `pom.xml`:**
  ```xml
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
      <argLine>-Duser.timezone=UTC</argLine>
    </configuration>
  </plugin>
  ```

### 4. Run the Backend

```bash
cd backend
mvn spring-boot:run
```

### 5. Verify

- App should start without Kafka connection errors (check `KafkaConfig` bean wiring if it fails)
- Hit the Google OAuth2 login flow to confirm a `User` row is created in PostgreSQL
- Check Swagger UI (typically `/swagger-ui.html` or `/swagger-ui/index.html`) for available endpoints

---



## Repository Structure

```
context-aware-personalAssistant/
├── backend/    # Spring Boot API (this README's scope)
├── web/        # React PWA frontend
└── mobile/     # React Native mobile app
```