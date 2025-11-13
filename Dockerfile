# Use official OpenJDK 21 image
FROM eclipse-temurin:21-jdk-jammy

LABEL authors="DaBram"

# Set working directory
WORKDIR /app

# Copy the built Spring Boot jar
COPY target/bram_vortex_Oauth2-0.0.1-SNAPSHOT.jar bram_vortex_Oauth2.jar

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "bram_vortex_Oauth2.jar"]
