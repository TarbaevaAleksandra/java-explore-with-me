FROM amazoncorretto:21
COPY stats-server/target/*.jar statsServerApp.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /statsServerApp.jar"]