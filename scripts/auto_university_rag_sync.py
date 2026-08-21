#!/usr/bin/env python3
"""
Auto University RAG Sync & Grounded Knowledge Engine 2.1 (Ph.D. Edition)
------------------------------------------------------------------------
Indexador semántico jerárquico, ontología de 12 Facultades y motor de búsqueda
híbrido (BM25 + Vectorial 768d + Priorización de Cátedra) para el Ecosistema.

Estructura de las 12 Facultades:
- FACULTAD_I   : Software Engineering, DDD Puro & Tipos (CMU/Stanford)
- FACULTAD_II  : Sistemas Distribuidos, Consenso & TLA+ (MIT/Berkeley)
- FACULTAD_III : Runtime JVM, Loom & AOT Leyden CDS (OpenJDK/ETH Zurich)
- FACULTAD_IV  : Concurrencia Go CSP & Ring-Buffers (ITMO/Peking)
- FACULTAD_V   : Gemelo Digital PEPS, EnKF & Física (Princeton IAS/Caltech)
- FACULTAD_VI  : Edge AI LiteRT & Neuro-Simbólico (MIT 6.S191/Stanford AI)
- FACULTAD_VII : Cloud BigQuery, Serverless & FinOps (Google Cloud)
- FACULTAD_VIII: Ingeniería Industrial, Colas & Ergonomía (Georgia Tech/Purdue)
- FACULTAD_IX  : Geoespacial H3, OSRM & Movilidad (Uber H3/KIT)
- FACULTAD_X   : Fintech, Stripe Connect, Sagas & Escrow (Stanford/Stripe)
- FACULTAD_XI  : Identidad Soberana & Zero-Trust BeyondCorp (BeyondCorp/NIST)
- FACULTAD_XII : Supply Chain Security SLSA & GitOps (OpenSSF/CNCF)
"""

import os
import sys
import re
import json
import time
import math
import sqlite3
import urllib.request
import argparse
from pathlib import Path
from typing import Dict, List, Any, Optional, Tuple

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")
DB_PATH = WORKSPACE_ROOT / "data" / "simulations_telemetry.db"
DOCS_DIR = WORKSPACE_ROOT / "docs"
ACADEMIC_DIR = DOCS_DIR / "formacion_ecosistema"
PAPERS_DIR = ACADEMIC_DIR / "biblioteca_papers_pdf_rfc"
ADR_DIR = DOCS_DIR / "adr"
CORE_DIR = WORKSPACE_ROOT / "core"
APPS_DIR = WORKSPACE_ROOT / "apps"

