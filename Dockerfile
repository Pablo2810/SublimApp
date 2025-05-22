# Usa la imagen base de OpenJDK 11
FROM openjdk:11

# Define una variable para la versión de Jetty
ENV JETTY_VERSION=9.4.56.v20240826
ENV JETTY_HOME=/opt/jetty

# Descarga y extrae Jetty
RUN apt-get update && apt-get install -y wget && \
    mkdir -p $JETTY_HOME && \
    wget https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-distribution/${JETTY_VERSION}/jetty-distribution-${JETTY_VERSION}.tar.gz && \
    tar -xzvf jetty-distribution-${JETTY_VERSION}.tar.gz -C $JETTY_HOME --strip-components=1 && \
    rm jetty-distribution-${JETTY_VERSION}.tar.gz

# Copia el archivo WAR de tu proyecto al directorio webapps de Jetty
# Mantenemos el nombre SublimApp.war para que se acceda vía /SublimApp
COPY target/SublimApp.war $JETTY_HOME/webapps/ROOT.war

# Expone el puerto por defecto
EXPOSE 8080

# Me paro en el directorio Jetty
WORKDIR $JETTY_HOME

# Ejecutar Jetty
CMD ["java", "-jar", "./start.jar"]

#construir imagen nueva: docker build -t sublimapp-jetty .
#crear el contenedor con nombre: docker run -d -p 8080:8080 --name sublimapp-web sublimapp-jetty
#verificar que este corriendo: docker ps
#vista del contenedor: http://localhost:8080/
#ver logs en tiempo real: docker logs -f sublimapp-web
#Iniciar el contenedor: docker start sublimapp-web
#Checkear que el contenedor este activo: docker ps


