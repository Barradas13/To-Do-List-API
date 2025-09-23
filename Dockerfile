# Etapa 1: Build
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Instala Maven
RUN apk add --no-cache maven

# Copia pom.xml e baixa dependências para cache
COPY demo/pom.xml .
RUN mvn -B -f pom.xml dependency:go-offline

# Copia o código e gera o JAR
COPY demo/src ./src
RUN mvn -f pom.xml clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copia o JAR gerado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
