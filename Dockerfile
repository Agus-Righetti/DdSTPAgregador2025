# Importing JDK and copying required files
FROM maven:3.8.6-openjdk-18 AS build
COPY . .
run mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
copy --from=build /target/my-app-name-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENV JDBC_DATABASE_URL="jdbc:postgresql://dpg-d30shp15pdvs73fqpacg-a.oregon-postgres.render.com/db_dds_agregador?sslmode=require&user=db_dds_agregador_user&password=W1lno6CnpLIMm4caMDB138sKOF6VqehZ"
ENTRYPOINT ["java","-jar","/app.jar"]