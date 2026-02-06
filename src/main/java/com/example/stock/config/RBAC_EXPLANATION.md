# RBAC Implementation - Step by Step Explanation

## What is RBAC?

**Role-Based Access Control (RBAC)** restricts system access based on user roles. Instead of assigning permissions to individual users, permissions are assigned to roles, and users are assigned roles.

---

## Your Two Roles

| Role | Description |
|------|-------------|
| **ADMIN** | Full system administrator with complete access |
| **GESTIONNAIRE** | Warehouse manager with limited access |

---

## How RBAC Works in This Project

### Step 1: Role Definition (`Role.java`)

```java
public enum Role {
    ADMIN, GESTIONNAIRE
}
```
Each user has ONE role stored in the database.

---

### Step 2: User Stores Their Role (`User.java`)

```java
@Enumerated(EnumType.STRING)
private Role role;  // Stores ADMIN or GESTIONNAIRE

@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
}
```
The `getAuthorities()` method converts the role into Spring Security's format: `ROLE_ADMIN` or `ROLE_GESTIONNAIRE`.

---

### Step 3: Load User During Login (`CustomUserDetailsService.java`)

```java
public UserDetails loadUserByUsername(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(...));
}
```
When user logs in:
1. Spring Security calls this method
2. User is loaded from database by email
3. User's role is available via `getAuthorities()`

---

### Step 4: Protect Endpoints (`SecurityConfig.java`)

```java
.authorizeHttpRequests(auth -> auth
    // Public - no auth needed
    .requestMatchers("/api/auth/**").permitAll()
    
    // ADMIN ONLY
    .requestMatchers("/api/admin/**").hasRole("ADMIN")
    .requestMatchers("/api/users/**").hasRole("ADMIN")
    .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
    
    // ADMIN + GESTIONNAIRE
    .requestMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("ADMIN", "GESTIONNAIRE")
    .requestMatchers("/api/stock/**").hasAnyRole("ADMIN", "GESTIONNAIRE")
    .requestMatchers("/api/entrepots/**").hasAnyRole("ADMIN", "GESTIONNAIRE")
    
    // Any other request requires login
    .anyRequest().authenticated()
)
```

---

## Permission Matrix

| Resource | ADMIN | GESTIONNAIRE |
|----------|-------|--------------|
| User Management | ✅ Full | ❌ No Access |
| Products (Create/Update/Delete) | ✅ Full | ❌ No Access |
| Products (Read) | ✅ Yes | ✅ Yes |
| Stock Management | ✅ Full | ✅ Full |
| Warehouse Management | ✅ Full | ✅ Full |

---

## Request Flow Diagram

```
┌─────────────────┐
│   HTTP Request  │
│  GET /products  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Spring Security │
│   Filter Chain  │
└────────┬────────┘
         │
         ▼
┌─────────────────────────────┐
│ CustomUserDetailsService    │
│ loadUserByUsername(email)   │
│ → Returns User with role    │
└────────┬────────────────────┘
         │
         ▼
┌─────────────────────────────┐
│ SecurityConfig rules check: │
│ Does user have ADMIN or     │
│ GESTIONNAIRE role?          │
└────────┬────────────────────┘
         │
    ┌────┴────┐
    │         │
   YES        NO
    │         │
    ▼         ▼
┌───────┐  ┌───────────┐
│ 200   │  │ 403       │
│ OK    │  │ Forbidden │
└───────┘  └───────────┘
```

---

## Key Components Summary

| File | Purpose |
|------|---------|
| `Role.java` | Enum defining available roles |
| `User.java` | Entity with role field + `getAuthorities()` |
| `CustomUserDetailsService.java` | Loads user from DB during authentication |
| `SecurityConfig.java` | Defines which endpoints each role can access |
// .requestMatchers("/api/auth/**").permitAll()
.requestMatchers("/api/admin/**").hasRole("ADMIN")
.requestMatchers("/api/users/**").hasRole("ADMIN")

                        // Product management - only ADMIN can create/update/delete
                        .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

                        // ═══════════════════════════════════════════════════════════
                        // ADMIN + GESTIONNAIRE - Shared access
                        // ═══════════════════════════════════════════════════════════
                        // Products - READ access for both roles
                        .requestMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("ADMIN", "GESTIONNAIRE")

                        // Stock management - FULL access for both roles
                        .requestMatchers("/api/stock/**").hasAnyRole("ADMIN", "GESTIONNAIRE")

                        // Warehouse (Entrepot) management - FULL access for both roles
                        .requestMatchers("/api/entrepots/**").hasAnyRole("ADMIN", "GESTIONNAIRE")