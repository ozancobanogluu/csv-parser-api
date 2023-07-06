# CSV Parser

The CSV Parser is a Java-based application that allows you to parse and manipulate CSV (Comma-Separated Values) files. It provides functionality to upload CSV data, retrieve data by code, and perform other operations.

## Features

- Upload CSV Data: Upload a CSV file containing data and store it in the system.
- Retrieve Data by Code: Retrieve CSV data based on a specific code.
- Export CSV Data: Export all CSV data as a downloadable CSV file.
- Delete All Data: Delete all stored CSV data.

## Technologies Used

- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- H2 Database
- MapStruct
- OpenCSV
- Maven
- Swagger UI

## Getting Started

### Prerequisites

- Java 17 or later
- Maven

### Installation

1. Clone the repository:

   ```shell
   git clone https://github.com/your-username/csv-parser.git

2. Navigate to the project directory:

    ```shell
    cd csv-parser

3. Build the project using Maven:

    ```shell
    mvn clean install

4. Run the application:

    ```shell
    mvn spring-boot:run

5. Access the application in your browser at: http://localhost:8080.

### Swagger API Documentation
The CSV Parser project integrates Swagger UI for API documentation and exploration. Swagger UI provides a user-friendly interface to view and interact with the available API endpoints.

To access the Swagger UI:

1. Open your browser and go to: http://localhost:8080/swagger-ui.html.

2. You will see a list of available endpoints, request/response details, and the ability to test the API directly from the Swagger UI interface.

3. Explore the endpoints and use the provided features to understand and interact with the CSV Parser API.

## Usage
### Upload CSV Data

    Endpoint: /api/upload
    Method: POST
    Request Body: CSV file
    Upload a CSV file containing data to be stored in the system.

### Fetch CSV Data

    Endpoint: /api/fetchAll
    Method: GET
    Export all CSV data as a downloadable CSV file.

### Fetch Data by Code

    Endpoint: api/code/{code}
    Method: GET
    Retrieve CSV data based on a specific code.

### Delete All Data

    Endpoint: /api/delete/all
    Method: DELETE
    Delete all stored CSV data.