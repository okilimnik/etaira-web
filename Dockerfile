FROM openjdk:11.0.9.1-jre
COPY . .
EXPOSE 8080
CMD ["java", "-jar", "target/workspace.jar"]