# Use an official Java 17 runtime image
FROM eclipse-temurin:17-jre-jammy

# Set the working directory inside the container
WORKDIR /app

# This copies your compiled Java code into the container.
# IMPORTANT: Change the JAR file name to match yours exactly.
COPY target/auth-0.0.1-SNAPSHOT.jar app.jar

# Tell Render that your app uses port 8080
EXPOSE 8080

# This is the command that starts your app
ENTRYPOINT ["java", "-jar", "app.jar"]