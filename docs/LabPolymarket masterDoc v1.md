# Polyrouter Architecture:  Real-Time Dashboard

1. Overview

The system is based on a modern client-server architecture with an event-driven approach. Its main goal is to consume data from the Polymarket public API and provide users with a real-time interactive experience using GraphQL.

The solution consists of two main components:

Backend: Built with Spring Boot and GraphQL, responsible for business logic, external API integration, authentication, and data management.
Frontend: Built with React and Apollo Client, responsible for user interaction and real-time data visualization.

The system uses a hybrid communication model:

HTTP for GraphQL Queries and Mutations
WebSocket for GraphQL Subscriptions (real-time updates)


---------------------------------------------------------------------------------------------------------------------------------------

2. PRODUCT GOAL

To deliver a predictive market visualization platform that allows users to monitor and react to changes in real time, centralizing Polyrouter data in an interactive, stable, and highly responsive dashboard.

For this:

    - Efficiently consume the Polyrouter API and detect even the smallest changes between data captures.
    - Ensure the Event Stream notifies the frontend in less than one second after detecting a change.
    - Create a React interface that is not only aesthetically pleasing but also manages Apollo Client subscriptions without degrading browser performance.


---------------------------------------------------------------------------------------------------------------------------------------


3. Backend Architecture

The backend follows a layered architecture inspired by the hexagonal pattern, ensuring clear separation of responsibilities and maintainability.

2.1 Core Layers

    1. GraphQL Layer (API Layer)

    This layer exposes the GraphQL schema and handles all incoming requests:

    Queries: Fetch data (markets, events)
    Mutations: Perform user actions (select markets, manage favorites, create predictions)
    Subscriptions: Deliver real-time updates to clients

    It acts as the entry point to the system.

    2. Service Layer

    Contains the core business logic of the application:

    Market processing and filtering
    Favorites management
    Prediction handling
    Coordination between internal components

    This layer acts as an intermediary between the GraphQL layer and the data sources.

---------------------------------------------------------------------------------------------------------------------------------------

3. External API Integration Layer

Responsible for communicating with the Polymarket API using WebClient:

Retrieves market and event data
Contains no business logic
Returns data as DTOs

---------------------------------------------------------------------------------------------------------------------------------------

4. Real-Time Event System

This is a key component of the architecture.

It includes:

Polling Service: Periodically fetches data from the Polyrouter API
In-memory Cache: Stores the previous state of markets
Change Detection Mechanism: Compares new and previous data
Event Stream (Sinks/Flux): Broadcasts updates to subscribers

When a change is detected, a MarketUpdate event is generated and pushed to subscribed clients via GraphQL Subscriptions.



---------------------------------------------------------------------------------------------------------------------------------------


# DIAGRAMS Type C4

1. C4 – Level 1: System Context Diagram (System Context.png)

![alt text](<System Context.png>)




2. C4 – Level 2: Container Diagram (container.png)

![alt text](Container.png)




3. C3 – Level 3: Backend Component Diagram (Backend Component Diagram.png)

![alt text](<Backend Component Diagram.png>)



---------------------------------------------------------------------------------------------------------------------------------------


# DATAMODEL

Java classes

public class Market {

    @Id
    private String id;
    private String question;
    private String conditionId;
    private String category;
    private String liquidity;
    private LocalDate endDate;
    private String outcomes;
    private String outcomePrices;
    private String volume;
    private Boolean active;
}

