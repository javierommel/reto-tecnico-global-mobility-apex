# Worker Java y Go para Procesamiento de Pedidos con Enriquecimiento de Datos y Resiliencia

Este proyecto implementa un sistema distribuido compuesto por:

- Un **Worker** en Java (Spring Boot + WebFlux) que:
  - Consume mensajes desde Kafka.
  - Enriquese datos desde APIs externas escritas en Go.
  - Valida y guarda la información procesada en MongoDB.
  - Aplica reintentos exponenciales y locking distribuido con Redis.

- Una **API externa** escrita en **Go + Gin**, conectada a PostgreSQL, que:
  - Expone información de clientes y productos.

- Un microservicio **productor de pedidos** (`orderproducer`) para simular tráfico en Kafka.

---

## Tecnologías Utilizadas

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

## Arquitectura General

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
```
---

## Estructura del Proyecto
```text
reto-tecnico-global-mobility-apex/
├── orderproducer/
│   └── Dockerfile
│   └── src/...
├── orderworker/
│   └── Dockerfile
│   └── src/...
├── go-api/
│   └── Dockerfile
│   └── main.go
├── init.sql
├── docker-compose.yml
├── README.md
└── ...
```

---

## Requisitos Técnicos

- Java 21
- Docker
- Docker Compose

---

## Instrucciones de Despliegue


```bash
  - git clone https://github.com/javierommel/reto-tecnico-global-mobility-apex.git
  - cd reto-tecnico-global-mobility-apex
#Compilar microservicio orderworker
  - cd reto-tecnico-global-mobility-apex/orderworker
  - mvn clean package -DskipTests

#Compilar microservicio orderproducer
  - cd reto-tecnico-global-mobility-apex/orderproducer
  - mvn clean package -DskipTests

#Despliegue
  - cd reto-tecnico-global-mobility-apex
  - docker-compose up --build -d
```

## Endpoints Expuestos
```bash
#OrderProducer
#Para lanzar las pruebas puedes hacerlas mediante Postman o curl

curl -X POST "http://localhost:8082/orders/send?customerId=customer-001&productIds=product-001&productIds=product-002"

```


## Ejecutar Pruebas Unitarias
```bash
- cd reto-tecnico-global-mobility-apex/orderworker
- mvn clean test
```

👤 Autor
Rommel Chocho
Desarrollador Senior Backend | Java + Go + Kafka + MongoDB + Redis