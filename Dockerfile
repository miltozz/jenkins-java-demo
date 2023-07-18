FROM openjdk:8-jre-alpine

EXPOSE 8080

COPY ./target/java-demo-app-*.jar /usr/app/
WORKDIR /usr/app

CMD java -jar java-demo-app-*.jar
