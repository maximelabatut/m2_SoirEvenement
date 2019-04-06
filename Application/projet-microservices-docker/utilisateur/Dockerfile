FROM java:8-jre
ENV PORT=8082
ENV DISCOVERY_URL="http://eurekaserver:8761"
ADD target/utilisateur.jar utilisateur.jar
ENTRYPOINT ["java", "-jar", "/utilisateur.jar"]
EXPOSE 8082

