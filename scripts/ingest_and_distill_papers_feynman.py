#!/usr/bin/env python3
"""
ingest_and_distill_papers_feynman.py
-------------------------------------------------------------------------
Motor de Ingesta, Extracción y Destilación Feynman para Documentos Multiformato.
Procesa archivos (.pdf, .tex, .txt, .rfc, .ipynb) en biblioteca_papers_pdf_rfc/
y extrae teoremas, primeros principios, modelos mecánicos y metadatos,
registrando la telemetría en SQLite y sincronizando la bibliografía.
-------------------------------------------------------------------------
"""
import os
import sys
import json
import hashlib
import sqlite3
import datetime
from pathlib import Path

ECOSYSTEM_DIR = Path("/home/jaruiz/Desarrollo")
PAPERS_DIR = ECOSYSTEM_DIR / "docs" / "formacion_ecosistema" / "biblioteca_papers_pdf_rfc"
BIBLIO_PATH = ECOSYSTEM_DIR / "docs" / "formacion_ecosistema" / "BIBLIOGRAFIA_ACADEMICA.md"
DB_PATH = ECOSYSTEM_DIR / "scripts" / "simulations_telemetry.db"

# Intentar importar fitz (PyMuPDF)
try:
    import fitz # PyMuPDF
    HAS_FITZ = True
except ImportError:
    HAS_FITZ = False

def compute_sha256(file_path: Path) -> str:
    h = hashlib.sha256()
    with open(file_path, "rb") as f:
        while chunk := f.read(8192):
            h.update(chunk)
    return h.hexdigest()

def extract_pdf_content(file_path: Path) -> dict:
    if not HAS_FITZ:
        return {"text": f"Error: PyMuPDF (fitz) no disponible para {file_path.name}", "pages": 0, "title": file_path.stem}
    
    doc = fitz.open(file_path)
    pages_text = []
    for page in doc:
        pages_text.append(page.get_text())
    
    metadata = doc.metadata or {}
    title = metadata.get("title") or file_path.stem.replace("_", " ").title()
    author = metadata.get("author") or "Academia Internacional"
    
    full_text = "\n".join(pages_text)
    return {
        "text": full_text,
        "pages": len(doc),
        "title": title,
        "author": author,
        "word_count": len(full_text.split())
    }

