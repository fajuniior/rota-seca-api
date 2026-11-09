# Usa a imagem oficial do Java (Eclipse Temurin) versão 21 em Alpine
FROM eclipse-temurin:21-jdk-alpine

# Fuso horário de Brasília dentro do container
ENV TZ=America/Sao_Paulo

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia todos os arquivos do projeto para dentro do container
COPY . ./

# Dá permissão de execução ao Maven Wrapper
RUN chmod +x mvnw

# Faz o build do projeto Spring Boot com Maven
# -DskipTests → ignora os testes durante o deploy
RUN ./mvnw -B -DskipTests clean package

# Expõe a porta utilizada pela aplicação
EXPOSE 8080

# Inicia a aplicação Spring Boot (memória limitada para o plano gratuito do Render)
CMD ["sh", "-c", "java -Xmx384m -jar target/*.jar"]
