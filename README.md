# IMDB DATA SCRAPER
## Introduction
The Movie Information Service is a Spring Boot application developed in Java 11 that consumes the IMDb REST API to retrieve movie details. It provides a REST endpoint to search for movies by title and returns relevant information such as the title, year of release, running time, and lead actors.

## Features
- *Search Movies*: Fetch movie details by title from the IMDb database using the /title/find endpoint.
- *Retrieve Information*: Return JSON responses containing movie title, year of release, running time, and lead actors.
- *Optional Persistence*: Optionally persists movie information in a PostgreSQL database using Liquibase scripts. Checks the database before calling IMDb API and updates with new movie data if available.

## Technologies Used
- Java 11
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Liquibase
- IMDb REST API
- Maven

## Configuration
Configure IMDb API credentials in application.properties
 ```bash
imdb.api.host=imdb8.p.rapidapi.com

# PostgreSQL database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/imdb?allowPublicKeyRetrieval=true&useSSL=false&ServerTimezone=UTC
spring.datasource.username=postgres
spring.datasource.password=georgia
```
Configure IMDB database credentials in liquibase.properties
 ```bash
 changeLogFile=db/changelog/db.changelog-master.xml
url=jdbc:postgresql://localhost:5432/imdb?allowPublicKeyRetrieval=true&useSSL=false&ServerTimezone=UTC
username=postgres
password=georgia
driver=org.postgresql.Driver

 ```

 ## Step-by-Step Guide
 **Prerequisites**

<h>Ensure you have the following installed on your system:<h>

- Java 11
- Maven
- PostgreSQL
- Git


 **Step 1: Clone the Repository**
  ```bash
git clone https://github.com/Grgeorgia/imdb_data_scraper
  ```
 **Step 2: Configure Database**
   ```bash
psql -U postgres
CREATE DATABASE imdb;
 ```

  **Step 3: Update Database Schema with Liquibase**
  ```bash 
mvn liquibase:update
  ```

**Step 4: Build and Package the Application**
  ```bash 
mvn clean install
  ```

  **Step 5: Run the Application**
  ```bash 
mvn spring-boot:run
  ```


## Usage
- Search Movies by Title
- Endpoint: /api/title/find/
- Method: GET
- Parameters: title (string)
- Example :
 ```bash
 http://localhost:9090/api/title/find/matrix
 ```

 ## Response Format
The API will return a JSON response with the following fields for each movie:
 ```bash
    {
        "title": "The Matrix",
        "runningTimeInMinutes": 136,
        "year": 1999,
        "actors": "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss"
    }
 ```
 ## Overview
- src/main/java/com/georgiagr/imdb_data_scraper
    - **controller**: Contains REST controllers.
    - **service**: Contains service classes.
    - **repository**: Contains repository interfaces.
    - **model**: Contains entity classes.
    - **response**: Contains response classes.
    - **configuration**: Contains config class.

## Main Components
- **ImdbService**: Handles logic for interacting with the IMDb API.
- **MovieController**: Defines REST endpoint for movie search and retrieval.
- **MovieDetailsResponse**: Represents the response structure for movie details.
- **MovieRepository**: JPA repository for Movie entities.
- **Movie**: Entity representing a movie with attributes like id, title, year, actors, running_time.
- **Config**: Config is a Java class designed to centralize configuration constants related to accessing and parsing IMDb data via a RapidAPI endpoint.