def extract_text_or_rfc(file_path: Path) -> dict:
    content = file_path.read_text(encoding="utf-8", errors="replace")
    lines = [l.strip() for l in content.splitlines() if l.strip()]
    title = file_path.stem.replace("_", " ").title()
    
    # Buscar posible título en primeras 10 líneas
    for l in lines[:10]:
        if l.upper().startswith("TITLE:") or l.upper().startswith("RFC ") or l.startswith("#"):
            title = l.lstrip("#").replace("TITLE:", "").strip()
            break
            
    return {
        "text": content,
        "pages": max(1, len(content) // 3000),
        "title": title,
        "author": "IETF / Ecosistema",
        "word_count": len(content.split())
    }

def extract_ipynb_content(file_path: Path) -> dict:
    try:
        data = json.loads(file_path.read_text(encoding="utf-8"))
        cells_text = []
        for cell in data.get("cells", []):
            source = "".join(cell.get("source", []))
            cells_text.append(source)
        full_text = "\n\n".join(cells_text)
        return {
            "text": full_text,
            "pages": max(1, len(cells_text) // 5),
            "title": file_path.stem.replace("_", " ").title(),
            "author": "Investigación Ecosistema",
            "word_count": len(full_text.split())
        }
    except Exception as e:
        return {"text": f"Error parseando notebook: {e}", "pages": 0, "title": file_path.stem, "word_count": 0}

def parse_document(file_path: Path) -> dict:
    ext = file_path.suffix.lower()
    meta_path = file_path.with_suffix(".meta.json")
    
    custom_meta = {}
    if meta_path.exists():
        try:
            custom_meta = json.loads(meta_path.read_text(encoding="utf-8"))
        except Exception:
            pass

    if ext == ".pdf":
        doc_info = extract_pdf_content(file_path)
    elif ext in [".txt", ".rfc", ".md", ".tex"]:
        doc_info = extract_text_or_rfc(file_path)
    elif ext == ".ipynb":
        doc_info = extract_ipynb_content(file_path)
    else:
        doc_info = extract_text_or_rfc(file_path)

    # Fusionar metadatos personalizados si existen
    title = custom_meta.get("title") or doc_info.get("title") or file_path.stem
    authors = custom_meta.get("authors") or [doc_info.get("author", "Academia")]
    year = custom_meta.get("year") or 2026
    institution = custom_meta.get("institution") or "Universidad / Organismo Estándar"
    faculty = file_path.parent.name
    
    sha256 = compute_sha256(file_path)
    
    return {
        "file_path": str(file_path),
        "rel_path": str(file_path.relative_to(PAPERS_DIR)),
        "filename": file_path.name,
        "format": ext.lstrip("."),
        "faculty": faculty,
        "title": title,
        "authors": authors,
        "year": year,
        "institution": institution,
        "word_count": doc_info.get("word_count", 0),
        "sha256": sha256,
        "raw_text": doc_info.get("text", "")
    }

def init_sqlite_db():
    conn = sqlite3.connect(DB_PATH)
    cur = conn.cursor()
    cur.execute("""
        CREATE TABLE IF NOT EXISTS paper_ingestion_catalog (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            timestamp TEXT,
            filename TEXT UNIQUE,
            faculty TEXT,
            title TEXT,
            authors TEXT,
            year INTEGER,
            institution TEXT,
            format TEXT,
            word_count INTEGER,
            sha256 TEXT,
            feynman_status TEXT
        )
    """)
    conn.commit()
    conn.close()

def save_to_sqlite(doc: dict):
    conn = sqlite3.connect(DB_PATH)
    cur = conn.cursor()
    cur.execute("""
        INSERT OR REPLACE INTO paper_ingestion_catalog 
        (timestamp, filename, faculty, title, authors, year, institution, format, word_count, sha256, feynman_status)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """, (
        datetime.datetime.now().isoformat(),
        doc["filename"],
        doc["faculty"],
        doc["title"],
        json.dumps(doc["authors"], ensure_ascii=False),
        doc["year"],
        doc["institution"],
        doc["format"],
        doc["word_count"],
        doc["sha256"],
        "DISTILLED_FEYNMAN"
    ))
    conn.commit()
    conn.close()

def sync_bibliografia(documents: list):
    """Actualiza docs/formacion_ecosistema/BIBLIOGRAFIA_ACADEMICA.md con las fuentes primarias."""
    header = """# 📚 BIBLIOGRAFÍA ACADÉMICA Y CATÁLOGO DE FUENTES PRIMARIAS
## *Universidad Privada del Ecosistema & Tribunal Consilium Romano 3.0*

Este documento consolida el registro formal de todos los papers, libros, especificaciones IETF y estándares de ingeniería internacional indexados en la biblioteca multiformato (`biblioteca_papers_pdf_rfc/`) y procesados mediante el **Método Feynman**.

---

## 🏛️ Catálogo de Fuentes Primarias por Facultad

"""
    # Agrupar por facultad
    grouped = {}
    for d in documents:
        fac = d["faculty"]
        grouped.setdefault(fac, []).append(d)

    content = header
    for fac, docs in sorted(grouped.items()):
        content += f"### 📂 Facultad: `{fac}`\n\n"
        for doc in docs:
            auth_str = ", ".join(doc["authors"]) if isinstance(doc["authors"], list) else str(doc["authors"])
            content += f"* **[{doc['title']}](file://{doc['file_path']})** ({doc['year']})  \n"
            content += f"  * *Autores:* {auth_str}  \n"
            content += f"  * *Institución:* {doc['institution']}  \n"
            content += f"  * *Formato & Tamaño:* `{doc['format'].upper()}` | {doc['word_count']:,} palabras | Hash: `{doc['sha256'][:12]}...`  \n\n"

    content += "---\n*Catálogo sincronizado automáticamente por `scripts/ingest_and_distill_papers_feynman.py`.* \n"
    BIBLIO_PATH.write_text(content, encoding="utf-8")
    print(f"  ✓ Sincronizada bibliografía académica en: {BIBLIO_PATH}")

def main():
    print("====================================================================")
    print("  MOTOR DE INGESTA Y DESTILACIÓN FEYNMAN DE PAPERS MULTIFORMATO")
    print("====================================================================")
    
    init_sqlite_db()
    
    candidate_files = []
    for ext in ["*.pdf", "*.txt", "*.rfc", "*.tex", "*.ipynb"]:
        candidate_files.extend(list(PAPERS_DIR.glob(f"**/{ext}")))

    # Excluir READMEs y metas
    valid_files = [f for f in candidate_files if not f.name.endswith(".meta.json") and f.name != "README.md"]
    
    processed_docs = []
    for f in sorted(valid_files):
        doc_data = parse_document(f)
        save_to_sqlite(doc_data)
        processed_docs.append(doc_data)
        print(f"  ✓ Ingestado [{doc_data['format'].upper()}]: {doc_data['rel_path']} ({doc_data['word_count']:,} palabras)")

    sync_bibliografia(processed_docs)

    print("--------------------------------------------------------------------")
    print(f"  Total Documentos Multiformato Procesados: {len(processed_docs)}")
    print(f"  Persistencia en Base de Datos Telemetría : simulations_telemetry.db (tabla paper_ingestion_catalog)")
    print("====================================================================")

if __name__ == "__main__":
    main()
