# Payroll Management System

## Overview
The Payroll Management System is a comprehensive solution developed for the Government of Rwanda to manage employee information and payroll processing across different institutions. The system enables centralized storage of employee details, calculation of salaries based on institutional rules, and management of deductions and taxes.

## Application Architecture

The application follows a layered architecture pattern:

1. **Presentation Layer**: REST API controllers that handle HTTP requests and responses
2. **Service Layer**: Business logic implementation
3. **Data Access Layer**: Repository interfaces for database operations
4. **Domain Layer**: Entity classes representing the business domain

### Component Diagram
```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  Controllers    │────▶│    Services     │────▶│  Repositories   │
│  (REST APIs)    │◀────│  (Business      │◀────│  (Data Access)  │
└─────────────────┘     │   Logic)        │     └─────────────────┘
                        └─────────────────┘             │
                                │                        ▼
                                │                ┌─────────────────┐
                                └───────────────▶│    Database     │
                                                 │  (PostgreSQL)   │
                                                 └─────────────────┘
```

### Security Architecture
The application uses JWT (JSON Web Tokens) for authentication and authorization. Role-based access control is implemented with three roles:
- ROLE_ADMIN: Can approve salary and manage all aspects of the system
- ROLE_MANAGER: Can process salary and add employee details
- ROLE_EMPLOYEE: Can view their own details, download payslip, and view pending salary payments

## List of POJOs (Entities)

### User (Employee)
- Properties: id, firstName, lastName, email, password, phoneNumber, nationalId, role, enabled, verificationToken, resetToken, dateOfBirth, status

### Employment
- Properties: code, employee, department, position, baseSalary, status, joiningDate

### Deduction
- Properties: code, deductionName, percentage

### Payslip
- Properties: id, employee, houseAmount, transportAmount, employeeTaxedAmount, pensionAmount, medicalInsuranceAmount, otherTaxedAmount, grossSalary, netSalary, month, year, status

### Message
- Properties: id, employee, message, month, year, sent, sentAt

## Technology Choices

### Spring Boot
Spring Boot was chosen as the primary framework for its robust features, ease of development, and wide industry adoption. It provides:
- Dependency injection
- Simplified configuration
- Built-in security
- Database integration
- REST API support

### Spring Data JPA
Spring Data JPA simplifies data access by providing repository interfaces that reduce boilerplate code. Benefits include:
- Automatic CRUD operations
- Custom query methods
- Pagination support
- Transaction management

### Spring Security with JWT
JWT-based authentication provides:
- Stateless authentication
- Secure token-based access
- Role-based authorization
- Protection against CSRF attacks

### PostgreSQL
PostgreSQL was selected as the database for:
- Reliability and robustness
- Advanced features like triggers and stored procedures
- Strong data integrity
- Support for complex queries
- Open-source nature

### MapStruct
MapStruct is used for object mapping between entities and DTOs:
- Type-safe mapping
- High performance
- Reduced boilerplate code

### Lombok
Lombok reduces boilerplate code by generating:
- Getters and setters
- Constructors
- Builder patterns
- Equals and hashCode methods

## API Endpoints

### Authentication
- `POST /api/v1/auth/register`: Register a new user
- `PATCH /api/v1/auth/verify-account`: Verify user account with OTP
- `POST /api/v1/auth/initiate-password-reset`: Initiate password reset process
- `PATCH /api/v1/auth/reset-password`: Reset password with OTP
- `POST /api/v1/auth/login`: Authenticate user and get JWT token

### User Management
- `GET /api/v1/users/me`: Get current user details
- `GET /api/v1/users/{id}`: Get user by ID (Admin/Manager only)
- `PATCH /api/v1/users/{id}/status`: Update user status (Admin only)

