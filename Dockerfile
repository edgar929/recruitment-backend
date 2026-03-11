# Build the JAR on your machine first: ./mvnw.cmd package -DskipTests
# Then this image only copies the JAR – no Maven inside Docker, so no hanging.

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN addgroup -g 1000 app && adduser -u 1000 -G app -D app

COPY target/*.jar app.jar

RUN mkdir -p /app/storage/cv && chown -R app:app /app

USER app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
