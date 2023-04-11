# Read Me First
The following was discovered as part of building this project:

* The original package name 'io.github.simplydemo.webflux-data-collector-s3' is invalid and this project uses 'io.github.simplydemo.webfluxdatacollectors3' instead.

## Build
```
mvn clean package -DskipTests=true 
```

## Run
```
java -jar -noverify target/spring-reactive-collector-s3-1.0.0-SNAPSHOT.jar
```

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.5/maven-plugin/reference/html/#build-image)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/3.0.5/reference/htmlsingle/#web.reactive)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)

