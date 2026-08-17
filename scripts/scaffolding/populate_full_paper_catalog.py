#!/usr/bin/env python3
"""
Populate Full Canonical Paper Catalog in SQLite Database
-------------------------------------------------------
Lee todos los archivos *.meta.json de docs/formacion_ecosistema/biblioteca_papers_pdf_rfc
e inserta el catálogo completo de las 12 Facultades en simulations_telemetry.db.
"""

import json
import sqlite3
from pathlib import Path

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")
BIBLIO_DIR = WORKSPACE_ROOT / "docs/formacion_ecosistema/biblioteca_papers_pdf_rfc"
DB_PATH = WORKSPACE_ROOT / "data/simulations_telemetry.db"

def main():
    if not BIBLIO_DIR.exists():
        print(f"Directorio no encontrado: {BIBLIO_DIR}")
        return

    conn = sqlite3.connect(DB_PATH)
    cur = conn.cursor()

    cur.execute("DROP TABLE IF EXISTS paper_ingestion_catalog")
    cur.execute("""
        CREATE TABLE paper_ingestion_catalog (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            filename TEXT UNIQUE,
            faculty TEXT,
            title TEXT,
            authors_json TEXT,
            year INTEGER,
            institution TEXT
        )
    """)

    meta_files = list(BIBLIO_DIR.rglob("*.meta.json"))
    print(f"📚 Encontrados {len(meta_files)} archivos de metadatos de papers.")

    inserted = 0

    for mf in sorted(meta_files):
        faculty_dir = mf.parent.name
        try:
            data = json.loads(mf.read_text(encoding="utf-8"))
            filename = data.get("filename", mf.stem.replace(".meta", "") + ".txt")
            title = data.get("title", mf.stem)
            authors = json.dumps(data.get("authors", []))
            year = data.get("year", 2020)
            institution = data.get("institution", data.get("venue", "Canonical Open Source"))

            cur.execute("""
                INSERT OR REPLACE INTO paper_ingestion_catalog (filename, faculty, title, authors_json, year, institution)
                VALUES (?, ?, ?, ?, ?, ?)
            """, (filename, faculty_dir, title, authors, year, institution))
            inserted += 1
        except Exception as e:
            print(f"Error procesando {mf.name}: {e}")

    conn.commit()

    cur.execute("SELECT faculty, count(*) FROM paper_ingestion_catalog GROUP BY faculty")
    rows = cur.fetchall()
    total = sum(r[1] for r in rows)

    print("\n📊 Resumen de Ingesta por Facultades:")
    for f, count in rows:
        print(f"  • {f}: {count} papers")
    print(f"\n🎉 Total: {total} papers canónicos indexados a través de {len(rows)} facultades.")

    conn.close()

if __name__ == "__main__":
    main()
