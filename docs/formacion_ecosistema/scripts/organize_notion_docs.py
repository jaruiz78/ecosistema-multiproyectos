#!/usr/bin/env python3
"""
Arquitectura y especificación formal para organize_notion_docs.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_0_software_engineering/01_arquitectura_hexagonal_ddd_puro.md
- Referencia Académica: Martin (2017) Clean Architecture & DDD Pure Domain Standard
"""
import os
import sys
import json
import time
import urllib.request
import urllib.error

NOTION_TOKEN = os.getenv("NOTION_TOKEN", "")
NOTION_VERSION = "2022-06-28"
WIKI_DB_ID = "379eb64b-620b-8135-a48b-db948408c74e"
STARTER_COMUN_PAGE_ID = "379eb64b-620b-8131-9016-d9388b1b6c12"

def request_notion(endpoint, method="POST", data=None):
    time.sleep(0.35)
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
                time.sleep((attempt + 1) * 3)
                continue
            err_body = e.read().decode('utf-8')
            sys.stderr.write(f"[ERROR {e.code}] {endpoint}: {err_body}\n")
            return None
        except Exception as e:
            time.sleep(1)
    return None

def determine_metadata(rel_path, filename):
    rel_path_lower = rel_path.lower()

    # Estrategia & Referencias
    if "00_estrategia" in rel_path_lower or "gap_analysis" in rel_path_lower:
        return "Estrategia & Referencias", "Ingeniería de Software & DDD", 1
    if "index_master" in rel_path_lower:
        return "Estrategia & Referencias", "Ingeniería de Software & DDD", 2
    if "expansion_verticales" in rel_path_lower:
        return "Estrategia & Referencias", "Ingeniería de Software & DDD", 3
    if "bibliografia" in rel_path_lower:
        return "Estrategia & Referencias", "Ingeniería de Software & DDD", 4

    # Módulo 0
    if "modulo_0_ingenieria_industrial" in rel_path_lower:
        return "Módulo 0: Fundamentos", "Ingeniería Industrial & DES", 10
    if "modulo_0_sistemas_distribuidos" in rel_path_lower:
        order = 20
        if "01_" in filename: order = 21
        elif "02_" in filename: order = 22
        elif "03_" in filename: order = 23
        elif "04_" in filename: order = 24
        elif "05_" in filename: order = 25
        elif "06_" in filename: order = 26
        return "Módulo 0: Fundamentos", "Sistemas Distribuidos & Consenso", order
    if "modulo_0_software_engineering" in rel_path_lower:
        order = 30
        if "00_" in filename: order = 30
        elif "01_" in filename: order = 31
        elif "02_" in filename: order = 32
        elif "03_" in filename: order = 33
        elif "04_" in filename: order = 34
        elif "05_" in filename: order = 35
        elif "06_" in filename: order = 36
        elif "07_" in filename: order = 37
        return "Módulo 0: Fundamentos", "Ingeniería de Software & DDD", order

    # Módulo 1: Java & Spring
    if "modulo_1_backend_java_spring" in rel_path_lower:
        order = 100
        parts = filename.split("_")
        if parts[0].isdigit():
            order = 100 + int(parts[0])
        return "Módulo 1: Java & Spring", "Lenguaje Java", order

    # Módulo 2: Go & Concurrencia
    if "modulo_2_go_y_concurrencia" in rel_path_lower:
        order = 200
        parts = filename.split("_")
        if parts[0].isdigit():
            order = 200 + int(parts[0])
        return "Módulo 2: Go & Concurrencia", "Lenguaje Go", order

    # Módulo 3: Unified Twin & Math
    if "modulo_3_unified_twin_math" in rel_path_lower:
        order = 300
        parts = filename.split("_")
        if parts[0].isdigit():
            order = 300 + int(parts[0])
        area = "Matemáticas & Gemelo Digital"
        if "python" in filename.lower() or "abm" in filename.lower() or "numpy" in filename.lower() or "pypsa" in filename.lower():
            area = "Lenguaje Python"
        return "Módulo 3: Unified Twin & Math", area, order

    # Módulo 4: Frontend & Motores UI
    if "modulo_4_frontend_y_motores_ui" in rel_path_lower:
        order = 400
        parts = filename.split("_")
        if parts[0].isdigit():
            order = 400 + int(parts[0])
        area = "Frontend Web & Performance"
        if "flutter" in filename.lower() or "dart" in filename.lower() or "h3" in filename.lower() or "osrm" in filename.lower():
            area = "Lenguaje Dart & Flutter"
        return "Módulo 4: Frontend & Motores UI", area, order

    # Módulo 5: Cloud-Native & GCP
    if "modulo_5_cloud_native_dbs" in rel_path_lower:
        order = 500
        parts = filename.split("_")
        if parts[0].isdigit():
            order = 500 + int(parts[0])
        return "Módulo 5: Cloud-Native & GCP", "Cloud Native & GCP", order

    # Módulo 6: SRE & Alta Disponibilidad
    if "modulo_6_sre_y_alta_disponibilidad" in rel_path_lower:
        order = 600
        parts = filename.split("_")
        if parts[0].isdigit():
            order = 600 + int(parts[0])
        return "Módulo 6: SRE & Alta Disponibilidad", "SRE & Resiliencia", order

    # Módulo 7: NoSQL & Multi-Tenancy
    if "modulo_7_bases_datos_nosql_multitenant" in rel_path_lower:
        order = 700
        parts = filename.split("_")
        if parts[0].isdigit():
            order = 700 + int(parts[0])
        return "Módulo 7: NoSQL & Multi-Tenancy", "NoSQL & Multi-Tenancy", order

    return "Estrategia & Referencias", "Ingeniería de Software & DDD", 999

