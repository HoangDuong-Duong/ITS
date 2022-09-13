FROM adoptopenjdk/openjdk11:alpine
VOLUME /logs
VOLUME /config
EXPOSE 8682 8112
COPY its-bff-service-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]