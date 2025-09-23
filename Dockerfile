# Etapa 1: Build
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Instala Maven
RUN apk add --no-cache maven bash

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

# Define variáveis de ambiente corretas
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-d38u87vfte5s73cdcd3g-a.oregon-postgres.render.com:5432/tododb_q0ws
ENV SPRING_DATASOURCE_USERNAME=aluno
ENV SPRING_DATASOURCE_PASSWORD=pFTSDlaeyuPoTrwhyVlgL2pQSDcfAYcB
ENV SERVER_PORT=8080
ENV SERVER_ADDRESS=0.0.0.0
ENV SPRING_APPLICATION_NAME=todo

# Expõe porta da aplicação
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
