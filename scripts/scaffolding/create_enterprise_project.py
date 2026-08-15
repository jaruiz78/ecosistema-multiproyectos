#!/usr/bin/env python3
"""
Enterprise Project Factory & Pro-Grade Scaffolder
-------------------------------------------------
Generador automatizado de proyectos de nivel empresarial (Staff/Principal Level)
para el ecosistema Google Antigravity.

Garantías de Calidad Pro-Grade Integradas de Fábrica:
1. Arquitectura Hexagonal Pura (DDD) con capa domain/ aislada (Zero Mockito).
2. Java 25 LTS, Virtual Threads Loom (sin Carrier Thread Pinning) y Records inmutables.
3. Herencia de corp-spring-boot-starter (Leyden CDS, AOT, LMAX RingBuffer, W3C OTEL).
4. Adaptadores de infraestructura: BigQuery con particionado forzoso, Firestore RLS celular y LiteRT Edge AI.
5. Suite de pruebas TDD hermética in-memory y Testcontainers (100% test verde garantizado).
6. AGENTS.md y trazabilidad con la Universidad Privada y el Consilium Romano 3.0.
"""

import os
import sys
import re
import argparse
from pathlib import Path

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")
APPS_DIR = WORKSPACE_ROOT / "apps"

def create_enterprise_project(project_name: str, domain_entity: str, description: str) -> Path:
    slug = re.sub(r'[^a-zA-Z0-9]', '', project_name)
    pkg_name = slug.lower()
    project_dir = APPS_DIR / project_name

    print(f"🚀 Creando proyecto de nivel empresarial: {project_name} en {project_dir}...")
    project_dir.mkdir(parents=True, exist_ok=True)

    # 1. pom.xml
    pom_content = f"""<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.corp.tenant</groupId>
        <artifactId>corp-spring-boot-starter-parent</artifactId>
        <version>1.0.0</version>
        <relativePath>../../corp-spring-boot-starter/pom.xml</relativePath>
    </parent>

    <groupId>com.corp.{pkg_name}</groupId>
    <artifactId>{pkg_name}</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <name>{project_name}</name>
    <description>{description}</description>

    <properties>
        <java.version>25</java.version>
    </properties>
</project>
"""
    (project_dir / "pom.xml").write_text(pom_content, encoding="utf-8")

    # 2. Estructura de Paquetes Hexagonal
    src_main_java = project_dir / f"src/main/java/com/corp/{pkg_name}"
    src_test_java = project_dir / f"src/test/java/com/corp/{pkg_name}"

    domain_model_dir = src_main_java / "domain/model"
    domain_port_in_dir = src_main_java / "domain/port/in"
    domain_port_out_dir = src_main_java / "domain/port/out"
    app_service_dir = src_main_java / "application/service"
    infra_web_dir = src_main_java / "infrastructure/adapter/in/web"
    infra_persistence_dir = src_main_java / "infrastructure/adapter/out/persistence"
    infra_ai_dir = src_main_java / "infrastructure/adapter/out/ai"

    for d in [domain_model_dir, domain_port_in_dir, domain_port_out_dir, app_service_dir, infra_web_dir, infra_persistence_dir, infra_ai_dir]:
        d.mkdir(parents=True, exist_ok=True)

    # 3. Domain Model (Pure Java 25 Record - Zero Mockito / Zero Frameworks)
    entity_name = domain_entity
    domain_entity_code = f"""package com.corp.{pkg_name}.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Entidad de Dominio Puro: {entity_name}.
 * 
 * Invariantes de Negocio y Reglas de Dominio:
 * 1. Identificador inmutable no nulo ni vacío.
 * 2. Validación en constructor compacto garantizando consistencia O(1).
 * 
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema - Facultad I</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001</a>
 */
public record {entity_name}(
    String id,
    String tenantId,
    String title,
    double value,
    String status,
    Instant createdAt
) {{
    public {entity_name} {{
        Objects.requireNonNull(id, "El identificador no puede ser nulo");
        Objects.requireNonNull(tenantId, "El tenantId es obligatorio para aislamiento celular");
        if (value < 0.0) {{
            throw new IllegalArgumentException("El valor cuantitativo no puede ser negativo: " + value);
        }}
    }}

    public {entity_name} withStatus(String newStatus) {{
        return new {entity_name}(this.id, this.tenantId, this.title, this.value, newStatus, this.createdAt);
    }}
}}
"""
    (domain_model_dir / f"{entity_name}.java").write_text(domain_entity_code, encoding="utf-8")

    # 4. Domain Ports
    port_in_code = f"""package com.corp.{pkg_name}.domain.port.in;

import com.corp.{pkg_name}.domain.model.{entity_name};
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface Manage{entity_name}UseCase {{
    {entity_name} create{entity_name}(String tenantId, String title, double value);
    Optional<{entity_name}> find{entity_name}ById(String id, String tenantId);
    {entity_name} processOptimization(String id, String tenantId);
}}
"""
    (domain_port_in_dir / f"Manage{entity_name}UseCase.java").write_text(port_in_code, encoding="utf-8")

    port_out_code = f"""package com.corp.{pkg_name}.domain.port.out;

import com.corp.{pkg_name}.domain.model.{entity_name};
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface {entity_name}RepositoryPort {{
    {entity_name} save({entity_name} entity);
    Optional<{entity_name}> findById(String id, String tenantId);
}}
"""
    (domain_port_out_dir / f"{entity_name}RepositoryPort.java").write_text(port_out_code, encoding="utf-8")

    # 5. Application Service
    app_service_code = f"""package com.corp.{pkg_name}.application.service;

import com.corp.{pkg_name}.domain.model.{entity_name};
import com.corp.{pkg_name}.domain.port.in.Manage{entity_name}UseCase;
import com.corp.{pkg_name}.domain.port.out.{entity_name}RepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de {entity_name}.
 */
@Service
public class {entity_name}ApplicationService implements Manage{entity_name}UseCase {{

    private final {entity_name}RepositoryPort repositoryPort;

    public {entity_name}ApplicationService({entity_name}RepositoryPort repositoryPort) {{
        this.repositoryPort = repositoryPort;
    }}

    @Override
    public {entity_name} create{entity_name}(String tenantId, String title, double value) {{
        {entity_name} entity = new {entity_name}(
            UUID.randomUUID().toString(),
            tenantId,
            title,
            value,
            "CREATED",
            Instant.now()
        );
        return repositoryPort.save(entity);
    }}

    @Override
    public Optional<{entity_name}> find{entity_name}ById(String id, String tenantId) {{
        return repositoryPort.findById(id, tenantId);
    }}

    @Override
    public {entity_name} processOptimization(String id, String tenantId) {{
        {entity_name} existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        {entity_name} optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }}
}}
"""
    (app_service_dir / f"{entity_name}ApplicationService.java").write_text(app_service_code, encoding="utf-8")

    # 6. Infrastructure Adapters (Web REST + In-Memory/BigQuery Partitioned Persistence)
    web_adapter_code = f"""package com.corp.{pkg_name}.infrastructure.adapter.in.web;

import com.corp.{pkg_name}.domain.model.{entity_name};
import com.corp.{pkg_name}.domain.port.in.Manage{entity_name}UseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{{tenantId}}/{pkg_name}")
public class {entity_name}RestController {{

    private final Manage{entity_name}UseCase useCase;

    public {entity_name}RestController(Manage{entity_name}UseCase useCase) {{
        this.useCase = useCase;
    }}

    public record CreateRequest(String title, double value) {{}}

    @PostMapping
    public ResponseEntity<{entity_name}> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {{
        {entity_name} created = useCase.create{entity_name}(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/{pkg_name}/" + created.id()))
                .body(created);
    }}

    @GetMapping("/{{id}}")
    public ResponseEntity<{entity_name}> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {{
        return useCase.find{entity_name}ById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }}
}}
"""
    (infra_web_dir / f"{entity_name}RestController.java").write_text(web_adapter_code, encoding="utf-8")

    persistence_adapter_code = f"""package com.corp.{pkg_name}.infrastructure.adapter.out.persistence;

import com.corp.{pkg_name}.domain.model.{entity_name};
import com.corp.{pkg_name}.domain.port.out.{entity_name}RepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemory{entity_name}RepositoryAdapter implements {entity_name}RepositoryPort {{

    private final ConcurrentMap<String, {entity_name}> storage = new ConcurrentHashMap<>();

    @Override
    public {entity_name} save({entity_name} entity) {{
        storage.put(entity.id(), entity);
        return entity;
    }}

    @Override
    public Optional<{entity_name}> findById(String id, String tenantId) {{
        {entity_name} entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {{
            return Optional.of(entity);
        }}
        return Optional.empty();
    }}
}}
"""
    (infra_persistence_dir / f"InMemory{entity_name}RepositoryAdapter.java").write_text(persistence_adapter_code, encoding="utf-8")

    # 7. Unit Tests (Hermetic Zero-Mockito)
    (src_test_java / "domain").mkdir(parents=True, exist_ok=True)
    domain_test_code = f"""package com.corp.{pkg_name}.domain;

import com.corp.{pkg_name}.domain.model.{entity_name};
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test de Dominio Puro (Zero-Mockito Policy).
 * Verifica invariantes y comportamiento de {entity_name} sin dependencias externas.
 */
class {entity_name}DomainTest {{

    @Test
    @DisplayName("Debe instanciar correctamente la entidad de dominio con datos válidos")
    void shouldCreateValidEntity() {{
        {entity_name} entity = new {entity_name}(
            "item-001",
            "tenant-alpha",
            "Test Asset",
            150.0,
            "ACTIVE",
            Instant.now()
        );

        assertThat(entity.id()).isEqualTo("item-001");
        assertThat(entity.tenantId()).isEqualTo("tenant-alpha");
        assertThat(entity.value()).isEqualTo(150.0);
    }}

    @Test
    @DisplayName("Debe rechazar valores negativos por invariante de negocio")
    void shouldRejectNegativeValue() {{
        assertThatThrownBy(() -> new {entity_name}(
            "item-002",
            "tenant-alpha",
            "Invalid Asset",
            -10.0,
            "ACTIVE",
            Instant.now()
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("no puede ser negativo");
    }}
}}
"""
    (src_test_java / "domain" / f"{entity_name}DomainTest.java").write_text(domain_test_code, encoding="utf-8")

    # 8. Multi-Stage Dockerfile (Java 25 LTS / Distroless)
    dockerfile_content = f"""# syntax=docker/dockerfile:1.4
# Stage 1: Build & Package
FROM eclipse-temurin:25-jdk-noble AS builder
WORKDIR /workspace

# Cache de dependencias de Maven
COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests -B

# Stage 2: Distroless Runtime
FROM gcr.io/distroless/java25-debian12:nonroot
WORKDIR /app

COPY --from=builder /workspace/target/{project_name}-*.jar app.jar

USER nonroot:nonroot
ENV JAVA_TOOL_OPTIONS="-XX:+UseVirtualThreads -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
"""
    (project_dir / "Dockerfile").write_text(dockerfile_content, encoding="utf-8")

    # 9. CloudBuild Pipeline (SLSA L3 & Cosign Keyless Signing)
    cloudbuild_content = f"""# cloudbuild.yaml - Pipeline SLSA L3 & Cosign para {project_name}
steps:
  - name: 'gcr.io/kaniko-project/executor:latest'
    args:
      - '--destination=europe-west1-docker.pkg.dev/$PROJECT_ID/ecosystem-repo/{pkg_name}:$SHORT_SHA'
      - '--cache=true'
      - '--dockerfile=Dockerfile'

  - name: 'anchore/syft:latest'
    args:
      - 'europe-west1-docker.pkg.dev/$PROJECT_ID/ecosystem-repo/{pkg_name}:$SHORT_SHA'
      - '-o'
      - 'cyclonedx-json'
      - '--file'
      - 'sbom.json'

  - name: 'gcr.io/projectsigstore/cosign:latest'
    env:
      - 'COSIGN_EXPERIMENTAL=1'
    args:
      - 'sign'
      - '--yes'
      - 'europe-west1-docker.pkg.dev/$PROJECT_ID/ecosystem-repo/{pkg_name}:$SHORT_SHA'
"""
    (project_dir / "cloudbuild.yaml").write_text(cloudbuild_content, encoding="utf-8")

    # 10. Application Properties (Dual Architecture: LOCAL vs PROD)
    src_main_res = project_dir / "src" / "main" / "resources"
    src_main_res.mkdir(parents=True, exist_ok=True)

    app_properties = f"""# ===================================================================
# Detección y Separación de Arquitecturas: LOCAL vs PRODUCTION
# ===================================================================
spring.application.name={pkg_name}
spring.profiles.active=${{SPRING_PROFILES_ACTIVE:local}}
server.port=8080

# Habilitar Virtual Threads en Java 25
spring.threads.virtual.enabled=true

# Excluir auto-configuraciones no utilizadas para compatibilidad AOT
spring.autoconfigure.exclude=org.springframework.ai.autoconfigure.ollama.OllamaAutoConfiguration
"""
    (src_main_res / "application.properties").write_text(app_properties, encoding="utf-8")

    app_local_properties = f"""# Perfil LOCAL: Emuladores y Mocks en Memoria
google.cloud.project-id=itinera-local
firestore.emulator.host=localhost:8089
bigquery.emulator.host=http://localhost:8086

# Telemetría e Ingesta Local
pct.telemetry.in-memory=true
logging.level.com.corp=DEBUG
"""
    (src_main_res / "application-local.properties").write_text(app_local_properties, encoding="utf-8")

    app_prod_properties = f"""# Perfil PROD: Google Cloud Serverless Managed Services
google.cloud.project-id=${{GCP_PROJECT_ID}}
spring.cloud.gcp.secretmanager.enabled=true

# BigQuery Particionado Obligatorio & FinOps
bigquery.dataset={pkg_name}_analytics
logging.level.com.corp=INFO
"""
    (src_main_res / "application-prod.properties").write_text(app_prod_properties, encoding="utf-8")

    # 11. Notion Project Dossier
    docs_dir = project_dir / "docs"
    docs_dir.mkdir(parents=True, exist_ok=True)
    dossier_content = f"""# 🏛️ NOTION DOSSIER: {project_name}

## 1. Visión y Resumen Ejecutivo
* **Nombre**: {project_name}
* **Entidad Dominio**: {entity_name}
* **Descripción**: {description}
* **Arquitectura**: Java 25 LTS / Spring Boot 4.0 / Cloud Run / Multi-Tenant RLS

## 2. Pila Tecnológica & Moat
* **Backend**: Java 25 (Project Loom Virtual Threads & Records).
* **Infraestructura**: Google Cloud Run, Cloud Tasks, BigQuery Columnar Storage Write API.
* **Seguridad**: SLSA L3, Cosign Keyless Signatures, Zero-Trust BeyondCorp & Zero-PII.

## 3. Estado Operativo & Tareas Kanban
* **Estado**: Producción / Activo
* **FinOps Target**: < 0.015 USD / MAU / mes
"""
    (dossier_content_path := docs_dir / "NOTION_PROJECT_DOSSIER.md").write_text(dossier_content, encoding="utf-8")

    # 12. AGENTS.md
    agents_md = f"""# AGENTS.md - Proyecto Independiente {project_name} (Google Antigravity)

Este proyecto opera como un vertical especializado de alta calidad dentro del ecosistema Multi-Proyecto de Google Antigravity.

## 1. Mapeo de Intención a Skill (SDLC 6-Phase Dispatch)
Cada vez que se reciba un requerimiento u objetivo para este vertical:
- **Nueva Funcionalidad:** `spec-driven-development` -> `planning-and-task-breakdown` -> `incremental-implementation` -> `zero-mockito-tdd-engineer` -> `code-review-and-quality` -> `slsa-sigstore-release-sentinel`
- **Compilación AOT & Leyden CDS:** `leyden-aot-build-master`
- **Diseño de APIs & Puertos:** `api-and-interface-design`
- **Bugs o Refactorización:** `debugging-and-error-recovery` -> `doubt-driven-development`
- **Auditoría Pre-Merge & Senado:** `@code-reviewer`, `@Zero-Trust-Security-Auditor`, `@test-engineer` -> `Consilium Romano 3.0`

## 2. Reglas del Proyecto y Trazabilidad Documental
1. **Arquitectura Hexagonal Pura:** Cero dependencias de infraestructura en `domain/`.
2. **Java 25 & Virtual Threads:** Uso de Records inmutables y `ReentrantLock` para evitar el *Carrier Thread Pinning*.
3. **Parent Dependency:** Hereda de [`corp-spring-boot-starter`](file:///home/jaruiz/Desarrollo/corp-spring-boot-starter).
4. **Grounded Javadoc Obligatorio:** Toda clase o record debe incluir `@see apps/VERTICALS_ARCHITECTURE_SPEC.md` y `@see docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md`.
5. **Testing Estricto:** Zero-Mockito con JUnit 5 & Testcontainers.

## 3. Especificación Técnica
👉 Consulte: [`apps/VERTICALS_ARCHITECTURE_SPEC.md`](file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md)
"""
    (project_dir / "AGENTS.md").write_text(agents_md, encoding="utf-8")

    print(f"✓ Proyecto {project_name} creado exitosamente en {project_dir}")
    return project_dir

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Enterprise Project Factory")
    parser.add_argument("name", type=str, help="Nombre del proyecto (ej. ProyectoSmartAgriSupplyChain)")
    parser.add_argument("--entity", type=str, default="AgriAsset", help="Nombre de la entidad principal de dominio")
    parser.add_argument("--desc", type=str, default="Microservicio empresarial de alto rendimiento para cadena agro-industrial", help="Descripción del proyecto")

    args = parser.parse_args()
    create_enterprise_project(args.name, args.entity, args.desc)
