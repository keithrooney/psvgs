FROM openjdk:11
COPY target/server-1.0-SNAPSHOT.jar /opt
CMD ["java", "-jar", "/opt/server-1.0-SNAPSHOT.jar"]
