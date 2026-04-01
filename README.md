Stock Management System
A Spring Boot backend application for managing multi-warehouse inventory with role-based access control, secured using Keycloak and JWT authentication.
---
Table of Contents
Overview
Tech Stack
Roles and Permissions
Project Structure
Getting Started
Prerequisites
Keycloak Setup
Environment Variables
Running the Application
API Endpoints
Security Architecture
---
Overview
This system allows businesses to track inventory across multiple warehouses with strict role separation. Administrators manage the full catalog, users, and warehouses. Warehouse managers are automatically scoped to their assigned warehouse and cannot access or modify data outside of it.
---
Tech Stack
Java 17+
Spring Boot 3
Spring Security with OAuth2 Resource Server
Keycloak for identity and access management
JWT for stateless authentication
Spring Data JPA with Hibernate
PostgreSQL
Lombok
---
Roles and Permissions
Feature	ADMIN	MANAGER
Manage users	✓	
Manage product catalog	✓	
Manage warehouses	✓	
View global stock	✓	
View assigned warehouse stock		✓
Update assigned warehouse stock		✓
Managers are linked to a specific warehouse via the `entrepotAssigne` field on their user profile. All stock queries and updates are automatically filtered based on this assignment at the service layer.
---
Project Structure
```
src/
└── main/
    └── java/com/example/stock/
        ├── config/
        │   ├── SecurityConfig.java        # Spring Security filter chain and authorization rules
        │   └── JwtAuthConverter.java      # Custom JWT converter extracting Keycloak realm roles
        ├── controllers/
        │   ├── adminController/           # Admin-only endpoints
        │   └── managerController/         # Manager-only endpoints
        ├── service/
        │   └── Securityutils.java         # Resolves the currently authenticated user from JWT
        ├── models/                        # JPA entities (User, Product, Entrepot, Stock)
        └── repositories/                  # Spring Data JPA repositories
```
---
Getting Started
Prerequisites
Java 17+
Maven
PostgreSQL
Keycloak (tested with Keycloak 23+)
Keycloak Setup
Start Keycloak on `http://localhost:8180`
Create a new realm (e.g. `AppRealm`)
Create a client (e.g. `stock-app`) with:
Client authentication: ON
Valid redirect URIs: `http://localhost:8080/*`
Create realm roles: `ADMIN` and `MANAGER`
Create users and assign the appropriate roles under Role Mapping
Environment Variables
Copy the example config and fill in your values:
```bash
cp src/main/resources/application.yml.example src/main/resources/application.yml
```
The following variables must be set (either in `application.yml` or as environment variables):
```env
DB_URL=jdbc:postgresql://localhost:5432/stockdb
DB_USERNAME=your_db_user
DB_PASSWORD=your_db_password
KEYCLOAK_ISSUER_URI=http://localhost:8180/realms/AppRealm
KEYCLOAK_JWK_URI=http://localhost:8180/realms/AppRealm/protocol/openid-connect/certs
```
> **Never commit real credentials.** The `application.yml` file is listed in `.gitignore`.
Running the Application
```bash
./mvnw spring-boot:run
```
The application starts on `http://localhost:8080`.
---
API Endpoints
All requests must include a valid Bearer token in the `Authorization` header.
Admin Endpoints — `ADMIN` role required
Method	Endpoint	Description
GET	`/admin/users`	List all users
POST	`/admin/users`	Create a user
PUT	`/admin/users/{id}`	Update a user
DELETE	`/admin/users/{id}`	Delete a user
GET	`/admin/products`	List all products
POST	`/admin/products`	Create a product
GET	`/admin/entrepots`	List all warehouses
POST	`/admin/entrepots`	Create a warehouse
GET	`/admin/stocks`	View global stock overview
Manager Endpoints — `MANAGER` role required
Method	Endpoint	Description
GET	`/manager/stocks`	View stock for assigned warehouse only
PUT	`/manager/stocks/{id}`	Update stock for assigned warehouse only
---
Security Architecture
Authentication is stateless. Every request is validated against a JWT issued by Keycloak.
The `JwtAuthConverter` extracts roles from the `realm_access.roles` claim and maps them to Spring Security `GrantedAuthority` objects with the `ROLE_` prefix, which allows standard `hasRole()` checks to work correctly.
```
Request → BearerTokenFilter → JwtAuthConverter → SecurityFilterChain → Controller
                                      ↓
                         realm_access.roles → ROLE_ADMIN / ROLE_MANAGER
```
Authorization is enforced at two levels:
URL level via `authorizeHttpRequests` in `SecurityConfig`
Method level via `@PreAuthorize` annotations where finer control is needed
