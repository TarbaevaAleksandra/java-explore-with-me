FROM amazoncorretto:21
COPY stats-client/target/*.jar statsClientApp.jar
COPY stats-server/target/*.jar statsServerApp.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]