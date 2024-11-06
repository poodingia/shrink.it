FROM eclipse-temurin:17-jre-alpine

# Copy the application jar
COPY build/libs/keygeneration-0.0.1-SNAPSHOT.jar /app/keygeneration.jar

# Expose the port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "/app/keygeneration.jar"]