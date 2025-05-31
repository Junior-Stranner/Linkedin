# 1. Imagem base: JDK para rodar Java
FROM openjdk:22-jdk-alpine

# 2. Diretório de trabalho dentro do container
WORKDIR /app

# 3. Copia o arquivo .jar gerado pelo build para dentro do container
COPY target/backend.jar backend.jar

# 4. Expõe a porta 8080 para acesso externo
EXPOSE 8080

# 5. Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "backend.jar"]
