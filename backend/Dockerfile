# Use uma imagem do Java com Maven para construir
FROM maven:3.8.5-openjdk-17 AS build

# Defina o diretório de trabalho dentro do container
WORKDIR /app

# Copie os arquivos pom.xml e src para o diretório de trabalho
COPY pom.xml .
COPY src ./src

# Compile e empacote a aplicação Spring Boot
RUN mvn clean package -DskipTests

# usa uma imagem menor para a aplicação
FROM eclipse-temurin:17-jre-alpine

# Defina o diretório de trabalho
WORKDIR /app

# Copie o arquivo JAR da etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponha a porta que a aplicação Spring Boot utiliza
EXPOSE 8080

# Usuário não root para executar a aplicação por seguraça
USER nobody

# Comando para executar a aplicação Spring Boot
CMD ["java", "-jar", "app.jar"]