#!/usr/bin/env python3
import re
from pathlib import Path

WORKSPACE = Path("/home/jaruiz/Desarrollo")

for pom in WORKSPACE.rglob("pom.xml"):
    content = pom.read_text(encoding="utf-8")
    # Reemplazar & no escapados en XML
    new_content = re.sub(r'&(?!amp;|lt;|gt;|quot;|apos;)', '&amp;', content)
    if new_content != content:
        pom.write_text(new_content, encoding="utf-8")
        print(f"✓ Corregido XML escaping en {pom}")
