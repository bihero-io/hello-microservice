# 'Hello' word microservice

Its purpose is to return 'Hello' word by http api:
```bash
mvn clean package
java -jar target/hello-microservice-fat.jar
# using another terminal window
curl http://localhost:8080/ # Hello
```

## How to customize port
Edit src/conf/config.json
```json
{
	...
	"serverPort": 8081,
  	"serverHost": "0.0.0.0"
}
```
Run using config.json
```bash
java -jar target/hello-microservice-fat.jar -conf src/conf/config.json
```