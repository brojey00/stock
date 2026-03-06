# Cahier des Charges - Stock Management System

## Project Overview
This project is a Spring Boot-based Stock Management System designed to handle multi-warehouse inventory tracking with role-based access control. It allows administrators to manage the global catalog of products and users, while providing warehouse managers with tools to monitor and update stock levels for their specific locations.

## User Roles and Access Control
- Administrator (ADMIN): Has full access to the system, including user management, product catalog maintenance, warehouse configuration, and global stock oversight.
- Warehouse Manager (MANAGER): Has restricted access to view and update stock levels only for the warehouse they are specifically assigned to.
- Authentication: Secure login and registration system with password encryption using BCrypt and stateless session management.

## Functional Requirements

### User Management (Admin Only)
- Create, update, and delete user accounts within the system.
- Assign a specific manager to a warehouse or remove an existing assignment.
- List all registered users and their details.

### Product Catalog Management (Admin Only)
- Define and maintain a global catalog of products including name, description, category, and units.
- Manage financial data for products such as buying prices and selling prices.
- Perform full CRUD operations on product entities.

### Warehouse (Entrepot) Management (Admin Only)
- Create and configure multiple warehouse locations (Entrepots).
- Update warehouse details or remove warehouses from the system.
- List all available storage locations.

### Stock Operations
- Global Stock Overview (Admin): Access to stock levels across all products and all warehouses.
- Targeted Stock Monitoring (Manager): Ability for managers to view current inventory levels only for their assigned warehouse.
- Stock Adjustments: Updating quantities and setting stock alert thresholds for products.
- Stock Alerts: Tracking 'stockAlert' levels to identify when inventory falls below required minimums.
- Automatic Validation: Ensuring stock entries are uniquely linked to a product-warehouse combination.

## Workflow and Logic
- Managers are linked to specific warehouses via the 'entrepotAssigne' attribute in their user profile.
- When a manager requests stock data, the system automatically filters results based on their assigned warehouse ID.
- Stock updates are restricted; a manager can only modify stock if the warehouse ID matches their profile assignment.
- Admins bypass these restrictions, allowing them to intervene in any warehouse's inventory when necessary.