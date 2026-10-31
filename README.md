# careround-patient-service

Patients and their next-of-kin. HTTP 8084, gRPC 9090, DB `careround_patient_db`.

- **REST:** `/api/v1/patients` (create / status: DOCTOR or ADMIN; reads: any authenticated user),
  `/api/v1/patients/{patientPublicId}/next-of-kin`.
- **Admission verifies the ward** with ward-service `WardExists`: unknown ward → 404, ward-service unreachable → 503
  (fails closed).
- **gRPC server (9090):** `PatientCensusService` — `GetAdmittedCount`, `PatientExists` (also returns the patient's
  `ward_id`), `GetPrimaryNextOfKin` (notification-service).

## Environment variables

Copy `.env.example` to `.env` (gitignored) and fill in real values. `main()` loads `.env` into system properties;
plain environment variables work too.

| Variable | Purpose |
|---|---|
| `DB_USERNAME` | Postgres user (root docker-compose: careround-postgres) |
| `DB_PASSWORD` | Postgres password |
| `JWT_PUBLIC_KEY` | RSA public key used to verify access tokens; same value in every service. PEM on one line, `|` for newlines |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka (root docker-compose: careround-kafka) |

## Run

Needs the root `docker-compose.yml` stack (Postgres, Kafka, Redis) and a `.env`.

```bash
./mvnw clean package -DskipTests && java -jar target/careround-patient-service-0.0.1-SNAPSHOT.jar
```

Clients reach the REST API through the gateway on `http://localhost:8080`; the service port also works directly.
