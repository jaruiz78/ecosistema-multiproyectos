# Módulo 0 - Lección 1: Arquitectura Hexagonal y DDD Puro

---

## 1. 🐣 Rincón Junior: Conceptos desde Cero & Analogías

### ¿Qué es la Arquitectura Hexagonal y por qué la usamos?
Imagina que construyes una consola de videojuegos (el **Dominio / Lógica de Negocio**). Quieres poder conectar mandos por cable, mandos Bluetooth o teclados (**Adaptadores de Entrada**), y quieres poder enviar el vídeo a un televisor HDMI, un proyector o una pantalla portátil (**Adaptadores de Salida**).

Si soldaras el cable del mando directamente a la placa base del procesador, reemplazar el mando requeriría romper la consola. En software ocurre igual: la **Arquitectura Hexagonal (o Puertos y Adaptadores)** garantiza que la regla de negocio (la consola) no sepa ni le importe si los datos vienen de una API REST, un test o un archivo de texto, ni si se guardan en PostgreSQL, BigQuery o la memoria RAM.

### Conceptos Clave
* **Dominio Puro**: El corazón del código donde residen las reglas del negocio. No conoce frameworks (Spring Boot, Hibernate, etc.).
* **Puerto (Port)**: Una contrato o interfaz Java/Go que define *qué* operaciones se pueden hacer.
* **Adaptador (Adapter)**: La implementación concreta en infraestructura (código con annotations de Spring Boot, llamadas SQL, bibliotecas de terceros).

---

## 2. 📐 Arquitectura Visual & Diagrama Mermaid

```mermaid
graph TD
    subgraph Adaptadores de Entrada / Primarios (Infraestructura)
        REST[Controller REST / Spring Web]
        gRPC[gRPC Handler]
        CLI[Comando CLI]
    end

    subgraph Puertos de Entrada / Inbound Ports
        IPORT[UseCase Interface / EnviarPedidoPort]
    end

    subgraph Núcleo de Dominio Puro / Core Domain (Zero Framework)
        DOM[Aggregates & Value Objects / Records Java 25]
        DS[Domain Service / CalculoTotalesService]
    end

    subgraph Puertos de Salida / Outbound Ports
        OPORT_DB[OrderRepositoryPort]
        OPORT_PAY[PaymentGatewayPort]
    end

    subgraph Adaptadores de Salida / Secundarios (Infraestructura)
        JPA[Adapter JPA / Hibernate Postgres]
        STRIPE[Adapter Client Stripe SDK]
    end

    REST -->|Invoca| IPORT
    gRPC -->|Invoca| IPORT
    CLI -->|Invoca| IPORT
    IPORT -->|Ejecuta| DS
    DS -->|Mutación/Consulta| DOM
    DS -->|Utiliza| OPORT_DB
    DS -->|Utiliza| OPORT_PAY
    JPA ..->|Implementa| OPORT_DB
    STRIPE ..->|Implementa| OPORT_PAY
```

---

## 3. 🚀 Guía Paso a Paso e Implementación Práctica (0 a 100)

### Paso 1: Definir el Modelo de Dominio Puro (Java 25 Record)

```java
package com.corp.domain.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

// Value Object inmutable (Zero Spring, Zero Annotations)
public record Money(BigDecimal amount, String currency) {
    public Money {
        Objects.requireNonNull(amount, "El importe no puede ser nulo");
        Objects.requireNonNull(currency, "La moneda no puede ser nula");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El importe no puede ser negativo");
        }
    }
}

// Agregado de Dominio
public record Order(UUID id, String tenantId, Money total, boolean confirmed) {
    public Order confirm() {
        if (this.confirmed) {
            throw new IllegalStateException("El pedido ya está confirmado");
        }
        return new Order(id, tenantId, total, true);
    }
}
```

### Paso 2: Definir los Puertos (Interfaces de Contrato)

```java
package com.corp.domain.port.out;

import com.corp.domain.model.Order;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(UUID id, String tenantId);
}
```

### Paso 3: Implementar el Adaptador Secundario (Spring JPA)

```java
package com.corp.infrastructure.adapter.out.persistence;

import com.corp.domain.model.Order;
import com.corp.domain.model.Money;
import com.corp.domain.port.out.OrderRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class OrderJpaAdapter implements OrderRepositoryPort {

    private final SpringDataOrderRepository repository;

    public OrderJpaAdapter(SpringDataOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = new OrderEntity(order.id(), order.tenantId(), order.total().amount(), order.total().currency(), order.confirmed());
        OrderEntity saved = repository.save(entity);
        return new Order(saved.getId(), saved.getTenantId(), new Money(saved.getAmount(), saved.getCurrency()), saved.isConfirmed());
    }

    @Override
    public Optional<Order> findById(UUID id, String tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(e -> new Order(e.getId(), e.getTenantId(), new Money(e.getAmount(), e.getCurrency()), e.isConfirmed()));
    }
}
```

---

## 4. 🧠 Referencia Senior: Cheatsheet, Performance & Internals

### Tabla de Complejidad y Rendimiento

| Capa / Operación | Complejidad Big-O | Latencia Típica | Carga de CPU / Memoria |
| :--- | :--- | :--- | :--- |
| **Instanciación de Dominio (Records)** | $O(1)$ | < 1 ns | Prácticamente 0 (Stack Allocation) |
| **Mapeo Adapter Entity-to-Domain** | $O(1)$ | < 100 ns | Mínima (Asignación corta de heap) |
| **Consulta JPA en Adaptador DB** | $O(\log N)$ con índice | 1 - 5 ms | Depende del pool I/O de la DB |

### Reglas de Diseño AOT (GraalVM / Leyden)
* **Zero Reflection en Dominio**: Nunca utilices `Class.forName()` ni `getDeclaredFields()` en el paquete `domain/`.
* **Immutabilidad Garantizada**: Los Records de Java 25 eliminan la necesidad de mutable getters/setters y facilitan la optimización del JIT compiler (Escape Analysis).

---

## 5. ⚠️ Errores Comunes & Anti-Patrones (Gotchas)

1. **Anotaciones de JPA (`@Entity`, `@Table`) dentro del paquete `domain/`**:
   * *Síntoma*: Tu modelo de negocio está acoplado al ORM. Si cambias de Hibernate a Firestore, tendrás que modificar la lógica de negocio.
   * *Solución*: Mantén las clases `@Entity` en `infrastructure/persistence/` y usa mappers explícitos.
2. **Depender de Spring `@Autowired` dentro de Domain Services**:
   * *Síntoma*: Imposible probar la lógica de negocio sin levantar el ApplicationContext de Spring.
   * *Solución*: Inyecta dependencias mediante constructores puros en Java/Go.
