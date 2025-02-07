
# Swift Data API

The Swift Data API allows users to access and manage SWIFT codes stored in a database. It provides a RESTful API to retrieve, add, and delete SWIFT code information for various banks and countries.

## Project Overview

### 1. **Parse SWIFT Code Data:**
   - SWIFT codes ending in "XXX" represent the headquarter of a bank, while others represent branches.
   - Branch codes are associated with a headquarter if their first 8 characters match.
   - Data is parsed from a provided Excel file.

### 2. **Store SWIFT Code Data:**
   - The parsed data is stored in a relational database (PostgreSQL) to enable fast querying.
   - Data is indexed by SWIFT codes and country ISO2 codes for efficient retrieval.

### 3. **Expose a REST API:**
   The application exposes the following endpoints to interact with SWIFT code data.

---

#### **Endpoint 1: Retrieve details of a single SWIFT code (headquarter or branch)**

- **GET**: `/v1/swift-codes/{swift-code}`

   This endpoint retrieves the details of a single SWIFT code, whether it represents a headquarter or a branch. 

   **Response**: 
   - If the SWIFT code represents a headquarter, it will include details of the headquarter and any associated branches.
   - If the SWIFT code represents a branch, it will return details for that specific branch.

---

#### **Endpoint 2: Return all SWIFT codes with details for a specific country (both headquarters and branches)**

- **GET**: `/v1/swift-codes/country/{countryISO2code}`

   This endpoint returns all SWIFT codes (both headquarter and branch) for a specific country, identified by the country ISO2 code.

   **Response**: 
   - The response includes a list of SWIFT codes for the country, with each SWIFT code containing its associated bank and address information.

---

#### **Endpoint 3: Add new SWIFT code entries to the database for a specific country**

- **POST**: `/v1/swift-codes/`

   This endpoint allows you to add a new SWIFT code entry to the database for a specific country. 

   **Request**: 
   - The request should include the SWIFT code details (address, bank name, country ISO2 code, etc.).

   **Response**: 
   - The response will confirm if the SWIFT code was successfully added to the database.

---

### Endpoint 4: Delete SWIFT Code

This endpoint allows you to delete a specific SWIFT code from the database. The deletion is based on the provided SWIFT code, bank name, and country ISO2 code. The data will be removed only if all three parameters match an existing record in the database.

#### **DELETE**: `/v1/swift-codes/{swift-code}`

#### **Parameters:**

- `swift-code` (Path Parameter): The SWIFT code that you wish to delete.
- `bankName` (Query Parameter): The name of the bank associated with the SWIFT code.
- `countryISO2` (Query Parameter): The ISO-2 code of the country where the bank is located.

#### **Example Request:**
http://localhost:8080/v1/swift-codes/CUIMPLP1XXX?bankName=AVIVA INVESTORS POLAND SPOLKA AKCYJNA W LIKWIDACJI&countryISO2=PL

---

# How to Run the Project

There are two ways to run the project: via **IntelliJ IDEA** or using **Docker Compose**.

## 1. Running the Project with IntelliJ IDEA and Maven

To run the project directly from IntelliJ IDEA:

1. **Open the Project in IntelliJ IDEA**:
   - Open IntelliJ IDEA and load the project by selecting **File** > **Open** and choosing the project directory.
   - Ensure that Maven is correctly configured in IntelliJ IDEA (usually it is by default).

2. **Run the Project**:
   - Once the project is loaded, click the green **Run** button in the top-right corner of IntelliJ IDEA.
   - Alternatively, right-click the main application file (the class with `@SwiftDataApiApplication`) and select **Run**.
   - The project will start running on port `8080` by default.


## 1. Running the Project with Docker Compose

You can run the project using Docker Compose, which sets up both the Spring Boot application and the PostgreSQL database in containers.

### Steps to Run with Docker Compose:

1. **Build and Start the Project**:

   Open a terminal in the project root directory and run the following command:

   ```bash
   docker-compose up --build
