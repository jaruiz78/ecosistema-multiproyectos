# 🏛️ Facultad V: Gemelo Digital Unificado, Física & Matemáticas
## *Cátedra de Fuentes Primarias (Princeton IAS / Caltech)*

---

### 📖 1. Descripción de la Cátedra
Papers de Verstraete/Cirac (PEPS Tensor Networks), Evensen (EnKF Data Assimilation), Raissi (PINNs) y Navier-Stokes/Saint-Venant.

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


---
## 🧠 Ejercicio Práctico: El Método Feynman

Para garantizar una asimilación profunda de los conceptos presentados en este módulo, aplica el **Método Feynman**:

> **Instrucción:** Explica los conceptos centrales de este módulo como si tu audiencia fuera un estudiante brillante de 12 años que no ha visto nunca este tema. Si no puedes hacerlo con lenguaje sencillo, analogías claras y sin jerga técnica, significa que aún no lo entiendes lo suficientemente bien.

*Inténtalo tú mismo:* Toma el concepto más complejo de este módulo, escríbelo en un papel en blanco y redáctalo usando únicamente términos cotidianos.
