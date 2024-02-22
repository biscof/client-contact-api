FROM eclipse-temurin:21-jdk as build

WORKDIR .

COPY . .

RUN ./gradlew bootJar

FROM bellsoft/liberica-openjdk-alpine-musl:21 as main

WORKDIR .

COPY --from=build build/libs/client-contact-api-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "client-contact-api-0.0.1-SNAPSHOT.jar"]
