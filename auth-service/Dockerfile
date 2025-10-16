# Etapa de build con Maven + JDK 17
FROM maven:3.9.2-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copiamos pom.xml y bajamos dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente
COPY src ./src

# Compilamos el proyecto y generamos el jar
RUN mvn clean package -DskipTests

# Etapa de runtime solo con JDK 17
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copiamos el jar generado desde la etapa de build
COPY --from=build /app/target/Usuarios-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto del servicio
EXPOSE 8081

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
