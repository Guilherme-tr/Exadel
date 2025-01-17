FROM openjdk:22-jdk-slim AS build
WORKDIR /app

# Copia o arquivo JAR para a imagem
COPY target/*.jar app.jar

# Define a variável de ambiente para otimizar a execução do Java
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:InitialRAMPercentage=50 -XX:MaxRAMPercentage=80"

# Expõe a porta do aplicativo
EXPOSE 8080

# Comando de entrada para rodar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]