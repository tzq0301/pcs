FROM openjdk:8-jdk-alpine
COPY target/*.jar gateway.jar
EXPOSE 12100
ENTRYPOINT ["java", "-jar", "/gateway.jar"]