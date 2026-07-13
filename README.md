# Kata: Flujo Genérico de Aprobación

Proyecto desarrollado como solución a un reto técnico (kata) de backend/cloud: aplicación web que centraliza flujos genéricos de aprobación — despliegue de microservicios, acceso a herramientas internas, cambios técnicos e incorporación de nuevas herramientas al catálogo. Permite crear solicitudes, notificar al aprobador, aprobar/rechazar con comentarios y mantener el histórico completo de decisiones.

> **Estado actual**: backend y frontend están completos. El backend expone una **API RESTful** (JSON sobre HTTP, ver sección [API](#api)) que se puede probar entera con Postman o Swagger sin necesidad de frontend. El frontend (React + Vite) cubre los 5 requisitos funcionales del kata de punta a punta. Lo único pendiente es el despliegue a AWS (ver [Pruebas en la nube](#pruebas-en-la-nube-aws)).

## Funcionalidad

- Crear una solicitud de aprobación (título, descripción, solicitante, aprobador, tipo).
- Notificar al aprobador mediante una bandeja de entrada in-app cuando tiene una solicitud pendiente.
- Aprobar o rechazar una solicitud, con comentario opcional.
- Consultar el histórico completo: estado, fecha, usuario que actuó y comentarios.
- Cada solicitud tiene un ID único (UUID).

## Stack

| Capa | Tecnología |
|---|---|
| Backend | Java 17 + Spring Boot 3 |
| Persistencia | PostgreSQL (prod) / H2 (tests) |
| Migraciones | Flyway |
| API | RESTful (JSON sobre HTTP), documentada con OpenAPI/Swagger |
| Comunicación Frontend↔Backend | El frontend consume la API REST directo vía `fetch`, sin gateway/BFF intermedio (ver [Limitaciones](#limitaciones-y-decisiones-de-alcance)). En local, el dev server de Vite hace proxy de `/api` hacia el backend — no hace falta configurar CORS. |
| Tests | JUnit 5 + Mockito + Spring Boot Test |
| Frontend | React (JavaScript) + Vite + Tailwind CSS — selector de usuario simulado (sin auth real, mismo criterio que `X-Usuario`) |
| Build | Maven (backend) / npm (frontend) |
| DB local | Docker Compose (PostgreSQL) |
| CI/CD | GitHub Actions — CI: build + tests en cada push/PR a `main` o `develop`. CD: deploy automático a EC2 en cada push a `main` |

## Arquitectura

El backend sigue arquitectura hexagonal (puertos y adaptadores): el dominio no depende de frameworks ni de infraestructura.

```mermaid
graph TD
    Client[Frontend / Cliente REST] --> Controller[Controller]
    Controller --> UseCase[Caso de Uso]
    UseCase --> DomainService[Servicio de Dominio]
    UseCase --> RepoPort[Puerto: Repositorio]
    UseCase --> NotifPort[Puerto: Notificación]
    RepoPort --> RepoAdapter[Adaptador JPA]
    NotifPort --> NotifAdapter[Adaptador Notificación]
    RepoAdapter --> DB[(PostgreSQL)]
    NotifAdapter --> DB
```

### Ciclo de vida de una solicitud

```mermaid
stateDiagram-v2
    [*] --> PENDIENTE: crear solicitud
    PENDIENTE --> APROBADO: aprobador aprueba
    PENDIENTE --> RECHAZADO: aprobador rechaza
    APROBADO --> [*]
    RECHAZADO --> [*]
```

### Estructura de paquetes (backend)

```
backend/src/main/java/com/kata/aprobaciones/
├── domain/
│   ├── model/        Solicitud, HistorialCambio, Notificacion, EstadoSolicitud, TipoSolicitud
│   ├── exception/     excepciones de dominio
│   └── port/
│       ├── in/        casos de uso (interfaces)
│       └── out/       repositorios, notificación (interfaces)
├── application/       implementación de los casos de uso
└── infrastructure/
    ├── persistence/    entidades JPA, mappers, adapters
    ├── web/            controllers + DTOs + manejo de errores
    └── notification/   bandeja de entrada simulada
```

### Estructura de paquetes (frontend)

```
frontend/src/
├── api/
│   └── client.js       wrapper de fetch para los 7 endpoints del backend
├── context/
│   └── UsuarioContext.jsx   selector de usuario simulado (jperez, mgarcia, alopez)
├── components/
│   └── Layout.jsx       nav + selector de usuario + Outlet del router
├── pages/
│   ├── CrearSolicitud.jsx
│   ├── ListarSolicitudes.jsx
│   ├── DetalleSolicitud.jsx   detalle + historial + aprobar/rechazar
│   └── Notificaciones.jsx
├── App.jsx              rutas (react-router-dom)
└── main.jsx
```

## API

Todas las rutas requieren el header `X-Usuario` (simula el usuario de red autenticado, sin JWT real).

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/solicitudes` | Crear solicitud |
| GET | `/api/solicitudes` | Listar solicitudes (filtros opcionales: `aprobador`, `estado`) |
| GET | `/api/solicitudes/{id}` | Detalle + histórico completo |
| PATCH | `/api/solicitudes/{id}/aprobar` | Aprobar (solo el aprobador asignado, solo si está `PENDIENTE`) |
| PATCH | `/api/solicitudes/{id}/rechazar` | Rechazar (mismas reglas que aprobar) |
| GET | `/api/notificaciones/{usuario}` | Bandeja de entrada del usuario |
| PATCH | `/api/notificaciones/{id}/leer` | Marcar notificación como leída |
| GET | `/api/catalogo/tipos` | Catálogo de tipos de solicitud (para el dropdown del frontend) |

Con el backend corriendo, la documentación interactiva está en `http://localhost:8080/swagger-ui/index.html`.

## Cómo correr el proyecto

### Requisitos
- Git
- Java 17
- Node 20+ (para el frontend)
- Docker + Docker Compose (para PostgreSQL local)

### 0. Clonar el repositorio

```bash
git clone https://github.com/CRISTIANGUZMAN-DR/kata-aprobaciones.git
cd kata-aprobaciones
```

Estructura del repo:

```
kata-aprobaciones/
├── backend/                  ← API Spring Boot (Java 17, Maven) + Dockerfile
├── frontend/                 ← SPA React + Vite + Tailwind + Dockerfile + nginx.conf
├── docker-compose.yml        ← solo Postgres, para desarrollo local
├── docker-compose.prod.yml   ← postgres + backend + frontend, para el deploy en AWS
├── postman/
└── README.md
```

### 1. Levantar la base de datos

```bash
cp .env.example .env
# editar .env y poner una contraseña real en DB_PASSWORD
docker compose up -d
```

Verificar que quedó sana:

```bash
docker compose ps   # debe mostrar "healthy"
```

### 2. Levantar el backend

```bash
cd backend
DB_PASSWORD=<la misma que pusiste en .env> ./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`. Al arrancar, Flyway crea el esquema automáticamente (no hace falta correr scripts a mano).

### 3. Levantar el frontend

```bash
cd frontend
npm install
npm run dev
```

La SPA queda disponible en `http://localhost:5173` — el dev server de Vite hace proxy de `/api` hacia `http://localhost:8080`, así que el backend tiene que estar corriendo (paso anterior).

### 4. Bajar todo cuando termines

```bash
# Ctrl+C en las terminales donde corren spring-boot:run y npm run dev (o):
pkill -f "spring-boot:run"
pkill -f "vite"

# Bajar y borrar el contenedor de PostgreSQL:
docker compose down

# Si además querés borrar los datos guardados (empezar de cero la próxima vez):
docker compose down -v
```

### Tests

```bash
cd backend
./mvnw test              # unit + @WebMvcTest (usa H2 en memoria, no necesita Docker)
./mvnw clean verify       # + tests de integración (@SpringBootTest, también con H2)
./mvnw jacoco:report      # reporte de cobertura en target/site/jacoco/index.html
```

## Probar con Postman

La colección está en [`postman/kata-aprobaciones.postman_collection.json`](postman/kata-aprobaciones.postman_collection.json), con los 7 endpoints organizados en 3 carpetas (Solicitudes, Notificaciones, Catálogo) y casos de error incluidos (400, 404, 409).

1. **Importar**: Postman → `Import` → seleccionar ese archivo.
2. La colección trae variables propias (`baseUrl`, `solicitudId`, `notificacionId`) con `baseUrl` ya en `http://localhost:8080` — no hace falta crear un Environment aparte.
3. Con el backend corriendo (ver arriba), flujo sugerido:
   1. Correr **"Crear solicitud"** → copiar el `id` de la respuesta y pegarlo en la variable de colección `solicitudId`
   2. Correr **"Bandeja de entrada del aprobador"** → copiar el `id` de la notificación en `notificacionId`
   3. Probar el resto: listar, obtener detalle, aprobar/rechazar, marcar notificación como leída, catálogo de tipos

## Pruebas en la nube (AWS)

> ⏳ **Pendiente** — la infraestructura de deploy (Dockerfiles, `docker-compose.prod.yml`, workflow de CD) ya está lista; falta levantar la EC2. Esta sección se completa apenas la app quede corriendo ahí.

- **App (frontend + API vía proxy)**: `<URL pendiente>`
- **Swagger**: `<URL pendiente>/swagger-ui/index.html`

### Arquitectura de despliegue

Una sola EC2 con Docker Compose corriendo 3 contenedores: `postgres` (sin puerto expuesto, solo red interna), `backend` (Spring Boot, puerto interno 8080) y `frontend` (Nginx, puerto 80 público) que sirve el build de React y proxea `/api` al backend — todo same-origin, sin CORS.

```mermaid
graph LR
    Internet -->|puerto 80| Nginx
    Nginx -->|estático| ReactBuild[Build de React]
    Nginx -->|"/api/*"| Backend[Backend Spring Boot]
    Backend --> Postgres[(PostgreSQL)]
```

### Checklist de infraestructura (una sola vez)

**1. Lanzar la EC2**
- Instancia Free Tier: `t2.micro` o `t3.micro`, Ubuntu 22.04
- Asignar una **Elastic IP** (para que la IP no cambie si la instancia se reinicia)
- Security Group: puerto **80** abierto al público, puerto **22** restringido a la IP de quien administra
- Descargar el `.pem` (llave SSH) al crearla

**2. Preparar el servidor** (por SSH, una sola vez)

```bash
# Actualizar sistema
sudo apt update && sudo apt upgrade -y

# Instalar Docker
curl -fsSL https://get.docker.com | sudo sh
sudo usermod -aG docker $USER
# cerrar la sesión SSH y volver a entrar para que el grupo tome efecto

# Hardening basico: parches de seguridad automaticos + proteccion contra fuerza bruta en SSH
sudo apt install -y unattended-upgrades fail2ban
sudo dpkg-reconfigure -plow unattended-upgrades
sudo systemctl enable --now fail2ban
```

**3. Clonar el repo y configurar el `.env`**

```bash
git clone https://github.com/CRISTIANGUZMAN-DR/kata-aprobaciones.git ~/kata-aprobaciones
cd ~/kata-aprobaciones
cp .env.example .env
nano .env   # poner un DB_PASSWORD real, este archivo nunca se commitea
```

**4. Primer deploy manual**

```bash
docker compose -f docker-compose.prod.yml up -d --build
```

La app queda disponible en `http://<IP-de-la-EC2>`.

**5. Cargar los secrets en GitHub** (Settings → Secrets and variables → Actions del repo)

| Secret | Valor |
|---|---|
| `EC2_HOST` | IP pública (Elastic IP) de la instancia |
| `EC2_USER` | usuario SSH (`ubuntu` en Ubuntu) |
| `EC2_SSH_KEY` | contenido completo del `.pem` |

### Cómo queda el flujo después de esto

De ahí en adelante es automático: cada merge a `main` dispara el workflow [`cd.yml`](.github/workflows/cd.yml), que se conecta por SSH a la EC2, hace `git pull` y `docker compose -f docker-compose.prod.yml up -d --build`. No hace falta tocar nada manualmente después del setup inicial.

## Usuarios de prueba

Usuarios ficticios para probar el flujo (sin autenticación real, se seleccionan desde el frontend o se pasan a mano en el header `X-Usuario`):

- `jperez`
- `mgarcia`
- `alopez`

## Cobertura y calidad

- 35 tests (dominio, casos de uso con Mockito, controllers con `@WebMvcTest`, integración con `@SpringBootTest` + H2 + Flyway)
- ≥80% de cobertura en `domain` + `application` (gate configurado en el proyecto)
- CI en GitHub Actions: build + tests en cada push/PR a `main` o `develop`

## Limitaciones y decisiones de alcance

Dado el tiempo acotado del reto, quedaron fuera de alcance a propósito (no por descuido):

- **Sin capa de API Gateway / BFF**: el frontend le habla directo al backend Spring Boot — no hay una capa intermedia que agregue requests, centralice rate limiting o desacople el contrato del front del contrato interno del backend. Para el tamaño de este kata no se justifica; en un sistema real con más de un cliente o más de un servicio backend, sí tendría sentido agregarla.
- **`X-Usuario` es simulado, no autenticación real**: no hay JWT ni validación de identidad — cualquiera puede mandar cualquier usuario en el header. Suficiente para el alcance del kata (que explícitamente no pide auth real), pero no es un patrón productivo.
- **Catálogo de tipos de solicitud como `enum` fijo, no dinámico**: `TipoSolicitud` es un enum de Java con 4 valores (`DESPLIEGUE`, `ACCESO`, `CAMBIO_TECNICO`, `INCORPORACION_TECNICA`) — agregar un tipo nuevo hoy requiere tocar código y redeployar. El frontend sí es dinámico (consume `GET /api/catalogo/tipos`, no tiene los tipos hardcodeados), pero el backend no. Por tiempo se optó por el enum; la alternativa extensible es mover el catálogo a una tabla en base de datos (migración Flyway + semilla de datos), de forma que agregar un tipo sea un insert y no un despliegue.
- **Sin HTTPS**: el deploy usa la IP pública de la EC2 directo, sin dominio — Let's Encrypt no emite certificados para IPs. Para una demo de un par de semanas no se justifica comprar un dominio.
- **CD por SSH directo a la EC2, sin registry de imágenes**: el workflow hace `git pull` + `docker compose build` en la propia instancia, en vez de construir la imagen en CI y publicarla en un registry (ECR/Docker Hub). Es más simple para una sola instancia y evita pagar/configurar un registry, a costa de que el puerto 22 tenga que aceptar conexiones desde las IPs (dinámicas) de los runners de GitHub Actions, además del acceso administrativo restringido a IPs conocidas.
