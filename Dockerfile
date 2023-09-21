FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /workspace
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-oracle
COPY --from=build /workspace/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
