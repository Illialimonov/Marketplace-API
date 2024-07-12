FROM openjdk:21-jdk-slim-buster
# Start with a base image containing Java runtime
# Set the working directory inside the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/Marketplace-API-0.0.1-SNAPSHOT.jar /app/Marketplace-API-0.0.1-SNAPSHOT.jar

# Environment variables
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/mydb
ENV SPRING_DATASOURCE_USERNAME=user
ENV SPRING_DATASOURCE_PASSWORD=password

# Expose the port on which your Spring Boot application runs
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java","-jar","Marketplace-API-0.0.1-SNAPSHOT.jar"]