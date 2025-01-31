FROM amazoncorretto:21
COPY target/*.jar ewmServiseApp.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /ewmServiseApp.jar"]