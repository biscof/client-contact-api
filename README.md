# Client Contacts API

[![build-and-test](https://github.com/biscof/client-contact-api/actions/workflows/build-and-test.yml/badge.svg)](https://github.com/biscof/client-contact-api/actions/workflows/build-and-test.yml)
[![Maintainability](https://api.codeclimate.com/v1/badges/2206441820c281e53b08/maintainability)](https://codeclimate.com/github/biscof/client-contact-api/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/2206441820c281e53b08/test_coverage)](https://codeclimate.com/github/biscof/client-contact-api/test_coverage)

## Overview

A RESTful API for managing clients' contact data (emails and phones).
[API Documentation](https://client-contact-api.onrender.com/docs/swagger-ui/index.html)


## Technologies and dependencies

- Java 21
- Spring Boot
- Spring Data
- PostgreSQL
- Liquibase
- Hibernate
- JUnit


## Getting started

To run this app locally, you'll need:

- JDK 21
- Docker and Docker Compose

### Usage and development

1. Clone this repository and move to the project directory:

```bash
git clone https://github.com/biscof/client-contact-api.git
cd client-contact-api
```

2. Build and run the app locally with a containerized PostgreSQL database using Docker and Docker Cmpose:

```bash
docker-compose up -d -V --remove-orphans
```

### Testing

To run tests use this command:

```bash
./gradlew test
```


## Base endpoints

- **Add a new client**

```http
POST /api/clients
Content-Type: application/json

{
    "firstName": "Ivan",
    "lastName": "Petrov"
}
```

- **Get a list of all the clients**

```http
GET /api/clients
```

- **Get client's details by ID**

```http
GET /api/clients/1
```

- **Add client's new phone number**

```http
POST /api/clients/1/phones
Content-Type: application/json

{
  "phone": "+79876543210"
}
```

- **Add client's new email**

```http
POST /api/clients/1/emails
Content-Type: application/json

{
  "email": "test@test.com"
}
```

- **Get all user's phone numbers by ID**

```http
GET /api/clients/1/phones
```

- **Get all user's emails by ID**

```http
GET /api/clients/1/emails
```

- **Retrieve all contacts of a user by ID**

```http
GET /api/clients/1/contacts
```

- **Delete a client by ID**

```http
DELETE /api/clients/1
```

## API Documentation

For more detailed information on the entry points, please refer to the API documentation which is available [here](https://client-contact-api.onrender.com/docs/swagger-ui/index.html) or on your host at `/docs/swagger-ui/index.html`.