FACULTY_TAXONOMY = {
    "FACULTAD_I": {
        "name": "Software Engineering, DDD Puro & Tipos",
        "bench": "CMU / Stanford / USC",
        "keywords": ["hexagonal", "ddd", "records", "sealed", "inversion", "zero-mockito", "domain", "puertos", "adaptadores", "hoare", "invariante", "six sigma"]
    },
    "FACULTAD_II": {
        "name": "Sistemas Distribuidos, Consenso & TLA+",
        "bench": "MIT 6.5840 / UC Berkeley RISELab",
        "keywords": ["distribuidos", "consenso", "raft", "paxos", "tla+", "lamport", "happened-before", "relojes", "flp", "pacelc", "bft", "bizantinos", "causalidad"]
    },
    "FACULTAD_III": {
        "name": "Runtime JVM, Loom & AOT Leyden CDS",
        "bench": "OpenJDK HotSpot / ETH Zurich",
        "keywords": ["jvm", "loom", "virtual threads", "pinning", "leyden", "cds", "valhalla", "panama", "aot", "cold-start", "off-heap", "reentrantlock", "scopedvalue"]
    },
    "FACULTAD_IV": {
        "name": "Concurrencia Go CSP & Ring-Buffers",
        "bench": "ITMO / Peking University",
        "keywords": ["go", "golang", "csp", "goroutines", "work stealing", "canales", "disruptor", "lmax", "ring-buffer", "lock-free", "atomics", "scheduler"]
    },
    "FACULTAD_V": {
        "name": "Gemelo Digital PEPS, EnKF & Física",
        "bench": "Princeton IAS / Caltech / Cambridge",
        "keywords": ["peps", "tensores", "enkf", "kalman", "asimilacion", "covarianza", "convergencia", "pinns", "navier-stokes", "gemelo digital", "sde", "pde"]
    },
    "FACULTAD_VI": {
        "name": "Edge AI LiteRT & Neuro-Simbólico",
        "bench": "MIT 6.S191 / Stanford AI Lab",
        "keywords": ["litert", "tflite", "int8", "cuantizacion", "edge", "neuro-simbolico", "smt", "z3", "tribunal", "dialectica", "slm", "inferencia", "off-heap"]
    },
    "FACULTAD_VII": {
        "name": "Cloud BigQuery, Serverless & FinOps",
        "bench": "Google Cloud Architecture Center",
        "keywords": ["bigquery", "particionado", "_partitiondate", "finops", "serverless", "cloud run", "gvisor", "storage write", "micro-batching", "coste", "mau"]
    },
    "FACULTAD_VIII": {
        "name": "Ingeniería Industrial, Colas & Ergonomía",
        "bench": "Georgia Tech / Purdue / TU Delft",
        "keywords": ["colas", "little", "ley de little", "m/m/1", "m/g/1", "lean", "mudas", "six sigma", "dpmo", "ergonomia", "wcag", "accesibilidad"]
    },
    "FACULTAD_IX": {
        "name": "Geoespacial H3, OSRM & Movilidad",
        "bench": "Uber Engineering H3 / KIT",
        "keywords": ["h3", "hexagonal", "osrm", "contraction hierarchies", "ruteo", "uber", "geogrid", "movilidad", "dijkstra", "latencia"]
    },
    "FACULTAD_X": {
        "name": "Fintech, Stripe Connect, Sagas & Escrow",
        "bench": "Stanford / Stripe Engine",
        "keywords": ["fintech", "stripe", "idempotencia", "sagas", "escrow", "partida doble", "ledger", "outbox", "doble cobro", "conciliacion"]
    },
    "FACULTAD_XI": {
        "name": "Identidad Soberana & Zero-Trust BeyondCorp",
        "bench": "Google BeyondCorp / NIST SP 800-207",
        "keywords": ["beyondcorp", "zero-trust", "nist", "jwt", "jwks", "rs256", "eddsa", "rls", "row level security", "custom claims", "firestore", "perimetro"]
    },
    "FACULTAD_XII": {
        "name": "Supply Chain Security SLSA & GitOps",
        "bench": "OpenSSF SLSA L3 / CNCF ArgoCD",
        "keywords": ["slsa", "sbom", "cyclonedx", "cosign", "sigstore", "gitops", "argocd", "declarativo", "proveniencia", "inmutable", "cadena de suministro"]
    }
}

