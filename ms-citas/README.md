# ms-citas

Microservicio de gestión de citas médicas perteneciente al sistema **RedNorte**.

## Tecnologías

- Java 21
- Spring Boot 4.0.7
- MySQL
- Spring Data JPA
- Lombok
- Swagger / OpenAPI

## Requisitos previos

- JDK 21
- MySQL corriendo en localhost
- Maven

## Configuración

En `src/main/resources/application.properties` configurar la contraseña de MySQL:

```properties
spring.datasource.password=tu_contraseña
```

## Cómo ejecutar

```bash
mvn spring-boot:run
```

El servicio queda disponible en `http://localhost:8083`

## Documentación de la API

Una vez corriendo, la documentación Swagger está en:

```
http://localhost:8083/swagger-ui.html
```

## Endpoints disponibles

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /api/v1/citas | Crear una cita |
| GET | /api/v1/citas | Listar todas las citas |
| GET | /api/v1/citas/{id} | Obtener cita por ID |
| GET | /api/v1/citas/paciente/{pacienteId} | Listar citas por paciente |
| GET | /api/v1/citas/especialidad/{especialidad} | Listar por especialidad |
| GET | /api/v1/citas/estado/{estado} | Listar por estado |
| GET | /api/v1/citas/medico?nombreMedico= | Listar por médico |
| PUT | /api/v1/citas/{id} | Actualizar cita |
| PATCH | /api/v1/citas/{id}/confirmar | Confirmar cita |
| PATCH | /api/v1/citas/{id}/cancelar | Cancelar cita |
| PATCH | /api/v1/citas/{id}/no-asiste | Marcar como no asistió |
| DELETE | /api/v1/citas/{id} | Eliminar cita |

## Estados de una cita

| Estado | Descripción |
|--------|-------------|
| DISPONIBLE | Horario disponible sin paciente asignado |
| PROGRAMADA | Cita creada con paciente asignado |
| CONFIRMADA | Cita confirmada por el paciente |
| CANCELADA | Cita cancelada |
| COMPLETADA | Cita realizada |
| NO_ASISTE | El paciente no asistió |

## Reglas de negocio

- No se pueden crear citas en fechas pasadas
- No se puede agendar dos citas con el mismo médico en la misma fecha y hora
- Al crear una cita el estado inicial es **PROGRAMADA**
- Una cita cancelada o completada no puede ser confirmada
- Una cita completada no puede ser cancelada

## Tests

```bash
mvn test
```

Coverage actual: ~93% en clases principales