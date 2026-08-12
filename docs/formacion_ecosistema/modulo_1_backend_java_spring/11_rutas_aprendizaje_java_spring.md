# 11. Rutas de Aprendizaje Práctico: Backend Java 25 & Spring Boot 4.0

El presente documento complementa la carga teórica del Módulo 1 (Arquitectura JVM, C1/C2, Project Loom, Coherencia MESI) estructurando los **recursos gratuitos, abiertos y de mayor rigor técnico** para dominar la pila moderna de desarrollo. La meta es alcanzar un dominio absoluto del stack (desde AOT hasta Virtual Threads) sin dependencia de plataformas de pago.

## 1. Fuentes de Verdad Absolutas (Ecosistema Core)

### A. Spring Academy & Guides (Pivotal / VMware)
El estándar de oro mantenido directamente por los creadores del framework.
- **Enfoque:** Guías prácticas para `spring-boot-starter-web`, Spring Security, compilación AOT y persistencia.
- **Acceso:** [Spring Academy](https://academy.spring.io) y [Spring Guides](https://spring.io/guides).
- **Rigor:** Ideal para arrancar microservicios Cloud-Native, integrar Testcontainers y asimilar la inyección de dependencias moderna (Constructor-based injection).

### B. Dev.java & JEPs Oficiales (Oracle)
La documentación canónica del lenguaje.
- **Enfoque:** Entender las *Java Enhancement Proposals* (JEPs) directamente. Project Loom (JEP 444), Pattern Matching (JEP 440/441) y la evolución hacia Project Valhalla.
- **Acceso:** [Dev.java](https://dev.java) y [OpenJDK JEPs](https://openjdk.org/jeps/0).
- **Rigor:** Obligatorio para comprender el diseño subyacente del *Generational ZGC*, el *Garbage Collection* adaptativo y las estructuras de datos concurrentes.

## 2. Bases de Conocimiento Comunitarias y Arquitectónicas

### C. Baeldung (Eugen Paraschiv)
La enciclopedia comunitaria por excelencia para el desarrollador enterprise.
- **Enfoque:** Recetas probadas, configuraciones complejas de Spring Security, JPA/Hibernate internals, y testeo avanzado (JUnit 5 + Testcontainers). Recordando siempre la política corporativa *Zero-Mockito* para la capa de dominio puro.
- **Acceso:** [Baeldung](https://www.baeldung.com).

### D. Vlad Mihalcea & Marco Behler
Especialistas en persistencia de alto rendimiento y ecosistema Spring.
- **Enfoque:** Optimización extrema de JPA/Hibernate (High-Performance Java Persistence), mitigación del problema *N+1 queries*, y caching L2.
- **Acceso:** [Vlad Mihalcea (Blog)](https://vladmihalcea.com/) y [Marco Codes (YouTube)](https://www.youtube.com/c/MarcoCodes).
- **Rigor:** Fundamental para evitar que cuellos de botella en la capa de datos bloqueen los Virtual Threads en sistemas de alta concurrencia.

## 3. Arquitecturas de Referencia OSS

### E. Spring PetClinic (Microservices Edition)
La arquitectura de referencia mantenida por la comunidad.
- **Enfoque:** Trazabilidad distribuida (Micrometer/OpenTelemetry), Service Discovery, API Gateways (Spring Cloud Gateway) y Resiliencia (Circuit Breakers).
- **Acceso:** [Spring PetClinic Microservices](https://github.com/spring-petclinic/spring-petclinic-microservices).
- **Rigor:** Actúa como plantilla para implementar arquitecturas hexagonales robustas y validar configuraciones de compilación con GraalVM Native Image y Project Leyden (CDS).

---

> **Objetivo de Competencia:** Mediante el dominio de estos 5 recursos, el ingeniero estará capacitado para diseñar microservicios deterministas en memoria, libres de *Carrier Thread Pinning* y con tiempos de *cold-start* ultrabajos ($<100ms$) óptimos para despliegues Serverless en Google Cloud Run.
