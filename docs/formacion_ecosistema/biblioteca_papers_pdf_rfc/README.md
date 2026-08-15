# 📚 BIBLIOTECA MULTIFORMATO DE PAPERS ACADÉMICOS, RFCS Y FUENTES PRIMARIAS
## *Repositorio Central de Fuentes de Verdad Doctoral & Estándares Internacionales*

---

### 🏛️ 1. Propósito y Estructura

Esta carpeta constituye el **almacén unificado de fuentes primarias** (PDFs, código fuente LaTeX `.tex`, textos planos de RFCs de la IETF, cuadernos Jupyter `.ipynb` y especificaciones técnicas oficiales) que fundamentan la **Universidad Privada del Ecosistema** y alimentan el **Motor de Destilación Feynman** (`scripts/ingest_and_distill_papers_feynman.py`).

```mermaid
mindmap
  root((Biblioteca Multiformato))
    01_software_eng_ddd_tipos["01. Software Engineering & Types (CMU/Stanford)"]
    02_sistemas_distribuidos_consenso["02. Distributed Systems & Consensus (MIT/Berkeley)"]
    03_runtime_jvm_memoria["03. JVM Runtime, Valhalla & Loom (OpenJDK/ETH)"]
    04_concurrencia_go_csp["04. Go Concurrency & Memory (ITMO/Peking)"]
    05_gemelo_digital_tensores_enkf["05. Digital Twin, PEPS & EnKF (Princeton IAS)"]
    06_edge_ai_litert_neurosimbolico["06. Edge AI LiteRT & SMT (MIT/Stanford)"]
    07_cloud_bigquery_finops["07. Cloud Native, BigQuery & FinOps (GCP)"]
    08_industrial_colas_hci["08. Industrial Eng, Queues & HCI (Georgia Tech/Purdue)"]
    09_geoespacial_h3_osrm["09. Geospatial H3 & OSRM (Uber/KIT)"]
    10_fintech_stripe_sagas["10. Fintech, Stripe & Sagas (Stanford/Stripe)"]
    11_identidad_zerotrust_beyondcorp["11. Zero-Trust BeyondCorp & Identity (Google/NIST)"]
    12_supplychain_slsa_gitops["12. Supply Chain SLSA & GitOps (OpenSSF/CNCF)"]
```

---

### 📁 2. Formatos Soportados

| Extensión | Tipo de Documento | Procesador Interno |
| :--- | :--- | :--- |
| **`.pdf`** | Papers de arXiv, IEEE, ACM, VLDB y manuales oficiales | Extracción nativa con `PyMuPDF` (`fitz`). |
| **`.tex` / `.tar.gz`** | Fuentes LaTeX originales de pre-prints de arXiv | Parser de fórmulas matemáticas directas. |
| **`.txt` / `.rfc`** | RFCs de la IETF, estándares W3C y especificaciones ISO | Parser de secciones y gramáticas EBNF. |
| **`.ipynb`** | Jupyter Notebooks de investigación e IA | Extractor de celdas de código y visualizaciones. |
| **`.epub` / `.md`** | Libros técnicos y whitepapers | Extractor semántico estructurado. |

---

### 🏷️ 3. Convención de Nomenclatura y Metadatos

Para que el script automático catalogue el documento con máxima precisión, nombra los archivos con el siguiente estándar:

```text
[AÑO]_[AUTOR_O_INSTITUCION]_[TITULO_CORTO].[ext]
```

* *Ejemplo PDF*: `2014_ongaro_raft_consensus.pdf`
* *Ejemplo RFC*: `2015_ietf_rfc7519_jwt.txt`
* *Ejemplo LaTeX*: `2008_verstraete_peps_tensor_networks.tex`

#### Archivo Opcional de Metadatos (`.meta.json`)
Puedes acompañar cualquier documento con un archivo JSON con el mismo nombre base para forzar metadatos adicionales:

```json
{
  "title": "In Search of an Understandable Consensus Algorithm (Extended Version)",
  "authors": ["Diego Ongaro", "John Ousterhout"],
  "year": 2014,
  "institution": "Stanford University",
  "faculty": "02_sistemas_distribuidos_consenso",
  "doi_or_url": "https://raft.github.io/raft.pdf",
  "target_lesson": "modulo_0_sistemas_distribuidos/03_algoritmos_de_eleccion_de_lider.md"
}
```

---

### 🚀 4. Procesamiento Automático

Una vez subido o descargado un archivo a cualquier subcarpeta, ejecuta:

```bash
# Ingestar y destilar todos los nuevos documentos
python3 /home/jaruiz/Desarrollo/scripts/ingest_and_distill_papers_feynman.py
```


---
## 🧠 Ejercicio Práctico: El Método Feynman

Para garantizar una asimilación profunda de los conceptos presentados en este módulo, aplica el **Método Feynman**:

> **Instrucción:** Explica los conceptos centrales de este módulo como si tu audiencia fuera un estudiante brillante de 12 años que no ha visto nunca este tema. Si no puedes hacerlo con lenguaje sencillo, analogías claras y sin jerga técnica, significa que aún no lo entiendes lo suficientemente bien.

*Inténtalo tú mismo:* Toma el concepto más complejo de este módulo, escríbelo en un papel en blanco y redáctalo usando únicamente términos cotidianos.
