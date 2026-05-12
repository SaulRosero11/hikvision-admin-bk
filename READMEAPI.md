# Hikvision Admin - API REST

Sistema de administracion de accesos por pisos con dispositivos Hikvision. Arquitectura Clean Architecture con Spring Boot 4.

## Configuracion

```properties
# Base de datos PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/hikvisionAdmin
spring.datasource.username=postgres
spring.datasource.password=123456

# JWT
jwt.secret=<base64-encoded-secret>
jwt.expiration=86400000
```

**Usuario por defecto:** `admin` / `admin123`

---

## Autenticacion

Todos los endpoints (excepto login y register) requieren header:
```
Authorization: Bearer <token>
```

---

## Endpoints

### 1. Auth (`/api/v1/auth`)

#### POST `/api/v1/auth/login`
Autenticar administrador.

**Request:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response 200:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tipo": "Bearer",
  "administradorId": 1,
  "username": "admin",
  "email": "admin@sistema.com",
  "nombreCompleto": "Administrador",
  "expiracion": 86400000,
  "buildings": [
    { "id": 1, "name": "Edificio Principal", "address": "..." }
  ]
}
```

---

#### POST `/api/v1/auth/register`
Registrar nuevo administrador.

**Request:**
```json
{
  "username": "nuevoadmin",
  "password": "password123",
  "email": "nuevo@email.com",
  "fullName": "Nuevo Admin",
  "buildingId": 1
}
```

**Response 200:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tipo": "Bearer",
  "username": "nuevoadmin",
  "email": "nuevo@email.com",
  "fullName": "Nuevo Admin",
  "isActive": true,
  "buildings": [...]
}
```

---

#### GET `/api/v1/auth/me`
Obtener perfil del administrador autenticado.

**Response 200:** Mismo formato que register response.

---

#### POST `/api/v1/auth/cambiar-password`
Cambiar contrasena.

**Request:**
```json
{
  "passwordActual": "admin123",
  "passwordNuevo": "newPassword456"
}
```

**Response 200:**
```json
{
  "mensaje": "Contraseña actualizada exitosamente"
}
```

---

### 2. Buildings (`/api/v1/building`)

#### GET `/api/v1/building`
Listar todos los edificios. Filtro opcional por `adminId`.

**Query params:** `?adminId=1` (opcional)

**Response 200:**
```json
[
  {
    "name": "Edificio Principal",
    "address": "Av. Principal 123",
    "numberOfFloors": 5,
    "description": "Edificio central",
    "phone": "0999999999",
    "isActive": true
  }
]
```

---

#### GET `/api/v1/building/{id}`
Obtener edificio por ID.

**Response 200:** Mismo objeto del array anterior.

---

#### POST `/api/v1/building`
Crear edificio. Asocia automaticamente al admin autenticado.

**Request:**
```json
{
  "name": "Edificio Norte",
  "address": "Calle Norte 456",
  "numberOfFloors": 3,
  "description": "Segundo edificio",
  "phone": "0988888888",
  "isActive": true
}
```

**Response 201:** Mismo formato response.

---

#### PUT `/api/v1/building/{id}`
Actualizar edificio.

**Request:** Mismo formato que POST.

**Response 200:** Edificio actualizado.

---

#### DELETE `/api/v1/building/{id}`
Eliminar edificio.

**Response 204:** No content.

---

### 3. Floors (`/api/v1/floors`)

#### GET `/api/v1/floors`
Listar pisos. Filtro opcional por `buildingId`.

**Query params:** `?buildingId=1` (opcional)

**Response 200:**
```json
[
  {
    "id": 1,
    "floorNumber": 1,
    "name": "Piso 1 - Recepcion",
    "description": "Planta baja",
    "buildingId": 1,
    "isActive": true,
    "createdUser": "admin",
    "createdDate": "2026-02-17T10:00:00",
    "modifiedUser": null,
    "modifiedDate": null
  }
]
```

---

#### GET `/api/v1/floors/{id}`
Obtener piso por ID.

---

#### POST `/api/v1/floors?buildingId={buildingId}`
Crear piso en un edificio.

**Query params:** `buildingId` (requerido)

**Request:**
```json
{
  "floorNumber": 2,
  "name": "Piso 2 - Oficinas",
  "description": "Oficinas administrativas"
}
```

**Response 201:** Piso creado con ID y auditoria.

---

#### PUT `/api/v1/floors/{id}`
Actualizar piso.

**Request:** Mismo formato que POST + `isActive` opcional.

---

#### DELETE `/api/v1/floors/{id}`
Eliminar piso.

**Response 204:** No content.

