# 🚀 ProyectoEnergia: Comunidades Energéticas y OPF Linealizado

> **Google Antigravity Sovereign Framework**  
> **Nivel de Rigor:** CMU / MIT / Stanford Architecture Benchmark  
> **Arquitectura:** Hexagonal Pura (DDD) | Java 25 (LTS) | Spring Boot 4.0 | Virtual Threads (Loom)

---

## 1. Visión General
Vertical para la gestión de microredes eléctricas locales, intercambio P2P y balance óptimo de potencia (OPF).

### Bounded Context & Dominio
* **Dominio Principal:** Subestaciones, buses de red, balance activo/reactivo, despacho local.
* **Integración Base:** Hereda de [`corp-spring-boot-starter`](file:///home/jaruiz/Desarrollo/corp-spring-boot-starter).
* **Enlace de Estado / Ledger:** `tensor_gnn_core.py`.

---

## 2. Principios de Arquitectura y Concurrencia
1. **Dominio Puro (Zero-Mockito TDD):** La capa `domain/` no contiene anotaciones de infraestructura ni dependencias externas.
2. **Concurrencia con Virtual Threads:** Uso de `ReentrantLock` y estructuras lock-free (`VarHandle`) para prevenir el *Carrier Thread Pinning*.
3. **AOT & Leyden Ready:** Estructuras inmutables con **Java 25 Records** preparadas para compilación nativa y Class Data Sharing (`.jsa`).

---

## 3. Especificación Técnica Completa
Para ver las entidades de dominio, agregados raíz y contratos de puertos hexagonales, consulte la especificación consolidada:
👉 [VERTICALS_ARCHITECTURE_SPEC.md](file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md)
