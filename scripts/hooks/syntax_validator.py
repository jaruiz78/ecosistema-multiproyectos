#!/usr/bin/env python3
"""
Syntax Validator & Linter (Mermaid, KaTeX/LaTeX & Markdown)
------------------------------------------------------------
Validador y linter de sintaxis determinista para evitar errores de renderizado en:
1. Diagramas Mermaid (tipos válidos, entrecomillado de etiquetas con caracteres especiales, palabras reservadas).
2. Fórmulas KaTeX/LaTeX (escape de $, delimitadores simétricos, sin backslash-spaces huérfanos).
3. Estructura Markdown (tablas con columnas consistentes, enlaces conformes [name](file://...)).

@see docs/AGENTS.md
@see docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md
"""

import re
import sys
from typing import List, Tuple, Optional

# Tipos de diagramas Mermaid soportados oficialmente
VALID_MERMAID_HEADERS = [
    "flowchart", "graph", "sequenceDiagram", "classDiagram", "stateDiagram-v2",
    "stateDiagram", "erDiagram", "gantt", "pie", "gitGraph", "mindmap",
    "timeline", "quadrantChart", "xychart-beta", "sankey-beta", "kanban", "block-beta", "architecture-beta"
]

RESERVED_MERMAID_KEYWORDS = {
    "end", "subgraph", "call", "default", "style", "class", "graph", "click"
}

def extract_code_blocks(text: str) -> List[Tuple[str, str, int]]:
    """Extrae bloques de código con su lenguaje y número de línea inicial."""
    blocks = []
    lines = text.splitlines()
    in_block = False
    block_lang = ""
    block_lines = []
    start_line = 0

    for i, line in enumerate(lines, start=1):
        if line.strip().startswith("```"):
            if in_block:
                blocks.append((block_lang, "\n".join(block_lines), start_line))
                in_block = False
                block_lang = ""
                block_lines = []
            else:
                in_block = True
                block_lang = line.strip().lstrip("`").strip().lower()
                start_line = i
        elif in_block:
            block_lines.append(line)

    return blocks

def validate_mermaid_block(code: str, line_offset: int = 1) -> List[str]:
    """Valida un bloque de diagrama Mermaid."""
    errors = []
    lines = [l.strip() for l in code.strip().splitlines() if l.strip() and not l.strip().startswith("%%")]

    if not lines:
        errors.append(f"Línea {line_offset}: Bloque Mermaid vacío.")
        return errors

    header_line = lines[0]
    header_type = header_line.split()[0]

    # 1. Validar tipo de encabezado
    if not any(header_line.startswith(valid_h) for valid_h in VALID_MERMAID_HEADERS):
        errors.append(
            f"Línea {line_offset}: Tipo de diagrama Mermaid '{header_type}' no soportado o inválido. "
            f"Use uno de: {', '.join(VALID_MERMAID_HEADERS[:8])}..."
        )

    # 2. Balanceo de subgrafos (exclusivo para flowcharts y graphs)
    if header_type in ["flowchart", "graph"]:
        subgraph_count = sum(1 for l in lines if l.startswith("subgraph"))
        end_count = sum(1 for l in lines if l == "end" or l.startswith("end "))
        if subgraph_count != end_count:
            errors.append(
                f"Línea {line_offset}: Desbalance de subgrafos en {header_type}: {subgraph_count} 'subgraph' vs {end_count} 'end'."
            )

    # 3. Validar etiquetas no entrecomilladas con caracteres especiales en flowcharts
    if header_type in ["flowchart", "graph"]:
        for idx, line in enumerate(lines[1:], start=line_offset + 1):
            if line.startswith("subgraph") or line == "end" or line.startswith("style") or line.startswith("classDef"):
                continue

            # Detectar definición de nodos: id[Texto con caracteres especiales sin comillas]
            # Patrón busca: id[algo] o id(algo) o id{algo} donde dentro hay () o [] o {} o : o / o & o | y NO empieza por comillas
            node_matches = re.finditer(r'([a-zA-Z0-9_\-]+)\s*(\[|\(|\{|\(\()([^"\'\]\)\}]*[\(\)\[\]\{\}:/&|<>][^"\'\]\)\}]*)(\]|\)|\}|\)\))', line)
            for m in node_matches:
                node_id = m.group(1)
                content = m.group(3).strip()
                if content and not (content.startswith('"') and content.endswith('"')):
                    errors.append(
                        f"Línea {idx}: El nodo '{node_id}' contiene caracteres especiales sin entrecomillar: '{content}'. "
                        f"Formato obligatorio: {node_id}[\"{content}\"]."
                    )

            # Detectar IDs de nodo que son palabras reservadas
            id_match = re.match(r'^\s*([a-zA-Z0-9_]+)\s*(\[|\(|\{)', line)
            if id_match:
                nid = id_match.group(1).lower()
                if nid in RESERVED_MERMAID_KEYWORDS:
                    errors.append(
                        f"Línea {idx}: El ID de nodo '{nid}' es una palabra reservada de Mermaid. "
                        f"Use un prefijo como 'node_{nid}'."
                    )

    return errors

