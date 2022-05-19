## Teams & contacts application
This a basic application which allows a user to create/update/delete teams and contact persons for the teams. 
The application consists of a Spring Boot-based backend server and an Angular-based frontend. The frontend is served from within backend, which is containerized via using Docker compose.
The backend provides REST APIs with hyper-media (HATEOAS) support for the CRUD operations for teams and contacts. It uses H2 embedded database server to store teams and contacts data.
The frontend shows the paginated lists of the teams and contacts and contains UI controls for CRUD operations on them.

## Building and running the application
Build the project with maven via the following command at the root directory:

```
mvn clean install
```

Then change into *teams-contacts-server/* project directory and run the following command:

```
docker compose up
```

After that, you can connect to *localhost:9090* in your browser and interact with the web application.
