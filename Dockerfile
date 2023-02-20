FROM openjdk:11-jdk
ARG JAR_FILE=./build/libs/jhouse_server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "/app.jar","--spring.profiles.active=prod"]
