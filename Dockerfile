FROM openjdk:11.0.9.1-jre
EXPOSE 8080
CMD ["java", "-jar", "target/workspace.jar"]