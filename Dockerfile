FROM ubuntu:latest
RUN apt-get update && apt-get install -y openjdk-17-jdk
COPY target/finalProject-0.0.1-SNAPSHOT.jar /app.jar
COPY entrypoint.sh /entrypoint.sh
RUN chmod 755 /entrypoint.sh
WORKDIR /
ENTRYPOINT ["./entrypoint.sh"]