# Use uma imagem base oficial do Java
FROM openjdk:17-jdk-alpine

# Defina o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo JAR (ou WAR) para o container
COPY backend /app/seu-aplicativo.jar

# Exponha a porta que o Spring Boot vai usar
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "seu-aplicativo.jar"]