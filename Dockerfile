FROM openjdk:8-jre-alpine

EXPOSE 8080

COPY ./target/jda-1.0.0.jar /usr/app/
WORKDIR /usr/app

ENTRYPOINT ["java", "-jar", "jda-1.0.0.jar"]