def fetch_all_db_pages(db_id):
    pages = []
    has_more = True
    next_cursor = None

    while has_more:
        payload = {}
        if next_cursor:
            payload["start_cursor"] = next_cursor

        res = request_notion(f"databases/{db_id}/query", method="POST", data=payload)
        if not res:
            break

        results = res.get("results", [])
        pages.extend(results)
        has_more = res.get("has_more", False)
        next_cursor = res.get("next_cursor")

    return pages

def main():
    print("=== ORGANIZANDO Y CLASIFICANDO PÁGINAS EN WIKI STARTER COMÚN ===")
    pages = fetch_all_db_pages(WIKI_DB_ID)
    print(f"Total de páginas encontradas en la Wiki: {len(pages)}")

    updated_count = 0
    grouped_pages = {}

    for page in pages:
        page_id = page["id"]
        props = page.get("properties", {})

        # Extraer fichero local
        fichero_text = ""
        if "Fichero Local" in props and props["Fichero Local"].get("rich_text"):
            fichero_text = props["Fichero Local"]["rich_text"][0].get("plain_text", "")

        title = ""
        if "Documento" in props and props["Documento"].get("title"):
            title = props["Documento"]["title"][0].get("plain_text", "")

        filename = os.path.basename(fichero_text) if fichero_text else title
        modulo, area, orden = determine_metadata(fichero_text, filename)

        update_payload = {
            "properties": {
                "Módulo": {"select": {"name": modulo}},
                "Área / Lenguaje": {"select": {"name": area}},
                "Orden": {"number": orden}
            }
        }

        resp = request_notion(f"pages/{page_id}", method="PATCH", data=update_payload)
        if resp:
            updated_count += 1
            grouped_pages.setdefault(modulo, []).append({
                "id": page_id,
                "title": title,
                "area": area,
                "orden": orden,
                "file": fichero_text
            })
            print(f"[{modulo} | {area} | #{orden}] Actualizado: {title}")
        else:
            print(f"[ERROR] No se pudo actualizar {title}")

    print(f"\n=== CLASIFICACIÓN COMPLETADA: {updated_count}/{len(pages)} PÁGINAS CATEGORIZADAS ===")

    # Crear la Página Índice Máster Interactiva en Notion
    create_master_index_page(grouped_pages)

def create_master_index_page(grouped_pages):
    print("\n=== CREANDO PÁGINA 'ÍNDICE MÁSTER - WIKI ECOSISTEMA CORPORATIVO' ===")
    
    blocks = [
        {
            "object": "block",
            "type": "heading_1",
            "heading_1": {"rich_text": [{"type": "text", "text": {"content": "🗺️ ÍNDICE MÁSTER - RUTA DE NAVEGACIÓN Y DOCUMENTACIÓN WIKI"}}]}
        },
        {
            "object": "block",
            "type": "callout",
            "callout": {
                "rich_text": [{"type": "text", "text": {"content": "Esta guía proporciona una estructura interactiva y secuencial para explorar toda la documentación técnica y formativa del Ecosistema Corporativo. Cada sección contiene accesos directos por módulo, lenguaje de programación y orden de aprendizaje recomendado."}}],
                "icon": {"emoji": "📌"}
            }
        }
    ]

    modulos_ordenados = [
        "Estrategia & Referencias",
        "Módulo 0: Fundamentos",
        "Módulo 1: Java & Spring",
        "Módulo 2: Go & Concurrencia",
        "Módulo 3: Unified Twin & Math",
        "Módulo 4: Frontend & Motores UI",
        "Módulo 5: Cloud-Native & GCP",
        "Módulo 6: SRE & Alta Disponibilidad",
        "Módulo 7: NoSQL & Multi-Tenancy"
    ]

    for mod in modulos_ordenados:
        if mod not in grouped_pages: continue
        items = sorted(grouped_pages[mod], key=lambda x: x["orden"])
        
        blocks.append({
            "object": "block",
            "type": "heading_2",
            "heading_2": {"rich_text": [{"type": "text", "text": {"content": f"📘 {mod} ({len(items)} Documentos)"}}]}
        })

        for item in items:
            title = item["title"]
            area = item["area"]
            orden = item["orden"]
            pid = item["id"]
            
            blocks.append({
                "object": "block",
                "type": "bulleted_list_item",
                "bulleted_list_item": {
                    "rich_text": [
                        {"type": "text", "text": {"content": f"#{orden} | [{area}] "}, "annotations": {"bold": True}},
                        {"type": "text", "text": {"content": title, "link": {"url": f"https://notion.so/{pid.replace('-', '')}"}}}
                    ]
                }
            })

    # Subir página principal
    initial_blocks = blocks[:95]
    remaining_blocks = blocks[95:]

    payload = {
        "parent": {"page_id": STARTER_COMUN_PAGE_ID},
        "properties": {
            "title": [{"text": {"content": "[ÍNDICE MÁSTER] Ruta de Aprendizaje y Documentación Wiki"}}]
        },
        "children": initial_blocks
    }

    res = request_notion("pages", method="POST", data=payload)
    if res and "id" in res:
        master_page_id = res["id"]
        print("Índice Máster Creado con Éxito. Page ID:", master_page_id)
        while remaining_blocks:
            batch = remaining_blocks[:95]
            remaining_blocks = remaining_blocks[95:]
            request_notion(f"blocks/{master_page_id}/children", method="PATCH", data={"children": batch})
        print("Índice Máster Completado!")
    else:
        print("[ERROR] No se pudo crear la página Índice Máster.")

if __name__ == "__main__":
    main()