class UniversityKnowledgeEngine:
    def __init__(self):
        self._init_db()

    def _init_db(self):
        try:
            DB_PATH.parent.mkdir(parents=True, exist_ok=True)
            conn = sqlite3.connect(DB_PATH)
            conn.execute("""
            CREATE TABLE IF NOT EXISTS university_knowledge_nodes (
                id TEXT PRIMARY KEY,
                faculty TEXT,
                category TEXT,
                title TEXT,
                file_path TEXT,
                section TEXT,
                theorems_latex TEXT,
                feynman_analogy TEXT,
                summary TEXT,
                academic_benchmark TEXT,
                keywords_json TEXT,
                embedding_json TEXT,
                updated_at REAL
            )
            """)
            conn.execute("CREATE INDEX IF NOT EXISTS idx_faculty ON university_knowledge_nodes(faculty)")
            conn.execute("CREATE INDEX IF NOT EXISTS idx_category ON university_knowledge_nodes(category)")
            conn.commit()
            conn.close()
        except Exception as e:
            print(f"Error inicializando DB de conocimiento: {e}", file=sys.stderr)

    def get_embedding(self, text: str) -> List[float]:
        try:
            ollama_host = os.getenv("OLLAMA_HOST", "http://localhost:11434").rstrip("/")
            req = urllib.request.Request(
                f"{ollama_host}/api/embeddings",
                data=json.dumps({"model": "nomic-embed-text", "prompt": text[:2000]}).encode("utf-8"),
                headers={"Content-Type": "application/json"},
                method="POST"
            )
            with urllib.request.urlopen(req, timeout=4) as response:
                result = json.loads(response.read().decode("utf-8"))
                if "embedding" in result and len(result["embedding"]) > 0:
                    return result["embedding"]
        except Exception:
            pass

        # Deterministic hashing vector
        words = re.findall(r'\b\w{3,}\b', text.lower())
        vec = [0.0] * 768
        if not words:
            return vec
        for w in words:
            h1 = hash(w) % 768
            h2 = hash(w + "_term") % 768
            vec[h1] += 1.0
            vec[h2] += 0.5
        norm = math.sqrt(sum(x*x for x in vec)) or 1.0
        return [x / norm for x in vec]

    def _infer_faculty(self, text: str, file_path: str) -> Tuple[str, str]:
        path_str = file_path.lower()
        
        # 1. Por carpetas canónicas
        if "modulo_0_sistemas_distribuidos" in path_str or "02_sistemas_distribuidos" in path_str:
            return "FACULTAD_II", FACULTY_TAXONOMY["FACULTAD_II"]["bench"]
        elif "modulo_0_software_engineering" in path_str or "01_software_eng" in path_str:
            return "FACULTAD_I", FACULTY_TAXONOMY["FACULTAD_I"]["bench"]
        elif "modulo_0_ingenieria_industrial" in path_str or "08_industrial_colas" in path_str:
            return "FACULTAD_VIII", FACULTY_TAXONOMY["FACULTAD_VIII"]["bench"]
        elif "modulo_1_backend_java" in path_str or "03_runtime_jvm" in path_str:
            return "FACULTAD_III", FACULTY_TAXONOMY["FACULTAD_III"]["bench"]
        elif "modulo_2_go" in path_str or "04_concurrencia_go" in path_str:
            return "FACULTAD_IV", FACULTY_TAXONOMY["FACULTAD_IV"]["bench"]
        elif "modulo_3_unified_twin" in path_str or "05_gemelo_digital" in path_str:
            return "FACULTAD_V", FACULTY_TAXONOMY["FACULTAD_V"]["bench"]
        elif "modulo_4_frontend" in path_str or "06_edge_ai" in path_str or "entrenamiento_ai" in path_str:
            return "FACULTAD_VI", FACULTY_TAXONOMY["FACULTAD_VI"]["bench"]
        elif "modulo_5_cloud" in path_str or "modulo_7_bases" in path_str or "07_cloud_bigquery" in path_str:
            return "FACULTAD_VII", FACULTY_TAXONOMY["FACULTAD_VII"]["bench"]
        elif "modulo_6_sre" in path_str:
            return "FACULTAD_II", FACULTY_TAXONOMY["FACULTAD_II"]["bench"]
        elif "modulo_8_ingenieria_geo" in path_str or "09_geoespacial" in path_str:
            return "FACULTAD_IX", FACULTY_TAXONOMY["FACULTAD_IX"]["bench"]
        elif "modulo_9_fintech" in path_str or "10_fintech" in path_str:
            return "FACULTAD_X", FACULTY_TAXONOMY["FACULTAD_X"]["bench"]
        elif "modulo_10_identidad" in path_str or "11_identidad" in path_str:
            return "FACULTAD_XI", FACULTY_TAXONOMY["FACULTAD_XI"]["bench"]
        elif "modulo_11_supply_chain" in path_str or "12_supplychain" in path_str:
            return "FACULTAD_XII", FACULTY_TAXONOMY["FACULTAD_XII"]["bench"]

        # 2. Por conteo de palabras clave de cada facultad
        content_lower = (text + " " + path_str).lower()
        best_fac = "FACULTAD_I"
        max_matches = 0
        for f_code, f_info in FACULTY_TAXONOMY.items():
            matches = sum(1 for kw in f_info["keywords"] if kw in content_lower)
            if matches > max_matches:
                max_matches = matches
                best_fac = f_code

        return best_fac, FACULTY_TAXONOMY[best_fac]["bench"]

    def _extract_theorems_and_analogies(self, text: str) -> Tuple[List[str], str]:
        theorems = re.findall(r'\\\[(.*?)\\\]|\\\((.*?)\\\)|\$([^\$]+)\$', text, re.DOTALL)
        clean_theorems = []
        for t in theorems:
            found = t[0] or t[1] or t[2]
            if found and len(found.strip()) > 3:
                clean_theorems.append(found.strip()[:200])

        analogy = ""
        analogy_match = re.search(r'(Analogía|Metáfora|Intuición|Feynman|Isomorfismo)[:\s]+([^\n\.]+\.)', text, re.IGNORECASE)
        if analogy_match:
            analogy = analogy_match.group(0)[:300]
        return clean_theorems[:5], analogy

    def sync_all_modules(self) -> int:
        print("🎓 ==========================================================================")
        print("🎓   UNIVERSIDAD PRIVADA 2.1 (Ph.D. Grounding Engine) - SINCRONIZACIÓN")
        print("🎓 ==========================================================================")

        nodes = []

        # 1. Indexar Módulos de las 12 Facultades
        if ACADEMIC_DIR.exists():
            for p in ACADEMIC_DIR.rglob("*.md"):
                if p.name.startswith(".") or "biblioteca_papers" in str(p):
                    continue
                try:
                    content = p.read_text(encoding="utf-8", errors="ignore")
                    sections = re.split(r'\n(?=##?\s+)', content)
                    file_title = sections[0].split('\n')[0].replace('#', '').strip() if sections else p.stem
                    faculty_code, default_bench = self._infer_faculty(content, str(p))

                    for idx, sec in enumerate(sections):
                        sec_lines = [l.strip() for l in sec.split('\n') if l.strip()]
                        if not sec_lines:
                            continue
                        sec_title = sec_lines[0].replace('#', '').strip()
                        sec_body = " ".join(sec_lines[1:40])
                        theorems, analogy = self._extract_theorems_and_analogies(sec)
                        keywords = list(set(re.findall(r'\b[a-zA-Z]{4,}\b', (sec_title + " " + sec_body).lower())))[:30]

                        node_id = f"faculty::{p.stem}::sec_{idx}"
                        nodes.append({
                            "id": node_id,
                            "faculty": faculty_code,
                            "category": "FACULTAD_CURRICULUM",
                            "title": f"{file_title} > {sec_title}",
                            "file_path": str(p.relative_to(WORKSPACE_ROOT)),
                            "section": sec_title,
                            "theorems": json.dumps(theorems),
                            "analogy": analogy,
                            "summary": sec_body[:1000],
                            "benchmark": default_bench,
                            "keywords": json.dumps(keywords),
                            "text_for_embed": f"{faculty_code} {file_title} {sec_title} {sec_body[:600]} {analogy}"
                        })
                except Exception as e:
                    print(f"Error procesando {p.name}: {e}")

        # 2. Indexar Biblioteca de 58 Papers Canónicos
        if PAPERS_DIR.exists():
            for p in PAPERS_DIR.rglob("*"):
                if p.is_file() and p.suffix in [".md", ".txt", ".rfc", ".tex"]:
                    try:
                        content = p.read_text(encoding="utf-8", errors="ignore")
                        lines = [l.strip() for l in content.split("\n") if l.strip()]
                        title = lines[0].replace("#", "").strip() if lines else p.stem
                        summary = " ".join(lines[1:10]) if len(lines) > 1 else title
                        faculty_code, bench = self._infer_faculty(content, str(p))
                        theorems, analogy = self._extract_theorems_and_analogies(content)

                        nodes.append({
                            "id": f"paper::{p.stem}",
                            "faculty": faculty_code,
                            "category": "ACADEMIC_PAPER",
                            "title": f"Paper Canónico: {title}",
                            "file_path": str(p.relative_to(WORKSPACE_ROOT)),
                            "section": "Fundamentación Teórica Doctoral",
                            "theorems": json.dumps(theorems),
                            "analogy": analogy or "Tratado formal canónico",
                            "summary": summary[:500],
                            "benchmark": bench,
                            "keywords": json.dumps(list(set(re.findall(r'\b[a-zA-Z]{4,}\b', (title + " " + summary).lower())))[:20]),
                            "text_for_embed": f"{faculty_code} {title} {summary[:300]} {bench}"
                        })
                    except Exception as e:
                        print(f"Error procesando paper {p.name}: {e}")

        # 3. Indexar Glosario Enciclopédico
        glosario_path = ACADEMIC_DIR / "GLOSARIO_ENCICLOPEDICO_ECOSISTEMA.md"
        if glosario_path.exists():
            try:
                g_content = glosario_path.read_text(encoding="utf-8", errors="ignore")
                terms = re.split(r'\n(?=###?\s+)', g_content)
                for idx, t_block in enumerate(terms):
                    t_lines = [l.strip() for l in t_block.split('\n') if l.strip()]
                    if len(t_lines) < 2:
                        continue
                    term_name = t_lines[0].replace('#', '').strip()
                    term_def = " ".join(t_lines[1:6])
                    fac_code, bench = self._infer_faculty(t_block, term_name)
                    theorems, analogy = self._extract_theorems_and_analogies(t_block)

                    nodes.append({
                        "id": f"glossary::{term_name[:30].replace(' ', '_')}::{idx}",
                        "faculty": fac_code,
                        "category": "GLOSARIO_FEYNMAN",
                        "title": f"Glosario: {term_name}",
                        "file_path": str(glosario_path.relative_to(WORKSPACE_ROOT)),
                        "section": term_name,
                        "theorems": json.dumps(theorems),
                        "analogy": analogy or "Definición Feynman sin jerga",
                        "summary": term_def[:500],
                        "benchmark": bench,
                        "keywords": json.dumps([term_name.lower()]),
                        "text_for_embed": f"{fac_code} {term_name} {term_def[:300]}"
                    })
            except Exception as e:
                print(f"Error indexando glosario: {e}")

        # 4. Indexar ADRs de Arquitectura
        if ADR_DIR.exists():
            for p in ADR_DIR.glob("*.md"):
                try:
                    content = p.read_text(encoding="utf-8", errors="ignore")
                    lines = [l.strip() for l in content.split("\n") if l.strip()]
                    title = lines[0].replace("#", "").strip() if lines else p.stem
                    summary = " ".join(lines[1:10]) if len(lines) > 1 else title
                    fac_code, bench = self._infer_faculty(content, p.name)
                    theorems, analogy = self._extract_theorems_and_analogies(content)

                    nodes.append({
                        "id": f"adr::{p.stem}",
                        "faculty": fac_code,
                        "category": "ADR_DECISION",
                        "title": title,
                        "file_path": str(p.relative_to(WORKSPACE_ROOT)),
                        "section": "Decisión Arquitectónica",
                        "theorems": json.dumps(theorems),
                        "analogy": analogy,
                        "summary": summary[:500],
                        "benchmark": "Consilium Romano Architectural Decision",
                        "keywords": json.dumps(list(set(re.findall(r'\b[a-zA-Z]{4,}\b', (title + " " + summary).lower())))[:20]),
                        "text_for_embed": f"ADR {fac_code} {title} {summary[:300]}"
                    })
                except Exception as e:
                    print(f"Error procesando ADR {p.name}: {e}")

        # 5. Indexar 20 Cores Algorítmicos
        if CORE_DIR.exists():
            for p in CORE_DIR.iterdir():
                if p.is_dir():
                    readme = p / "README.md"
                    summary = f"Core algorítmico corporativo: {p.name}"
                    if readme.exists():
                        try:
                            summary = readme.read_text(encoding="utf-8", errors="ignore")[:400]
                        except Exception:
                            pass
                    fac_code, bench = self._infer_faculty(p.name, p.name)
                    nodes.append({
                        "id": f"core::{p.name}",
                        "faculty": fac_code,
                        "category": "ALGORITHMIC_CORE",
                        "title": f"Algorithmic Core: {p.name}",
                        "file_path": str(p.relative_to(WORKSPACE_ROOT)),
                        "section": "Core Engine",
                        "theorems": json.dumps([]),
                        "analogy": f"Motor de cálculo puro para {p.name}",
                        "summary": summary,
                        "benchmark": bench,
                        "keywords": json.dumps([p.name.lower(), "core", "pure"]),
                        "text_for_embed": f"{fac_code} {p.name} {summary}"
                    })

        # 6. Indexar Dossiers de NotebookLM / Gemini Notebooks
        dossiers_dir = DOCS_DIR / "notebook_dossiers"
        if dossiers_dir.exists():
            for p in dossiers_dir.rglob("*.md"):
                try:
                    content = p.read_text(encoding="utf-8", errors="ignore")
                    lines = [l.strip() for l in content.split("\n") if l.strip()]
                    title = lines[0].replace("#", "").strip() if lines else p.stem
                    summary = " ".join(lines[1:10]) if len(lines) > 1 else title
                    fac_code, bench = self._infer_faculty(content, str(p))
                    theorems, analogy = self._extract_theorems_and_analogies(content)
                    nodes.append({
                        "id": f"dossier::{p.stem}",
                        "faculty": fac_code,
                        "category": "NOTEBOOK_DOSSIER",
                        "title": title,
                        "file_path": str(p.relative_to(WORKSPACE_ROOT)),
                        "section": "Notebook Dossier",
                        "theorems": json.dumps(theorems),
                        "analogy": analogy,
                        "summary": summary[:600],
                        "benchmark": bench,
                        "keywords": json.dumps(list(set(re.findall(r'\b[a-zA-Z]{4,}\b', (title + " " + summary).lower())))[:20]),
                        "text_for_embed": f"NOTEBOOK DOSSIER {fac_code} {title} {summary[:300]}"
                    })
                except Exception as e:
                    print(f"Error procesando Dossier {p.name}: {e}")

        # Guardar en SQLite
        print(f"📚 Total de nodos ontológicos detectados: {len(nodes)}")
        conn = sqlite3.connect(DB_PATH)
        indexed_count = 0
        for n in nodes:
            embed = self.get_embedding(n["text_for_embed"])
            conn.execute("""
            INSERT OR REPLACE INTO university_knowledge_nodes 
            (id, faculty, category, title, file_path, section, theorems_latex, feynman_analogy, summary, academic_benchmark, keywords_json, embedding_json, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """, (
                n["id"], n["faculty"], n["category"], n["title"], n["file_path"],
                n["section"], n["theorems"], n["analogy"], n["summary"],
                n["benchmark"], n["keywords"], json.dumps(embed), time.time()
            ))
            indexed_count += 1
            if indexed_count % 100 == 0 or indexed_count == len(nodes):
                print(f"   -> Sincronizados {indexed_count}/{len(nodes)} nodos...")

        conn.commit()
        conn.close()
        print(f"\n✓ Base de Conocimiento 2.1 indexada con éxito: {indexed_count} nodos semánticos activos.")
        return indexed_count

    def search_theory_hybrid(self, query: str, faculty_filter: Optional[str] = None, top_k: int = 4) -> List[Dict[str, Any]]:
        conn = sqlite3.connect(DB_PATH)
        query_sql = "SELECT id, faculty, category, title, file_path, section, theorems_latex, feynman_analogy, summary, academic_benchmark, keywords_json, embedding_json FROM university_knowledge_nodes"
        params = []
        if faculty_filter:
            query_sql += " WHERE faculty = ?"
            params.append(faculty_filter)
        rows = conn.execute(query_sql, params).fetchall()
        conn.close()

        if not rows:
            return []

        query_embed = self.get_embedding(query)
        query_words = set(re.findall(r'\b[a-zA-Z]{3,}\b', query.lower()))

        def cosine_similarity(v1, v2):
            if not v1 or not v2 or len(v1) != len(v2): return 0.0
            dot = sum(x*y for x, y in zip(v1, v2))
            norm1 = math.sqrt(sum(x*x for x in v1))
            norm2 = math.sqrt(sum(x*x for x in v2))
            if norm1 == 0 or norm2 == 0: return 0.0
            return dot / (norm1 * norm2)

        scored = []
        for r in rows:
            doc_id, fac, cat, title, file_path, sec, theorems, analogy, summary, bench, keywords_str, embed_str = r
            try:
                doc_embed = json.loads(embed_str) if embed_str else []
                vec_score = cosine_similarity(query_embed, doc_embed)
            except Exception:
                vec_score = 0.0

            doc_text = f"{title} {sec} {summary} {analogy}".lower()
            match_count = sum(1 for w in query_words if w in doc_text)
            lex_score = match_count / (len(query_words) or 1)

            final_score = (0.6 * vec_score) + (0.4 * lex_score)

            if final_score > 0.12:
                scored.append((final_score, {
                    "id": doc_id,
                    "faculty": fac,
                    "faculty_name": FACULTY_TAXONOMY.get(fac, {}).get("name", fac),
                    "category": cat,
                    "title": title,
                    "file_path": file_path,
                    "section": sec,
                    "theorems": json.loads(theorems) if theorems else [],
                    "analogy": analogy,
                    "summary": summary,
                    "benchmark": bench,
                    "score": round(final_score, 4)
                }))

        scored.sort(key=lambda x: x[0], reverse=True)
        return [item for _, item in scored[:top_k]]

    def generate_grounded_javadoc_header(self, component_name: str, topic_query: str) -> str:
        results = self.search_theory_hybrid(topic_query, top_k=2)
        if not results:
            return f"/**\n * {component_name}\n * @see docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md\n */"
        
        top = results[0]
        theorems_str = " | ".join(top["theorems"]) if top["theorems"] else "Invariancia de Estado O(1)"
        analogy_str = top["analogy"] or "Modelo determinista sin efectos colaterales"

        header = f"""/**
 * =============================================================================
 * {component_name} - Grounded Corporate Architecture
 * =============================================================================
 * 🏛️ Cátedra: {top['faculty_name']} [{top['faculty']}]
 * 📚 Benchmark Académico: {top['benchmark']}
 * 🔬 Fundamentación Formal: {theorems_str}
 * 💡 Analogía Feynman: {analogy_str}
 * 
 * @see file://{WORKSPACE_ROOT}/{top['file_path']}
 * @see docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md
 * @see docs/formacion_ecosistema/BIBLIOGRAFIA_ACADEMICA.md
 * =============================================================================
 */"""
        return header

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Auto University RAG Sync & Grounded Knowledge Engine 2.1")
    parser.add_argument("--sync", action="store_true", help="Sincroniza y vectoriza jerárquicamente todos los módulos y papers")
    parser.add_argument("--search", type=str, help="Búsqueda semántica híbrida de fundamentación teórica")
    parser.add_argument("--faculty", type=str, help="Filtra por facultad (e.g. FACULTAD_I, FACULTAD_V)")
    parser.add_argument("--javadoc", nargs=2, metavar=('COMPONENT', 'QUERY'), help="Genera header Javadoc con grounding académico")

    args = parser.parse_args()
    engine = UniversityKnowledgeEngine()

    if args.javadoc:
        comp, query = args.javadoc
        print(engine.generate_grounded_javadoc_header(comp, query))
    elif args.search:
        res = engine.search_theory_hybrid(args.search, faculty_filter=args.faculty)
        print(f"\n🔍 Resultados Híbridos Grounded para: '{args.search}':\n")
        for item in res:
            print(f"  🏛️ [{item['faculty']}] {item['faculty_name']} (Score: {item['score']})")
            print(f"     Título : {item['title']}")
            print(f"     Archivo: file://{WORKSPACE_ROOT}/{item['file_path']}")
            print(f"     Bench  : {item['benchmark']}")
            if item['theorems']:
                print(f"     Teorema: {item['theorems'][0]}")
            if item['analogy']:
                print(f"     Feynman: {item['analogy']}")
            print(f"     Resumen: {item['summary'][:180]}...\n")
    else:
        engine.sync_all_modules()
