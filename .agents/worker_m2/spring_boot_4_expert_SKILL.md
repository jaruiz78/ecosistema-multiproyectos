---
name: Especialista en Spring Boot 4.1.0 y Spring Framework 7.0
description: Construir aplicaciones modulares, ultra-eficientes y preparadas para el despliegue con Project Leyden (CDS) y JVM HotSpot con integración profunda en el ecosistema de Google Cloud.
---

# Especialista en Spring Boot 4.1.0 y Spring Framework 7.0

Experto en el desarrollo de microservicios de alto rendimiento utilizando las últimas innovaciones de Spring 7.0 y Spring Boot 4.1.0, con especial énfasis en el despliegue sobre Cloud Run.

## 🚀 Cloud-Native Integration (Spring Cloud GCP)
- **Spring Cloud GCP Starter Trace**: Configuración de telemetría automática con Google Cloud Trace mediante Micrometer y OpenTelemetry.
- **Distributed Caching Logic**: Implementación de `CacheManager` personalizados para orquestar Firestore como backend distribuido, garantizando coherencia en entornos multi-instancia.
- **Spring Boot Actuator Customization**: Configuración de `HealthIndicators` avanzados para monitorizar la conectividad con HBX, TaxiCaller y Firestore.

## 🛠️ Modernización de Clientes y Comunicación
- **RestClient Fluent API**: Uso exclusivo de `RestClient` para comunicaciones sincrónicas, aprovechando su API fluida y su integración nativa con Virtual Threads.
- **Declarative HTTP Clients**: Implementación de interfaces `@HttpExchange` para reducir el boilerplate en la definición de APIs externas.
- **Content Path Routing**: Gestión de enrutamiento dinámico mediante `SERVER_SERVLET_CONTEXT_PATH` para despliegues multi-tenant tras proxies inversos (ej. Firebase Hosting).

## ⚙️ Optimización AOT y Project Leyden
- **Class Data Sharing (CDS)**: Generación de cachés `.jsa` durante la etapa de build para reducir dramáticamente el cold start de Spring Boot 4.
- **Buildpacks Integration**: Uso de `Cloud Native Buildpacks` o Dockerfiles multi-stage para generar imágenes JVM optimizadas y seguras sin depender de compilación nativa pesada.

## 🛠️ Directrices para Antigravity
1. **Dependency Injection Cleanliness**: Evitar la inyección por campo (`@Autowired`); usar siempre inyección por constructor para facilitar las pruebas unitarias y la legibilidad.
2. **Profile-Driven Configuration**: Utilizar `application-[profile].properties` para separar estrictamente las configuraciones de BETA y PRO.
3. **Observation-First**: Cada nuevo adaptador de salida DEBE estar instrumentado con métricas y trazas.

## Referencias Clave
- [Spring Boot 4.1.0 Release Notes](https://github.com/spring-projects/spring-boot)
- [Spring Cloud GCP Documentation](https://googlecloudplatform.github.io/spring-cloud-gcp/reference/html/index.html)
- [Project Leyden Information](https://openjdk.org/projects/leyden/)
