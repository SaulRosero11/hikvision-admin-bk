# Prueba con esta versión que es más robusta
FROM eclipse-temurin:17-jdk

# 2. Carpeta de trabajo dentro del contenedor
WORKDIR /app

# 3. Copiar el JAR desde tu carpeta target local al contenedor
# Usamos el comodín *.jar para que no importe si la versión cambia
COPY target/*.jar app.jar

# 4. Exponer el puerto de Spring Boot
EXPOSE 8080

# 5. Comando para arrancar la app
ENTRYPOINT ["java", "-jar", "app.jar"]