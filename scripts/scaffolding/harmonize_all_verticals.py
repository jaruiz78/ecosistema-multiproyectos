#!/usr/bin/env python3
"""
Harmonize All Enterprise Verticals
----------------------------------
Asegura la total homogeneidad y completitud arquitectónica de los 64 proyectos verticales en apps/:
1. Generación de configuración dual: application.properties, application-local.properties y application-prod.properties.
2. Pipeline CloudBuild: cloudbuild.yaml con generación de SBOM (CycloneDX) y firmas Cosign Keyless (SLSA L3).
3. Dockerfile Multi-Stage Distroless para Java 25 LTS.
4. AGENTS.md conectado al estándar corporativo y Universidad Privada.
5. Dossier formal de Notion (docs/NOTION_PROJECT_DOSSIER.md).
"""

import os
import re
from pathlib import Path

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")
APPS_DIR = WORKSPACE_ROOT / "apps"

def get_entity_name(project_name: str) -> str:
    name = project_name.replace("Proyecto", "")
    return name if name else "CoreEntity"

def harmonize_project(project_dir: Path):
    project_name = project_dir.name
    slug = re.sub(r'[^a-zA-Z0-9]', '', project_name)
    pkg_name = slug.lower()
    entity_name = get_entity_name(project_name)

    # 1. Multi-Stage Dockerfile (si no existe)
    dockerfile_path = project_dir / "Dockerfile"
    if not dockerfile_path.exists():
        dockerfile_content = f"""# syntax=docker/dockerfile:1.4
# Stage 1: Build & Package
FROM eclipse-temurin:25-jdk-noble AS builder
WORKDIR /workspace

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
        dockerfile_path.write_text(dockerfile_content, encoding="utf-8")

    # 2. CloudBuild Pipeline (SLSA L3 & Cosign)
    cloudbuild_path = project_dir / "cloudbuild.yaml"
    if not cloudbuild_path.exists():
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
        cloudbuild_path.write_text(cloudbuild_content, encoding="utf-8")

    # 3. Application Properties (Dual Architecture: LOCAL vs PROD)
    src_main_res = project_dir / "src" / "main" / "resources"
    src_main_res.mkdir(parents=True, exist_ok=True)

    app_properties_path = src_main_res / "application.properties"
    if not app_properties_path.exists():
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
        app_properties_path.write_text(app_properties, encoding="utf-8")

    app_local_path = src_main_res / "application-local.properties"
    if not app_local_path.exists():
        app_local_properties = f"""# Perfil LOCAL: Emuladores y Mocks en Memoria
google.cloud.project-id=itinera-local
firestore.emulator.host=localhost:8089
bigquery.emulator.host=http://localhost:8086

# Telemetría e Ingesta Local
pct.telemetry.in-memory=true
logging.level.com.corp=DEBUG
"""
        app_local_path.write_text(app_local_properties, encoding="utf-8")

    app_prod_path = src_main_res / "application-prod.properties"
    if not app_prod_path.exists():
        app_prod_properties = f"""# Perfil PROD: Google Cloud Serverless Managed Services
google.cloud.project-id=${{GCP_PROJECT_ID}}
spring.cloud.gcp.secretmanager.enabled=true

# BigQuery Particionado Obligatorio & FinOps
bigquery.dataset={pkg_name}_analytics
logging.level.com.corp=INFO
"""
        app_prod_path.write_text(app_prod_properties, encoding="utf-8")

    # 4. Notion Project Dossier
    docs_dir = project_dir / "docs"
    docs_dir.mkdir(parents=True, exist_ok=True)
    dossier_path = docs_dir / "NOTION_PROJECT_DOSSIER.md"
    if not dossier_path.exists():
        dossier_content = f"""# 🏛️ NOTION DOSSIER: {project_name}

## 1. Visión y Resumen Ejecutivo
* **Nombre**: {project_name}
* **Entidad Dominio**: {entity_name}
* **Arquitectura**: Java 25 LTS / Spring Boot 4.0 / Cloud Run / Multi-Tenant RLS
* **Parent Chassis**: `corp-spring-boot-starter`

## 2. Pila Tecnológica & Moat
* **Backend**: Java 25 (Project Loom Virtual Threads & Records).
* **Infraestructura**: Google Cloud Run, Cloud Tasks, BigQuery Columnar Storage Write API.
* **Seguridad**: SLSA L3, Cosign Keyless Signatures, Zero-Trust BeyondCorp & Zero-PII.

## 3. Estado Operativo & Tareas Kanban
* **Estado**: Producción / Activo
* **FinOps Target**: < 0.015 USD / MAU / mes
"""
        dossier_path.write_text(dossier_content, encoding="utf-8")

    # 5. AGENTS.md
    agents_path = project_dir / "AGENTS.md"
    if not agents_path.exists():
        agents_md = f"""# AGENTS.md
👉 Consulte: [`docs/AGENTS.md`](file:///home/jaruiz/Desarrollo/docs/AGENTS.md)
"""
        agents_path.write_text(agents_md, encoding="utf-8")

def main():
    print("🚀 Armonizando y completando scaffolding de todos los proyectos en apps/...")
    count = 0
    for d in sorted(APPS_DIR.iterdir()):
        if d.is_dir() and d.name.startswith("Proyecto"):
            harmonize_project(d)
            count += 1
    print(f"✓ {count} Proyectos verticales armonizados y completados al 100% de scaffolding.")

if __name__ == "__main__":
    main()
