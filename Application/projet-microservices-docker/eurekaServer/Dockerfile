FROM java:8-jre
ENV PORT=8761
ADD target/eurekaServer.jar eurekaServer.jar
ENTRYPOINT ["java", "-jar", "/eurekaServer.jar"]
EXPOSE 8761
