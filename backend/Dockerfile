FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw -B dependency:go-offline

COPY src src
RUN ./mvnw -B clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

RUN groupadd --system app && useradd --system --gid app app

COPY --from=build /app/target/*.jar app.jar
RUN mkdir -p /app/uploads/user/profile && chown -R app:app /app

USER app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
