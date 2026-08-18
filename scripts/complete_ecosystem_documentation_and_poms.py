#!/usr/bin/env python3
"""
complete_ecosystem_documentation_and_poms.py
=============================================================================
Estandarización Total del Ecosistema MultiProyectos:
1. Genera OpenAPI 3.1, AsyncAPI 3.0, Cloud Run FinOps y AGENTS.md para todas las apps.
2. Dota de pom.xml a todos los cores Java/Maven.
3. Sincroniza el pom.xml raíz para incluir el 100% de apps y cores.
=============================================================================
"""

import os
import re
from pathlib import Path

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")
ROOT_POM = WORKSPACE_ROOT / "pom.xml"

OPENAPI_TEMPLATE = """openapi: 3.1.0
info:
  title: {project_name} API
  description: API Corporativa de Alta Precisión para {project_name} (Ecosistema MultiProyectos)
  version: 1.0.0
servers:
  - url: http://localhost:8080
    description: Entorno Local de Desarrollo (0.00 EUR)
  - url: https://{service_name}-prod-ew1.a.run.app
    description: Entorno de Producción Cloud Run (GCP)
paths:
  /api/v1/health:
    get:
      summary: Health check y telemetría AOT
      responses:
        '200':
          description: OK
"""

ASYNCAPI_TEMPLATE = """asyncapi: 3.0.0
info:
  title: {project_name} Event Stream
  version: 1.0.0
  description: Transmisión de eventos reactivos y telemetría para {project_name}
channels:
  telemetryEvents:
    address: 'corp.{project_lower}.events'
    messages:
      telemetryMessage:
        payload:
          type: object
          properties:
            tenantId:
              type: string
            timestamp:
              type: string
              format: date-time
"""

CLOUDRUN_TEMPLATE = """apiVersion: serving.knative.dev/v1
kind: Service
metadata:
  name: {service_name}
  annotations:
    run.googleapis.com/launch-stage: GA
spec:
  template:
    metadata:
      annotations:
        autoscaling.knative.dev/minScale: "0"
        autoscaling.knative.dev/maxScale: "10"
        run.googleapis.com/startup-cpu-boost: "true"
        run.googleapis.com/execution-environment: "gen2"
    spec:
      containerConcurrency: 250
      containers:
        - image: europe-west1-docker.pkg.dev/corp-ecosystem/{service_name}:latest
          resources:
            limits:
              cpu: 1000m
              memory: 512Mi
"""

AGENTS_TEMPLATE = """# AGENTS.md - {project_name} (Google Antigravity)

Vertical especializado de alta precisión dentro del Ecosistema MultiProyectos.

## 1. Mapeo de Intención a Skill (SDLC 6-Phase Dispatch)
- **Nueva Funcionalidad:** `spec-driven-development` -> `planning-and-task-breakdown` -> `incremental-implementation` -> `zero-mockito-tdd-engineer` -> `code-review-and-quality` -> `slsa-sigstore-release-sentinel`
- **Compilación AOT & Leyden CDS:** `leyden-aot-build-master`
- **Diseño de APIs & Puertos:** `api-and-interface-design`
- **Bugs o Refactorización:** `debugging-and-error-recovery` -> `doubt-driven-development`
- **Auditoría Pre-Merge & Senado:** `@code-reviewer`, `@Zero-Trust-Security-Auditor`, `@test-engineer` -> `Consilium Romano 3.0`

## 2. Reglas del Proyecto
1. **Arquitectura Hexagonal Pura:** Cero dependencias de infraestructura en `domain/`.
2. **Java 25 & Virtual Threads:** Uso de Records inmutables y `ReentrantLock` para evitar *Carrier Thread Pinning*.
3. **Grounded Javadoc Obligatorio:** `@see docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md`.
4. **Testing Estricto:** Zero-Mockito con JUnit 5 y Property-Based Testing.
"""

