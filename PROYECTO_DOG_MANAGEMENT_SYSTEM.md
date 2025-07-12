# 🐕 SISTEMA DE GESTIÓN DE PERROS Y PROPIETARIOS
## Dog Management System

---

### 📋 INFORMACIÓN DEL PROYECTO

**Nombre del Proyecto:** Dog Management System  
**Repositorio:** https://github.com/rjokerlikely1690/PatsPetprofesor  
**Tecnología Principal:** Spring Boot 3.4.7  
**Tipo de Aplicación:** API REST  
**Base de Datos:** H2 Database  
**Documentación:** Swagger UI / OpenAPI 3.0  

---

## 📖 ÍNDICE

1. [Introducción](#introducción)
2. [Objetivos del Proyecto](#objetivos)
3. [Arquitectura Técnica](#arquitectura)
4. [Funcionalidades Principales](#funcionalidades)
5. [Tecnologías Utilizadas](#tecnologías)
6. [Estructura del Proyecto](#estructura)
7. [API REST](#api-rest)
8. [Base de Datos](#base-de-datos)
9. [Pruebas Unitarias](#pruebas)
10. [Instalación y Uso](#instalación)
11. [Documentación Swagger](#swagger)
12. [Conclusiones](#conclusiones)

---

## 🎯 INTRODUCCIÓN

El **Dog Management System** es una aplicación web desarrollada en Spring Boot que permite gestionar de manera integral información sobre perros y sus propietarios. El sistema está diseñado para refugios de animales, veterinarias, o cualquier organización que necesite llevar un control detallado de mascotas y sus dueños.

La aplicación implementa una **API REST completamente funcional** con arquitectura limpia, siguiendo las mejores prácticas de desarrollo de software y los principios **SOLID**.

---

## 🎯 OBJETIVOS DEL PROYECTO

### Objetivo General
Desarrollar un sistema integral de gestión de perros y propietarios que permita realizar operaciones CRUD completas, implementando tecnologías modernas y siguiendo estándares de desarrollo profesional.

### Objetivos Específicos
- ✅ Implementar una API REST robusta y escalable
- ✅ Aplicar principios de arquitectura limpia
- ✅ Integrar HATEOAS para navegación dinámica
- ✅ Documentar la API con Swagger/OpenAPI
- ✅ Implementar validaciones y manejo de errores
- ✅ Desarrollar pruebas unitarias completas
- ✅ Configurar base de datos con datos de prueba
- ✅ Aplicar buenas prácticas de codificación

---

## 🏗️ ARQUITECTURA TÉCNICA

### Patrón de Arquitectura
El proyecto sigue el patrón **MVC (Model-View-Controller)** con una arquitectura en capas:

#### 📊 Capas de la Aplicación
1. **Capa de Presentación (Controllers)**
   - Manejo de peticiones HTTP
   - Implementación de HATEOAS
   - Validación de entrada

2. **Capa de Negocio (Services)**
   - Lógica de negocio
   - Validaciones complejas
   - Coordinación entre entidades

3. **Capa de Acceso a Datos (Repositories)**
   - Persistencia de datos
   - Consultas personalizadas
   - Gestión de transacciones

4. **Capa de Modelo (Entities)**
   - Representación de datos
   - Relaciones entre entidades
   - Validaciones de campo

### 🎯 Principios Aplicados
- **SOLID**: Principios de diseño orientado a objetos
- **DRY**: Don't Repeat Yourself
- **Clean Code**: Código limpio y legible
- **REST**: Representational State Transfer
- **HATEOAS**: Hypermedia as the Engine of Application State

---

## 🚀 FUNCIONALIDADES PRINCIPALES

### 🐕 Gestión de Perros
- **✅ Crear perros** con información completa
- **✅ Consultar perros** individual y masivamente
- **✅ Actualizar información** de perros existentes
- **✅ Eliminar perros** del sistema
- **✅ Buscar perros** por nombre y raza
- **✅ Filtrar perros** disponibles para adopción
- **✅ Obtener cachorros** (menores de 1 año)
- **✅ Generar estadísticas** por raza

### 👥 Gestión de Propietarios
- **✅ Crear propietarios** con datos de contacto
- **✅ Consultar propietarios** con sus perros
- **✅ Actualizar información** de contacto
- **✅ Eliminar propietarios** del sistema
- **✅ Buscar propietarios** por nombre y ciudad
- **✅ Activar/Desactivar** propietarios
- **✅ Generar estadísticas** por ciudad

### 🔗 Funcionalidades Relacionales
- **✅ Asignar propietarios** a perros
- **✅ Remover propietarios** de perros
- **✅ Consultar perros** por propietario
- **✅ Consultar propietarios** con/sin perros

---

## 💻 TECNOLOGÍAS UTILIZADAS

### Backend Framework
- **Spring Boot 3.4.7** - Framework principal
- **Spring Data JPA** - Persistencia de datos
- **Spring Web** - Desarrollo de APIs REST
- **Spring HATEOAS** - Implementación de HATEOAS
- **Spring Validation** - Validaciones

### Base de Datos
- **H2 Database** - Base de datos en memoria
- **JPA/Hibernate** - ORM para persistencia

### Documentación
- **SpringDoc OpenAPI 3** - Documentación automática
- **Swagger UI** - Interfaz interactiva de API

### Testing
- **JUnit 5** - Framework de pruebas
- **Mockito** - Simulación de dependencias
- **Spring Boot Test** - Pruebas de integración

### Herramientas de Desarrollo
- **Maven** - Gestión de dependencias
- **Lombok** - Reducción de código boilerplate
- **SLF4J** - Logging

---

## 📁 ESTRUCTURA DEL PROYECTO

```
dog-management-system/
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/com/dogapp/
│   │   │   ├── 🐕 DogManagementSystemApplication.java
│   │   │   ├── 📁 config/
│   │   │   │   └── 🔧 OpenApiConfig.java
│   │   │   ├── 📁 controller/
│   │   │   │   ├── 🎮 DogController.java
│   │   │   │   ├── 👥 OwnerController.java
│   │   │   │   └── 🏠 HomeController.java
│   │   │   ├── 📁 model/
│   │   │   │   ├── 🐕 Dog.java
│   │   │   │   └── 👤 Owner.java
│   │   │   ├── 📁 service/
│   │   │   │   ├── 🔧 DogService.java
│   │   │   │   └── 👥 OwnerService.java
│   │   │   └── 📁 repository/
│   │   │       ├── 🗄️ DogRepository.java
│   │   │       └── 👥 OwnerRepository.java
│   │   └── 📁 resources/
│   │       ├── ⚙️ application.yml
│   │       └── 📝 data.sql
│   └── 📁 test/
│       └── 📁 java/com/dogapp/service/
│           └── 🧪 DogServiceTest.java
├── 📄 pom.xml
├── 📚 README.md
└── 📁 .mvn/
```

---

## 🌐 API REST

### Endpoints de Perros
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/dogs` | Obtener todos los perros |
| GET | `/api/dogs/{id}` | Obtener perro por ID |
| POST | `/api/dogs` | Crear nuevo perro |
| PUT | `/api/dogs/{id}` | Actualizar perro |
| DELETE | `/api/dogs/{id}` | Eliminar perro |
| GET | `/api/dogs/search?name={name}` | Buscar por nombre |
| GET | `/api/dogs/breed/{breed}` | Buscar por raza |
| GET | `/api/dogs/available-for-adoption` | Perros disponibles |
| GET | `/api/dogs/puppies` | Obtener cachorros |
| PUT | `/api/dogs/{id}/assign-owner/{ownerId}` | Asignar propietario |
| PUT | `/api/dogs/{id}/remove-owner` | Remover propietario |

### Endpoints de Propietarios
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/owners` | Obtener todos los propietarios |
| GET | `/api/owners/{id}` | Obtener propietario por ID |
| POST | `/api/owners` | Crear nuevo propietario |
| PUT | `/api/owners/{id}` | Actualizar propietario |
| DELETE | `/api/owners/{id}` | Eliminar propietario |
| GET | `/api/owners/search?name={name}` | Buscar por nombre |
| GET | `/api/owners/city/{city}` | Buscar por ciudad |
| GET | `/api/owners/with-dogs` | Propietarios con perros |
| GET | `/api/owners/without-dogs` | Propietarios sin perros |
| PUT | `/api/owners/{id}/activate` | Activar propietario |
| PUT | `/api/owners/{id}/deactivate` | Desactivar propietario |

### 🔗 Implementación HATEOAS
Cada respuesta incluye enlaces navegables que permiten descobrir dinámicamente las acciones disponibles:

```json
{
  "id": 1,
  "name": "Max",
  "breed": "Golden Retriever",
  "_links": {
    "self": {"href": "/api/dogs/1"},
    "owner": {"href": "/api/owners/1"},
    "update": {"href": "/api/dogs/1"},
    "delete": {"href": "/api/dogs/1"}
  }
}
```

---

## 🗄️ BASE DE DATOS

### Modelo de Entidades

#### 🐕 Entidad Dog
```java
@Entity
@Table(name = "dogs")
public class Dog {
    @Id @GeneratedValue
    private Long id;
    
    @NotBlank @Size(min = 2, max = 50)
    private String name;
    
    @NotBlank @Size(min = 2, max = 50)
    private String breed;
    
    @Min(0) @Max(30)
    private Integer age;
    
    @NotBlank @Size(min = 2, max = 30)
    private String color;
    
    @DecimalMin("0.1") @DecimalMax("100.0")
    private Double weight;
    
    @Past
    private LocalDate birthDate;
    
    @Size(max = 500)
    private String description;
    
    private Boolean isVaccinated;
    
    @Enumerated(EnumType.STRING)
    private DogSize size;
    
    private Boolean isAvailable;
    
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;
    
    // Campos automáticos
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

#### 👤 Entidad Owner
```java
@Entity
@Table(name = "owners")
public class Owner {
    @Id @GeneratedValue
    private Long id;
    
    @NotBlank @Size(min = 2, max = 50)
    private String firstName;
    
    @NotBlank @Size(min = 2, max = 50)
    private String lastName;
    
    @Email @NotBlank
    private String email;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    private String phone;
    
    @Size(min = 5, max = 100)
    private String address;
    
    @Size(min = 2, max = 50)
    private String city;
    
    @Size(min = 5, max = 10)
    private String postalCode;
    
    @Min(18) @Max(100)
    private Integer age;
    
    @Size(max = 500)
    private String observations;
    
    private Boolean isActive;
    
    @OneToMany(mappedBy = "owner")
    private List<Dog> dogs;
    
    // Campos automáticos
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### Datos de Prueba
El sistema incluye **13 perros** y **5 propietarios** precargados para testing:

#### Perros de Ejemplo:
- Max (Golden Retriever) - Con propietario Juan Pérez
- Bella (Labrador) - Con propietario María González
- Rocky (Pastor Alemán) - Con propietario Carlos Rodríguez
- Buddy (Beagle) - Disponible para adopción
- Zeus (Rottweiler) - Disponible para adopción
- Y más...

---

## 🧪 PRUEBAS UNITARIAS

### Cobertura de Pruebas
El proyecto incluye **25 pruebas unitarias** que cubren:

#### Pruebas de DogService
- ✅ Operaciones CRUD básicas
- ✅ Validaciones de datos
- ✅ Búsquedas por nombre y raza
- ✅ Filtros por disponibilidad
- ✅ Gestión de propietarios
- ✅ Manejo de errores
- ✅ Casos límite

#### Ejemplos de Pruebas
```java
@Test
void getAllDogs_ShouldReturnListOfDogs() {
    // Given
    List<Dog> dogs = Arrays.asList(createTestDog(), createTestDog());
    when(dogRepository.findAll()).thenReturn(dogs);
    
    // When
    List<Dog> result = dogService.getAllDogs();
    
    // Then
    assertEquals(2, result.size());
    verify(dogRepository).findAll();
}

@Test
void createDog_WithValidData_ShouldReturnSavedDog() {
    // Given
    Dog dog = createTestDog();
    when(dogRepository.save(any(Dog.class))).thenReturn(dog);
    
    // When
    Dog result = dogService.createDog(dog);
    
    // Then
    assertNotNull(result);
    assertEquals(dog.getName(), result.getName());
}
```

### Ejecutar Pruebas
```bash
mvn test
```

---

## 🚀 INSTALACIÓN Y USO

### Requisitos Previos
- **Java 17** o superior
- **Maven 3.6** o superior
- **Git** para clonar el repositorio

### Instalación
1. **Clonar el repositorio:**
```bash
git clone https://github.com/rjokerlikely1690/PatsPetprofesor.git
cd PatsPetprofesor
```

2. **Compilar el proyecto:**
```bash
mvn clean compile
```

3. **Ejecutar las pruebas:**
```bash
mvn test
```

4. **Ejecutar la aplicación:**
```bash
mvn spring-boot:run
```

### Acceso a la Aplicación
Una vez ejecutada, la aplicación estará disponible en:

- **🌐 Aplicación**: http://localhost:8080
- **📚 Swagger UI**: http://localhost:8080/swagger-ui.html
- **🗄️ H2 Console**: http://localhost:8080/h2-console
- **❤️ Health Check**: http://localhost:8080/actuator/health
- **📊 API Docs**: http://localhost:8080/v3/api-docs

---

## 📚 DOCUMENTACIÓN SWAGGER

### Configuración OpenAPI
El proyecto incluye configuración completa de OpenAPI 3.0:

```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI dogManagementOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Dog Management System API")
                .description("Sistema integral de gestión de perros y propietarios")
                .version("1.0.0"))
            .servers(List.of(new Server()
                .url("http://localhost:8080")
                .description("Servidor de Desarrollo")));
    }
}
```

### Características de la Documentación
- **📋 Documentación automática** de todos los endpoints
- **🎯 Ejemplos de peticiones** y respuestas
- **✅ Validaciones detalladas** para cada campo
- **🔧 Interfaz interactiva** para probar la API
- **📊 Esquemas de datos** completos
- **🏷️ Organización por tags** (Dogs, Owners)

---

## 🏆 CONCLUSIONES

### Logros Alcanzados
1. **✅ Sistema completo** de gestión de perros y propietarios
2. **✅ API REST robusta** con 20+ endpoints
3. **✅ Arquitectura limpia** siguiendo principios SOLID
4. **✅ HATEOAS implementado** para navegación dinámica
5. **✅ Documentación completa** con Swagger UI
6. **✅ Pruebas unitarias** con alta cobertura
7. **✅ Validaciones robustas** en todos los niveles
8. **✅ Manejo de errores** profesional

### Tecnologías Dominadas
- **Spring Boot** y su ecosistema
- **Spring Data JPA** para persistencia
- **Spring HATEOAS** para APIs navegables
- **OpenAPI/Swagger** para documentación
- **JUnit 5** para testing
- **H2 Database** para desarrollo
- **Maven** para gestión de dependencias

### Aplicaciones Prácticas
Este sistema puede ser utilizado en:
- **🏥 Clínicas veterinarias**
- **🏠 Refugios de animales**
- **🏢 Organizaciones de rescate**
- **📱 Aplicaciones móviles** de mascotas
- **🌐 Plataformas web** de adopción

### Escalabilidad
El proyecto está diseñado para:
- **📈 Fácil extensión** con nuevas funcionalidades
- **🔄 Migración simple** a bases de datos de producción
- **🛡️ Integración** con sistemas de autenticación
- **📱 Desarrollo** de interfaces frontend
- **☁️ Despliegue** en la nube

---

## 📞 INFORMACIÓN DE CONTACTO

**Repositorio del Proyecto:**  
https://github.com/rjokerlikely1690/PatsPetprofesor

**Swagger UI:**  
http://localhost:8080/swagger-ui.html

**Documentación API:**  
http://localhost:8080/v3/api-docs

---

*Documento generado automáticamente para el proyecto Dog Management System*  
*Fecha: 2025* 