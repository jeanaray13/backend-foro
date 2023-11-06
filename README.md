# Backend Foro

Proyecto desarrollado usando Full Stack (Spring Boot y React)

Este proyecto es una aplicación donde el usuario pueda dejar mensajes en un foro, en donde, tiene la posibilidad de responder mínimo en dos niveles.

Para esta parte, se tiene las siguientes características:

* **Almacenamiento:** JSON

* **Backend:** Spring Boot
  
* **Frontend:** https://github.com/jeanaray13/frontend-foro

## Descripción
La sección del backend se fundamenta en Spring Boot para el desarrollo de aplicaciones Java, además, la inclusión de Swagger UI proporciona una interfaz gráfica amigable para interactuar con los servicios, simplificando la documentación y permitiendo utilizar fácilmente los puntos finales de la API.

**URL del Backend:** http://localhost:9000/swagger-ui.html

![image](https://github.com/jeanaray13/backend-foro/blob/main/Snapshots/backend-swaggerUI.png)

## Requerimientos

* **JDK17:** https://www.oracle.com/es/java/technologies/downloads/#java8-windows
* **Swagger UI:** https://mvnrepository.com/artifact/io.springfox/springfox-swagger-ui/2.9.2
* **Swagger 2:** https://mvnrepository.com/artifact/io.springfox/springfox-swagger2/2.9.2
* **v2.3.2 RELEASE:** https://mvnrepository.com/artifact/org.springframework.boot/spring-boot/2.3.2.RELEASE
* **Descargar proyecto en Spring Initializr:** https://start.spring.io/ (Agregar la dependencia de "Spring Web" y el proyecto tipo "Maven")

Con toda la información anterior, se termina configurando el archivo **"pom.xml"** para que exista compatibilidad con las versiones de Swagger UI:

https://github.com/jeanaray13/backend-foro/blob/main/pom.xml

## Ejecución

1. Clonar el proyecto:

`git clone https://github.com/jeanaray13/backend-foro.git`

2. Compilar el proyecto dentro de la carpeta "backend-foro":
   
`mvn clean install` o `mvnm clean install`

2. Ejecutar la aplicación dentro de la carpeta "backend-foro":

`java -jar target/backend-foro-0.0.1-SNAPSHOT.jar`

