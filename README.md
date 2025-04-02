# Gathu Bank App with Security

A secure banking application built with Spring Boot, featuring user authentication, account management, standing orders, and scheduled transactions. This project demonstrates a robust backend with JWT-based authentication, email verification, and role-based access control.

## Features
- **User Authentication:** Register, login, logout, and refresh tokens using JWT.
- **Email Verification:** Users must verify their email before accessing the system.
- **Role-Based Access:** Admin and user roles with specific permissions.
- **Account Management:** Create and manage customer accounts.
- **Standing Orders:** Schedule recurring transactions between accounts.
- **Scheduler:** Automatically execute standing orders based on their schedule.

## Tech Stack
- **Backend:** Spring Boot, Spring Security, JPA/Hibernate
- **Database:** PostgreSQL
- **Authentication:** JWT (JSON Web Tokens)
- **Email Service:** Integration for sending verification emails
- **Scheduler:** Spring `@Scheduled` for executing standing orders
- **API Documentation:** Swagger UI

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven
- PostgreSQL
- SMTP server (for email verification, e.g., Gmail SMTP)

### Steps
1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/Gathu-Bank-App-With-Security.git
   cd Gathu-Bank-App-With-Security
   ```

2. **Configure the Database**
   - Create a PostgreSQL database named `gathu_bank`.
   - Update `src/main/resources/application.properties` with your database credentials:
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/gathu_bank
     spring.datasource.username=your-username
     spring.datasource.password=your-password
     spring.jpa.hibernate.ddl-auto=update
     ```

3. **Configure Email Service**
   - Update `application.properties` with your SMTP settings:
     ```properties
     spring.mail.host=smtp.gmail.com
     spring.mail.port=587
     spring.mail.username=your-email@gmail.com
     spring.mail.password=your-app-password
     spring.mail.properties.mail.smtp.auth=true
     spring.mail.properties.mail.smtp.starttls.enable=true
     ```

4. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. **Access Swagger UI**
   - Open `http://localhost:9000/swagger-ui/index.html` in your browser to view the API documentation.

## API Usage
Below are the key API endpoints with example requests and links to screenshots. You can test these using Swagger UI or tools like Postman.

### 1. Create an Account (`POST /api/v1/admin/accounts`)
- **Description:** Creates a new account for a customer.
- **Request Body:**
  ```json
  {
    "customerId": 2,
    "accountType": "CHECKING"
  }
  ```
- **Screenshot:** [Create Account Screenshot](https://github.com/your-username/Gathu-Bank-App-With-Security/blob/main/screenshots/create-account.png)

### 2. Fund an Account (`POST /api/v1/admin/accounts/fund`)
- **Description:** Adds funds to an account for testing standing orders.
- **Request Body:**
  ```json
  {
    "accountId": 1,
    "amount": 500.0
  }
  ```
- **Screenshot:** [Fund Account Screenshot](https://github.com/your-username/Gathu-Bank-App-With-Security/blob/main/screenshots/fund-account.png)

### 3. Create a Standing Order (`POST /api/v1/admin/standing-orders`)
- **Description:** Creates a standing order to transfer money between accounts.
- **Request Body:**
  ```json
  {
    "sourceAccountId": 1,
    "destinationAccountId": 2,
    "amount": 100.0,
    "schedule": "DAILY"
  }
  ```
- **Screenshot:** [Create Standing Order Screenshot](https://github.com/your-username/Gathu-Bank-App-With-Security/blob/main/screenshots/create-standing-order.png)

### 4. Register a User (`POST /api/v1/auth/register`)
- **Description:** Registers a new user (automatically creates a customer).
- **Request Body:**
  ```json
  {
    "email": "testuser@example.com",
    "username": "testuser",
    "password": "password123",
    "firstname": "Test",
    "lastname": "User"
  }
  ```
- **Screenshot:** [Register User Screenshot](https://github.com/your-username/Gathu-Bank-App-With-Security/blob/main/screenshots/register-user.png)

### 5. Login (`POST /api/v1/auth/login`)
- **Description:** Logs in a user and returns a JWT token.
- **Request Body:**
  ```json
  {
    "email": "testuser@example.com",
    "password": "password123"
  }
  ```
- **Screenshot:** [Login Screenshot](https://github.com/your-username/Gathu-Bank-App-With-Security/blob/main/screenshots/login.png)

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -m "Add your feature"`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a Pull Request.

## License
This project is licensed under the MIT License.
 with 
---

***Note:*** 
- Tn this project you cannot register a customer with inval email unless you do it on the database side and set verificatio is equals to true; otherwise emails must be valid; 
-I have provided this project to you on public repo with love dont try to penetratee my security because 1. Not possible 2. You will never get my secret key and 3. I will pull it down
