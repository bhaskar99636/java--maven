FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/demo-2.0-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/demo-2.0-SNAPSHOT.jar"]