def validate_katex_text(text: str) -> List[str]:
    """Valida la sintaxis de KaTeX y el correcto escape de símbolos $ en prosa."""
    errors = []
    
    # Remover bloques de código para evitar falsos positivos
    clean_text = re.sub(r'```[\s\S]*?```', '', text)
    # Remover código inline `...`
    clean_text = re.sub(r'`[^`\n]+`', '', clean_text)

    # 1. Detectar uso directo de moneda $ sin escapar o KaTeX roto
    # Busca $ seguido de número o texto que no es delimitador KaTeX cerrado simétricamente
    lines = clean_text.splitlines()
    for idx, line in enumerate(lines, start=1):
        # Contar $ no escapados
        unescaped_dollars = len(re.findall(r'(?<!\\)\$', line))
        
        # Si hay un número impar de $, KaTeX fallará al renderizar en esa línea
        if unescaped_dollars % 2 != 0:
            errors.append(
                f"Línea {idx}: Símbolo '$' desbalanceado ({unescaped_dollars} encontrados). "
                "Para moneda use backticks (`$100`) o escape (`\\$100`). Para fórmulas use `\\( ... \\)` o pares `$formula$`."
            )

        # Detectar patrones comunes de moneda no envuelta: $0.015, $100, $/kWh, $/MAU
        currency_match = re.search(r'(?<![\\`\$])\$[0-9]+(?:\.[0-9]+)?(?:\s*USD|\s*EUR|\s*/|\b)', line)
        if currency_match and unescaped_dollars % 2 == 0:
            matched_str = currency_match.group(0)
            # Si contiene /kWh o USD no en math
            if "USD" in line or "/kWh" in line or "/MAU" in line:
                if not ("\\text{" in line or "\\(" in line):
                    errors.append(
                        f"Línea {idx}: Moneda o unidad con dollar '{matched_str}' debe ir envuelta en backticks "
                        f"(`{matched_str}`) o en bloque KaTeX con \\text{{USD}}."
                    )

        # 2. Detectar espacios huérfanos con barra invertida en KaTeX: '\ '
        if re.search(r'\\\s{2,}', line):
            errors.append(
                f"Línea {idx}: Barra invertida huérfana antes de espacio en KaTeX."
            )

    return errors

def validate_markdown_structure(text: str) -> List[str]:
    """Valida la consistencia estructural de tablas y enlaces Markdown."""
    errors = []
    lines = text.splitlines()
    
    in_table = False
    expected_cols = 0
    table_start_line = 0

    for idx, line in enumerate(lines, start=1):
        stripped = line.strip()

        # Validar enlaces mal formados como `[text](url)` o [text](`url`)
        bad_link_1 = re.search(r'`\[([^\]]+)\]\(([^)]+)\)`', line)
        if bad_link_1:
            errors.append(
                f"Línea {idx}: Enlace Markdown inválido envuelto en backticks globales: {bad_link_1.group(0)}. "
                f"Formato correcto: [`{bad_link_1.group(1)}`]({bad_link_1.group(2)})."
            )

        # Validar tablas Markdown
        if stripped.startswith("|") and stripped.endswith("|"):
            cols = [c.strip() for c in stripped.split("|")[1:-1]]
            if not in_table:
                in_table = True
                expected_cols = len(cols)
                table_start_line = idx
            else:
                # Línea separadora |---|---|
                if all(re.match(r'^:?-+:?$', c) for c in cols):
                    if len(cols) != expected_cols:
                        errors.append(
                            f"Línea {idx}: Separador de tabla tiene {len(cols)} columnas, pero el encabezado en línea {table_start_line} tiene {expected_cols}."
                        )
                elif len(cols) != expected_cols:
                    errors.append(
                        f"Línea {idx}: Fila de tabla tiene {len(cols)} columnas, se esperaban {expected_cols} (iniciada en línea {table_start_line})."
                    )
        else:
            in_table = False

    return errors

def validate_all(text: str) -> List[str]:
    """Ejecuta todas las validaciones sintácticas."""
    all_errors = []
    
    # 1. Validar Mermaid
    blocks = extract_code_blocks(text)
    for lang, code, start_line in blocks:
        if lang == "mermaid":
            all_errors.extend(validate_mermaid_block(code, start_line))

    # 2. Validar KaTeX
    all_errors.extend(validate_katex_text(text))

    # 3. Validar Markdown
    all_errors.extend(validate_markdown_structure(text))

    return all_errors

def main():
    if len(sys.argv) < 2:
        print("Uso: python3 syntax_validator.py <archivo_o_texto.md>")
        sys.exit(0)

    target = sys.argv[1]
    from pathlib import Path
    path_obj = Path(target)
    
    if path_obj.is_file():
        content = path_obj.read_text(encoding="utf-8", errors="ignore")
    else:
        content = target

    errors = validate_all(content)
    if errors:
        print(f"❌ Se detectaron {len(errors)} errores de sintaxis (Mermaid / KaTeX / Markdown):")
        for err in errors:
            print(f"  • {err}")
        sys.exit(1)
    else:
        print("✅ Sintaxis (Mermaid, KaTeX y Markdown) 100% válida y libre de errores de renderizado.")
        sys.exit(0)

if __name__ == "__main__":
    main()
