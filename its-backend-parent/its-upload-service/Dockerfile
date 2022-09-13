FROM adoptopenjdk/openjdk11:alpine
VOLUME /logs
VOLUME /config
EXPOSE 8683 8883
COPY its-upload-service-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","-Xmx2048m","-Xss512k","/app.jar"]