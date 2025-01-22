FROM openjdk:17-jdk-alpine
MAINTAINER com.qre
COPY qr_gen/qr_gen_api/target/qr_gen_api-1.0-SNAPSHOT.jar qr_gen.jar
ENTRYPOINT ["java", "-jar", "/qr_gen.jar"]
EXPOSE 8080