---

### 4. Devices (`/api/v1/devices`)

#### GET `/api/v1/devices`
Listar dispositivos. Filtros opcionales.

**Query params:** `?floorId=1` o `?buildingId=1` (opcionales)

**Response 200:**
```json
[
  {
    "id": 1,
    "code": "AC-P1-001",
    "ip": "192.168.1.100",
    "port": 80,
    "model": "DS-K1T671M",
    "location": "Entrada principal piso 1",
    "deviceUser": "admin",
    "devicePassword": "password123",
    "macAddress": "AA:BB:CC:DD:EE:FF",
    "serialNumber": "DS-K1T671M20210101",
    "deviceType": "FACIAL_READER",
    "firmwareVersion": "V2.3.97",
    "isEnabled": true,
    "floorId": 1,
    "isActive": true,
    "createdUser": "admin",
    "createdDate": "2026-02-17T10:00:00",
    "modifiedUser": null,
    "modifiedDate": null
  }
]
```

---

#### GET `/api/v1/devices/{id}`
Obtener dispositivo por ID.

---

#### POST `/api/v1/devices?floorId={floorId}`
Crear dispositivo. **Prueba conexion automaticamente** y auto-completa `model`, `serialNumber`, `macAddress`, `firmwareVersion` desde el dispositivo.

**Query params:** `floorId` (requerido)

**Request (minimo):**
```json
{
  "code": "AC-P1-001",
  "ip": "192.168.1.100",
  "port": 80,
  "deviceUser": "admin",
  "devicePassword": "Hik12345",
  "deviceType": "FACIAL_READER",
  "location": "Entrada piso 1"
}
```

**Soporte ngrok/URL completa:**
```json
{
  "code": "AC-P1-001",
  "ip": "https://397a-2800-bf0-4587-c327-d8c5-20ce-fb2a-c400.ngrok-free.app",
  "deviceUser": "admin",
  "devicePassword": "Hik12345",
  "deviceType": "FACIAL_READER",
  "location": "Entrada piso 1"
}
```

**Response 201:** Dispositivo con toda la info auto-completada.

**Error si no hay conexion:**
```json
{
  "timestamp": "2026-02-17T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Cannot add device: connection test failed for 192.168.1.100"
}
```

**DeviceType valores posibles:**
`ACCESS_CONTROL`, `FACIAL_READER`, `CARD_READER`, `FINGERPRINT_READER`, `TURNSTILE`, `AUTOMATIC_GATE`, `IP_CAMERA`, `INTERCOM`, `OTHER`

---

#### PUT `/api/v1/devices/{id}`
Actualizar dispositivo.

**Request:** Mismo formato que POST.

---

#### DELETE `/api/v1/devices/{id}`
Eliminar dispositivo.

**Response 204:** No content.

---

#### GET `/api/v1/devices/{id}/test-connection`
Probar conexion a un dispositivo existente.

**Response 200:**
```json
{
  "connected": true
}
```

---

#### GET `/api/v1/devices/{id}/info`
Obtener informacion real del dispositivo Hikvision via ISAPI.

**Response 200:**
```json
{
  "connected": true,
  "deviceName": "Access Controller",
  "model": "DS-K1T671M",
  "serialNumber": "DS-K1T671M20210101",
  "firmwareVersion": "V2.3.97 build 220120",
  "macAddress": "aa:bb:cc:dd:ee:ff",
  "rawResponse": "<DeviceInfo>...</DeviceInfo>"
}
```

---

#### POST `/api/v1/devices/{id}/events`
Buscar historial de eventos de acceso del dispositivo. Incluye imagen capturada si esta disponible.

**Query params:**
- `beginTime` (requerido) - formato ISO8601: `2026-02-17T00:00:00`
- `endTime` (requerido) - formato ISO8601: `2026-02-17T23:59:59`
- `searchPosition` (opcional, default: 0)
- `maxResults` (opcional, default: 30)

**Response 200:**
```json
{
  "AcsEvent": {
    "searchID": "1",
    "responseStatusStrg": "OK",
    "numOfMatches": 2,
    "totalMatches": 2,
    "InfoList": [
      {
        "major": 5,
        "minor": 75,
        "time": "2026-02-17T08:30:00+05:00",
        "cardNo": "",
        "cardType": 1,
        "name": "Juan Perez",
        "employeeNoString": "1723456789",
        "doorNo": 1,
        "currentVerifyMode": "faceOrFp",
        "serialNo": 1234,
        "pictureURL": "data:image/jpeg;base64,/9j/4AAQ..."
      }
    ]
  }
}
```

---

