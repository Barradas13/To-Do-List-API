# Usa uma imagem Java como base
FROM eclipse-temurin:21-jdk-alpine

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o jar gerado pelo Maven ou Gradle
COPY demo/target/*.jar app.jar

# Exponha a porta que sua aplicação usa (geralmente 8080)
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
