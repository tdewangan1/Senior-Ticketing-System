This ticketing system application is utilized for Seniors Empowered in Technology (SET), a community service organization aimed toward helping seniors with their technological struggles

You have the ability to create different tickets for different residents living at a retirement home.

## Features
- User authentication and authorization
- CRUD operations for support tickets
- Role-based access control
- User-friendly interface for managing tickets
- Firebase integration for database operations

## Technologies Used
- Java 17
- Spring Boot 3
- Firebase Firestore
- Thymeleaf
- HTML/CSS
- JavaScript

## Prerequisites
- Java 17 or higher
- Gradle


## Installation
1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/senior-ticketing-system.git
    cd senior-ticketing-system
    ```

2. Install dependencies:
    ```sh
    ./gradlew build
    ```
## Dependencies
Include the following dependencies in your `build.gradle` file:

```groovy
dependencies {
    implementation 'com.google.firebase:firebase-admin:9.2.0'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

## Usage
1. Run the application:
    ```sh
    ./gradlew bootRun
    ```

2. Open a web browser and navigate to `http://localhost:8080`.
3. You can create a custom username and password on the signup page or you can use the default login with username: "admin" and password: "Password123"

## Endpoints
- `/login`: User login page
- `/signup`: User registration page
- `/user/dashboard`: User dashboard
- `/user/dashboard/add-ticket`: Add a new ticket (GET and POST)
- `/user/dashboard/delete-ticket`: Delete a ticket (POST)

## Project Structure

```plaintext
src
 ├── main
 │   ├── java
 │   │   └── com
 │   │       └── example
 │   │           └── servingwebcontent
 │   │               ├── config
 │   │               │   └── FirebaseConfig.java
 │   │               ├── controllers
 │   │               │   ├── DashboardController.java
 │   │               │   └── UserController.java
 │   │               ├── database
 │   │               │   └── DatabaseOperations.java
 │   │               ├── login
 │   │               │   └── AuthUtils.java
 │   │               ├── service
 │   │               │   └── TicketService.java
 │   │               ├── tickets
 │   │               │   └── Ticket.java
 │   │               ├── users
 │   │               │   └── User.java
 │   │               └── ServingWebContentApplication.java
 │   └── resources
 │       ├── static
 │       │   └── css
 │       │       └── styles.css
 │       ├── templates
 │       │   ├── dashboard.html
 │       │   ├── login.html
 │       │   └── signup.html
 │       ├── serviceAccountKey.json
 │       └── application.properties   


