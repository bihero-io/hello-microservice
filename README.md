# 'Hello' word microservice

Its purpose is to return 'Hello' word by http api:
```bash
mvn clean package
java -jar target/hello-microservice-fat.jar
# using another terminal window
curl http://localhost:8081/ # Hello
```