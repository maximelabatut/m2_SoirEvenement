FROM java:8-jre
ENV PORT=8084
ENV DISCOVERY_URL="http://eurekaserver:8761"
ADD target/soiree.jar soiree.jar
ENTRYPOINT ["java", "-jar", "/soiree.jar"]
EXPOSE 8084

