FROM openjdk:8
EXPOSE 8080
ADD /build/libs/springboot-assignment-0.0.1-SNAPSHOT.jar springboot-assignment-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","springboot-assignment-0.0.1-SNAPSHOT.jar"]