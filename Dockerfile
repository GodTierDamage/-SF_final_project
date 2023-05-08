FROM openjdk:17-jdk-alpine
RUN apk update --update-cache && apk add --no-cache maven
ENV PATH="/usr/local/apache-maven/apache-maven-3.8.1/bin:${PATH}"
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY target/finalProject-0.0.1-SNAPSHOT.jar /app/
CMD ["java", "-jar", "/app/finalProject-0.0.1-SNAPSHOT.jar"]