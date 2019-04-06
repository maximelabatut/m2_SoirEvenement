FROM java:8-jre
ENV PORT=8081
ENV DISCOVERY_URL="http://eurekaserver:8761"
ADD target/authentification.jar authentification.jar
ENTRYPOINT ["java", "-jar", "/authentification.jar"]
EXPOSE 8081

