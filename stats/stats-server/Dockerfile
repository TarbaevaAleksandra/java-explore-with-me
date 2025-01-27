FROM amazoncorretto:21
COPY target/*.jar statsServerApp.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /statsServerApp.jar"]