FROM amazoncorretto:17-alpine

LABEL name="spring-reactive-collector-s3" version=1.0.0-snapshot \
 io.github.symplesims.image.created="2023-04-12" \
 io.github.symplesims.version="1" \
 io.github.symplesims.title="spring-reactive-collector-s3" \
 io.github.symplesims.description="This is a demo application that collects data and ingests it to S3 bucket using the spring-webflux functional ways and the logback library." \
 io.github.symplesims.source="https://github.com/simplydemo/spring-reactive-collector-s3"

RUN addgroup -S spring && adduser -S spring -G spring

ARG SPRING_PROFILE
ARG AWS_REGION
ARG AWS_BUCKET
ARG AWS_PROFILE

ENV SPRING_PROFILE ${SPRING_PROFILE}
ENV AWS_REGION ${AWS_REGION}
ENV AWS_BUCKET ${AWS_BUCKET}
ENV AWS_PROFILE ${AWS_PROFILE}

# RUN apk add --no-cache bash curl

WORKDIR /app

COPY ./target/*.jar /app/springApp.jar

# COPY ./cicd/docker/entrypoint.sh /app/
# RUN chmod +x /app/entrypoint.sh

USER spring:spring

EXPOSE 8080

CMD [ "sh", "-c", "java -jar -Daws.region=${AWS_REGION} -Daws.bucket=${AWS_BUCKET} -Daws.profile=${AWS_PROFILE} -Dspring.profiles.active=${SPRING_PROFILE} /app/springApp.jar" ]
