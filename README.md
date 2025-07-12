# 🐕 Dog Management System

Sistema de gestión de perros desarrollado con Spring Boot, JPA, HATEOAS y arquitectura de microservicios.

## 📋 Descripción del Proyecto

Este sistema permite gestionar perros y sus propietarios de manera completa, implementando las mejores prácticas de desarrollo con Spring Boot. Está diseñado para refugios de animales, veterinarias o cualquier organización que necesite gestionar información de perros y sus dueños.

## 🏗️ Arquitectura del Sistema

### Estrategia de Microservicios
- **Microservicio de Gestión de Perros**: Maneja toda la lógica relacionada con perros
- **Microservicio de Gestión de Propietarios**: Administra información de los dueños
- **Comunicación RESTful**: APIs REST con HATEOAS para navegabilidad
- **Base de Datos Compartida**: H2 en memoria para desarrollo, fácil migración a PostgreSQL/MySQL

### Tecnologías Utilizadas
- **Spring Boot 3.4.7** - Framework principal
- **Spring Data JPA** - Persistencia de datos
- **Spring HATEOAS** - APIs navegables
- **H2 Database** - Base de datos en memoria para desarrollo
- **Maven** - Gestión de dependencias
- **JUnit 5** - Pruebas unitarias
- **Mockito** - Mocking para pruebas
- **OpenAPI/Swagger** - Documentación de APIs
- **Spring Boot Actuator** - Monitoreo y métricas
- **Lombok** - Reducción de código boilerplate

## 🚀 Instalación y Ejecución

### Requisitos Previos
- Java 17 o superior
- Maven 3.6 o superior
- Git

### Instrucciones de Instalación

1. **Clonar el repositorio**
```bash
git clone <repository-url>
cd dog-management-system
```

2. **Compilar el proyecto**
```bash
mvn clean compile
```

3. **Ejecutar pruebas**
```bash
mvn test
```

4. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

5. **Acceder a la aplicación**
- **Aplicación**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console
- **Health Check**: http://localhost:8080/actuator/health

### Configuración de Base de Datos H2
- **URL**: `jdbc:h2:mem:dogdb`
- **Usuario**: `sa`
- **Contraseña**: `password`

## 📊 Modelo de Datos

### Entidad Dog (Perro)
- **id**: Identificador único
- **name**: Nombre del perro
- **breed**: Raza
- **age**: Edad en años
- **color**: Color del pelaje
- **weight**: Peso en kilogramos
- **birthDate**: Fecha de nacimiento
- **description**: Descripción adicional
- **isVaccinated**: Estado de vacunación
- **size**: Tamaño (PEQUEÑO, MEDIANO, GRANDE)
- **isAvailable**: Disponibilidad para adopción
- **owner**: Propietario (relación ManyToOne)

### Entidad Owner (Propietario)
- **id**: Identificador único
- **firstName**: Nombre
- **lastName**: Apellido
- **email**: Correo electrónico
- **phone**: Teléfono
- **address**: Dirección
- **city**: Ciudad
- **postalCode**: Código postal
- **age**: Edad
- **observations**: Observaciones
- **isActive**: Estado activo
- **dogs**: Lista de perros (relación OneToMany)

## 🔗 Endpoints de la API

### Perros (Dogs)

#### CRUD Básico
- `GET /api/dogs` - Obtener todos los perros
- `GET /api/dogs/{id}` - Obtener perro por ID
- `POST /api/dogs` - Crear nuevo perro
- `PUT /api/dogs/{id}` - Actualizar perro
- `DELETE /api/dogs/{id}` - Eliminar perro

#### Funcionalidades Especiales
- `GET /api/dogs/search?name={name}` - Buscar perros por nombre
- `GET /api/dogs/breed/{breed}` - Buscar perros por raza
- `GET /api/dogs/available-for-adoption` - Perros disponibles para adopción
- `GET /api/dogs/puppies` - Perros cachorros (< 1 año)
- `GET /api/dogs/statistics/breed` - Estadísticas por raza
- `PUT /api/dogs/{dogId}/assign-owner/{ownerId}` - Asignar propietario
- `PUT /api/dogs/{dogId}/remove-owner` - Remover propietario

### Propietarios (Owners)

#### CRUD Básico
- `GET /api/owners` - Obtener todos los propietarios
- `GET /api/owners/{id}` - Obtener propietario por ID
- `POST /api/owners` - Crear nuevo propietario
- `PUT /api/owners/{id}` - Actualizar propietario
- `DELETE /api/owners/{id}` - Eliminar propietario

