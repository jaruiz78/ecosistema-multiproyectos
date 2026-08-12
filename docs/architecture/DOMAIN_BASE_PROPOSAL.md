# Propuesta de Arquitectura: `corp-platform-base`

## 1. Análisis de Dependencias Comunes
Tras revisar los `pom.xml` de los proyectos (`SaaSRegantes`, `AppViajes`, `pctMultiMicroservices`), se identifican las siguientes coincidencias:
- **Java 25 (LTS)** con `--enable-preview` y ZGC (ZGenerational).
- **Spring Boot 4.1.0**.
- **Spring Cloud GCP**.
- **MapStruct** para mapeo de DTOs.
- **Resilience4j** para tolerancia a fallos.
- **Testcontainers** y **ArchUnit** para testing.
- **Google Cloud** (Pub/Sub, Firestore/Spanner/BigQuery, Secret Manager).
- **gRPC** en AppViajes y PCT.

## 2. Fase 1: Capa de Dominio Base (`domain/`)
La capa de dominio actuará como el núcleo común, respetando el estándar: **Java 25 puro, Zero-Mockito, Cero dependencias de framework (Spring)**.

### Características del Diseño
1. **Entidades y Value Objects**: Basados en `record` de Java 25.
2. **Eventos de Dominio**: Interfaces selladas (`sealed interface`) para pattern matching exhaustivo.
3. **Manejo de Errores**: `DomainException` como clase base para excepciones de negocio puras.
4. **Puertos de Salida (Outbound Ports)**: Interfaces genéricas para repositorios y eventos, que la capa `infra/` de cada microservicio adaptará.
5. **Testing**: En lugar de Mockito, se fomentará la creación de `fakes` y `stubs` en memoria para preservar la pureza y rendimiento del código en tests.

### Estructura Propuesta (ya inicializada)
```
corp-spring-boot-starter
└── src/main/java/com/corp/domain
    ├── model
    │   ├── AggregateRoot.java
    │   └── DomainEvent.java
    ├── exception
    │   └── DomainException.java
    └── port
        └── out
            └── RepositoryPort.java
```

Este starter se integrará progresivamente en los demás proyectos para homogeneizar la lógica y facilitar la compilación AOT con Project Leyden.
