# 🏠 Real Estate Platform – Microservices Architecture

Plataforma **Real Estate** desarrollada con **arquitectura de microservicios**, orientada a la gestión de propiedades, usuarios, mensajería y agenda de turnos, con autenticación segura mediante **JWT**.

---
<img width="1919" height="992" alt="Screenshot 2026-06-04 154757" src="https://github.com/user-attachments/assets/2e6097f1-8cb1-4531-847f-116a44031f8a" />

## 🚀 Tecnologías

### Backend

* **Spring Boot**
* **Spring Security + JWT**
* **Spring Cloud** (Eureka Server, API Gateway)
* **PostgreSQL**
* **Docker / Docker Compose**
* **WebSocket (STOMP)** para chat en tiempo real

### Frontend

* **Angular**

---

## 🧩 Arquitectura

El sistema está compuesto por los siguientes microservicios:

* **Auth Service** → Registro, login, refresh token y gestión de usuarios
* **User Service** → Gestión de usuarios y propiedades favoritas
* **Properties Service** → CRUD y búsqueda de propiedades
* **Message Service** → Mensajería y contacto con propietarios (email + chat)
* **Agenda Service** → Gestión de turnos / citas
* **API Gateway** → Entrada única al sistema
* **Eureka Server** → Service Discovery

Todos los servicios se comunican a través del **API Gateway** y están registrados en **Eureka**.

---

## 🔐 Autenticación y Seguridad

* Autenticación basada en **JWT**
* **Access Token** + **Refresh Token**
* Spring Security integrado en los microservicios
* El API Gateway propaga headers de seguridad (`X-User-Id`, `X-User-Role`)

---

## 📌 Endpoints Principales

### 🔑 Auth Service (`/auth`)

* `POST /auth/register` → Registro de usuario
* `POST /auth/login` → Login
* `POST /auth/refresh` → Renovar access token
* `GET /auth/me` → Usuario autenticado actual
* `GET /auth/users` → Listado de usuarios
* `GET /auth/{id}` → Usuario por ID

---

### ⭐ Propiedades Favoritas (`/favorites`)

* `POST /favorites` → Agregar propiedad favorita
* `DELETE /favorites/{userId}/{propertyId}` → Eliminar favorita
* `GET /favorites/user/{userId}` → Listar favoritas del usuario

---

### 🏘️ Properties Service (`/properties`)

* `GET /properties/list` → Listar propiedades
* `GET /properties/{id}` → Obtener propiedad por ID
* `POST /properties/create` → Crear propiedad
* `PUT /properties/{id}` → Actualizar propiedad
* `DELETE /properties/{id}` → Eliminar propiedad
* `GET /properties/search?city=` → Buscar por ciudad

---

### 💬 Message Service

#### Contacto por Email

* `POST /api/contact` → Enviar mensaje al propietario

#### Chat en tiempo real

* `WS /app/send` → Envío de mensajes privados
* `GET /api/messages/{userId}` → Historial de mensajes

---

### 📅 Agenda Service (`/appointments`)

* `POST /appointments` → Crear cita
* `GET /appointments/agent/{agentId}` → Citas del agente
* `GET /appointments/client/{clientId}` → Citas del cliente
* `PUT /appointments/{id}/status` → Actualizar estado
* `GET /appointments/date/{yyyy-MM-dd}` → Citas por fecha

---

## 🐳 Docker

El proyecto está preparado para ejecutarse con **Docker Compose**, incluyendo:

* PostgreSQL
* Eureka Server
* API Gateway
* Todos los microservicios

```bash
docker-compose up --build
```

---

## 🖥️ Frontend (Angular)

* Autenticación con JWT
* Listado y búsqueda de propiedades
* Propiedades favoritas
* Chat en tiempo real
* Solicitud y gestión de turnos
---

## 👤 Autor

Desarrollado por **Nicolás Perlo**
