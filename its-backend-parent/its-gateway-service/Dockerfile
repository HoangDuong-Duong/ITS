FROM adoptopenjdk/openjdk11:alpine
VOLUME /logs
VOLUME /config
EXPOSE 8681 8680
COPY its-gateway-service-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]