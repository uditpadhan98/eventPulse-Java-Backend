# --- Stage 1: Build the application ---
# Use a Maven image with a full JDK to build the project
FROM maven:3.9-eclipse-temurin-17 AS builder

# Set the working directory
WORKDIR /app

# Copy the Maven project definition files
COPY pom.xml .
COPY .mvn/ .mvn
COPY mvnw .

# Copy the source code
COPY src ./src

# Build the project and create the JAR file. This creates the /app/target directory.
RUN mvn package -DskipTests


# --- Stage 2: Create the final runtime image ---
# Use a lightweight JRE image to run the application
FROM eclipse-temurin:17-jre-jammy

# Set the working directory
WORKDIR /app

# IMPORTANT: Make sure this JAR file name matches the one in your target directory!
# Copy the JAR file from the 'builder' stage
COPY --from=builder /app/target/auth-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app will run on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]