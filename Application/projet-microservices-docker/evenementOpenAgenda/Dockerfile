FROM java:8-jre
ENV PORT=8085
ENV DISCOVERY_URL="http://eurekaserver:8761"
ADD target/evenementopenagenda.jar evenementopenagenda.jar
CMD ["java","-Xmx200m","-jar","/evenementopenagenda.jar"]

EXPOSE 8085
