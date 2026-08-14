#!/usr/bin/env python3
"""
Arquitectura y especificación formal para upload_to_notion_starter_comun.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_0_software_engineering/01_arquitectura_hexagonal_ddd_puro.md
- Referencia Académica: Martin (2017) Clean Architecture & DDD Pure Domain Standard
"""
import os
import sys
import glob
import json
import time
import urllib.request
import urllib.error

NOTION_TOKEN = os.getenv("NOTION_TOKEN", "")
NOTION_VERSION = "2022-06-28"

STARTER_COMUN_PAGE_ID = "379eb64b-620b-8131-9016-d9388b1b6c12"
WIKI_DB_ID = "379eb64b-620b-8135-a48b-db948408c74e"

BASE_DIR = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))

def request_notion(endpoint, method="POST", data=None):
    time.sleep(0.35) # Respetar rate limits
    url = f"https://api.notion.com/v1/{endpoint}"
    req = urllib.request.Request(
        url,
        method=method,
        headers={
            "Authorization": f"Bearer {NOTION_TOKEN}",
            "Notion-Version": NOTION_VERSION,
            "Content-Type": "application/json"
        }
    )
    if data:
        req.data = json.dumps(data).encode("utf-8")
    
    for attempt in range(4):
        try:
            with urllib.request.urlopen(req, timeout=15) as resp:
                return json.loads(resp.read().decode("utf-8"))
        except urllib.error.HTTPError as e:
            if e.code == 429:
                wait_time = (attempt + 1) * 3
                print(f"[RATE LIMIT] Esperando {wait_time}s...")
                time.sleep(wait_time)
                continue
            err_body = e.read().decode('utf-8')
            sys.stderr.write(f"[ERROR {e.code}] {endpoint}: {err_body}\n")
            return None
        except Exception as e:
            print(f"[RETRY {attempt+1}] Error en request: {e}")
            time.sleep(2)
    return None

def split_text_chunks(text, max_len=1900):
    if not text:
        return [""]
    return [text[i:i+max_len] for i in range(0, len(text), max_len)]

def map_language(lang):
    lang = lang.lower().strip()
    mapping = {
        "java": "java",
        "python": "python",
        "py": "python",
        "go": "go",
        "golang": "go",
        "dart": "dart",
        "flutter": "dart",
        "sh": "shell",
        "bash": "shell",
        "shell": "shell",
        "json": "json",
        "yaml": "yaml",
        "yml": "yaml",
        "sql": "sql",
        "html": "html",
        "css": "css",
        "javascript": "javascript",
        "js": "javascript",
        "typescript": "typescript",
        "ts": "typescript",
        "c": "c",
        "cpp": "c++",
        "c++": "c++",
        "latex": "plain text",
        "tex": "plain text",
        "math": "plain text",
        "mermaid": "plain text"
    }
    return mapping.get(lang, "plain text")

def parse_markdown_to_notion_blocks(content):
    blocks = []
    lines = content.splitlines()
    in_code = False
    code_lang = "plain text"
    code_lines = []

    for line in lines:
        stripped = line.strip()

        if stripped.startswith("```"):
            if in_code:
                full_code = "\n".join(code_lines)
                chunks = split_text_chunks(full_code, 1900)
                for chunk in chunks:
                    blocks.append({
                        "object": "block",
                        "type": "code",
                        "code": {
                            "rich_text": [{"type": "text", "text": {"content": chunk}}],
                            "language": code_lang
                        }
                    })
                code_lines = []
                in_code = False
            else:
                in_code = True
                lang = stripped[3:].strip()
                code_lang = map_language(lang)
            continue

        if in_code:
            code_lines.append(line)
            continue

        if not stripped:
            continue

        if stripped.startswith("# "):
            text = stripped[2:].strip()
            blocks.append({
                "object": "block",
                "type": "heading_1",
                "heading_1": {"rich_text": [{"type": "text", "text": {"content": text[:1900]}}]}
            })
        elif stripped.startswith("## "):
            text = stripped[3:].strip()
            blocks.append({
                "object": "block",
                "type": "heading_2",
                "heading_2": {"rich_text": [{"type": "text", "text": {"content": text[:1900]}}]}
            })
        elif stripped.startswith("### "):
            text = stripped[4:].strip()
            blocks.append({
                "object": "block",
                "type": "heading_3",
                "heading_3": {"rich_text": [{"type": "text", "text": {"content": text[:1900]}}]}
            })
        elif stripped.startswith("> "):
            text = stripped[2:].strip()
            blocks.append({
                "object": "block",
                "type": "callout",
                "callout": {
                    "rich_text": [{"type": "text", "text": {"content": text[:1900]}}],
                    "icon": {"emoji": "💡"}
                }
            })
        elif stripped.startswith("- ") or stripped.startswith("* "):
            text = stripped[2:].strip()
            blocks.append({
                "object": "block",
                "type": "bulleted_list_item",
                "bulleted_list_item": {"rich_text": [{"type": "text", "text": {"content": text[:1900]}}]}
            })
        elif len(stripped) > 2 and stripped[0].isdigit() and stripped[1] in [".", ")"]:
            parts = stripped.split(" ", 1)
            text = parts[1].strip() if len(parts) > 1 else stripped
            blocks.append({
                "object": "block",
                "type": "numbered_list_item",
                "numbered_list_item": {"rich_text": [{"type": "text", "text": {"content": text[:1900]}}]}
            })
        else:
            chunks = split_text_chunks(stripped, 1900)
            for chunk in chunks:
                blocks.append({
                    "object": "block",
                    "type": "paragraph",
                    "paragraph": {"rich_text": [{"type": "text", "text": {"content": chunk}}]}
                })

    if in_code and code_lines:
        full_code = "\n".join(code_lines)
        chunks = split_text_chunks(full_code, 1900)
        for chunk in chunks:
            blocks.append({
                "object": "block",
                "type": "code",
                "code": {
                    "rich_text": [{"type": "text", "text": {"content": chunk}}],
                    "language": code_lang
                }
            })

    return blocks

