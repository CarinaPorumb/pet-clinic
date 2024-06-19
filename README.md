# Pet Clinic  [![CircleCI](https://dl.circleci.com/status-badge/img/circleci/5vWvCHPxWZ7cHvpFmPPqjK/FMuALQo8kLTAXHGsgUwVkq/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/circleci/5vWvCHPxWZ7cHvpFmPPqjK/FMuALQo8kLTAXHGsgUwVkq/tree/main)

This is a simple Java application that aims to become a pet clinic management system. The app uses JPA to handle database operations and RESTful controllers for API endpoints. Security is enforced with JWT tokens via an OAuth2 Authorization Server. Additionally, it uses Spring Cloud Gateway to manage and route requests securely.

**Note: This project requires two additional
repositories ([Spring Cloud Gateway](https://github.com/CarinaPorumb/spring-cloud-gateway)
and [OAuth2 Authorization Server Repository](https://github.com/CarinaPorumb/spring-oauth2)) to be set up and running.**

**Optional: For additional functionality, you may set up the [Pet Clinic Client](https://github.com/CarinaPorumb/pet-clinic-client) project.**

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
- Spring MVC for RESTful controllers
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

- #### 3. Clone and Set Up the Pet Clinic Project

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

### Optional: Pet Clinic Client

For additional functionality, you may set up the Pet Clinic Client project. This client can interact with the Pet Clinic service for various operations.

Clone and set up the Pet Clinic Client project:

```bash
git clone https://github.com/CarinaPorumb/pet-clinic-client
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

Once the application is running, it will be available at [http://localhost:8082](http://localhost:8082).


### Startup Order

1. **Start the OAuth2 Authorization Server**: [http://localhost:9000](http://localhost:9000)
2. **Start the Pet Clinic**: [http://localhost:8081](http://localhost:8081)
3. **Start the Spring Cloud Gateway**: [http://localhost:8080](http://localhost:8080)
4. **_Optional_: Start the Pet Clinic Client**: [http://localhost:8082](http://localhost:8082)

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