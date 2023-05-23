FROM openjdk:17
ADD target/alltodo-api.jar alltodo-api.jar
ENTRYPOINT ["java", "-jar", "/alltodo-api.jar"]