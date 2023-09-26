#FROM openjdk:17
#ADD target/alltodo-api.jar alltodo-api.jar
#ENTRYPOINT ["java", "-jar", "/alltodo-api.jar"]

#
# Build stage
#
FROM maven:3.8.4-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:17
COPY --from=build /home/app/target/alltodo-api.jar /usr/local/lib/alltodo-api.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/alltodo-api.jar"]
