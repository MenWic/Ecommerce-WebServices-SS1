# Proyecto E-Commerce SS1

**SS1-WebServices** es una plataforma de comercio electrónico diseñada para ofrecer una solución completa de venta en línea. La plataforma cuenta con una **App Web** desarrollada en **Vue 3** y una **API REST** construida con **Spring Boot**, que interactúan entre sí para brindar una experiencia robusta tanto a los administradores como a los usuarios finales. El proyecto incluye funcionalidades clave como la gestión de productos, usuarios, pedidos, reportes y roles, permitiendo un control flexible y escalable del negocio.

## Características del Proyecto
- **App Web** desarrollada en **Vue 3** con un diseño modular y dinámico.
- **API REST** en **Spring Boot**, responsable de la lógica de negocio y la interacción con la base de datos.
- Gestión de usuarios, productos, pedidos y reportes.
- Control de roles y permisos para administradores y ayudantes.
- Integración con **MySQL** para la persistencia de datos.
- Documentación completa utilizando **Swagger** para probar y validar la API.

## Tecnologías Utilizadas
- **Frontend**: Vue 3, Pinia (estado global), Axios (comunicación HTTP).
- **Backend**: Spring Boot 2.7.3, Hibernate, MySQL.
- **Base de Datos**: MySQL 8.x.
- **Gestor de dependencias**: Maven.
- **Documentación de API**: Swagger.

## Instalación y Despliegue
### Requisitos previos:
- **Java 11+** [Instalación](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- **Node.js 14+** [Descargar](https://nodejs.org/en/download/)
- **Maven** [Instalación](https://maven.apache.org/install.html)
- **MySQL 8.x** [Instalación](https://dev.mysql.com/downloads/windows/installer/8.0.html)

### Instrucciones:
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/mwnwic/Ecommerce-WebServices-SS1
   cd Ecommerce-WebServices-SS1
   ```

2. **Instalar dependencias del Frontend** (carpeta `app_ecommerce`):
   ```bash
   cd app_ecommerce
   npm install
   ```

3. **Configurar el archivo `.env`** con la URL base de la API:
   ```bash
   VITE_API_BASE_URL="http://localhost:8080"
   ```

4. **Compilar y lanzar el Frontend**:
   ```bash
   npm run dev
   ```

5. **Compilar y ejecutar la API REST** (carpeta `APIecommerce`):
   ```bash
   cd ../APIecommerce
   mvn clean package
   mvn spring-boot:run
   ```

6. **Configurar MySQL**: Crear la base de datos en MySQL con el nombre `ayd1_db` y ejecutar la API para poblarla con datos iniciales.

## Autor
Este proyecto fue desarrollado por *[MenWic](https://github.com/MenWic)*

# Ecommerce-WebServices-SS1
Proyecto final (E-commerce con servicios web de otros compañeros) de Seminario de Sistemas 1, II Semestre 2024 
