"""
Arquitectura y especificación formal para mcp_ollama_enricher.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-twin-peps-enkf.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_unified_twin_math/10_gemelo_digital_unificado_core.md
- Referencia Académica: Verstraete, Murg, Cirac (2008) PEPS Tensor Networks; Evensen (2003) EnKF
"""
"""
mcp_ollama_enricher.py
-------------------------------------------------------------------------
Motor Enriquecedor de Servidores MCP mediante Arquitectura Dual-Engine AI
(Lemonade NPU + Ollama GPU).
Añade embeddings vectoriales 768d (nomic-embed-text en NPU/GPU) a codebase-memory-mcp
y traducción Text-to-SQL (qwen2.5-coder:7b en GPU) a sqlite-mcp-server.
-------------------------------------------------------------------------
"""
from typing import List
from ollama_local_bridge import OllamaLocalBridge

class MCPOllamaEnricher:
    def __init__(self):
        self.bridge = OllamaLocalBridge()

    def generate_node_embedding(self, symbol_name: str, code_snippet: str) -> List[float]:
        prompt = f"Symbol: {symbol_name}\nCode:\n{code_snippet}"
        return self.bridge.get_embedding(prompt, model="nomic-embed-text:latest")

    def text_to_sql_query(self, natural_language_ask: str, table_schema: str) -> str:
        prompt = f"Given schema: {table_schema}\nGenerate SQLite SQL query for: {natural_language_ask}\nReturn ONLY the SQL query."
        response, _ = self.bridge.generate_completion(prompt, model="qwen2.5-coder:7b", temperature=0.0)
        return response.strip() if response else "SELECT * FROM simulations_telemetry ORDER BY tick DESC LIMIT 10;"

if __name__ == "__main__":
    enricher = MCPOllamaEnricher()
    print("🔍 Probando Enriquecedor MCP Dual-Engine (NPU+GPU)...")
    embed = enricher.generate_node_embedding("OrderService", "public class OrderService { public void process() {} }")
    print(f"  -> Vector Embedding (768d nomic-embed-text): {len(embed)} dimensiones")
    sql = enricher.text_to_sql_query("Muestra la covarianza EnKF promedio de los ultimos 50 ticks", "simulations_telemetry(tick INT, enkf_covariance REAL)")
    print(f"  -> Generación Text-to-SQL (qwen2.5-coder:7b):\n     {sql}")
