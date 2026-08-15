#!/usr/bin/env python3
"""
Auto University RAG Sync & Grounded Knowledge Engine
---------------------------------------------------
Indexador semántico y sincronizador de la Base de Conocimiento de la
Universidad Privada del Ecosistema con los servidores MCP y SQLite.

Capacidades:
1. Indexa todos los módulos formativos de docs/formacion_ecosistema/, ADRs y especificaciones.
2. Genera vector embeddings 768d (nomic-embed-text) acelerados por Lemonade NPU / Ollama GPU.
3. Persiste el índice semántico en simulations_telemetry.db (tabla university_knowledge_nodes).
4. Proporciona búsqueda semántica y grounded Javadoc suggestions para cualquier nuevo proyecto.
"""

import os
import sys
import json
import time
import sqlite3
import argparse
from pathlib import Path
from typing import Dict, List, Any, Optional

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

try:
    from lemonade_npu_bridge import LemonadeNPUBridge
    from ollama_local_bridge import OllamaLocalBridge
except ImportError:
    LemonadeNPUBridge = None
    OllamaLocalBridge = None

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")
DB_PATH = WORKSPACE_ROOT / "simulations_telemetry.db"
DOCS_DIR = WORKSPACE_ROOT / "docs"
ACADEMIC_DIR = DOCS_DIR / "formacion_ecosistema"
ADR_DIR = DOCS_DIR / "adr"

