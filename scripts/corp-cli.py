#!/usr/bin/env python3
import argparse
import os
from pathlib import Path

POM_TEMPLATE = """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.corp.tenant</groupId>
        <artifactId>corp-spring-boot-starter-parent</artifactId>
        <version>1.0.0</version>
        <relativePath>../../corp-spring-boot-starter/pom.xml</relativePath>
    </parent>

    <groupId>com.corp.{pkg}</groupId>
    <artifactId>{artifact_id}</artifactId>
    <version>1.0.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>com.corp.tenant</groupId>
            <artifactId>corp-core-spring-boot-starter</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>com.corp.tenant</groupId>
            <artifactId>corp-telemetry-spring-boot-starter</artifactId>
            <version>1.0.0</version>
        </dependency>
        <!-- Añade más starters corporativos según necesidad (ej. fintech, ai) -->
    </dependencies>
</project>
"""

APP_TEMPLATE = """package com.corp.{pkg};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class {capitalized}Application {{
    public static void main(String[] args) {{
        SpringApplication.run({capitalized}Application.class, args);
    }}
}}
"""

def generate_project(name: str, pkg: str, destination: str):
    base_dir = Path(destination) / name
    print(f"🚀 Iniciando scaffolding de nuevo microservicio corporativo en: {base_dir}")
    
    # Directorios Base
    src_main_java = base_dir / "src" / "main" / "java" / "com" / "corp" / pkg
    src_main_resources = base_dir / "src" / "main" / "resources"
    src_test_java = base_dir / "src" / "test" / "java" / "com" / "corp" / pkg
    docs_adr = base_dir / "docs" / "adr"
    
    for d in [
        src_main_java / "domain",
        src_main_java / "application",
        src_main_java / "infrastructure" / "in",
        src_main_java / "infrastructure" / "out",
        src_main_resources,
        src_test_java,
        docs_adr
    ]:
        d.mkdir(parents=True, exist_ok=True)

    # POM
    with open(base_dir / "pom.xml", "w") as f:
        f.write(POM_TEMPLATE.format(pkg=pkg, artifact_id=name))

    # Application Class
    cap_name = name.replace("Proyecto", "").replace("-", " ").title().replace(" ", "")
    if not cap_name: cap_name = "Core"
    
    with open(src_main_java / f"{cap_name}Application.java", "w") as f:
        f.write(APP_TEMPLATE.format(pkg=pkg, capitalized=cap_name))

    print("✅ Arquetipo generado con éxito. Arquitectura Hexagonal y POM corporativo heredado instalados.")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Corp CLI: Generador de Proyectos Ecosistema")
    parser.add_argument("name", help="Nombre del proyecto (ej. ProyectoNuevo)")
    parser.add_argument("--pkg", required=True, help="Nombre del paquete (ej. nuevo)")
    parser.add_argument("--dest", default="/home/jaruiz/Desarrollo/apps", help="Directorio destino (def: ~/Desarrollo/apps)")
    
    args = parser.parse_args()
    generate_project(args.name, args.pkg, args.dest)