#### Funcionalidades Especiales
- `GET /api/owners/search?name={name}` - Buscar propietarios por nombre
- `GET /api/owners/city/{city}` - Buscar propietarios por ciudad
- `GET /api/owners/with-dogs` - Propietarios con perros
- `GET /api/owners/without-dogs` - Propietarios sin perros
- `GET /api/owners/active` - Propietarios activos
- `GET /api/owners/statistics/city` - Estadísticas por ciudad
- `GET /api/owners/statistics/dog-ownership` - Estadísticas por número de perros
- `PUT /api/owners/{id}/deactivate` - Desactivar propietario
- `PUT /api/owners/{id}/activate` - Activar propietario

## 📝 Ejemplos de Uso

### Crear un Perro
```bash
curl -X POST http://localhost:8080/api/dogs \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Max",
    "breed": "Golden Retriever",
    "age": 3,
    "color": "Dorado",
    "weight": 28.5,
    "birthDate": "2021-03-15",
    "description": "Perro muy amigable",
    "isVaccinated": true,
    "size": "MEDIANO",
    "isAvailable": true
  }'
```

### Crear un Propietario
```bash
curl -X POST http://localhost:8080/api/owners \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Juan",
    "lastName": "Pérez",
    "email": "juan@email.com",
    "phone": "3001234567",
    "address": "Calle 123",
    "city": "Bogotá",
    "postalCode": "11111",
    "age": 30,
    "isActive": true
  }'
```

### Asignar Propietario a Perro
```bash
curl -X PUT http://localhost:8080/api/dogs/1/assign-owner/1
```

## 🔍 HATEOAS (Hypermedia as the Engine of Application State)

Todas las respuestas incluyen enlaces HATEOAS para navegación:

```json
{
  "id": 1,
  "name": "Max",
  "breed": "Golden Retriever",
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/dogs/1"
    },
    "all-dogs": {
      "href": "http://localhost:8080/api/dogs"
    },
    "owner": {
      "href": "http://localhost:8080/api/owners/1"
    },
    "update": {
      "href": "http://localhost:8080/api/dogs/1"
    },
    "delete": {
      "href": "http://localhost:8080/api/dogs/1"
    }
  }
}
```

## 🧪 Pruebas

### Ejecutar todas las pruebas
```bash
mvn test
```

### Ejecutar pruebas con coverage
```bash
mvn clean test jacoco:report
```

### Tipos de Pruebas Implementadas
- **Pruebas Unitarias**: Servicios y lógica de negocio
- **Pruebas de Integración**: Controladores y endpoints
- **Pruebas de Repositorio**: Consultas JPA
- **Pruebas de Validación**: Anotaciones de validación

## 📈 Monitoreo y Métricas

### Actuator Endpoints
- `/actuator/health` - Estado de salud de la aplicación
- `/actuator/info` - Información de la aplicación
- `/actuator/metrics` - Métricas de rendimiento
- `/actuator/prometheus` - Métricas para Prometheus

## 🛠️ Configuración Maven

### Ciclo de Vida del Proyecto
```bash
# Limpiar proyecto
mvn clean

# Compilar
mvn compile

# Ejecutar pruebas
mvn test

# Empaquetar
mvn package

# Instalar en repositorio local
mvn install

# Ejecutar aplicación
mvn spring-boot:run
```

### Plugins Importantes
- **spring-boot-maven-plugin**: Ejecución y empaquetado
- **maven-surefire-plugin**: Ejecución de pruebas
- **jacoco-maven-plugin**: Coverage de pruebas

## 🔧 Buenas Prácticas Implementadas

### Arquitectura
- **Separación de responsabilidades**: Controladores, Servicios, Repositorios
- **Inyección de dependencias**: Constructor injection con Lombok
- **Validación de datos**: Bean Validation con anotaciones
- **Manejo de errores**: Exceptions personalizadas
- **Transacciones**: Gestión declarativa con @Transactional

### Código
- **Código limpio**: Nombres descriptivos, métodos pequeños
- **Documentación**: JavaDoc completo
- **Logging**: SLF4J para trazabilidad
- **Configuración externalizadas**: application.yml
- **Tests comprehensivos**: Alta cobertura de pruebas

### Base de Datos
- **Índices optimizados**: En campos de búsqueda frecuente
- **Relaciones bien definidas**: FK constraints
- **Auditoría**: Campos createdAt y updatedAt
- **Datos de prueba**: Carga automática con data.sql

## 📚 Documentación Adicional

### OpenAPI/Swagger
La documentación interactiva de la API está disponible en:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### Consideraciones Éticas

Este sistema ha sido desarrollado considerando:
- **Privacidad**: Protección de datos personales
- **Responsabilidad**: Gestión responsable de información de mascotas
- **Empleabilidad**: Código mantenible y escalable
- **Transparencia**: Documentación completa y APIs claras

## 🤝 Contribución

Para contribuir al proyecto:
1. Fork el repositorio
2. Crear branch para feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push al branch (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

