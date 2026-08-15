#!/usr/bin/env python3
"""
setup_paper_library_scaffolding.py
-------------------------------------------------------------------------
Crea la estructura de carpetas de las 12 Facultades en la Biblioteca de Papers
junto con sus README.md explicativos de subida de fuentes multiformato.
-------------------------------------------------------------------------
"""
import os
from pathlib import Path

BASE_DIR = Path("/home/jaruiz/Desarrollo/docs/formacion_ecosistema/biblioteca_papers_pdf_rfc")

FACULTIES = [
    ("01_software_eng_ddd_tipos", "Facultad I: Ingeniería de Software, DDD Puro & Teoría de Tipos", "CMU / Stanford", "Papers sobre Hexagonal Architecture, Type Theory, Algebraic Data Types, TDD y Formal Verification (Coq/Isabelle)."),
    ("02_sistemas_distribuidos_consenso", "Facultad II: Sistemas Distribuidos, Consenso & TLA+", "MIT 6.5840 / UC Berkeley", "Papers de Lamport (Paxos/Relojes), Ongaro (Raft), Fischer-Lynch-Paterson (FLP), Castro-Liskov (PBFT) y especificaciones TLA+."),
    ("03_runtime_jvm_memoria", "Facultad III: Runtime JVM, Compilación AOT & Memoria", "OpenJDK / ETH Zurich", "JEPs de OpenJDK (Valhalla, Loom, Leyden, Panama), Papers de Garbage Collection (ZGC, C4) y Java Memory Model (JMM)."),
    ("04_concurrencia_go_csp", "Facultad IV: Concurrencia Go, Runtime CSP & Algoritmia", "ITMO / Peking University", "Papers de Hoare (CSP), Planificador Go M:N work-stealing, Lock-Free Ring Buffers (LMAX Disruptor) y análisis de escapes."),
    ("05_gemelo_digital_tensores_enkf", "Facultad V: Gemelo Digital Unificado, Física & Matemáticas", "Princeton IAS / Caltech", "Papers de Verstraete/Cirac (PEPS Tensor Networks), Evensen (EnKF Data Assimilation), Raissi (PINNs) y Navier-Stokes/Saint-Venant."),
    ("06_edge_ai_litert_neurosimbolico", "Facultad VI: IA Híbrida, Edge AI & Neuro-Simbólico", "MIT 6.S191 / Stanford AI", "Papers de cuantización INT8, LiteRT/TFLite, SMT Solvers (Z3, CVC5), HNSW Vector Indexing y Dual-Engine Architecture."),
    ("07_cloud_bigquery_finops", "Facultad VII: Cloud-Native, Big Data & FinOps", "Google Cloud Architecture", "Papers de Google Dremel/Capacitor, Spanner TrueTime, Borg/Kubernetes, Knative Serverless y Cloud FinOps."),
    ("08_industrial_colas_hci", "Facultad VIII: Ingeniería Industrial, Colas & Ergonomía (HCI)", "Georgia Tech / Purdue", "Papers de John Little (Little's Law L=λW), Pollaczek-Khinchine, Ohno (Toyota Production System / Lean) y Ley de Fitts."),
    ("09_geoespacial_h3_osrm", "Facultad IX: Ingeniería Geoespacial & Movilidad", "Uber Engineering / KIT", "Papers de Isaac Brodsky (Uber H3 Discrete Global Grid), Geisberger (Contraction Hierarchies) y Algoritmos de Subastas de Movilidad."),
    ("10_fintech_stripe_sagas", "Facultad X: Fintech, Pagos & Sagas", "Stanford / Stripe", "Papers de Hector Garcia-Molina (Sagas), Patrón Transactional Outbox, Idempotencia Transaccional y Double-Entry Bookkeeping."),
    ("11_identidad_zerotrust_beyondcorp", "Facultad XI: Identidad, Criptografía & Zero-Trust", "BeyondCorp / NIST", "RFC 7519 (JWT), RFC 8725 (JWT BCP), RFC 6749/7636 (OAuth 2.1 PKCE), NIST SP 800-207 y Papers de BeyondCorp."),
    ("12_supplychain_slsa_gitops", "Facultad XII: Seguridad de Cadena de Suministro & GitOps", "OpenSSF / CNCF", "Especificaciones SLSA v1.0, Sigstore/Cosign Architecture, CycloneDX/SPDX SBOMs y GitOps Reconciliation (ArgoCD).")
]

def main():
    BASE_DIR.mkdir(parents=True, exist_ok=True)
    for folder_name, title, benchmark, desc in FACULTIES:
        folder_path = BASE_DIR / folder_name
        folder_path.mkdir(parents=True, exist_ok=True)
        readme_path = folder_path / "README.md"
        
        content = f"""# 🏛️ {title}
## *Cátedra de Fuentes Primarias ({benchmark})*

---

### 📖 1. Descripción de la Cátedra
{desc}

---

### 📂 2. Archivos Aceptados en esta Carpeta
* **Papers Académicos (`.pdf`)**: Descargas de arXiv, IEEE Xplore, ACM, VLDB.
* **Fuentes LaTeX (`.tex`)**: Pre-prints descargados de arXiv (código fuente para fórmulas matemáticas).
* **Especificaciones y RFCs (`.txt`, `.rfc`)**: Documentos oficiales de la IETF, W3C, ISO o NIST.
* **Cuadernos de Investigación (`.ipynb`)**: Notebooks de simulación y reproducibilidad empírica.

---

### 🏷️ 3. Formato de Nombre Sugerido
```text
[AÑO]_[AUTOR_O_INSTITUCION]_[TITULO_CORTO].[ext]
```
*Ejemplo:* `2014_ongaro_raft_consensus.pdf`

---

### 🤖 4. Destilación Automática Feynman
Al colocar un archivo aquí y ejecutar `python3 scripts/ingest_and_distill_papers_feynman.py`, el sistema:
1. Extrae el texto y las fórmulas mediante PyMuPDF (`fitz`).
2. Utiliza las LLMs locales (`deepseek-r1:8b` y `qwen2.5-coder:7b`) para destilar los teoremas clave.
3. Genera o actualiza la lección correspondiente en `docs/formacion_ecosistema/` bajo el formato Feynman de 5 capas.
4. Registra la proveniencia en `simulations_telemetry.db`.
"""
        readme_path.write_text(content, encoding="utf-8")
        print(f"  ✓ Creada carpeta y README: {folder_name}")

if __name__ == "__main__":
    main()
