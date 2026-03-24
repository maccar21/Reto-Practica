# 📋 Ejemplos de Respuestas de Error - API Chocolatinazo

## 📝 Estructura de Respuesta de Error

Todas las respuestas de error siguen este formato JSON:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Descripción clara del error",
  "path": "/api/auth/register",
  "timestamp": "2026-03-24T12:30:45.123456"
}
```

---

## 🔐 AUTENTICACIÓN (AuthService)

### Error 1: Username ya existe (registro duplicado)

**Request:**
```bash
POST /api/auth/register
{
  "username": "juan",
  "email": "juan123@gmail.com",
  "password": "Pass123"
}
```

**Respuesta (400 Bad Request):**
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Username already exists",
  "path": "/api/auth/register",
  "timestamp": "2026-03-24T12:30:45.123456"
}
```

---

### Error 2: Email ya existe (registro duplicado)

**Request:**
```bash
POST /api/auth/register
{
  "username": "nuevouser",
  "email": "juan@gmail.com",
  "password": "Pass123"
}
```

**Respuesta (400 Bad Request):**
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Email already exists",
  "path": "/api/auth/register",
  "timestamp": "2026-03-24T12:30:45.123456"
}
```

---

### Error 3: Credenciales inválidas (login fallido)

**Request:**
```bash
POST /api/auth/login
{
  "email": "noexiste@gmail.com",
  "password": "WrongPassword"
}
```

**Respuesta (400 Bad Request):**
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid email/username or password",
  "path": "/api/auth/login",
  "timestamp": "2026-03-24T12:30:45.123456"
}
```

---

## 👤 GESTIÓN DE USUARIOS (UserService)

### Error 4: Usuario no encontrado

**Request:**
```bash
PUT /api/users/invalid-uuid-here/role
Authorization: Bearer <ADMIN_TOKEN>
{
  "roleName": "AUDITOR"
}
```

**Respuesta (404 Not Found):**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: invalid-uuid-here",
  "path": "/api/users/invalid-uuid-here/role",
  "timestamp": "2026-03-24T12:30:45.123456"
}
```

---

### Error 5: Nombre de rol inválido

**Request:**
```bash
PUT /api/users/550e8400-e29b-41d4-a716-446655440001/role
Authorization: Bearer <ADMIN_TOKEN>
{
  "roleName": "SUPERUSER"
}
```

**Respuesta (400 Bad Request):**
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid role name: SUPERUSER. Valid roles are: PLAYER, AUDITOR, ADMIN",
  "path": "/api/users/550e8400-e29b-41d4-a716-446655440001/role",
  "timestamp": "2026-03-24T12:30:45.123456"
}
```

---

### Error 6: Usuario ya tiene ese rol

**Request:**
```bash
PUT /api/users/550e8400-e29b-41d4-a716-446655440001/role
Authorization: Bearer <ADMIN_TOKEN>
{
  "roleName": "PLAYER"
}
```

**Respuesta (400 Bad Request):**
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "User already has role: PLAYER",
  "path": "/api/users/550e8400-e29b-41d4-a716-446655440001/role",
  "timestamp": "2026-03-24T12:30:45.123456"
}
```

---

## 🔓 AUTORIZACIÓN (SecurityConfig)

### Error 7: PLAYER intenta agregar rol (sin permiso ADMIN)

**Request:**
```bash
PUT /api/users/550e8400-e29b-41d4-a716-446655440002/role
Authorization: Bearer <PLAYER_TOKEN>
{
  "roleName": "AUDITOR"
}
```

**Respuesta (403 Forbidden):**
```json
{
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/users/550e8400-e29b-41d4-a716-446655440002/role",
  "timestamp": "2026-03-24T12:30:45.123456"
}
```

---

### Error 8: Acceso sin autenticación

**Request:**
```bash
PUT /api/users/550e8400-e29b-41d4-a716-446655440001/role
(Sin header Authorization)
{
  "roleName": "ADMIN"
}
```

**Respuesta (403 Forbidden):**
```json
{
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/users/550e8400-e29b-41d4-a716-446655440001/role",
  "timestamp": "2026-03-24T12:30:45.123456"
}
```

---

## ✅ VALIDACIÓN DE ENTRADA (Annotations)

### Error 9: Campo roleName vacío (validación @NotBlank)

**Request:**
```bash
PUT /api/users/550e8400-e29b-41d4-a716-446655440001/role
Authorization: Bearer <ADMIN_TOKEN>
{
  "roleName": ""
}
```

**Respuesta (400 Bad Request):**
```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "roleName: Role name is required",
  "path": "/api/users/550e8400-e29b-41d4-a716-446655440001/role",
  "timestamp": "2026-03-24T12:30:45.123456"
}
```

---

### Error 10: Request body faltante o inválido

**Request:**
```bash
PUT /api/users/550e8400-e29b-41d4-a716-446655440001/role
Authorization: Bearer <ADMIN_TOKEN>
{
  "nombre_incorrecto": "ADMIN"
}
```

**Respuesta (400 Bad Request):**
```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "roleName: Role name is required",
  "path": "/api/users/550e8400-e29b-41d4-a716-446655440001/role",
  "timestamp": "2026-03-24T12:30:45.123456"
}
```

---

## 📊 Resumen de Códigos HTTP

| Código | Tipo | Ejemplo |
|--------|------|---------|
| **200** | ✅ OK | Operación exitosa, respuesta con UserResponse |
| **201** | ✅ Created | Usuario registrado exitosamente |
| **400** | ❌ Bad Request | Username duplicado, rol inválido, validación fallida |
| **403** | ❌ Forbidden | Sin permiso ADMIN, sin autenticación |
| **404** | ❌ Not Found | Usuario no encontrado |
| **500** | ❌ Server Error | Error interno del servidor (raro) |

---

## 🧪 Casos de Prueba en Postman

### Test Suite Recomendado

1. **Registro exitoso** → 201 Created
2. **Registro con username duplicado** → 400 Bad Request
3. **Registro con email duplicado** → 400 Bad Request
4. **Login exitoso** → 200 OK con JWT
5. **Login con credenciales inválidas** → 400 Bad Request
6. **Agregar rol como ADMIN** → 200 OK
7. **Agregar rol como PLAYER** → 403 Forbidden
8. **Agregar rol con nombre inválido** → 400 Bad Request
9. **Agregar rol a usuario inexistente** → 404 Not Found
10. **Agregar rol que el usuario ya tiene** → 400 Bad Request

---

## 🔍 Depuración

**En el servidor:**
- Todos los errores se registran automáticamente en los logs de Spring Boot.
- El timestamp en la respuesta te ayuda a correlacionar con logs del servidor.
- El campo "path" muestra qué endpoint generó el error.

**En Postman:**
- Revisa la pestaña "Body" en la respuesta para ver el JSON de error completo.
- Usa el tab "Tests" para validar códigos HTTP esperados.

---

## 📝 Nota Importante

Todos los errores son manejados por **GlobalExceptionHandler**, que captura excepciones y las convierte en respuestas JSON estándar. Si la aplicación recibe una excepción no esperada, retorna un 500 con mensaje genérico para no exponer detalles internos sensibles.

