# ADR 010: Arquitectura de Inferencia Híbrida Edge-Cloud con Modelos Locales (NPU/GPU) y Consilium Romano

## Estado
Aprobado (Consilium Romano)

## Contexto
El ecosistema requería capacidad de razonamiento agéntico, validación dialéctica pre-merge y enriquecimiento semántico continuo con un coste operativo marginal de \$0.00 USD/mes, evitando latencias de red impredecibles y riesgos de filtración de datos propietarios a proveedores externos en tiempo de desarrollo.

## Decisión
1. Adoptar una **arquitectura de inferencia híbrida y desacoplada**:
   - **Capa Local Primaria (NPU/GPU):** Ejecución de SLMs especializados vía Lemonade NPU Bridge (puerto 8000) y Ollama Local (puerto 11434) utilizando modelos de inferencia determinista (`deepseek-r1:8b`, `qwen2.5-coder:7b`, `gemma4:12b`, `nomic-embed-text`).
   - **Fallback Simbólico Determinista:** Si los daemons de aceleración local no están disponibles, el motor cae inmediatamente en un analizador AST neuro-simbólico de tiempo constante \(\mathcal{O}(1)\) sin interrumpir el flujo de compilación ni el ciclo SDLC.
2. Integrar el tribunal dialéctico **Consilium Romano 3.0** en el pipeline de validación pre-merge (`scripts/consilium_romano_tribunal.py`) con persistencia telemétrica en `data/simulations_telemetry.db`.
3. Utilizar embeddings locales de 768 dimensiones para el motor RAG de la Universidad Privada (`scripts/auto_university_rag_sync.py`).

## Consecuencias
* **Positivas:**
  - Coste marginal de inferencia agéntica de \$0.00 USD/mes.
  - Cero dependencias de APIs externas para la validación de código y compliance de arquitectura.
  - Trazabilidad y auditoría persistida en SQLite directamente consultable por MCPs.
* **Negativas:**
  - Requiere hardware local con acelerador (GPU CUDA / NPU) para máxima velocidad de inferencia, aunque mitigado por el fallback determinista.