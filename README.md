# 🏥 Clinic Management System - Backend API

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](LICENSE)

> Sistema de gestión clínica desarrollado como **Trabajo de Integración Curricular** para la obtención del título de Desarrollador de Software en la **Escuela Politécnica Nacional**.

---

## 📋 Tabla de Contenidos

- [Descripción](#-descripción)
- [Stack Tecnológico](#-stack-tecnológico)
- [Arquitectura del Sistema](#-arquitectura-del-sistema)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Módulos del Sistema](#-módulos-del-sistema)
- [Seguridad y Autenticación](#-seguridad-y-autenticación)
- [API Endpoints](#-api-endpoints)
- [Configuración e Instalación](#-configuración-e-instalación)
- [Variables de Entorno](#-variables-de-entorno)
- [Despliegue](#-despliegue)
- [Autor](#-autor)

---

## 📖 Descripción

El **Clinic Management System** es una API RESTful robusta diseñada para la administración integral de información clínica. El sistema permite gestionar pacientes, empleados, historias clínicas, evoluciones médicas y catálogos de datos, implementando las mejores prácticas de desarrollo de software empresarial.

### Características Principales

- ✅ **Autenticación JWT** con refresh tokens y verificación de cuenta por email
- ✅ **Autorización basada en roles** (RBAC) con Spring Security
- ✅ **Arquitectura modular** siguiendo principios de Clean Architecture
- ✅ **Validación de datos** con Bean Validation (Jakarta Validation)
- ✅ **Manejo centralizado de excepciones** con `@ControllerAdvice`
- ✅ **Mapeo objeto-objeto** optimizado con MapStruct
- ✅ **Integración con microservicio de correo electrónico**
- ✅ **Soporte CORS** configurable para múltiples orígenes
- ✅ **Preparado para despliegue en Railway** con Nixpacks

---

## 🛠 Stack Tecnológico

### Core

| Tecnología          | Versión  | Propósito                          |
| ------------------- | -------- | ---------------------------------- |
| **Java**            | 21 (LTS) | Lenguaje de programación principal |
| **Spring Boot**     | 3.2.5    | Framework de aplicación            |
| **Spring Security** | 6.x      | Seguridad y autenticación          |
| **Spring Data JPA** | 3.x      | Capa de persistencia ORM           |
| **Hibernate**       | 6.x      | Implementación JPA                 |
| **PostgreSQL**      | 16+      | Base de datos relacional           |

### Dependencias Adicionales

| Librería               | Versión | Propósito                             |
| ---------------------- | ------- | ------------------------------------- |
| **Auth0 Java JWT**     | 4.5.0   | Generación y validación de tokens JWT |
| **MapStruct**          | 1.5.5   | Mapeo automático DTO ↔ Entity         |
| **Lombok**             | 1.18.30 | Reducción de código boilerplate       |
| **Jakarta Validation** | 3.x     | Validación declarativa de datos       |

### Build & Deploy

| Herramienta       | Propósito                       |
| ----------------- | ------------------------------- |
| **Maven**         | Gestión de dependencias y build |
| **Maven Wrapper** | Portabilidad del build          |
| **Nixpacks**      | Containerización para Railway   |

---

## 🏗 Arquitectura del Sistema

El proyecto implementa una **arquitectura modular en capas** inspirada en Clean Architecture y Domain-Driven Design (DDD):

```
┌─────────────────────────────────────────────────────────────┐
│                      PRESENTATION LAYER                      │
│                   (Controllers / REST API)                   │
├─────────────────────────────────────────────────────────────┤
│                      APPLICATION LAYER                       │
│                (Services / Business Logic)                   │
├─────────────────────────────────────────────────────────────┤
│                        DOMAIN LAYER                          │
│              (Entities / DTOs / Repositories)                │
├─────────────────────────────────────────────────────────────┤
│                    INFRASTRUCTURE LAYER                      │
│         (Security / Config / External Services)              │
└─────────────────────────────────────────────────────────────┘
```

### Patrones de Diseño Implementados

- **Repository Pattern**: Abstracción de la capa de datos con Spring Data JPA
- **DTO Pattern**: Transferencia de datos entre capas con objetos dedicados
- **Builder Pattern**: Construcción de objetos complejos (Lombok `@Builder`)
- **Dependency Injection**: Inversión de control con Spring IoC Container
- **Filter Chain**: Procesamiento de autenticación JWT
- **Global Exception Handler**: Manejo centralizado de errores

---

## 📁 Estructura del Proyecto

```
src/main/java/com/clinic/webapi/
├── ClinicApplication.java              # Punto de entrada de la aplicación
├── config/                             # Configuraciones globales
│   ├── AppConfig.java                  # Configuración general
│   ├── DataInitializer.java            # Inicialización de datos
│   └── SecurityConfig.java             # Configuración de Spring Security
├── modules/                            # Módulos de dominio
│   ├── auth/                           # Autenticación y autorización
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── repository/
│   │   └── service/
│   ├── catalogos/                      # Gestión de catálogos
│   ├── empleados/                      # Gestión de empleados
│   ├── evolucionesmedicas/             # Evoluciones médicas
│   ├── historiasclinicas/              # Historias clínicas
│   └── pacientes/                      # Gestión de pacientes
└── shared/                             # Componentes compartidos
    ├── dto/                            # DTOs genéricos (ApiResponse)
    ├── exception/                      # Manejo global de excepciones
    ├── model/                          # Modelos base
    ├── security/                       # Componentes de seguridad JWT
    │   ├── CustomUserDetailsService.java
    │   ├── JwtAuthenticationFilter.java
    │   └── JwtService.java
    └── util/                           # Utilidades
```

---

## 📦 Módulos del Sistema

### 🔐 Auth Module

Gestión completa del ciclo de vida de autenticación:

- Login con credenciales (email/password)
- Registro de usuarios con verificación por email
- Refresh tokens para renovación de sesión
- Recuperación de contraseña (forgot/reset password)

### 👥 Empleados Module

Administración del personal de la clínica:

- CRUD completo de empleados
- Gestión de roles y permisos
- Vinculación con usuarios del sistema

### 🏥 Pacientes Module

Gestión de información de pacientes:

- Registro y actualización de datos personales
- Búsqueda y filtrado avanzado
- Historial de atenciones

### 📋 Historias Clínicas Module

Administración de expedientes médicos:

- Creación y mantenimiento de historias clínicas
- Vinculación con pacientes
- Trazabilidad de modificaciones

### 📈 Evoluciones Médicas Module

Seguimiento de atenciones médicas:

- Registro de signos vitales
- Diagnósticos médicos
- Planes de tratamiento

### 📚 Catálogos Module

Gestión de datos maestros:

- Catálogos configurables
- Items de catálogo con estados
- Soporte para múltiples tipos de datos

---

## 🔒 Seguridad y Autenticación

### Flujo de Autenticación JWT

```
┌──────────┐      ┌──────────┐      ┌──────────┐      ┌──────────┐
│  Client  │──────│  Filter  │──────│ Service  │──────│   JWT    │
│          │ 1.   │  Chain   │ 2.   │  Layer   │ 3.   │ Service  │
└──────────┘      └──────────┘      └──────────┘      └──────────┘
     │                 │                 │                 │
     │ POST /login     │                 │                 │
     │────────────────>│                 │                 │
     │                 │ Authenticate    │                 │
     │                 │────────────────>│                 │
     │                 │                 │ Generate Token  │
     │                 │                 │────────────────>│
     │                 │                 │<────────────────│
     │                 │<────────────────│   JWT Token     │
     │<────────────────│ AuthResponse    │                 │
     │  {token, refresh}                 │                 │
```

### Configuración de Seguridad

- **Session Management**: Stateless (sin estado en servidor)
- **Password Encoding**: BCrypt con factor de costo por defecto
- **Token Expiration**: Access token (1 hora), Refresh token (7 días)
- **Method Security**: `@PreAuthorize` para control granular de acceso

### Roles del Sistema

| Rol             | Descripción                                  |
| --------------- | -------------------------------------------- |
| `ADMINISTRADOR` | Acceso total al sistema, gestión de usuarios |
| `MEDICO`        | Gestión de pacientes y evoluciones médicas   |
| `RECEPCIONISTA` | Gestión de pacientes y citas                 |

---

## 🌐 API Endpoints

### Base URL

```
/api/v1
```

### Autenticación

| Método | Endpoint                | Descripción         | Acceso        |
| ------ | ----------------------- | ------------------- | ------------- |
| `POST` | `/auth/login`           | Iniciar sesión      | Público       |
| `POST` | `/auth/register`        | Registrar usuario   | ADMINISTRADOR |
| `POST` | `/auth/verify/{token}`  | Verificar cuenta    | Público       |
| `POST` | `/auth/refresh`         | Renovar token       | Autenticado   |
| `POST` | `/auth/forgot-password` | Solicitar reset     | Público       |
| `POST` | `/auth/reset-password`  | Resetear contraseña | Público       |

### Recursos Protegidos

| Módulo              | Base Endpoint          |
| ------------------- | ---------------------- |
| Pacientes           | `/pacientes`           |
| Empleados           | `/empleados`           |
| Historias Clínicas  | `/historias-clinicas`  |
| Evoluciones Médicas | `/evoluciones-medicas` |
| Catálogos           | `/catalogos`           |

### Respuesta Estándar

```json
{
  "success": true,
  "message": "Operación exitosa",
  "data": {},
  "timestamp": "2026-01-31T12:00:00Z",
  "path": "/api/v1/resource"
}
```

---

## ⚙️ Configuración e Instalación

### Prerrequisitos

- **Java 21** (OpenJDK recomendado)
- **PostgreSQL 16+**
- **Maven 3.9+** (opcional, se incluye Maven Wrapper)

### Instalación Local

1. **Clonar el repositorio**

   ```bash
   git clone https://github.com/jmvillanueva-dev/clinic-management-system.git
   cd clinic-management-system/clinic
   ```

2. **Configurar la base de datos**

   ```sql
   CREATE DATABASE clinic_db;
   CREATE USER clinic_user WITH PASSWORD 'your_password';
   GRANT ALL PRIVILEGES ON DATABASE clinic_db TO clinic_user;
   ```

3. **Configurar variables de entorno**

   ```bash
   export DB_URL=jdbc:postgresql://localhost:5432/clinic_db
   export DB_USER=clinic_user
   export DB_PASS=your_password
   export JWT_SECRET=your_super_secret_key_min_256_bits
   export FRONTEND_URL=http://localhost:4321
   ```

4. **Compilar y ejecutar**

   ```bash
   # Con Maven Wrapper (recomendado)
   ./mvnw spring-boot:run

   # O con Maven instalado
   mvn spring-boot:run
   ```

5. **Verificar la instalación**
   ```bash
   curl http://localhost:8080/api/v1/auth/login
   ```

---

## 🔑 Variables de Entorno

| Variable                | Descripción                    | Requerida | Valor por Defecto       |
| ----------------------- | ------------------------------ | --------- | ----------------------- |
| `DB_URL`                | URL de conexión PostgreSQL     | ✅        | -                       |
| `DB_USER`               | Usuario de base de datos       | ✅        | -                       |
| `DB_PASS`               | Contraseña de base de datos    | ✅        | -                       |
| `JWT_SECRET`            | Clave secreta para firmar JWT  | ✅        | -                       |
| `FRONTEND_URL`          | URL del frontend (CORS)        | ❌        | `http://localhost:4321` |
| `EMAIL_SERVICE_URL`     | URL del microservicio de email | ❌        | `http://localhost:3000` |
| `EMAIL_SERVICE_API_KEY` | API Key del servicio de email  | ❌        | -                       |

---

## 🚀 Despliegue

### Railway (Recomendado)

El proyecto incluye configuración para **Nixpacks** en `nixpacks.toml`:

```toml
[phases.setup]
nixPkgs = ["jdk21_headless"]

[phases.build]
cmds = ["chmod +x mvnw", "./mvnw clean package -DskipTests"]

[start]
cmd = "java -Dserver.port=$PORT -jar target/*.jar"
```

### Build de Producción

```bash
# Generar JAR ejecutable
./mvnw clean package -DskipTests

# Ejecutar JAR
java -jar target/webapi-0.0.1-SNAPSHOT.jar
```

### Docker (Alternativo)

```dockerfile
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 📄 Licencia

Este proyecto fue desarrollado con fines académicos como parte del programa de **Desarrollo de Software** de la **Escuela Politécnica Nacional**.

---

## 👤 Autor

<table>
  <tr>
    <td align="center">
      <strong>Jhonny Villanueva M.</strong><br>
      <em>Desarrollador de Software</em><br><br>
      <a href="https://www.linkedin.com/in/jmvillanueva-m">
        <img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" alt="LinkedIn"/>
      </a>
      <a href="https://github.com/jmvillanueva-dev">
        <img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" alt="GitHub"/>
      </a>
    </td>
  </tr>
</table>

---

<p align="center">
  <strong>© 2026 - Escuela Politécnica Nacional</strong><br>
  <em>Trabajo de Integración Curricular</em>
</p>
