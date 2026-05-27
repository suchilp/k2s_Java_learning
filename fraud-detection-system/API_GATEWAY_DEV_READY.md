# API Gateway - Dev Readiness Implementation

## Changes Made

### 1. **pom.xml - Enhanced Dependencies**
- ✅ Added **Resilience4j** (Circuit Breaker, Time Limiter)
- ✅ Added **Micrometer + Brave** for distributed tracing
- ✅ Added **Spring Boot Test** framework
- ✅ Added **Lombok** for annotation processing

### 2. **application.yml - Enhanced Configuration**
- ✅ Added **Circuit Breaker filters** for each service route with fallback support
- ✅ Added **Request ID tracking** (X-Request-ID header)
- ✅ Added **Resilience4j configuration** with sensible defaults:
  - Failure rate threshold: 50%
  - Sliding window: 20 requests
  - Timeout: 2 seconds per request
- ✅ Added **Logging configuration** with DEBUG level for Gateway
- ✅ Added **Management endpoints** (health, metrics, prometheus)
- ✅ Added **Distributed tracing** configuration

### 3. **application-docker.yml - Docker Profile**
- ✅ Service names instead of localhost (for Docker Compose compatibility)
- ✅ Same resilience & circuit breaker configs
- ✅ Profiles: `docker` (activated in docker-compose.yml)

### 4. **RequestIdGatewayFilterFactory.java - Request Tracking**
- ✅ Generates/propagates X-Request-ID across all requests
- ✅ Logs all incoming requests with tracking ID
- ✅ Custom gateway filter for tracing

### 5. **FallbackController.java - Graceful Degradation**
- ✅ Handles circuit breaker fallback scenarios
- ✅ Returns 503 SERVICE_UNAVAILABLE with timestamp & message
- ✅ Prevents cascading failures

### 6. **docker-compose.yml - Profile Activation**
- ✅ API Gateway now activates `docker` profile via `SPRING_PROFILES_ACTIVE`
- ✅ Uses service names for inter-container communication

---

## Local Development Setup

### Prerequisites
```bash
java -version              # Java 17+
mvn -version              # Maven 3.9+
docker --version          # Docker
docker-compose --version  # Docker Compose
```

### Build & Run Locally (Dev Mode)
```bash
cd api-gateway
mvn clean package
java -jar target/api-gateway-0.0.1-SNAPSHOT.jar
```
The app will use `application.yml` (localhost mode) by default.

### Test Endpoints
```bash
curl http://localhost:8080/auth/login
curl http://localhost:8080/transaction/list
curl http://localhost:8080/fraud/check
curl http://localhost:8080/health
```

### Run in Docker (Docker Mode)
```bash
cd ..  # From project root
docker-compose up --build
```
The API Gateway will use `application-docker.yml` (service names) automatically.

---

## Monitoring & Debugging

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Metrics
```bash
curl http://localhost:8080/actuator/metrics
curl http://localhost:8080/actuator/prometheus
```

### Circuit Breaker Status
```bash
curl http://localhost:8080/actuator/health | jq '.components.circuitBreakers'
```

### Logs
```bash
# Dev mode
tail -f logs/api-gateway.log

# Docker mode
docker-compose logs -f api-gateway
```

---

## Resilience4j Configuration Details

### Circuit Breaker States
- **CLOSED**: Normal operation ✅
- **OPEN**: Service failing, rejecting requests ❌
- **HALF_OPEN**: Testing if service recovered 🔄

### Current Settings
| Parameter | Value | Purpose |
|-----------|-------|---------|
| `failureRateThreshold` | 50% | Open CB if 50% of calls fail |
| `slidingWindowSize` | 20 | Sample last 20 calls |
| `wait-duration-in-open-state` | 10s | Wait 10s before trying again |
| `permitted-calls-half-open` | 3 | Allow 3 test calls in half-open |
| `timeout` | 2s | Kill slow requests |

### Fallback Behavior
When circuit opens, all requests → `FallbackController` → 503 response

---

## Next Steps (Recommended)

1. **Add Database Connectivity**
   - Update each service's pom.xml with Spring Data JPA + PostgreSQL driver
   - Configure `application.yml` with DB credentials

2. **Add Test Coverage**
   - Write integration tests for gateway routes
   - Add test containers for Kafka, PostgreSQL

3. **Add Security**
   - Implement JWT validation in API Gateway
   - Add OAuth2/Spring Security

4. **Add API Documentation**
   - Integrate Springdoc OpenAPI (Swagger 3.0)

5. **Kubernetes Deployment**
   - Create Helm charts
   - Add health/readiness probes

---

## Files Modified
```
api-gateway/
├── pom.xml                                    [UPDATED]
├── src/main/resources/
│   ├── application.yml                        [UPDATED]
│   └── application-docker.yml                 [NEW]
└── src/main/java/com/example/apigateway/
    ├── config/
    │   └── RequestIdGatewayFilterFactory.java  [NEW]
    └── controller/
        └── FallbackController.java            [NEW]

docker-compose.yml                            [UPDATED]
```

---

## Validation Checklist

- [x] Circuit breaker configured for all routes
- [x] Request tracing enabled (X-Request-ID)
- [x] Graceful fallback handling
- [x] Docker profile configured
- [x] Logging configured
- [x] Actuator endpoints enabled
- [x] Health monitoring ready
- [x] Service discovery friendly (Docker compose names)

**Status: ✅ API Gateway is now dev-ready!**
