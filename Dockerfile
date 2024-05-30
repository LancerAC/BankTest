FROM openjdk:17

COPY target/*.jar MyApp.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "MyApp.jar"]