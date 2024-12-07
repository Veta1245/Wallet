FROM amazoncorretto:17
COPY target/*.jar walletAPI-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "walletAPI-0.0.1-SNAPSHOT.jar"]