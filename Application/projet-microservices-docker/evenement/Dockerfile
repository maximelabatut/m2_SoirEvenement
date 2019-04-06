FROM java:8-jre
ENV PORT=8083
ENV DISCOVERY_URL="http://eurekaserver:8761"
ADD target/evenement.jar evenement.jar
ENTRYPOINT ["java", "-jar", "/evenement.jar"]
EXPOSE 8083
