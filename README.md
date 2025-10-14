# Sistema web para la gestión digital de historias clínicas y evoluciones médicas

Este repositorio contiene una **API RESTful** desarrollada con **Spring Boot 3**, diseñada para la **gestión de pacientes y sus historias clínicas** en un entorno médico.  
Incluye autenticación basada en **JWT (JSON Web Tokens)** y un modelo de datos estructurado para soportar diferentes funcionalidades.


---

## ⚙️ Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.5.5**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **Auth0 Java JWT**
- **Maven**

---

---

## 🧩 Configuración del Entorno

El archivo `application.yml` requiere las siguientes variables de entorno:

```yaml
DB_URL=jdbc:postgresql://localhost:5432/clinicdb
DB_USER=postgres
DB_PASS=123456
JWT_SECRET=clave_secreta_segura
```

Ejecuta el proyecto con:

```bash
./mvnw spring-boot:run
```

---

## 👨‍💻 Autor

**Jhonny Villanueva Montoya**  
📍 [GitHub](https://github.com/jmvillanueva-dev)  
💼 Desarrollador Full Stack