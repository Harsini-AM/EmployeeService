FROM openjdk:17-jdk-alpine
EXPOSE 8080
ADD target/Employee.jar Employee.jar
ENTRYPOINT ["java", "-jar", "/Employee.jar"]