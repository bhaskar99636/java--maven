FROM ubuntu
ARG JAR_FILE=target/*.jar
COPY ./target/demo-2.0-SNAPSHOT.jar
EXPOSE 8085
ENTRYPOINT ["java -jar","/demo-2.0-SNAPSHOT.jar"]
