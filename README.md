# Box Delivery Service (Spring Boot)

A small RESTful service for managing "boxes" that carry items to remote locations.

## Assumptions
- "Available for loading" means boxes in `IDLE` state with battery >= 25%.
- Loading flow: if battery >= 25%, service sets state to `LOADING`, validates total weight (`current items + new items <= weightLimit`), saves items, then sets state to `LOADED`.
- Weight unit is grams; `weightLimit <= 500` by business rule.
- `txref` is unique, <= 20 chars.
- Item name: `^[A-Za-z0-9_-]+$`; Item code: `^[A-Z0-9_]+$`.
- If battery < 25%, box cannot enter LOADING. Instead, it can be “charged” via an explicit charging endpoint until the battery is ≥ 25%.
- Charging is capped at 100%. Charging does not alter box state.

## Tech
- Java 17, Spring Boot 3.3.x, Spring Web, Spring Data JPA, Validation (Jakarta), H2 in-memory DB.
- Build tool: Maven.

## Run
```bash
# from the project root
./mvnw spring-boot:run            # if on Unix/macOS
# or
mvn spring-boot:run               # if Maven is installed
```

H2 console: `/h2-console` (JDBC URL: `jdbc:h2:mem:boxesdb`)

## Build
```bash
mvn clean package
java -jar target/box-delivery-0.0.1-SNAPSHOT.jar
```

## API
- **Create box**  
  `POST /api/boxes`  
  Body:
  ```json
  { "txref":"BX-001","weightLimit":300,"batteryCapacity":90 }
  ```

- **Load items**  
  `POST /api/boxes/{boxId}/load`  
  Body:
  ```json
  {
    "items":[
      {"name":"camera_mount","weight":100,"code":"MOUNT_01"},
      {"name":"med-pack_1","weight":50,"code":"MED_01"}
    ]
  }
  ```

- **List items in a box**  
  `GET /api/boxes/{boxId}/items`

- **Available boxes for loading**  
  `GET /api/boxes/available`

- **Check battery**  
  `GET /api/boxes/{boxId}/battery`

- **List all boxes**  
  `GET /api/boxes`

- **Charge box battery**
  `PUT /api/boxes/{boxId}/charge?level=80`

## Preloaded Data
The application starts with a few boxes in the database (see `data.sql`).

## Tests
Run all tests:
```bash
mvn test
```

## Notes
- Business rules enforced:
  - Cannot load above `weightLimit`.
  - Cannot be in `LOADING` state if battery < 25% (service rejects load attempt).

## Swagger link
- `http://localhost:8080/swagger-ui.html`
