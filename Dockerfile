FROM openjdk:11
ARG JAR_FILE=target/google-analytics-example.jar

ENV GA_API_SECRET=apiSecret
ENV GA_MEASUREMENT_ID=measurementId

WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
