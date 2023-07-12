FROM openjdk:8-jre-alpine

EXPOSE 8080

COPY ./target/java-demo-app-1.0.0.jar /usr/app/
WORKDIR /usr/app

ENTRYPOINT ["java", "-jar", "java-demo-app-1.0.0.jar"]
