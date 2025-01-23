# Use uma imagem base oficial do Java com Maven
FROM maven:3.8.5-openjdk-17 AS build

# Defina o diretório de trabalho dentro do container
WORKDIR /app

# Copie os arquivos do projeto (pom.xml e src/) para o container
COPY pom.xml .
COPY src ./src

# Compile o projeto com o Maven
RUN mvn clean package -DskipTests

# --- Segunda etapa: construir a imagem final ---

# Use uma imagem base oficial do Java mais leve
FROM openjdk:17-jdk-alpine

# Defina o diretório de trabalho dentro do container
WORKDIR /app

# Copie o arquivo JAR gerado na etapa de build para a nova imagem
COPY --from=build /app/target/*.jar /app/app.jar

# Exponha a porta que o Spring Boot vai usar
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "app.jar"]