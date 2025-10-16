# Etapa de build con Maven
FROM maven:3.9.2-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copiamos el pom.xml y descargamos dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente
COPY src ./src

# Construimos el jar
RUN mvn clean package -DskipTests

# Etapa final con solo JDK
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copiamos el jar desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto del servicio
EXPOSE 8083

# Comando para ejecutar la app
ENTRYPOINT ["java","-jar","app.jar"]
