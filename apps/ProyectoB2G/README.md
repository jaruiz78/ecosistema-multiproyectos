# 🚀 ProyectoB2G: Plataforma Soberana de Contratación y Licitación Pública

> **Google Antigravity Sovereign Framework**  
> **Nivel de Rigor:** CMU / MIT / Stanford Architecture Benchmark  
> **Arquitectura:** Hexagonal Pura (DDD) | Java 25 (LTS) | Spring Boot 4.0 | Virtual Threads (Loom)

---

## 1. Visión General
Vertical GovTech para la gestión inmutable, auditoría y evaluación algorítmica de pliegos y ofertas públicas.

### Bounded Context & Dominio
* **Dominio Principal:** Licitaciones públicas, scoring de solvencia técnica, registro de pliegos.
* **Integración Base:** Hereda de [`corp-spring-boot-starter`](file:///home/jaruiz/Desarrollo/corp-spring-boot-starter).
* **Enlace de Estado / Ledger:** `core-govtech-ledger`.

---

## 2. Principios de Arquitectura y Concurrencia
1. **Dominio Puro (Zero-Mockito TDD):** La capa `domain/` no contiene anotaciones de infraestructura ni dependencias externas.
2. **Concurrencia con Virtual Threads:** Uso de `ReentrantLock` y estructuras lock-free (`VarHandle`) para prevenir el *Carrier Thread Pinning*.
3. **AOT & Leyden Ready:** Estructuras inmutables con **Java 25 Records** preparadas para compilación nativa y Class Data Sharing (`.jsa`).

---

## 3. Especificación Técnica Completa
Para ver las entidades de dominio, agregados raíz y contratos de puertos hexagonales, consulte la especificación consolidada:
👉 [VERTICALS_ARCHITECTURE_SPEC.md](file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md)
