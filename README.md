# Smart Parking Management REST API

A Spring Boot 3 REST backend using Java 17, MySQL, Spring Data JPA, Jakarta Validation, OpenAPI/Swagger UI, and JUnit 5.

## Features
- Available, occupied, and all parking slots
- Allocate a selected slot or automatically allocate the first available slot
- Active booking list and booking details
- Live fare estimate and checkout
- Flat billing: ₹50 per 180 minutes or fraction thereof
- Daily revenue by checkout date
- Validation and consistent JSON error responses (400, 404, 409)
- Transactional booking/checkout operations and pessimistic slot locking
- Schema SQL, Postman collection, and fee-engine tests

## Requirements
- JDK 17+
- Maven 3.8+
- MySQL 8+
- VS Code with Extension Pack for Java (recommended)

## Setup
1. Start MySQL and create the database (optional because the JDBC URL uses `createDatabaseIfNotExist=true`):
   ```sql
   CREATE DATABASE smart_parking;
   ```
2. Configure credentials. Defaults are `root` / `root`; override them with environment variables when needed:
   - `DB_URL` (default `jdbc:mysql://localhost:3306/smart_parking?...`)
   - `DB_USERNAME`
   - `DB_PASSWORD`
3. Open this folder in VS Code.
4. In the terminal run:
   ```bash
   mvn clean test
   mvn spring-boot:run
   ```
5. Swagger UI: http://localhost:8080/swagger-ui/index.html
   OpenAPI JSON: http://localhost:8080/v3/api-docs

The app uses `schema.sql` to create tables. `spring.jpa.hibernate.ddl-auto=validate` checks entity/schema alignment. To add parking spaces, run SQL after startup:
```sql
INSERT INTO parking_slots (slot_code, occupied) VALUES
('A-01', false), ('A-02', false), ('A-03', false), ('B-01', false), ('B-02', false);
```

## API examples
### List available slots
`GET http://localhost:8080/api/slots/available`

### Book a slot
`POST http://localhost:8080/api/bookings/entry`
```json
{
  "vehicleNumber": "MH12AB1234",
  "slotId": 1
}
```
Omit `slotId` to auto-allocate the first available slot. `entryTime` is optional and defaults to server local time.

### List active bookings
`GET http://localhost:8080/api/bookings/active`

### Calculate current fare
`GET http://localhost:8080/api/bookings/1/fare`
Optionally pass `?at=2026-10-26T14:30:00` (ISO local date-time).

### Checkout
`POST http://localhost:8080/api/bookings/1/checkout`
```json
{ "exitTime": "2026-10-26T14:30:00" }
```
Omit the body or `exitTime` to use the current server time. Successful checkout marks payment `PAID` and releases the slot. This is a demo backend: payment is recorded as paid during checkout; no external payment gateway is integrated.

### Revenue for a date
`GET http://localhost:8080/api/revenue?date=2026-10-26`
Revenue is grouped by checkout/exit date.

## Fee rule
The fee is `₹50 × ceil(duration seconds / 10,800)`. A zero-length stay costs ₹0. Any positive stay up to and including 3 hours costs ₹50; 3 hours plus one second costs ₹100.

## Error format
```json
{
  "timestamp": "2026-10-26T12:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "Parking slot A-01 is already occupied",
  "path": "/api/bookings/entry",
  "validationErrors": {}
}
```

## Notes / assumptions
- Timestamps use server-local `LocalDateTime`; configure the server/DB timezone consistently for deployment.
- Revenue is the sum of completed bookings whose exit timestamp falls on the requested date.
- Checkout marks the payment as `PAID` as a simulation; add a payment provider for real transactions.
- The request accepts common Indian registration patterns such as `MH12AB1234` and `MH 12 AB 1234`.
- This project intentionally has no authentication; add Spring Security before public deployment.
