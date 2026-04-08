FROM eclipse-temurin:25-jdk-jammy

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo JAR que genera Spring Boot
# Asegúrate de que el nombre coincida con el que genera tu Maven
COPY target/*.jar app.jar

ENV PORT=7860
EXPOSE 7860

# Ejecutamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=7860"]