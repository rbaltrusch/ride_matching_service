# Example Ride Service

This is a simple example implementation of a ride hailing service that allows riders to request and complete rides, being matched to the closest driver.

## Running the application

Run the application using:

```
mvn spring-boot:run
```

This runs the web application locally on port `http://localhost:8080`.

### Tests

Run the tests using:

```
mvn test
```

## List of supported endpoints

To get a full, up-to-date list of endpoints and try them out, run the application and visit the Swagger-UI page on `http://localhost:8080/swagger-ui.html`.

Supported endpoints:
- Request a ride: `POST /api/riders/{id}`, accepts a JSON location as request body (`{latitude: number, longitude: number}`) and returns ride info.
- Complete a ride: `PATCH /api/riders{id}/rides/{rideId}`.
- Fetch all available drivers: `POST /api/drivers?amount`, accepts a JSON location as request body (`{latitude: number, longitude: number}`) and returns list of available drivers, sorted by closeness.
- Register driver as available: `PATCH /api/drivers/{id}/register-available`.
