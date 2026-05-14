# LabPolymarket backend
This folder contains the LabPolymarket's backend.


This repository contains the backend code for the LabPolymarket project. The backend is built using the Spring Boot framework and follows a reactive architecture. The main purpose of this project is to provide an API for managing the LabPolymarket application.

## Architecture

The backend follows a reactive architecture based on WebFlux. It uses the Spring WebFlux framework to handle HTTP requests and responses asynchronously. The application connects to a Neon DB database for storing and retrieving data.

## Models

The following are the main entities in the LabPolymarket backend:

- `Market`: Represents a market in the LabPolymarket application.
- `Product`: Represents a product in a market.
- `User`: Represents a user of the LabPolymarket application.

## Controllers

The following are the main controllers in the LabPolymarket backend:

- `MarketController`: Handles requests related to markets.
  - `GET /markets`: Retrieves a list of active markets.
  - `GET /markets/{id}`: Retrieves a specific market by ID.
  - `POST /markets`: Creates a new market.
  - `PUT /markets/{id}`: Updates an existing market.
  - `DELETE /markets/{id}`: Deletes a market.

- `ProductController`: Handles requests related to products.
  - `GET /markets/{marketId}/products`: Retrieves a list of products in a specific market.
  - `GET /markets/{marketId}/products/{id}`: Retrieves a specific product by ID.
  - `POST /markets/{marketId}/products`: Creates a new product in a specific market.
  - `PUT /markets/{marketId}/products/{id}`: Updates an existing product.
  - `DELETE /markets/{marketId}/products/{id}`: Deletes a product.

- `UserController`: Handles requests related to users.
  - `POST /users`: Creates a new user.
  - `PUT /users/{id}`: Updates an existing user.
  - `DELETE /users/{id}`: Deletes a user.

## Services

The following are the main services in the LabPolymarket backend:

- `MarketService`: Handles the business logic for markets.
- `ProductService`: Handles the business logic for products.
- `UserService`: Handles the business logic for users.

## Installation and Configuration

To run the LabPolymarket backend locally, follow these steps:

1. Clone the repository: `git clone https://github.com/LabPolymarket/code/tree/main/sb/polymarket-backend`
2. Open the project in your preferred IDE.
3. Update the `application.properties` file with the correct database configuration.
4. Configure the `launch.json` file in Visual Studio Code to run the backend:
   - Set the `mainClass` to `com.polymarket.polymarket_backend.PolymarketBackendApplication`.
   - Set the `env` variables to the correct values for your database configuration.
5. Run the backend by clicking on the "Run" button in your IDE or by using the command `mvn spring-boot:run` in the terminal.

