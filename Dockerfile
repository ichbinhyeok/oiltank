FROM bellsoft/liberica-openjdk-alpine:21 AS build
WORKDIR /app

COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -DskipTests

COPY src src

RUN ./mvnw package -DskipTests

FROM bellsoft/liberica-openjre-alpine:21
WORKDIR /app

COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTS:--XX:+UseG1GC -Xms256m -Xmx384m -Xss512k -Djava.security.egd=file:/dev/./urandom} -jar /app/app.jar"]
