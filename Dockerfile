FROM openjdk:21-jdk-slim
WORKDIR /redis-app
COPY target/redis-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]