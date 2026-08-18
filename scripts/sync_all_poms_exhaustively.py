#!/usr/bin/env python3
"""
EXHAUSTIVE POM SYNCHRONIZER
----------------------------
Sincroniza el 100% de apps y cores en root pom.xml y el 100% de starters en corp-spring-boot-starter/pom.xml
sin perder ninguna configuración de dependencias existente.
"""

from pathlib import Path
import re

WORKSPACE = Path("/home/jaruiz/Desarrollo")
ROOT_POM = WORKSPACE / "pom.xml"
STARTER_POM = WORKSPACE / "corp-spring-boot-starter/pom.xml"

def sync_root_pom():
    apps = sorted([f"apps/{p.name}" for p in (WORKSPACE / "apps").iterdir() if p.is_dir() and (p / "pom.xml").exists()])
    cores = sorted([f"core/{p.name}" for p in (WORKSPACE / "core").iterdir() if p.is_dir() and (p / "pom.xml").exists()])

    all_modules = ["corp-spring-boot-starter", "SaaSRegantes"] + cores + apps
    
    modules_xml = "\n".join([f"        <module>{m}</module>" for m in all_modules])
    
    content = ROOT_POM.read_text(encoding="utf-8")
    new_content = re.sub(
        r'<modules>.*?</modules>',
        f'<modules>\n{modules_xml}\n    </modules>',
        content,
        flags=re.DOTALL
    )
    ROOT_POM.write_text(new_content, encoding="utf-8")
    print(f"✓ Root pom.xml sincronizado con {len(all_modules)} módulos (Apps: {len(apps)}, Cores: {len(cores)}).")

def sync_starter_pom():
    starters = sorted([p.name for p in (WORKSPACE / "corp-spring-boot-starter").iterdir() if p.is_dir() and (p / "pom.xml").exists() and p.name != "corp-spring-boot-starter"])
    all_starters = starters + ["corp-spring-boot-starter"]
    
    modules_xml = "\n".join([f"        <module>{s}</module>" for s in all_starters])
    
    content = STARTER_POM.read_text(encoding="utf-8")
    new_content = re.sub(
        r'<modules>.*?</modules>',
        f'<modules>\n{modules_xml}\n    </modules>',
        content,
        flags=re.DOTALL
    )
    STARTER_POM.write_text(new_content, encoding="utf-8")
    print(f"✓ corp-spring-boot-starter/pom.xml sincronizado con {len(all_starters)} starters.")

if __name__ == "__main__":
    sync_root_pom()
    sync_starter_pom()
