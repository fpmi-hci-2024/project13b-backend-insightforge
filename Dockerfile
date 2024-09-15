FROM eclipse-temurin:17 as build

LABEL maintainer="Semyon Shilovskiy <fpm.shilovsk@bsu.by>"

WORKDIR /bookstore

COPY [".", "."]

RUN chmod +x ./gradlew

RUN ./gradlew build -x check

FROM eclipse-temurin:17-alpine

WORKDIR /bookstore

COPY --from=build /bookstore/build/libs/bookstore-0.0.1.jar .

EXPOSE 8080 8000

CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000","-Dfile.encoding=UTF-8", "-jar", "bookstore-0.0.1.jar", "--spring.profiles.active=local"]