### Employment Management
- `GET /api/v1/employments`: Get all employments (Admin/Manager only)
- `GET /api/v1/employments/{id}`: Get employment by ID
- `GET /api/v1/employments/employee/{employeeId}`: Get employments by employee ID
- `GET /api/v1/employments/active`: Get all active employments (Admin/Manager only)
- `POST /api/v1/employments`: Create new employment (Admin/Manager only)
- `PUT /api/v1/employments/{id}`: Update employment (Admin/Manager only)
- `DELETE /api/v1/employments/{id}`: Delete employment (Admin only)

### Deduction Management
- `GET /api/v1/deductions`: Get all deductions
- `GET /api/v1/deductions/{id}`: Get deduction by ID
- `GET /api/v1/deductions/name/{name}`: Get deduction by name
- `POST /api/v1/deductions`: Create new deduction (Admin/Manager only)
- `PUT /api/v1/deductions/{id}`: Update deduction (Admin/Manager only)
- `DELETE /api/v1/deductions/{id}`: Delete deduction (Admin only)
- `POST /api/v1/deductions/initialize`: Initialize default deductions (Admin only)

### Payroll Management
- `GET /api/v1/payroll/payslips`: Get all payslips (Admin/Manager only)
- `GET /api/v1/payroll/payslips/paged`: Get paginated payslips (Admin/Manager only)
- `GET /api/v1/payroll/payslips/{id}`: Get payslip by ID
- `GET /api/v1/payroll/payslips/employee/{employeeId}`: Get payslips by employee ID
- `GET /api/v1/payroll/payslips/employee/{employeeId}/paged`: Get paginated payslips by employee ID
- `GET /api/v1/payroll/payslips/month/{month}/year/{year}`: Get payslips by month and year (Admin/Manager only)
- `GET /api/v1/payroll/payslips/month/{month}/year/{year}/paged`: Get paginated payslips by month and year (Admin/Manager only)
- `GET /api/v1/payroll/payslips/employee/{employeeId}/month/{month}/year/{year}`: Get payslip by employee, month, and year
- `POST /api/v1/payroll/generate/month/{month}/year/{year}`: Generate payroll for month and year (Admin/Manager only)
- `PATCH /api/v1/payroll/approve/payslip/{id}`: Approve payslip (Admin only)
- `PATCH /api/v1/payroll/approve/month/{month}/year/{year}`: Approve all payslips for month and year (Admin only)

### Message Management
- `GET /api/v1/messages`: Get all messages (Admin/Manager only)
- `GET /api/v1/messages/{id}`: Get message by ID
- `GET /api/v1/messages/employee/{employeeId}`: Get messages by employee ID
- `GET /api/v1/messages/employee/{employeeId}/month/{month}/year/{year}`: Get messages by employee, month, and year
- `POST /api/v1/messages`: Create new message (Admin/Manager only)
- `POST /api/v1/messages/{id}/send`: Send message (Admin/Manager only)

## Features

### Payroll Generation
The system calculates employee salaries based on:
- Base salary
- Housing allowance (14% of base salary)
- Transport allowance (14% of base salary)
- Deductions:
  - Employee tax (30% of base salary)
  - Pension (6% of base salary)
  - Medical insurance (5% of base salary)
  - Others (5% of base salary)

### Duplicate Payroll Prevention
The system prevents duplicate payroll generation for the same employee in the same month/year through:
- Unique constraint in the database
- Validation in the service layer before payroll generation

### Automatic Notifications
When a payslip is approved:
- A database trigger automatically creates a message
- The message is sent to the employee's email
- The message includes details about the salary payment

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven
- PostgreSQL database

### Configuration
Configure the application in `application.properties`:
```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT Configuration
spring.jwt.secret=your_secret_key
spring.jwt.access-token-expiration=86400000

# Email Configuration
spring.mail.host=your_mail_host
spring.mail.port=your_mail_port
spring.mail.username=your_mail_username
spring.mail.password=your_mail_password
```

### Running the Application
```bash
mvn spring-boot:run
```

### API Documentation
Access Swagger UI at: http://localhost:8080/swagger-ui.html