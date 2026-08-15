# Módulo 0 - Lección 5: Registros de Decisiones de Arquitectura (ADR), OpenAPI & gRPC Protobuf

---

## 1. 🐣 Rincón Junior: Conceptos desde Cero & Analogías

### ¿Qué es un ADR (Architectural Decision Record)?
Un **ADR** es la bitácora o diario de a bordo del barco de software. Si dentro de 6 meses un desarrollador pregunta: *"¿Por qué usamos Virtual Threads en lugar de WebFlux?"*, no tiene que adivinar ni preguntar a ex-compañeros; lee el ADR correspondiente en `docs/adr/` donde se explica el problema, las opciones descartadas y la razón técnica de la decisión.

### Contratos de API: OpenAPI vs gRPC Protobuf
* **OpenAPI (Swagger)**: Especificación en YAML/JSON que documenta APIs REST para navegadores y aplicaciones web.
* **Protobuf (`.proto`)**: Contrato binario ultra-rápido usado por **gRPC** para la comunicación interna entre microservicios.

---

## 2. 📐 Arquitectura Visual & Diagrama Mermaid

```mermaid
graph TD
    subgraph Especificaciones y Decisiones (Single Source of Truth)
        ADR[docs/adr/0001-virtual-threads.md]
        PROTO[proto/order_service.proto]
    end

    subgraph Generación de Código y Clientes
        PROTOC[Compilador protoc / Protobuf Plugin]
        DOC_GEN[OpenAPI UI / Swagger Docs]
    end

    subgraph Código de Aplicación
        JAVA[Stubs Servidor Java 25]
        GO[Stubs Cliente Go Worker]
    end

    PROTO --> PROTOC
    PROTOC --> JAVA
    PROTOC --> GO
    PROTO --> DOC_GEN
```

---

## 3. 🚀 Guía Paso a Paso e Implementación Práctica (0 a 100)

### Definición de Contrato `.proto` en gRPC

```protobuf
syntax = "proto3";

package com.corp.orders.v1;

option java_multiple_files = true;
option java_package = "com.corp.orders.v1";

service OrderService {
  rpc CreateOrder (CreateOrderRequest) returns (CreateOrderResponse);
}

message CreateOrderRequest {
  string tenant_id = 1;
  double amount = 2;
  string currency = 3;
}

message CreateOrderResponse {
  string order_id = 1;
  string status = 2;
}
```

---

## 4. 🧠 Referencia Senior: Cheatsheet, Performance & Internals

### Comparativa gRPC Protobuf vs REST JSON

| Propiedad | gRPC (Protobuf Binario) | REST (JSON HTTP/1.1) |
| :--- | :--- | :--- |
| **Tamaño de Payload** | **Ultra-pequeño (Binario comprimido)** | Mediano-Grande (Texto plano) |
| **Serialización / Deserialización** | **\(O(N)\) nativa directa (< 1ms)** | \(O(N)\) con parsing String/Jackson |
| **Multiplexación** | Nativa sobre HTTP/2 | Requiere HTTP/2 o conexiones múltiples |
| **Uso Recomendado** | Comunicación interna Microservicio-a-Microservicio | APIs públicas web/móvil expuestas a Internet |

---

## 5. ⚠️ Errores Comunes & Anti-Patrones (Gotchas)

1. **Modificar números de campo en un `.proto` existente (`string tenant_id = 1;` -> `= 2;`)**:
   * *Síntoma*: Corrupción de datos o fallos de deserialización silenciosos en microservicios clientes.
   * *Solución*: Los campos en Protobuf son retrocompatibles únicamente si mantienes el índice numérico inmutable. Para eliminar un campo, usa la palabra clave `reserved`.


---

## 5. 🎯 Desafío Feynman & Auto-Evaluación sin Jerga

> [!NOTE]
> **El Reto de los 12 Años**: Explica el mecanismo esencial y la utilidad práctica de **Registros de Decisiones de Arquitectura (ADR), OpenAPI & gRPC Protobuf** a un estudiante de secundaria, **sin usar las palabras:** "Registros", "de", "Decisiones" ni tecnicismos complejos de memoria.

### Criterio de Verificación
* **Aprobado**: Si logras construir una analogía mecánica o física del mundo real donde se entienda por qué fallaría el sistema sin esta solución y cómo resuelve el problema en términos elementales.
* **No Aprobado**: Si dependes de definiciones de diccionario, siglas de frameworks o nombres de patrones sin explicar la causa física subyacente.



---
## 🧠 Ejercicio Práctico: El Método Feynman

Para garantizar una asimilación profunda de los conceptos presentados en este módulo, aplica el **Método Feynman**:

> **Instrucción:** Explica los conceptos centrales de este módulo como si tu audiencia fuera un estudiante brillante de 12 años que no ha visto nunca este tema. Si no puedes hacerlo con lenguaje sencillo, analogías claras y sin jerga técnica, significa que aún no lo entiendes lo suficientemente bien.

*Inténtalo tú mismo:* Toma el concepto más complejo de este módulo, escríbelo en un papel en blanco y redáctalo usando únicamente términos cotidianos.

---

## ⚙️ Primeros Principios & Fundamentos Conceptuales
1. **Descomposición Atómica:** Cada componente en Módulo 0 - Lección 5: Registros de Decisiones de Arquitectura (ADR), OpenAPI & gRPC Protobuf se modela de forma determinista y sin estado mutable compartido.
2. **Invariante de Dominio:** Los estados del sistema transicionan exclusivamente a través de funciones puras e interfaces selladas.
3. **Cero Suposiciones:** No se asume fiabilidad de red ni memoria infinita; cada llamada maneja explícitamente fallos y límites de cuota.

