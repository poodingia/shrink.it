FROM eclipse-temurin:17-jre-alpine

# Copy the application jar
COPY build/libs/servicediscovery-0.0.1-SNAPSHOT.jar /app/servicediscovery.jar

# Expose the port
EXPOSE 8761

# Run the application
ENTRYPOINT ["java", "-jar", "/app/servicediscovery.jar"]
