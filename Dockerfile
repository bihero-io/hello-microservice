FROM adoptopenjdk/openjdk11:alpine-jre
COPY target/hello-microservice-fat.jar app.jar
COPY src/conf/config.json .
COPY src/conf/logback-console.xml .
COPY run.sh .
RUN chmod +x run.sh
CMD ["./run.sh"]