# Stock Management Application - Project Structure

## Overview

This is a **Spring Boot 4.0.2** application for inventory/stock management. It uses a layered architecture with JPA for data persistence and Spring Security for authentication and authorization.

---

## Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Runtime |
| Spring Boot | 4.0.2 | Framework |
| Spring Data JPA | - | ORM & Data Access |
| Spring Security | - | Authentication & Authorization |
| MySQL | - | Database |
| Lombok | - | Boilerplate reduction |
| Maven | - | Build tool |

---

## Package Structure

```
com.example.stock
├── StockApplication.java          # Main entry point
├── config/                         # Configuration classes
│   ├── SecurityConfig.java         # Spring Security setup
│   └── CustomUserDetailsService.java
├── controllers/
│   └── adminController/
│       └── HomeController.java     # REST API endpoints
├── dao/
│   ├── entities/                   # JPA entities
│   │   ├── Product.java
│   │   ├── Stock.java
│   │   ├── User.java
│   │   ├── Entrepot.java
│   │   ├── Role.java
│   │   └── Day.java
│   └── repositories/               # JPA repositories
│       ├── ProductRepository.java
│       ├── StockRepository.java
│       ├── UserRepository.java
│       └── EntropotRepository.java
├── dto/
│   └── ProductDTO.java             # Data Transfer Object
└── service/                        # Business logic layer
    ├── ProductManager.java
    ├── StockManager.java
    ├── UserManager.java
    └── EntropotManager.java
```

---

## Domain Model

### Entities

#### `Product`
Represents a product that can be stored in warehouses.

| Field | Type | Description |
|-------|------|-------------|
| id | Integer | Primary key (auto-generated) |
| name | String | Product name |
| Description | String | Product description |
| Category | String | Product category |
| buy_price | int | Purchase price |
| sell_price | int | Selling price |
| unit | String | Unit of measurement |

#### `Entrepot` (Warehouse)
Represents a storage location/warehouse.

| Field | Type | Description |
|-------|------|-------------|
| id | Integer | Primary key |
| name | String | Warehouse name |
| city | String | City location |
| location | String | Address/location details |

#### `Stock`
Links products to warehouses with quantity tracking.

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Primary key |
| product | Product | Referenced product (ManyToOne) |
| entrepot | Entrepot | Referenced warehouse (ManyToOne) |
| quantity | int | Current stock quantity |
| stock_alert | int | Low stock threshold |

#### `User`
Application user implementing Spring Security's `UserDetails`.

| Field | Type | Description |
|-------|------|-------------|
| id | Integer | Primary key |
| fullname | String | User's full name |
| email | String | Email (used as username) |
| password | String | Encrypted password |
| role | Role | User role (ADMIN/GESTIONNAIRE) |
| entrepotAssigne | Entrepot | Assigned warehouse (ManyToOne) |

#### `Role` (Enum)
User roles for authorization:
- `ADMIN` - Full system access
- `GESTIONNAIRE` - Warehouse manager access

---

## Security Configuration

The application uses **HTTP Basic Authentication** with role-based access control:

| Endpoint Pattern | Access |
|-----------------|--------|
| `/api/auth/**` | Public (no auth required) |
| `/api/admin/**` | ADMIN role only |
| `/api/entrepots/**` | ADMIN or GESTIONNAIRE roles |
| All other endpoints | Authenticated users |

Password encoding: **BCrypt**

---

## REST API Endpoints

### Products (Admin Only)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/products` | List all products |
| GET | `/product/{id}` | Get product by ID |
| POST | `/addproduct` | Create new product |
| PUT | `/updateproduct` | Update product |
| DELETE | `/product/{id}` | Delete product |

### Warehouses (Admin Only)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/entropots` | List all warehouses |
| GET | `/entrepot/{id}` | Get warehouse by ID |
| POST | `/addentrepot` | Create warehouse |
| PUT | `/updateentrepot` | Update warehouse |
| DELETE | `/entrepot/{id}` | Delete warehouse |

---

## Database Configuration

Located in `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mydb
    username: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

- **Database**: MySQL on localhost:3306, database name `mydb`
- **Schema generation**: Auto-update (Hibernate creates/updates tables)
- **SQL logging**: Enabled for debugging

---

## Service Layer

The service layer (`*Manager` classes) implements business logic:

- **ProductManager**: CRUD operations for products with validation
- **StockManager**: Stock quantity management
- **UserManager**: User management
- **EntropotManager**: Warehouse management

All services use constructor injection via `@Autowired` and throw `ResponseStatusException` for error handling.

---

## Build & Run

```bash
# Build the project
./mvnw clean package

# Run the application
./mvnw spring-boot:run
```

The application requires a running MySQL instance with the configured database.
