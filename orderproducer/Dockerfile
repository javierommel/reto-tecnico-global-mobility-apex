# Usar una imagen base de Java 17
FROM openjdk:21-jdk-slim

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el archivo JAR del microservicio al contenedor
COPY target/orderproducer-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto en el que corre la aplicación
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]