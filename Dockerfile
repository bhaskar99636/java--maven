FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} Docker-EmployeeServices-1.0.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/Docker-EmployeeServices-1.0.jar"]
