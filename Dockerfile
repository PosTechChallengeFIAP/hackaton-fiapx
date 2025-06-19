FROM openjdk:17-jdk-slim

ENV JAVA_HOME=/usr/local/openjdk-17
ENV PATH="$JAVA_HOME/bin:$PATH"

RUN apt-get update && apt-get install -y maven

WORKDIR /home/java/app

ENTRYPOINT ["mvn", "spring-boot:run"]