### 5. Users (`/api/v1/users`)

#### GET `/api/v1/users?buildingId={buildingId}`
Listar usuarios de un edificio.

**Query params:** `buildingId` (requerido)

**Response 200:**
```json
[
  {
    "id": 1,
    "identification": "1723456789",
    "fullName": "Juan Perez",
    "createdDate": "2026-02-17T10:00:00",
    "buildingId": 1,
    "buildingName": null,
    "gender": "MALE",
    "roomNumber": 101,
    "floorNumber": 1,
    "doorRight": "1",
    "localUiRight": false,
    "validity": {
      "isEnabled": true,
      "beginTime": "2026-01-01T00:00:00",
      "endTime": "2027-12-31T23:59:59",
      "timeType": "LOCAL"
    },
    "images": [
      { "id": 1, "imageType": "face", "extension": "jpg" }
    ],
    "floorPermissions": [
      {
        "id": 1,
        "floorId": 1,
        "floorName": null,
        "accessAllowed": true,
        "isSynced": false,
        "assignedDate": "2026-02-17T10:00:00",
        "expirationDate": null,
        "isActive": true
      }
    ]
  }
]
```

---

#### GET `/api/v1/users/{id}`
Obtener usuario por ID.

---

#### POST `/api/v1/users`
Crear usuario con imagen y permisos de pisos. Al asignar pisos, automaticamente inscribe al usuario en todos los dispositivos Hikvision del piso (crea persona + sube foto facial).

**Request:**
```json
{
  "identification": "1723456789",
  "fullName": "Juan Perez",
  "buildingId": 1,
  "gender": "MALE",
  "roomNumber": 101,
  "floorNumber": 1,
  "doorRight": "1",
  "localUiRight": false,
  "validity": {
    "isEnabled": true,
    "beginTime": "2026-01-01T00:00:00",
    "endTime": "2027-12-31T23:59:59",
    "timeType": "LOCAL"
  },
  "images": [
    {
      "imageBase64": "/9j/4AAQSkZJRgABAQ...",
      "imageType": "face",
      "extension": "jpg"
    }
  ],
  "floorIds": [1, 2]
}
```

**Response 201:** Usuario completo con permisos y imagenes.

**Gender valores:** `MALE`, `FEMALE`, `OTHER`

---

#### PUT `/api/v1/users/{id}`
Actualizar usuario. Si se envian nuevas imagenes, se reemplazan y se actualizan en todos los dispositivos. Si se cambian los `floorIds`, se agregan/eliminan permisos en los dispositivos correspondientes.

**Request:** Mismo formato que POST.

---

#### DELETE `/api/v1/users/{id}`
Eliminar usuario. **Automaticamente elimina al usuario de todos los dispositivos Hikvision** donde tenia acceso antes de borrar de la base de datos.

**Response 204:** No content.

---

#### PUT `/api/v1/users/{id}/floor-permissions`
Sincronizar permisos de pisos independientemente. Compara pisos actuales vs nuevos: agrega los nuevos (inscribe en dispositivos) y elimina los que ya no estan (borra de dispositivos).

**Request body (lista de IDs de pisos):**
```json
[1, 2, 3]
```

**Response 200:** OK (sin body).

---

## Logica de negocio clave

### Sincronizacion de pisos con dispositivos Hikvision

Cuando se asigna un piso a un usuario:
1. Se crea el permiso en la base de datos
2. Se obtienen todos los dispositivos del piso
3. Para cada dispositivo:
   - Se crea la persona via ISAPI (`UserInfo/Record`)
   - Se sube la foto facial via ISAPI (`FaceDataRecord/Record`)

Cuando se revoca un piso:
1. Para cada dispositivo del piso:
   - Se elimina la foto facial (`FaceDataRecord/Delete`)
   - Se elimina la persona (`UserInfoDetail/Delete`)
2. Se elimina el permiso de la base de datos

### Conexion a dispositivos

- Soporta IP directa: `192.168.1.100` con puerto
- Soporta URLs ngrok: `https://xxx.ngrok-free.app` (sin puerto)
- Autenticacion: **DigestAuth** con usuario y contrasena del dispositivo
- La contrasena se almacena encriptada (AES-256) en la base de datos

### Errores

Todas las respuestas de error siguen el formato:
```json
{
  "timestamp": "2026-02-17T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Descripcion del error"
}
```

Errores de validacion:
```json
{
  "timestamp": "2026-02-17T10:00:00",
  "status": 400,
  "error": "Validation Failed",
  "fields": {
    "identification": "Identification is required",
    "fullName": "Full name is required"
  }
}
```
