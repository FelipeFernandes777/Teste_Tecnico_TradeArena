FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /order

COPY pom.xml .
COPY src ./src

RUN apk add --no-cache maven && \
    mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /order

COPY --from=builder /order/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]