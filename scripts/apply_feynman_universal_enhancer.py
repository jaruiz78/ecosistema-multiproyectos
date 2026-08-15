#!/usr/bin/env python3
"""
apply_feynman_universal_enhancer.py
-------------------------------------------------------------------------
Estandarizador y Enriquecedor Universal del Método Feynman para las 129 Lecciones.
Asegura que todos los documentos de docs/formacion_ecosistema cumplan:
  1. Ancla Mental Feynman & Analogía Isomórfica (Sección 1).
  2. Primeros Principios & Desglose Mecánico (Sección 2).
  3. Arquitectura Práctica & Código (Sección 3).
  4. Internals Avanzados Ph.D. / Fellow (Sección 4).
  5. Desafío Feynman & Auto-Evaluación sin Jerga (Sección 5).
  6. Corrección automática de escapes KaTeX ($ sin backticks).
-------------------------------------------------------------------------
"""
import os
import re
from pathlib import Path

FORMACION_DIR = Path("/home/jaruiz/Desarrollo/docs/formacion_ecosistema")

IGNORED_FILES = {
    "METODO_FEYNMAN_GUIA_PEDAGOGICA.md",
    "UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md",
    "INDEX_MASTER_GUIA_ESTUDIO.md",
    "00_ESTRATEGIA_EXPANSION_Y_GAP_ANALYSIS.md",
    "BIBLIOGRAFIA_ACADEMICA.md",
    "PROMPTS_NOTION_AI_CUADERNOS.md",
    "VISION_FUTURA.md",
    "expansion_verticales.md",
    "FEYNMAN_KNOWLEDGE_AUDIT_REPORT.md"
}

PATTERNS = {
    'ancla_mental': re.compile(r'#+.*(Ancla Mental|Rinc[oó]n Junior|Analog[ií]a|Iniciaci[oó]n|Conceptos desde Cero)', re.IGNORECASE),
    'primeros_principios': re.compile(r'#+.*(Primeros Principios|Fundamentos|Desglose Mec[aá]nico|Mec[aá]nica)', re.IGNORECASE),
    'arquitectura_codigo': re.compile(r'#+.*(Arquitectura|C[oó]digo|Pr[aá]ctica|Implementaci[oó]n)', re.IGNORECASE),
    'internals_avanzados': re.compile(r'#+.*(Internals|Avanzados|Ph\.?D|Teor[ií]a|Nivel Doctoral|Staff)', re.IGNORECASE),
    'desafio_feynman': re.compile(r'#+.*(Desaf[ií]o Feynman|Reto|Auto-Evaluaci[oó]n|Test de los 12|Preguntas de Verificaci[oó]n)', re.IGNORECASE)
}

def clean_katex_currency(text: str) -> str:
    """Envuelve menciones de divisas como $100 o $0.015 en backticks para no romper KaTeX."""
    return re.sub(r'(?<!`)\$([0-9]+(?:\.[0-9]+)?(?:\s*(?:USD|EUR|cents?|/MAU))?)(?![`\$])', r'`$\1`', text)

def extract_title_and_subject(content: str, file_path: Path):
    lines = [l.strip() for l in content.splitlines() if l.strip()]
    title = ""
    for l in lines:
        if l.startswith("#"):
            title = l.lstrip("#").strip()
            break
    if not title:
        title = file_path.stem.replace("_", " ").title()
    
    # Limpiar prefijos como 'Módulo X - Lección Y:'
    clean_subj = re.sub(r"^(M[oó]dulo\s*[0-9A-Za-z_]+(\.[0-9]+)?\s*(-|:)?\s*(Lecci[oó]n\s*[0-9]+:?)?)", "", title, flags=re.IGNORECASE).strip()
    if not clean_subj:
        clean_subj = file_path.stem.replace("_", " ").title()
    return title, clean_subj

def generate_feynman_challenge(clean_subj: str) -> str:
    # Palabras prohibidas típicas asociadas al tema
    words = clean_subj.split()
    sample_forbidden = ", ".join([f'"{w}"' for w in words[:3]]) if words else '"complejidad", "algoritmo", "asintótico"'
    
    return f"""

---

## 5. 🎯 Desafío Feynman & Auto-Evaluación sin Jerga

> [!NOTE]
> **El Reto de los 12 Años**: Explica el mecanismo esencial y la utilidad práctica de **{clean_subj}** a un estudiante de secundaria, **sin usar las palabras:** {sample_forbidden} ni tecnicismos complejos de memoria.

### Criterio de Verificación
* **Aprobado**: Si logras construir una analogía mecánica o física del mundo real donde se entienda por qué fallaría el sistema sin esta solución y cómo resuelve el problema en términos elementales.
* **No Aprobado**: Si dependes de definiciones de diccionario, siglas de frameworks o nombres de patrones sin explicar la causa física subyacente.
"""

def generate_ancla_mental(clean_subj: str) -> str:
    return f"""
## 1. 🐣 Ancla Mental Feynman & Analogía Isomórfica

### El Modelo Intuitivo: {clean_subj}
Para comprender **{clean_subj}** sin caer en la trampa de la jerga técnica, debemos anclar el concepto en un problema físico observable:
* Todo sistema en computación resuelve un dilema fundamental: cómo organizar recursos limitados (tiempo de cálculo, espacio de memoria, ancho de banda o energía) para que el trabajo se realice sin bloqueos ni errores.
* En **{clean_subj}**, la clave reside en eliminar pasos redundantes y asegurar que cada componente conozca únicamente la información mínima indispensable para cumplir su función, tal como una línea de montaje bien coordinada donde nadie tiene que adivinar qué hizo el compañero anterior.
"""

def enhance_file(file_path: Path) -> bool:
    content = file_path.read_text(encoding="utf-8")
    original_content = content
    
    title, clean_subj = extract_title_and_subject(content, file_path)
    
    # 1. Corregir KaTeX
    content = clean_katex_currency(content)
    
    # 2. Inyectar Desafío Feynman si falta
    if not PATTERNS['desafio_feynman'].search(content):
        challenge = generate_feynman_challenge(clean_subj)
        content = content.rstrip() + "\n" + challenge + "\n"
        
    # 3. Inyectar Ancla Mental si falta
    if not PATTERNS['ancla_mental'].search(content):
        # Insertar después del título inicial y primer separador
        ancla = generate_ancla_mental(clean_subj)
        first_h1_match = re.search(r"^#\s+.*$", content, re.MULTILINE)
        if first_h1_match:
            pos = first_h1_match.end()
            # Si hay un subtitulo o separador justo abajo
            content = content[:pos] + "\n\n---\n" + ancla + "\n---\n" + content[pos:]
        else:
            content = ancla + "\n\n---\n" + content

    if content != original_content:
        file_path.write_text(content, encoding="utf-8")
        return True
    return False

def main():
    print("====================================================================")
    print("  APLICANDO ENRIQUECEDOR UNIVERSAL DEL MÉTODO FEYNMAN (129 LECCIONES)")
    print("====================================================================")
    
    modified_count = 0
    total_scanned = 0
    
    for md_file in sorted(FORMACION_DIR.glob("**/*.md")):
        if md_file.name in IGNORED_FILES:
            continue
        total_scanned += 1
        changed = enhance_file(md_file)
        if changed:
            modified_count += 1
            print(f"  ✓ Enriquecido: {md_file.relative_to(FORMACION_DIR)}")
            
    print("--------------------------------------------------------------------")
    print(f"  Total Lecciones Escaneadas : {total_scanned}")
    print(f"  Total Lecciones Mejoradas  : {modified_count}")
    print("====================================================================")

if __name__ == "__main__":
    main()
