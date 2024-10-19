FROM bellsoft/liberica-openjdk-alpine:17

CMD ["./gradlew", "clean", "build"]

WORKDIR /app

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} /app/app.jar

ARG PROFILE=prod
ENV SPRING_PROFILES_ACTIVE=${PROFILE}

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]