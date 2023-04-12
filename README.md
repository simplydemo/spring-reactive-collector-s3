# spring-reactive-collector-s3
This is a demo application that collects data and ingests it to S3 bucket using the spring-webflux functional ways and the logback library.

<br>

## Checkout
```
git clone https://github.com/simplydemo/spring-reactive-collector-s3.git
```

<br>

## Build
```
mvn clean package -DskipTests=true 
```

<br>

## Run
```
java -jar -Daws.region=<your-aws-region> -Daws.bucket=<your-aws-bucket> -Daws.profile=<your-aws-profile> target/spring-reactive-collector-s3-1.0.0-SNAPSHOT.jar
```

<br>

## Build Image
```
docker build -t "spring-reactive-collector-s3:latest" -f ./cicd/docker/Dockerfile .
```

<br>

## Run Container

### for ecs service
ECS 서비스가 S3 를 액세스 할수있도록 TASK Role 을 구성 하여야 합니다. 
```
docker run --rm --name spring-reactive-collector-s3 \
  -e AWS_REGION="<your-aws-region>" \
  -e AWS_BUCKET="<your-aws-bucket>" \
  --publish "0.0.0.0:8080:8080" spring-reactive-collector-s3:latest
```

### for local test
```
docker run --rm --name spring-reactive-collector-s3 \
  -e AWS_REGION="<your-aws-region>" \
  -e AWS_BUCKET="<your-aws-bucket>" \
  -e AWS_PROFILE="<your-aws-profile>" \
  -v $HOME/.aws:/home/spring/.aws:ro \
  --publish "0.0.0.0:8080:8080" spring-reactive-collector-s3:latest
```

<br>

## Check
```
curl --location 'http://localhost:8080/api/collect' \
--header 'Content-Type: application/json' \
--data '{"id":"I1000","name":"mhit8m7dw6","birthday":"1977-11-27","height":175,"weight":73,"timestamp":1681270178378}'
```

<br>

## Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.5/maven-plugin/reference/html/#build-image)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/3.0.5/reference/htmlsingle/#web.reactive)

<br>

## Guides
The following guides illustrate how to use some features concretely:

* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)