class UniversityKnowledgeEngine:
    def __init__(self):
        self.bridge = OllamaLocalBridge() if OllamaLocalBridge else None
        self.npu_bridge = LemonadeNPUBridge() if LemonadeNPUBridge else None
        self._init_db()

    def _init_db(self):
        try:
            conn = sqlite3.connect(DB_PATH)
            conn.execute("""
            CREATE TABLE IF NOT EXISTS university_knowledge_nodes (
                id TEXT PRIMARY KEY,
                category TEXT,
                title TEXT,
                file_path TEXT,
                summary TEXT,
                academic_benchmark TEXT,
                embedding_json TEXT,
                updated_at REAL
            )
            """)
            conn.commit()
            conn.close()
        except Exception as e:
            print(f"Error inicializando DB de conocimiento: {e}", file=sys.stderr)

    def get_embedding(self, text: str) -> List[float]:
        try:
            import urllib.request
            import json
            
            req = urllib.request.Request(
                "http://localhost:11434/api/embeddings",
                data=json.dumps({"model": "nomic-embed-text", "prompt": text}).encode("utf-8"),
                headers={"Content-Type": "application/json"},
                method="POST"
            )
            with urllib.request.urlopen(req, timeout=10) as response:
                result = json.loads(response.read().decode("utf-8"))
                if "embedding" in result:
                    return result["embedding"]
        except Exception as e:
            print(f"Aviso: Fallo al obtener embedding de Ollama local, usando fallback: {e}", file=sys.stderr)
            
        if self.bridge:
            return self.bridge.get_embedding(text)
        return [0.0] * 768

    def sync_all_modules(self) -> int:
        print("🎓 ==========================================================================")
        print("🎓   SINCRONIZACIÓN Y VECTORIZACIÓN DE LA UNIVERSIDAD PRIVADA DEL ECOSISTEMA")
        print("🎓 ==========================================================================")

        indexed_count = 0
        nodes = []

        # 1. Indexar Módulos Formativos
        if ACADEMIC_DIR.exists():
            for p in ACADEMIC_DIR.rglob("*.md"):
                if p.name.startswith("."):
                    continue
                try:
                    content = p.read_text(encoding="utf-8", errors="ignore")
                    lines = [l.strip() for l in content.split("\n") if l.strip()]
                    title = lines[0].replace("#", "").strip() if lines else p.stem
                    summary = " ".join(lines[1:8]) if len(lines) > 1 else title
                    
                    # Extraer benchmark académico si existe
                    bench = "CMU / MIT / Stanford"
                    if "Berkeley" in content: bench = "UC Berkeley RISELab"
                    elif "Princeton" in content: bench = "Princeton IAS"
                    elif "Loom" in content or "Valhalla" in content: bench = "OpenJDK HotSpot"
                    elif "Industrial" in content: bench = "Georgia Tech / Purdue"

                    node_id = f"module::{p.stem}"
                    nodes.append({
                        "id": node_id,
                        "category": "FACULTAD_CURRICULUM",
                        "title": title,
                        "file_path": str(p.relative_to(WORKSPACE_ROOT)),
                        "summary": summary[:400],
                        "benchmark": bench,
                        "text_for_embed": f"{title} {bench} {summary}"
                    })
                except Exception as e:
                    print(f"Error procesando {p.name}: {e}")

        # 2. Indexar ADRs
        if ADR_DIR.exists():
            for p in ADR_DIR.glob("*.md"):
                try:
                    content = p.read_text(encoding="utf-8", errors="ignore")
                    lines = [l.strip() for l in content.split("\n") if l.strip()]
                    title = lines[0].replace("#", "").strip() if lines else p.stem
                    summary = " ".join(lines[1:5]) if len(lines) > 1 else title

                    node_id = f"adr::{p.stem}"
                    nodes.append({
                        "id": node_id,
                        "category": "ADR_DECISION",
                        "title": title,
                        "file_path": str(p.relative_to(WORKSPACE_ROOT)),
                        "summary": summary[:400],
                        "benchmark": "Consilium Romano Architecture Review",
                        "text_for_embed": f"{title} {summary}"
                    })
                except Exception as e:
                    print(f"Error procesando ADR {p.name}: {e}")

        # 3. Indexar Katas Maestras
        katas_dir = DOCS_DIR / "katas_formacion"
        if katas_dir.exists():
            for p in katas_dir.glob("*.md"):
                try:
                    content = p.read_text(encoding="utf-8", errors="ignore")
                    lines = [l.strip() for l in content.split("\n") if l.strip()]
                    title = lines[0].replace("#", "").strip() if lines else p.stem
                    summary = " ".join(lines[1:6]) if len(lines) > 1 else title

                    node_id = f"kata::{p.stem}"
                    nodes.append({
                        "id": node_id,
                        "category": "KATA_MAESTRA_FEYNMAN",
                        "title": title,
                        "file_path": str(p.relative_to(WORKSPACE_ROOT)),
                        "summary": summary[:400],
                        "benchmark": "Feynman Method / Elite Engineering",
                        "text_for_embed": f"{title} {summary}"
                    })
                except Exception as e:
                    print(f"Error procesando Kata {p.name}: {e}")

        # Guardar y generar embeddings
        print(f"📚 Nodos de conocimiento detectados: {len(nodes)}")
        conn = sqlite3.connect(DB_PATH)
        for n in nodes:
            # Generar vector embedding
            embed = self.get_embedding(n["text_for_embed"])
            conn.execute("""
            INSERT OR REPLACE INTO university_knowledge_nodes 
            (id, category, title, file_path, summary, academic_benchmark, embedding_json, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """, (
                n["id"], n["category"], n["title"], n["file_path"],
                n["summary"], n["benchmark"], json.dumps(embed), time.time()
            ))
            indexed_count += 1
            if indexed_count % 10 == 0:
                print(f"   -> Vectorizados {indexed_count}/{len(nodes)} nodos...")

        conn.commit()
        conn.close()

        print(f"\n✓ Base de Conocimiento sincronizada exitosamente: {indexed_count} nodos indexados.")
        return indexed_count

    def search_theory(self, query: str, top_k: int = 3) -> List[Dict[str, Any]]:
        """Busca en la base de conocimiento universitaria la fundamentación teórica para un concepto."""
        conn = sqlite3.connect(DB_PATH)
        rows = conn.execute("SELECT id, category, title, file_path, summary, academic_benchmark, embedding_json FROM university_knowledge_nodes").fetchall()
        conn.close()

        query_embed = self.get_embedding(query)

        def cosine_similarity(v1, v2):
            if not v1 or not v2 or len(v1) != len(v2): return 0.0
            import math
            dot = sum(x*y for x, y in zip(v1, v2))
            norm1 = math.sqrt(sum(x*x for x in v1))
            norm2 = math.sqrt(sum(x*x for x in v2))
            if norm1 == 0 or norm2 == 0: return 0.0
            return dot / (norm1 * norm2)

        scored = []
        for r in rows:
            try:
                doc_embed = json.loads(r[6]) if r[6] else []
                score = cosine_similarity(query_embed, doc_embed)
            except Exception:
                score = 0.0
            
            # Penalize slightly if embedding fails and fall back to keyword
            if score == 0.0:
                query_words = set(query.lower().split())
                text = f"{r[2]} {r[4]} {r[5]}".lower()
                score = sum(0.1 for w in query_words if len(w) > 3 and w in text)

            if score > 0.1:
                scored.append((score, {
                    "id": r[0],
                    "category": r[1],
                    "title": r[2],
                    "file_path": r[3],
                    "summary": r[4],
                    "benchmark": r[5]
                }))

        scored.sort(key=lambda x: x[0], reverse=True)
        return [item for _, item in scored[:top_k]]

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="University RAG & Knowledge Engine")
    parser.add_argument("--sync", action="store_true", help="Sincroniza e indexa todos los módulos formativos")
    parser.add_argument("--search", type=str, help="Busca fundamentación teórica para un término")

    args = parser.parse_args()
    engine = UniversityKnowledgeEngine()

    if args.search:
        results = engine.search_theory(args.search)
        print(f"🔍 Resultados teóricos para: '{args.search}':")
        for res in results:
            print(f"  • [{res['benchmark']}] {res['title']}")
            print(f"    Archivo: file://{WORKSPACE_ROOT}/{res['file_path']}")
            print(f"    Resumen: {res['summary']}\n")
    else:
        engine.sync_all_modules()