CORE_POM_TEMPLATE = """<?xml version="1.0" encoding="UTF-8"?>
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

    <artifactId>{core_name}</artifactId>
    <name>{core_name}</name>
    <description>Módulo Core Algorítmico y Matemático: {core_name}</description>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
"""

def standardize():
    apps_dir = WORKSPACE_ROOT / "apps"
    cores_dir = WORKSPACE_ROOT / "core"
    
    # 1. Documentar todas las apps
    for app in apps_dir.iterdir():
        if not app.is_dir() or app.name.startswith("."):
            continue
        
        name = app.name
        lower_name = name.lower()
        service_name = re.sub(r'(?<!^)(?=[A-Z])', '-', name).lower()
        
        docs_dir = app / "docs"
        docs_dir.mkdir(exist_ok=True, parents=True)
        infra_dir = app / "infra" / "gcp"
        infra_dir.mkdir(exist_ok=True, parents=True)
        
        openapi_file = docs_dir / "openapi.yaml"
        if not openapi_file.exists():
            openapi_file.write_text(OPENAPI_TEMPLATE.format(project_name=name, service_name=service_name))
            
        asyncapi_file = docs_dir / "asyncapi.yaml"
        if not asyncapi_file.exists():
            asyncapi_file.write_text(ASYNCAPI_TEMPLATE.format(project_name=name, project_lower=lower_name))
            
        cloudrun_file = infra_dir / "cloudrun_service.yaml"
        if not cloudrun_file.exists():
            cloudrun_file.write_text(CLOUDRUN_TEMPLATE.format(service_name=service_name))
            
        agents_file = app / "AGENTS.md"
        if not agents_file.exists():
            agents_file.write_text(AGENTS_TEMPLATE.format(project_name=name))
            
    print("✓ Documentación OpenAPI 3.1, AsyncAPI 3.0, FinOps y AGENTS.md estandarizada al 100% de apps.")

    # 2. Dotar de pom.xml a cores que no lo tengan
    for core in cores_dir.iterdir():
        if not core.is_dir() or core.name.startswith("."):
            continue
        core_pom = core / "pom.xml"
        if not core_pom.exists():
            core_pom.write_text(CORE_POM_TEMPLATE.format(core_name=core.name))
            # Crear estructura básica src/main/java y src/test/java si no existe
            (core / "src" / "main" / "java").mkdir(parents=True, exist_ok=True)
            (core / "src" / "test" / "java").mkdir(parents=True, exist_ok=True)
    print("✓ Todos los módulos de core/ disponen de pom.xml configurado.")

    # 3. Sincronizar pom.xml raíz
    all_apps = sorted([d.name for d in apps_dir.iterdir() if d.is_dir() and not d.name.startswith(".")])
    all_cores = sorted([d.name for d in cores_dir.iterdir() if d.is_dir() and not d.name.startswith(".")])
    
    modules_xml = ["        <!-- Librerías Core y Starter -->", "        <module>corp-spring-boot-starter</module>", "", "        <!-- Proyectos Legacy / SaaS -->", "        <module>SaaSRegantes</module>", "", "        <!-- Módulos Core Algorítmicos -->"]
    for c in all_cores:
        modules_xml.append(f"        <module>core/{c}</module>")
        
    modules_xml.append("")
    modules_xml.append("        <!-- Verticales Apps del Ecosistema MultiProyectos -->")
    for a in all_apps:
        modules_xml.append(f"        <module>apps/{a}</module>")

    root_pom_content = f"""<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.corp.ecosystem</groupId>
    <artifactId>multiproyectos-aggregator</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <name>MultiProyectos Aggregator</name>
    <description>Aggregator POM para el ecosistema global de Google Antigravity</description>

    <modules>
{chr(10).join(modules_xml)}
    </modules>

</project>
"""
    ROOT_POM.write_text(root_pom_content)
    print(f"✓ pom.xml raíz actualizado con {len(all_cores)} cores y {len(all_apps)} verticales (Total: {len(all_cores) + len(all_apps) + 2} módulos agregados).")

if __name__ == "__main__":
    standardize()
