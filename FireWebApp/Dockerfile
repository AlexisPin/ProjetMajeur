FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/fire.jar fire.jar
ENTRYPOINT ["java","-jar","/fire.jar"]