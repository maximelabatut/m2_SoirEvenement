FROM java:8-jre
ENV PORT=8080
ENV DISCOVERY_URL="http://eurekaserver:8761"
ADD target/gateway.jar gateway.jar
ENTRYPOINT ["java", "-jar", "/gateway.jar"]
EXPOSE 8080
