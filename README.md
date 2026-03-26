# Chocolatinazo — Backend API

> Sistema de backend para simular el juego "El Chocolatinazo", donde un grupo de jugadores toman chocolatinas con números aleatorios y, según una regla definida, se determina quién pierde y debe pagar por todas.

**Reto Aprendices Evolución — Bancolombia**

---

## Tabla de Contenidos

1. [Descripción del Proyecto](#-descripción-del-proyecto)
2. [Tecnologías Utilizadas](#-tecnologías-utilizadas)
3. [Arquitectura del Proyecto](#-arquitectura-del-proyecto)
4. [Autenticación y Autorización](#-autenticación-y-autorización)
5. [Endpoints de la API](#-endpoints-de-la-api)
6. [Reglas de Negocio](#-reglas-de-negocio)
7. [Manejo de Excepciones](#-manejo-de-excepciones)
8. [Validaciones](#-validaciones)
9. [Configuración y Ejecución](#-configuración-y-ejecución)
10. [Investigación — Métodos de Control de Acceso](#-investigación--métodos-de-control-de-acceso)
11. [Decisiones de Diseño](#-decisiones-de-diseño)
12. [Uso de Inteligencia Artificial](#-uso-de-inteligencia-artificial)

---

## Descripción del Proyecto

El Chocolatinazo es un juego donde:

1. Existe una caja de chocolatinas, cada una con un sticker numerado del **1 al 320**.
2. Cada jugador toma una chocolatina al azar.
3. En cada ronda se define si pierde quien saque el número **mayor (MAX)** o **menor (MIN)**.
4. Un auditor recopila los números y determina quién va perdiendo.
5. El perdedor debe **pagar la chocolatina de todos los demás jugadores**.

Este backend expone una API REST que simula completamente este juego, protegida con **JWT** y autorización basada en **roles (RBAC)**.

---

## 🛠 Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|---|---|---|
| **Java** | 21 | Lenguaje principal |
| **Spring Boot** | 4.0.4 | Framework backend |
| **Spring Security** | 7.0.4 | Autenticación y autorización |
| **Spring Data JPA** | 4.0.4 | Persistencia y ORM |
| **PostgreSQL** | 18.x | Base de datos relacional |
| **jjwt (io.jsonwebtoken)** | 0.12.3 | Generación y validación de tokens JWT |
| **Hibernate Validator** | 9.0.1 | Validación de DTOs con Bean Validation |
| **Maven** | 3.x | Gestión de dependencias y build |
| **BCrypt** | — | Encriptación de contraseñas |

> **Nota:** No se utiliza Lombok. Todos los getters, setters y constructores están escritos manualmente para mayor transparencia del código.

---

## 🏗 Arquitectura del Proyecto

El proyecto sigue los principios de **Arquitectura Limpia (Clean Architecture)** con tres capas claramente separadas:

```
src/main/java/com/bancolombia/chocolatinazo/
│
├── domain/                          ← Capa de Dominio (núcleo del negocio)
│   ├── enums/                       ← Enumeraciones del dominio
│   │   ├── GameStatus.java          ← ACTIVE, FINISHED
│   │   ├── RoleName.java            ← PLAYER, AUDITOR, ADMIN
│   │   └── RuleType.java            ← MIN, MAX
│   ├── model/                       ← Entidades JPA
│   │   ├── Role.java
│   │   ├── User.java
│   │   ├── Game.java
│   │   ├── GameRecord.java
│   │   ├── FinishedGame.java
│   │   └── ChocolatinaConfig.java
│   └── port/                        ← Interfaces (puertos) de repositorio
│       ├── IRoleRepository.java
│       ├── IUserRepository.java
│       ├── IGameRepository.java
│       ├── IGameRecordRepository.java
│       ├── IFinishedGameRepository.java
│       └── IChocolatinaConfigRepository.java
│
├── application/                     ← Capa de Aplicación (casos de uso)
│   ├── dto/
│   │   ├── request/                 ← DTOs de entrada
│   │   │   ├── AuthRegisterRequest.java
│   │   │   ├── AuthLoginRequest.java
│   │   │   ├── CreateGameRequest.java
│   │   │   ├── PickChocolatinaRequest.java
│   │   │   ├── CalculateLoserRequest.java
│   │   │   ├── UpdateChocolatinaValueRequest.java
│   │   │   └── AddRoleRequest.java
│   │   └── response/                ← DTOs de salida
│   │       ├── UserResponse.java
│   │       ├── AuthLoginResponse.java
│   │       ├── GameResponse.java
│   │       ├── GameRecordResponse.java
│   │       ├── FinishedGameResponse.java
│   │       ├── ChocolatinaConfigResponse.java
│   │       └── ErrorResponse.java
│   └── service/                     ← Servicios de aplicación
│       ├── AuthService.java
│       ├── GameService.java
│       ├── ChocolatinaService.java
│       ├── AuditService.java
│       └── AddRoleService.java
│
└── infrastructure/                  ← Capa de Infraestructura (detalles técnicos)
    ├── config/
    │   └── BeanConfig.java          ← Bean de PasswordEncoder (BCrypt)
    ├── exception/
    │   ├── GlobalExceptionHandler.java
    │   ├── InvalidInputException.java
    │   ├── ResourceNotFoundException.java
    │   └── UnauthorizedException.java
    ├── repository/                  ← Implementaciones JPA (extienden ports)
    │   ├── RoleRepository.java
    │   ├── UserRepository.java
    │   ├── GameRepository.java
    │   ├── GameRecordRepository.java
    │   ├── FinishedGameRepository.java
    │   └── ChocolatinaConfigRepository.java
    ├── security/
    │   ├── JwtService.java          ← Generación/validación de JWT
    │   ├── JwtRequestFilter.java    ← Filtro que intercepta cada request
    │   └── SecurityConfig.java      ← Configuración RBAC de endpoints
    └── web/                         ← Controladores REST
        ├── AuthController.java
        ├── GameController.java
        ├── ChocolatinaController.java
        ├── AuditController.java
        └── UserController.java
```

### Principio de Dependencia

```
Controllers → Services → Ports (interfaces) ← Repositories (implementaciones JPA)
```

Los **servicios** nunca dependen de implementaciones JPA directamente. Solo conocen las **interfaces (ports)** del dominio. Las implementaciones JPA en `infrastructure/repository/` extienden tanto `JpaRepository` como el port correspondiente, permitiendo que Spring las inyecte automáticamente.

---

### Decisiones sobre el modelo

- **`user_roles` (ManyToMany):** Un usuario puede tener múltiples roles simultáneamente (ej: ser PLAYER y ADMIN). Esto permite mayor flexibilidad en la gestión de permisos.
- **`chocolatina_price` en `finished_games`:** Es un **snapshot** del precio al momento del cálculo, no una referencia FK. Esto garantiza que el historial no se altere si el precio cambia posteriormente.
- **`rule_type` en `finished_games`:** Se almacena la regla usada (MIN/MAX) para saber por qué ese número fue el perdedor.
- **Normalización:** El username del perdedor se obtiene vía JOIN con la tabla `users` (no se duplica como columna), siguiendo principios de normalización de bases de datos.
- **Asignación de roles:** Se optó por implementar un endpoint público (sin restricciones de acceso) para el registro de usuarios, donde el sistema asignará el rol PLAYER por defecto, sin posibilidad de elección. Adicionalmente, se dispondrá de otro endpoint protegido, donde solo un usuario con rol ADMIN podrá asignar roles superiores a los usuarios que lo necesiten.

---

## 🔐 Autenticación y Autorización

### Método elegido: JWT + RBAC

Tras la investigacion de metodos de acceso, se seleccionó **RBAC (Role-Based Access Control)** combinado con **JWT (JSON Web Tokens)** por las siguientes razones:

1. **RBAC** es ideal para este caso donde los permisos se definen por el tipo de usuario (PLAYER, AUDITOR, ADMIN), no por atributos individuales.
2. **JWT** permite autenticación stateless, eliminando la necesidad de sesiones en el servidor.
3. La combinación es estándar en la industria y está nativamente soportada por Spring Security.

### Flujo de Autenticación

```
┌──────────┐    POST /api/auth/login     ┌──────────────┐
│  Cliente  │ ─────────────────────────► │  AuthService  │
│ (Postman) │    { email, password }      │              │
│           │                             │ 1. Busca user│
│           │    { token, user info }     │ 2. Valida pw │
│           │ ◄───────────────────────── │ 3. Genera JWT│
└──────────┘                              └──────────────┘

┌──────────┐   GET /api/audit/...        ┌──────────────────┐
│  Cliente  │ ─────────────────────────► │ JwtRequestFilter  │
│           │  Authorization: Bearer xxx │                    │
│           │                            │ 1. Extrae token   │
│           │                            │ 2. Valida firma   │
│           │                            │ 3. Extrae roles   │
│           │                            │ 4. Set SecurityCtx│
│           │                            └────────┬─────────┘
│           │                                     ▼
│           │                            ┌──────────────────┐
│           │   200 OK / 403 Forbidden   │  SecurityConfig   │
│           │ ◄───────────────────────── │  (RBAC rules)     │
└──────────┘                             └──────────────────┘
```

### Estructura del Token JWT

```json
{
  "sub": "d800fe7e-f501-4d92-b5ad-c3dfaaca00b8",
  "roles": ["PLAYER", "ADMIN"],
  "username": "Admin",
  "iat": 1774350784,
  "exp": 1774437184
}
```

- **Algoritmo:** HMAC SHA-256 (HS256)
- **Expiración:** 24 horas (86400000 ms)
- **Secret:** Clave Base64 configurada en `application.properties`

### Matriz de Permisos (RBAC)

| Endpoint | Método | PLAYER | AUDITOR | ADMIN | Público |
|---|---|:---:|:---:|:---:|:---:|
| `/api/auth/register` | POST | — | — | — | ✅ |
| `/api/auth/login` | POST | — | — | — | ✅ |
| `/api/game/pick` | POST | ✅ | ❌ | ❌ | ❌ |
| `/api/game/create` | POST | ❌ | ❌ | ✅ | ❌ |
| `/api/game/calculate-loser` | POST | ❌ | ❌ | ✅ | ❌ |
| `/api/audit/current-game` | GET | ❌ | ✅ | ✅ | ❌ |
| `/api/audit/current-game/records` | GET | ❌ | ✅ | ✅ | ❌ |
| `/api/audit/finished-games` | GET | ❌ | ✅ | ✅ | ❌ |
| `/api/chocolatina/value` | PUT | ❌ | ❌ | ✅ | ❌ |
| `/api/chocolatina/value` | GET | ✅ | ✅ | ✅ | ❌ |
| `/api/users/{id}/role` | PUT | ❌ | ❌ | ✅ | ❌ |

### Componentes de Seguridad

| Clase | Responsabilidad |
|---|---|
| `JwtService` | Genera tokens con `Jwts.builder()`, valida con `Jwts.parser().verifyWith()`, extrae claims (userId, roles, username) |
| `JwtRequestFilter` | Extiende `OncePerRequestFilter`. Intercepta cada request, extrae Bearer token, valida, y establece `SecurityContextHolder` con authorities `ROLE_*` |
| `SecurityConfig` | Configura `SecurityFilterChain`: CSRF disabled, sesiones stateless, reglas de autorización por rol en cada endpoint |

---

## 📡 Endpoints de la API

### 1. Autenticación

#### `POST /api/auth/register` — Registrar usuario
Crea un nuevo usuario con rol PLAYER por defecto.

**Request Body:**
```json
{
  "username": "JuanPerez",
  "email": "juan@example.com",
  "password": "123456"
}
```

**Response (201):**
```json
{
  "id": "uuid",
  "username": "JuanPerez",
  "email": "juan@example.com",
  "roles": ["PLAYER"],
  "createdAt": "2026-03-24T10:00:00"
}
```

#### `POST /api/auth/login` — Iniciar sesión
Autentica al usuario y devuelve un token JWT.

**Request Body:**
```json
{
  "email": "juan@example.com",
  "password": "123456"
}
```

**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "userId": "uuid",
  "username": "JuanPerez",
  "roles": ["PLAYER"]
}
```

---

### 2. Gestión de Juego

> ⚠ Todos los endpoints requieren token JWT en el header: `Authorization: Bearer <token>`

#### `POST /api/game/create` — Crear juego (ADMIN)

**Request Body:**
```json
{
  "ruleType": "MIN"
}
```

**Response (201):**
```json
{
  "id": "uuid",
  "status": "ACTIVE",
  "ruleType": "MIN",
  "createdAt": "2026-03-24T10:00:00"
}
```

#### `POST /api/game/pick` — Elegir chocolatina (PLAYER)

**Response (200):**
```json
{
  "id": "uuid",
  "gameId": "uuid",
  "userId": "uuid",
  "username": "JuanPerez",
  "chocolatinaNumber": 157,
  "pickedAt": "2026-03-24T10:05:00"
}
```

#### `POST /api/game/calculate-loser` — Calcular perdedor (ADMIN)

**Request Body:**
```json
{
  "rule": "MIN"
}
```

**Response (200):**
```json
{
  "id": "uuid",
  "gameId": "uuid",
  "loserId": "uuid",
  "loserUsername": "JuanPerez",
  "losingNumber": 3,
  "totalChocolatinas": 5,
  "chocolatinaPrice": 2500.00,
  "totalPaid": 12500.00,
  "ruleType": "MIN",
  "finishedAt": "2026-03-24T10:30:00"
}
```

---

### 3. Configuración de Chocolatina

#### `PUT /api/chocolatina/value` — Actualizar precio (ADMIN)

**Request Body:**
```json
{
  "price": 3000.00
}
```

#### `GET /api/chocolatina/value` — Consultar precio actual (Autenticado)

---

### 4. Auditoría

#### `GET /api/audit/current-game` — Ver juego actual (AUDITOR/ADMIN)

#### `GET /api/audit/current-game/records` — Ver movimientos del juego actual (AUDITOR/ADMIN)

#### `GET /api/audit/finished-games` — Ver juegos finalizados (AUDITOR/ADMIN)

---

### 5. Gestión de Usuarios

#### `PUT /api/users/{userId}/add-role` — Asignar rol (ADMIN)

**Request Body:**
```json
{
  "roleName": "AUDITOR"
}
```

> **Nota:** El primer usuario ADMIN se crea directamente en la base de datos mediante SQL. Los demás usuarios se registran como PLAYER y un ADMIN les puede asignar roles adicionales.

---

##  Reglas de Negocio

| Regla | Descripción |
|---|---|
| **Un juego activo a la vez** | No se puede crear un nuevo juego si ya existe uno con estado ACTIVE |
| **Un pick por jugador por juego** | Cada jugador solo puede tomar una chocolatina por ronda |
| **Números únicos por juego** | No se repiten números de chocolatina dentro del mismo juego |
| **Mínimo 2 jugadores** | No se puede determinar un perdedor con solo 1 jugador |
| **Precio snapshot** | El precio de la chocolatina se captura al momento del cálculo, no como referencia |
| **Regla persistida** | La regla (MIN/MAX) se almacena en `finished_games` junto con el resultado |
| **Limpieza automática** | Al calcular el perdedor, se eliminan los `game_records` y el juego pasa a FINISHED |
| **Rol por defecto PLAYER** | Todo usuario nuevo recibe automáticamente el rol PLAYER |
| **UserId del JWT** | El ID del usuario autenticado se extrae del token JWT, nunca del body del request |

---

## Manejo de Excepciones

El sistema utiliza un `GlobalExceptionHandler` (`@RestControllerAdvice`) que intercepta todas las excepciones y devuelve respuestas JSON estandarizadas:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Username already exists",
  "path": "/api/auth/register",
  "timestamp": "2026-03-24T10:00:00.000"
}
```

| Excepción | HTTP Status | Ejemplo |
|---|---|---|
| `ResourceNotFoundException` | 404 | "No active game found" |
| `InvalidInputException` | 400 | "Username already exists" |
| `UnauthorizedException` | 403 | "Forbidden" |
| `MethodArgumentNotValidException` | 400 | Validaciones de Bean Validation (@NotBlank, @Email, etc.) |
| `HttpMessageNotReadableException` | 400 | JSON malformado o campos con tipos incorrectos |
| `AccessDeniedException` | 403 | Rol no autorizado para el endpoint |
| `RuntimeException` | 500 | Error interno inesperado |

---

## Validaciones

Los DTOs de request utilizan **Bean Validation** (Jakarta Validation):

| Campo | Validación | Mensaje |
|---|---|---|
| `username` | `@NotBlank`, `@Size(3-50)` | "Username is required" |
| `email` | `@NotBlank`, `@Email`, `@Pattern` | "Email must have a valid domain" |
| `password` | `@NotBlank`, `@Size(6-100)` | "Password must be between 6 and 100 characters" |
| `price` | `@NotNull`, `@Positive` | "Price is required" |
| `ruleType` | `@NotBlank` | "Rule type is required" |
| `roleName` | `@NotBlank` | "Role name is required" |

---

## Configuración y Ejecución

### Prerrequisitos

- Java 21+
- PostgreSQL 15+ con una base de datos llamada `chocolatinazodb`
- Maven 3.x

### Pasos para ejecutar

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/<tu-usuario>/chocolatinazo.git
   cd chocolatinazo
   ```

2. **Configurar la base de datos** en `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/chocolatinazodb
   spring.datasource.username=postgres
   spring.datasource.password=tu_password
   ```

3. **Crear los roles iniciales en PostgreSQL:**
   ```sql
   INSERT INTO roles (id, name) VALUES
     (gen_random_uuid(), 'PLAYER'),
     (gen_random_uuid(), 'AUDITOR'),
     (gen_random_uuid(), 'ADMIN');
   ```

4. **Crear el primer usuario ADMIN** (registrar vía `/api/auth/register`, luego por SQL):
   ```sql
   INSERT INTO user_roles (user_id, role_id)
   VALUES ('<user-uuid>', (SELECT id FROM roles WHERE name = 'ADMIN'));
   ```

5. **Compilar y ejecutar:**
   ```bash
   ./mvnw spring-boot:run
   ```
   En Windows:
   ```cmd
   mvnw.cmd spring-boot:run
   ```

6. **La API estará disponible en:** `http://localhost:8080`

### Variables de configuración

| Propiedad | Valor por defecto | Descripción |
|---|---|---|
| `server.port` | 8080 | Puerto del servidor |
| `spring.jpa.hibernate.ddl-auto` | update | Hibernate auto-crea/actualiza tablas |
| `jwt.secret` | (Base64 key) | Clave secreta para firmar tokens JWT |
| `jwt.expiration` | 86400000 | Expiración del token (24h en ms) |

---

## Investigación — Métodos de Control de Acceso

> - La investigación completa se encuentra en la carpeta [`docs/research/`](docs/research/).
> - Los diagramas del proyecto están en [`docs/diagrams/`](docs/diagrams/).


### ¿Por qué RBAC para el Chocolatinazo?

1. **Roles claramente definidos:** PLAYER (juega), AUDITOR (consulta), ADMIN (administra).
2. **Simplicidad:** No se requieren políticas complejas ni atributos dinámicos.
3. **Soporte nativo:** Spring Security implementa RBAC con `hasRole()` y `hasAnyRole()`.
4. **Escalabilidad:** Si se agregan nuevos endpoints, basta con definir qué roles acceden.
5. **ManyToMany:** Un usuario puede tener múltiples roles, dando flexibilidad sin complejidad.

---

## Decisiones de Diseño

### 1. Arquitectura Limpia (Clean Architecture)
Se separaron las responsabilidades en tres capas para que el dominio no dependa de frameworks. Los servicios solo conocen **interfaces (ports)**, nunca implementaciones JPA directamente.

### 2. ManyToMany para User-Role
Un usuario puede tener múltiples roles simultáneamente (ej: PLAYER + ADMIN). Esto es más realista y flexible.

### 3. Snapshot de precio en finished_games
El precio se guarda como valor fijo al momento del cálculo, no como FK. Los juegos históricos no se alteran si el precio cambia.

### 4. UserId extraído del JWT
El ID del usuario autenticado siempre se extrae del token JWT en el SecurityContext, nunca del body. Previene suplantación de identidad.

### 5. Token JWT con roles como claim
Los roles se incluyen en el token para evitar consultas adicionales a la BD en cada request.

### 6. Registro público con rol restringido
Cualquiera puede registrarse, pero siempre como PLAYER. Solo un ADMIN puede asignar roles adicionales. El primer ADMIN se crea por SQL.

### 7. Documentación con Javadoc
Todas las clases, interfaces y métodos públicos están documentados con Javadoc estándar.

---

## Uso de Inteligencia Artificial

Se utilizó **GitHub Copilot** como herramienta de asistencia durante las siguientes fases:

| Fase | Uso de IA |
|---|---|
| **Investigación** | Comparación y análisis de métodos de control de acceso (RBAC, ABAC, PBAC, etc.) |
| **Diseño** | Validación de decisiones arquitectónicas (Clean Architecture, ManyToMany, snapshots) |
| **Desarrollo** | Generación y revisión de código (DTOs, servicios, configuración de seguridad, validaciones) |
| **Documentación** | Generación de Javadoc y documentación técnica |

> **Importante:** Todo el código generado por IA fue revisado, comprendido y adaptado al contexto del proyecto. Se analizaron las sugerencias antes de incorporarlas.


<p align="center">
  <strong>Desarrollado para el Reto Aprendices Evolución — Bancolombia 2026</strong>
</p>

