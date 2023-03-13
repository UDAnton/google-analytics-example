# Google analytics example
Application example that will show hot send events to GA4 using measurement protocol 

## Build spring boot application .jar with maven
```
mvn clean package
```

## Build docker image
```
docker build -t google-analytics-example .
```

## Run docker container with you GA4 api secret and measurement Id
```
docker run --name google-analytics-example -e GA_API_SECRET=**your_api_secret** -e GA_MEASUREMENT_ID=**your_measurement_id** -p 80:8080 google-analytics-example
```
