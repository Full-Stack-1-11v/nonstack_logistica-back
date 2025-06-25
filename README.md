# Backend Microservicio de Logística - Perfulandia

Este proyecto desarrollado durante el ramo de "Fullstack I" es un microservicio de Spring Boot diseñado para gestionar todas las operaciones de logística de la plataforma "Perfulandia", es una parte del diseño de arquitectura de microservicios. Se encarga de la administración de envíos, guías de despacho, vehículos y rutas de entrega.

La API está versionada para facilitar la evolución y la compatibilidad:
*   **v1**: Endpoints RESTful tradicionales que devuelven datos en formato JSON.
*   **v2**: Endpoints que siguen los principios HATEOAS, enriqueciendo las respuestas con enlaces para descubrir otras acciones y recursos relacionados.

## Tecnologías Utilizadas

*   **Java 17**
*   **Spring Boot 3**
*   **Maven**: Gestor de dependencias y construcción del proyecto.
*   **Spring Data JPA**: Para la persistencia de datos.
*   **Spring Web**: Para la creación de la API REST.
*   **H2 Database**: Base de datos en memoria para pruebas.
*   **PostgreSQL**: Base de datos relacional para producción (configurable).
*   **Spring Cloud OpenFeign**: Para la comunicación declarativa con otros microservicios.
*   **Spring HATEOAS**: Para la implementación de los endpoints de la v2.
*   **Swagger (OpenAPI 3)**: Para la documentación interactiva de la API.
*   **JaCoCo**: Para la generación de reportes de cobertura de pruebas.
*   **JUnit 5 & Mockito**: Para las pruebas unitarias y de integración.
*   **spring-dotenv**: Para la gestión de variables de entorno.

## Primeros Pasos

Sigue estos pasos para configurar y ejecutar el proyecto en tu entorno local.

### Prerrequisitos

*   JDK 17 o superior.
*   Maven 3.8 o superior.
*   Una instancia de PostgreSQL en ejecución (opcional, para producción).

### Configuración

1.  **Clonar el repositorio:**
    ```bash
    git clone <URL_DEL_REPOSITORIO>
    cd nonstack_logistica-back
    ```

2.  **Configurar las variables de entorno:**
    Esta aplicación utiliza `spring-dotenv` para gestionar datos sensibles. Debes crear un archivo `.env` en el directorio `src/main/resources/`. Puedes usar el archivo `.env.template` como guía.
    ```
    # Contenido de tu archivo .env
    DB_URL=jdbc:postgresql://localhost:5432/tu_base_de_datos
    DB_USERNAME=tu_usuario
    DB_PASSWORD=tu_contraseña
    ```

### Ejecución

Puedes compilar y ejecutar la aplicación usando el siguiente comando de Maven:

```bash
mvn spring-boot:run
```

La aplicación se iniciará y estará disponible en `http://localhost:8080`.

## Documentación de la API

La documentación completa y interactiva de la API está disponible a través de Swagger UI. Una vez que la aplicación esté en ejecución, puedes acceder a ella en los siguientes enlaces:

*   **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
*   **Definición OpenAPI (JSON)**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Endpoints Principales

A continuación se detallan los recursos principales y algunos de sus endpoints. Para ver la lista completa y probarlos, por favor, utiliza el enlace de **Swagger UI**.

---

### 🚚 Envíos (`/api/{v1|v2}/logistica/envios`)

Gestiona la información de los envíos de órdenes.

*   **`GET /api/v1/logistica/envios`**: Obtiene una lista de todos los envíos.
*   **`GET /api/v2/logistica/envios`**: Obtiene una colección HATEOAS de todos los envíos.
*   **`GET /api/v1/logistica/envios/{id}`**: Obtiene un envío específico por su ID.
*   **`POST /api/v2/logistica/envios`**: Crea un nuevo envío.
    *   **Ejemplo de `curl`**:
        ```bash
        curl -X POST http://localhost:8080/api/v2/logistica/envios \
        -H "Content-Type: application/json" \
        -d '{
              "idCliente": 999,
              "idOrden": 1,
              "fechaEntrega": "2024-12-11",
              "entregado": false,
              "observacion": "Cliente solicita llamar antes de llegar.",
              "guiaDespacho": { "idDespacho": 1 },
              "vehiculoDespacho": { "idVehiculoDespacho": 1 },
              "ruta": { "idRuta": 1 }
            }'
        ```

---

### 📄 Guías de Despacho (`/api/{v1|v2}/logistica/despachos`)

Administra las guías de despacho asociadas a los envíos.

*   **`GET /api/v1/logistica/despachos`**: Obtiene una lista de todas las guías de despacho.
*   **`GET /api/v2/logistica/despachos/{id}`**: Obtiene una guía de despacho específica con enlaces HATEOAS.
*   **`POST /api/v1/logistica/despachos`**: Crea una nueva guía de despacho.
    *   **Ejemplo de `curl`**:
        ```bash
        curl -X POST http://localhost:8080/api/v1/logistica/despachos \
        -H "Content-Type: application/json" \
        -d '{
              "idEnvio": 1,
              "idOrden": 1
            }'
        ```

---

### 🗺️ Rutas (`/api/{v1|v2}/logistica/envios/rutas`)

Define y gestiona las rutas de entrega.

*   **`GET /api/v1/logistica/envios/rutas`**: Obtiene una lista de todas las rutas.
*   **`GET /api/v2/logistica/envios/rutas`**: Obtiene una colección HATEOAS de todas las rutas.
*   **`DELETE /api/v1/logistica/envios/rutas/{idRuta}`**: Elimina una ruta por su ID.

---

### 🚛 Vehículos de Despacho (`/api/{v1|v2}/logistica/vehiculos`)

Gestiona los vehículos utilizados para los despachos.

*   **`GET /api/v1/logistica/vehiculos`**: Obtiene una lista de todos los vehículos.
*   **`GET /api/v2/logistica/vehiculos/{patente}`**: Obtiene un vehículo por su patente con enlaces HATEOAS.
*   **`POST /api/v1/logistica/vehiculos`**: Registra un nuevo vehículo.
    *   **Ejemplo de `curl`**:
        ```bash
        curl -X POST http://localhost:8080/api/v1/logistica/vehiculos \
        -H "Content-Type: application/json" \
        -d '{
              "patente": "BC-DF-21",
              "ano": 2025
            }'
        ```

Y muchos endpoints mas que puedes encontrar en el package controller

## Pruebas y Cobertura

El proyecto cuenta con un conjunto de pruebas unitarias y de integración para garantizar la calidad y el correcto funcionamiento del código.

### Ejecutar Pruebas

Para ejecutar todas las pruebas del proyecto, utiliza el siguiente comando:

```bash
mvn test
```

### Informe de Cobertura

Para generar un informe de cobertura de código con JaCoCo, ejecuta el siguiente comando. Este compilará el código, ejecutará las pruebas y generará el informe.

```bash
mvn clean verify
```

El informe estará disponible en la siguiente ruta. Puedes abrir el archivo `index.html` en tu navegador para verlo.

**`target/site/jacoco/index.html`**

## JavaDoc

Para generar la documentación JavaDoc del proyecto, puedes usar el siguiente comando de Maven:

```bash
mvn javadoc:javadoc
```

La documentación se generará en el directorio `target/site/apidocs`. Abre el archivo `index.html` de esa carpeta para navegar por la documentación.