FROM eclipse-temurin:17-jre-alpine

# Copy the application jar
COPY build/libs/appserver-0.0.1-SNAPSHOT.jar /app/appserver.jar

# Expose the port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "/app/appserver.jar"]
