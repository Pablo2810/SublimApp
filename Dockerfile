# Usa una imagen base de OpenJDK 11
FROM openjdk:11

# Instalar wget y crear el directorio para Jetty
RUN apt-get update && apt-get install -y wget && \
    mkdir -p /opt/jetty && \
    wget https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-runner/9.4.45.v20220203/jetty-runner-9.4.45.v20220203.jar -O /opt/jetty/jetty-runner.jar

# Copiar el archivo WAR generado en la carpeta target
COPY target/SublimApp.war /opt/jetty/webapps/ROOT.war

# Exponer el puerto en el que Jetty corre (por defecto 8080)
EXPOSE 8080

# Ejecutar Jetty para lanzar la aplicación
CMD ["java", "-jar", "/opt/jetty/jetty-runner.jar", "/opt/jetty/webapps/ROOT.war"]


