FROM eclipse-temurin:17-jdk-alpine
COPY build/libs/web-1.0-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]

EXPOSE 8080