def format_title(rel_path):
    parts = rel_path.split(os.sep)
    filename = parts[-1].replace(".md", "")
    
    mod_map = {
        "00_ESTRATEGIA_EXPANSION_Y_GAP_ANALYSIS.md": "[Estrategia] 00 - Gap Analysis y Expansión de Verticales",
        "BIBLIOGRAFIA_ACADEMICA.md": "[Referencias] Bibliografía Académica de Élite (CMU, MIT, Stanford)",
        "INDEX_MASTER_GUIA_ESTUDIO.md": "[Index Master] Guía Global de Estudio y Mapa de Módulos",
        "expansion_verticales.md": "[Estrategia] Expansión a Nuevos Verticales Corporativos",
        "modulo_0_ingenieria_industrial": "M0: Ing. Industrial",
        "modulo_0_sistemas_distribuidos": "M0: Sist. Distribuidos",
        "modulo_0_software_engineering": "M0: Software Eng.",
        "modulo_1_backend_java_spring": "M1: Java & Spring",
        "modulo_2_go_y_concurrencia": "M2: Go & Concurrencia",
        "modulo_3_unified_twin_math": "M3: Unified Twin & Math",
        "modulo_4_frontend_y_motores_ui": "M4: Frontend & UI Engine",
        "modulo_5_cloud_native_dbs": "M5: Cloud-Native & GCP",
        "modulo_6_sre_y_alta_disponibilidad": "M6: SRE & Alta Disponibilidad",
        "modulo_7_bases_datos_nosql_multitenant": "M7: NoSQL & Multi-Tenancy"
    }

    if rel_path in mod_map:
        return mod_map[rel_path]

    prefix = mod_map.get(parts[0], parts[0])
    
    # Formatear el nombre del archivo
    clean_name = filename.replace("_", " ").title()
    return f"[{prefix}] {clean_name}"

def upload_file_to_wiki(file_path):
    rel_path = os.path.relpath(file_path, BASE_DIR)
    title = format_title(rel_path)

    print(f"Subiendo: {title} ({rel_path})...")

    with open(file_path, "r", encoding="utf-8") as f:
        content = f.read()

    blocks = parse_markdown_to_notion_blocks(content)

    initial_blocks = blocks[:95]
    remaining_blocks = blocks[95:]

    payload = {
        "parent": {"database_id": WIKI_DB_ID},
        "properties": {
            "Documento": {
                "title": [{"text": {"content": title[:200]}}]
            },
            "Tipo": {
                "select": {"name": "Técnico"}
            },
            "Fichero Local": {
                "rich_text": [{"text": {"content": rel_path[:2000]}}]
            }
        },
        "children": initial_blocks
    }

    resp = request_notion("pages", method="POST", data=payload)
    if not resp or "id" not in resp:
        print(f"[FALLO] Error al crear página para {title}")
        return False

    page_id = resp["id"]

    # Subir bloques restantes en lotes de 95
    while remaining_blocks:
        batch = remaining_blocks[:95]
        remaining_blocks = remaining_blocks[95:]
        append_payload = {"children": batch}
        app_resp = request_notion(f"blocks/{page_id}/children", method="PATCH", data=append_payload)
        if not app_resp:
            print(f"[WARNING] Fallo al añadir lote secundario a {page_id}")

    print(f"[OK] Subido exitosamente: {title} (Page ID: {page_id})")
    return True

def main():
    print("=== INICIANDO CARGA MASIVA DE DOCUMENTACIÓN EN STARTER COMÚN DE NOTION ===")
    
    all_files = sorted(glob.glob(os.path.join(BASE_DIR, "**/*.md"), recursive=True))
    # Excluir scripts y prompts temporales
    files_to_upload = [f for f in all_files if "scripts" not in f and "PROMPTS" not in f]

    print(f"Se subirán {len(files_to_upload)} archivos markdown a Wiki Starter Común.")

    success_count = 0
    for file_path in files_to_upload:
        if upload_file_to_wiki(file_path):
            success_count += 1

    print(f"\n=== PROCESO COMPLETADO: {success_count}/{len(files_to_upload)} ARCHIVOS CARGADOS EXITOSAMENTE ===")

if __name__ == "__main__":
    main()
