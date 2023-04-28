# My Application
This spring boot application ...

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

To run this project, you'll need to have the following software installed on your system:

- Java Development Kit (JDK) - version 17 or higher
- [Gradle](https://gradle.org/install/) - version 7.6 or higher

### Installing

To get started, follow these steps:

1. Install PostgreSQL relational database and apply MyApp DDL and DML scripts.
   TODO: Data repo details

2. Clone this repository to your local machine:
   ```bash
   git clone https://github.com/your-username/your-project.git
   ```

3. Navigate to the project directory:
   ```bash
   cd your-project
   ```
   
4. Copy or rename the file `.env.example` to `.env` and replace the values in
angle brackets with those appropriate for your environment.

5. Build the project into an executable JAR file using Gradle:
   ```bash
   ./gradlew build
   ```

### Running the Application
To run the application fom source, use the following command:

   ```bash
   ./gradlew bootRun
   ```

To run the application from executable JAR file:
   ```bash
   java -jar build/libs/myapp-backend-0.0.1-SNAPSHOT.jar
   ```

The application will start up and be available at http://localhost:8080. 
You can now access the application in your web browser or use a tool like 
curl to make HTTP requests to the server.

## Built With
- Spring Boot - The web framework used
- Gradle - Dependency Management
- Thymeleaf - Template engine for rendering HTML templates
- PostgreSQL - Database used for persistence

## Authors
- Your Name - [yourgithub](https://github.com/yourgithub)

## License
Copyright (c) You - 2023. All rights reserved.