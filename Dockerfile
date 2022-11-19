# syntax=docker/dockerfile:1
#FROM openjdk:11.0.15-oraclelinux7
#
#WORKDIR /app
#
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#
#RUN ./mvnw dependency:go-offline
#COPY src ./src
#
#CMD ["./mvnw", "spring-boot:run"]

FROM openjdk:11.0.15-oraclelinux7
EXPOSE 8080
ADD target/demo-0.0.1-SNAPSHOT.jar ElevatorBackend.jar
ENTRYPOINT ["java", "-jar", "/ElevatorBackend.jar"]