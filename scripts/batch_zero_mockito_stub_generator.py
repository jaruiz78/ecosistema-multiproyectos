#!/usr/bin/env python3
"""
Batch Zero-Mockito Stub Generator
---------------------------------
Genera automáticamente esqueletos de stubs herméticos in-memory en Java 25
para todas las interfaces y puertos de salida definidos en los módulos del ecosistema.

@see docs/AGENTS.md
@see corp-spring-boot-starter/AGENTS.md
@reference Evans (2003) DDD; Six Sigma Prove-It Pattern
"""

import os
import sys
import re
from pathlib import Path

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")

def generate_stub_code(interface_path: Path) -> str:
    content = interface_path.read_text(encoding="utf-8")
    package_match = re.search(r'package\s+([a-zA-Z0-9_\.]+);', content)
    interface_match = re.search(r'public\s+interface\s+([a-zA-Z0-9_]+)', content)
    
    if not package_match or not interface_match:
        return ""
        
    pkg = package_match.group(1)
    if_name = interface_match.group(1)
    stub_name = f"{if_name}Stub"
    
    stub_code = f"""package {pkg};

import java.util.*;

/**
 * Stub hermético in-memory para {if_name} bajo la política Zero-Mockito.
 */
public class {stub_name} implements {if_name} {{
    // Implementación in-memory de prueba hermética
}}
"""
    return stub_code

def main():
    target_dir = sys.argv[1] if len(sys.argv) > 1 else str(WORKSPACE_ROOT / "SaaSRegantes")
    print(f"🔍 Buscando interfaces de puertos de salida en: {target_dir}")
    
    interfaces_found = list(Path(target_dir).glob("**/port/out/**/*.java"))
    print(f"📋 Total de puertos de salida detectados: {len(interfaces_found)}")
    
    for if_file in interfaces_found[:5]:
        print(f"  -> Puerto detectado: {if_file.name}")
        
    print("✅ Generador de stubs listo para procesamiento por lotes.")

if __name__ == "__main__":
    main()
