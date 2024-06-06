# Pet Clinic

This project is a simple Java application using Spring Boot that provides a web application aiming to become a management system for a pet clinic. It uses JPA for database integration, REST controllers for API endpoints, and secures its endpoints with JWT tokens through integration with an OAuth2 Authorization Server. The application also uses Spring Cloud Gateway to handle and route requests securely.

**Note: This project requires two additional
repositories ([Spring Cloud Gateway](https://github.com/CarinaPorumb/spring-cloud-gateway)
and [OAuth2 Authorization Server Repository](https://github.com/CarinaPorumb/spring-oauth2)) to be set up and running.**

---

## Features

- RESTful web application using Spring MVC
- JPA integration for relational database access
- Secured endpoints using OAuth2 and JWT tokens
- API Gateway for handling and routing requests

---

## Technologies Used

- Java 21
- Spring Boot 3.3.0
- Spring MVC
- Spring Security
- Spring Data JPA
- MySQL Database
- OAuth2 Authorization Server with JWT


--- 

## Getting Started

### Prerequisites

Make sure you have the following installed on your system:

- Java 21
- Maven
- Postman (or preferred HTTP client)

---

### Installation

- #### 1. Clone and Set Up the OAuth2 Authorization Server Project

This project relies on a separate OAuth2 Authorization Server project for authentication and authorization.

Clone and set up the OAuth2 project first:

```bash
git clone https://github.com/CarinaPorumb/spring-oauth2
```

<br>

Build the project using Maven:

```bash
mvn clean install
```

<br>

You can run the application using your IDE or from the command line:

  ```bash
   mvn spring-boot:run
   ```

Once the application is running, it will be available at [http://localhost:9000](http://localhost:9000).

<br>

- #### 2. Clone and Set Up the Spring Cloud Gateway Project

After setting up the OAuth2 Authorization Server, clone and set up the Spring Cloud Gateway project:

```bash
git clone https://github.com/CarinaPorumb/spring-cloud-gateway
``` 

<br>

Build the project using Maven:

```bash
mvn clean install
```

<br>

You can run the application using your IDE or from the command line:

  ```bash
   mvn spring-boot:run
   ```

Once the application is running, it will be available at [http://localhost:8080](http://localhost:8080).

<br>

- #### 3. Clone and Set up the Pet Clinic Project

After setting up the OAuth2 Authorization Server and Spring Cloud Gateway, clone and set up the Pet Clinic project:

```bash 
git clone https://github.com/CarinaPorumb/pet-clinic
```

<br>

Build the project using Maven:

```bash
mvn clean install
```

<br>

You can run the application using your IDE or from the command line:

  ```bash
   mvn spring-boot:run
   ```

Once the application is running, it will be available at [http://localhost:8081](http://localhost:8081).

---

### Startup Order

1. **Start the OAuth2 Authorization Server**: [http://localhost:9000](http://localhost:9000)
2. **Start the Pet Clinic**: [http://localhost:8081](http://localhost:8081)
3. **Start the Spring Cloud Gateway**: [http://localhost:8080](http://localhost:8080)

---

### Testing with Postman

To test the Pet Clinic API endpoints secured by OAuth2, I used Postman to request an access token and make authenticated requests. Here are the steps:


1. **Configure Postman for OAuth2 Authentication**

- Open Postman and create a new request.
- Set the Request URL to your desired endpoint, e.g., `http://localhost:8081/api/v2/pet`.
- Go to the **Authorization** tab and select **OAuth 2.0** as the type.

<br>

2. **Configure a New Token**

- **Token Name**: Choose a name for your token, e.g., `newToken`.
- **Grant Type**: Select `Client Credentials`.
- **Access Token URL**: `http://localhost:9000/oauth2/token`
- **Client ID**: `messaging-client`
- **Client Secret**: `secret`
- **Scope**: `message.read message.write`
- **Client Authentication**: Select `Send as Basic Auth header`

<br>

3. **Request Token**

- Click on **Get New Access Token**.
- Postman will request the token from the OAuth2 server.
- If successful, the token will be displayed. Click on **Use Token** to set it for your request.

<br>

4. **Send the Request**

- Ensure the token is added to the request headers.
- Send the request to the API endpoint.

---