#!/usr/bin/env python3
import os
import sys
import glob
import json
import time
import hashlib
import urllib.request
import urllib.error

NOTION_TOKEN = os.getenv("NOTION_TOKEN", "")
NOTION_VERSION = "2022-06-28"

WIKI_DB_ID = "379eb64b-620b-8135-a48b-db948408c74e"
STARTER_COMUN_PAGE_ID = "379eb64b-620b-8131-9016-d9388b1b6c12"

SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
BASE_DIR = os.path.abspath(os.path.join(SCRIPT_DIR, ".."))
PROJECT_ROOT = os.path.abspath(os.path.join(BASE_DIR, "..", ".."))
CACHE_FILE = os.path.join(PROJECT_ROOT, "scratch", "notion_sync_cache.json")

os.makedirs(os.path.dirname(CACHE_FILE), exist_ok=True)

def request_notion(endpoint, method="POST", data=None):
    time.sleep(0.3)
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
    
    for attempt in range(3):
        try:
            with urllib.request.urlopen(req, timeout=12) as resp:
                return json.loads(resp.read().decode("utf-8"))
        except urllib.error.HTTPError as e:
            if e.code == 429:
                time.sleep((attempt + 1) * 2)
                continue
            err_body = e.read().decode('utf-8')
            sys.stderr.write(f"[ERROR {e.code}] {endpoint}: {err_body}\n")
            return None
        except Exception as e:
            time.sleep(1)
    return None

def compute_sha256(filepath):
    h = hashlib.sha256()
    with open(filepath, "rb") as f:
        while chunk := f.read(8192):
            h.update(chunk)
    return h.hexdigest()

def split_text_chunks(text, max_len=1900):
    if not text:
        return [""]
    return [text[i:i+max_len] for i in range(0, len(text), max_len)]

def map_language(lang):
    lang = lang.lower().strip()
    mapping = {
        "java": "java", "python": "python", "py": "python",
        "go": "go", "golang": "go", "dart": "dart", "flutter": "dart",
        "sh": "shell", "bash": "shell", "shell": "shell",
        "json": "json", "yaml": "yaml", "yml": "yaml",
        "sql": "sql", "html": "html", "css": "css",
        "js": "javascript", "ts": "typescript"
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
            blocks.append({
                "object": "block",
                "type": "heading_1",
                "heading_1": {"rich_text": [{"type": "text", "text": {"content": stripped[2:].strip()[:1900]}}]}
            })
        elif stripped.startswith("## "):
            blocks.append({
                "object": "block",
                "type": "heading_2",
                "heading_2": {"rich_text": [{"type": "text", "text": {"content": stripped[3:].strip()[:1900]}}]}
            })
        elif stripped.startswith("### "):
            blocks.append({
                "object": "block",
                "type": "heading_3",
                "heading_3": {"rich_text": [{"type": "text", "text": {"content": stripped[4:].strip()[:1900]}}]}
            })
        elif stripped.startswith("> "):
            blocks.append({
                "object": "block",
                "type": "callout",
                "callout": {
                    "rich_text": [{"type": "text", "text": {"content": stripped[2:].strip()[:1900]}}],
                    "icon": {"emoji": "💡"}
                }
            })
        elif stripped.startswith("- ") or stripped.startswith("* "):
            blocks.append({
                "object": "block",
                "type": "bulleted_list_item",
                "bulleted_list_item": {"rich_text": [{"type": "text", "text": {"content": stripped[2:].strip()[:1900]}}]}
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
    clean_name = filename.replace("_", " ").title()
    return f"[{prefix}] {clean_name}"

def clear_page_blocks(page_id):
    children_resp = request_notion(f"blocks/{page_id}/children", method="GET")
    if not children_resp:
        return
    for block in children_resp.get("results", []):
        block_id = block.get("id")
        request_notion(f"blocks/{block_id}", method="DELETE")

def sync_file(filepath, cache):
    rel_path = os.path.relpath(filepath, BASE_DIR)
    current_sha = compute_sha256(filepath)
    
    cached_info = cache.get(rel_path, {})
    if cached_info.get("sha256") == current_sha:
        return False # No hay cambios

    title = format_title(rel_path)
    page_id = cached_info.get("page_id")

    with open(filepath, "r", encoding="utf-8") as f:
        content = f.read()

    blocks = parse_markdown_to_notion_blocks(content)

    if page_id:
        print(f"[ACTUALIZANDO] {rel_path} -> Notion Page: {page_id}")
        # Limpiar bloques anteriores y resubir
        clear_page_blocks(page_id)
        remaining = blocks[:]
        while remaining:
            batch = remaining[:95]
            remaining = remaining[95:]
            request_notion(f"blocks/{page_id}/children", method="PATCH", data={"children": batch})
    else:
        print(f"[NUEVO DOCUMENTO] Subiendo {rel_path} a Notion Wiki...")
        initial_blocks = blocks[:95]
        remaining = blocks[95:]
        payload = {
            "parent": {"database_id": WIKI_DB_ID},
            "properties": {
                "Documento": {"title": [{"text": {"content": title[:200]}}]},
                "Tipo": {"select": {"name": "Técnico"}},
                "Fichero Local": {"rich_text": [{"text": {"content": rel_path[:2000]}}]}
            },
            "children": initial_blocks
        }
        resp = request_notion("pages", method="POST", data=payload)
        if resp and "id" in resp:
            page_id = resp["id"]
            while remaining:
                batch = remaining[:95]
                remaining = remaining[95:]
                request_notion(f"blocks/{page_id}/children", method="PATCH", data={"children": batch})
        else:
            print(f"[ERROR] No se pudo crear página para {rel_path}")
            return False

    cache[rel_path] = {
        "sha256": current_sha,
        "page_id": page_id,
        "title": title,
        "last_synced": time.strftime("%Y-%m-%dT%H:%M:%SZ", time.gmtime())
    }
    return True

def main():
    cache = {}
    if os.path.exists(CACHE_FILE):
        try:
            with open(CACHE_FILE, "r", encoding="utf-8") as f:
                cache = json.load(f)
        except Exception:
            cache = {}

    all_md_files = sorted(glob.glob(os.path.join(BASE_DIR, "**/*.md"), recursive=True))
    files_to_check = [f for f in all_md_files if "scripts" not in f and "PROMPTS" not in f]

    updated_count = 0
    for f in files_to_check:
        if sync_file(f, cache):
            updated_count += 1

    with open(CACHE_FILE, "w", encoding="utf-8") as f:
        json.dump(cache, f, indent=2)

    if updated_count > 0:
        print(f"[NOTION SYNC COMPLETADO] {updated_count} archivos actualizados en Notion.")
    else:
        print("[NOTION SYNC] Todos los documentos locales están al día con Notion.")

if __name__ == "__main__":
    main()

