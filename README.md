# 🛠️ Worker Java y Go para Procesamiento de Pedidos con Enriquecimiento de Datos y Resiliencia

Este proyecto implementa un sistema distribuido compuesto por:

- Un **Worker** en Java (Spring Boot + WebFlux) que:
  - Consume mensajes desde Kafka.
  - Enriquese datos desde APIs externas escritas en Go.
  - Valida y guarda la información procesada en MongoDB.
  - Aplica reintentos exponenciales y locking distribuido con Redis.

- Una **API externa** escrita en **Go + Gin**, conectada a PostgreSQL, que:
  - Expone información de clientes y productos.

- Un microservicio **productor de pedidos** (`order-producer`) para simular tráfico en Kafka.

---

## 📦 Tecnologías Utilizadas

| Lenguaje / Herramienta | Rol |
|------------------------|-----|
| Java 21 + Spring Boot + WebFlux | Worker reactivo |
| Kafka + Zookeeper     | Mensajería (event-driven) |
| MongoDB               | Almacenamiento de pedidos |
| Redis                 | Reintentos + locking distribuido |
| Go + Gin              | Microservicio REST externo |
| PostgreSQL            | Base de datos para APIs Go |
| Docker Compose        | Orquestación de entorno |
| GitHub                | Versionamiento |

---

## 🧩 Arquitectura General

```plaintext
Kafka (orders-topic)
      │
      ▼
[Worker Java]
   │     │
   ▼     ▼
[API Cliente - Go]  [API Productos - Go]
       │
       ▼
    PostgreSQL
       │
       ▼
   [MongoDB] (guardar pedido procesado)
       │
       ▼
     Redis (retry + locks)

👤 Autor
Rommel Chocho
Desarrollador Senior Backend | Java + Go + Kafka + MongoDB + Redis
LinkedIn | GitHub