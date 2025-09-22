# Etapa 1: Build
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copia o pom.xml e baixa dependências primeiro (para cache melhor)
COPY demo/pom.xml .
RUN apk add --no-cache maven
RUN mvn -B -f pom.xml dependency:go-offline

# Copia o restante do código e gera o JAR
COPY demo/src ./src
RUN mvn -f pom.xml clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copia o JAR gerado da etapa anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
