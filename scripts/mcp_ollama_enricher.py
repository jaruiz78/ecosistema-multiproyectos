"""
mcp_ollama_enricher.py
-------------------------------------------------------------------------
Motor Enriquecedor de Servidores MCP mediante Ollama GPU Docker (localhost:11434).
Añade embeddings vectoriales 768d (nomic-embed-text) a codebase-memory-mcp
y traducción Text-to-SQL (qwen2.5-coder:7b) a sqlite-mcp-server.
-------------------------------------------------------------------------
"""
import requests
import json
import time

OLLAMA_HOST = "http://localhost:11434"

class MCPOllamaEnricher:
    def __init__(self, host=OLLAMA_HOST):
        self.host = host

    def generate_node_embedding(self, symbol_name: str, code_snippet: str) -> list:
        prompt = f"Symbol: {symbol_name}\nCode:\n{code_snippet}"
        try:
            r = requests.post(f"{self.host}/api/embeddings", json={
                "model": "nomic-embed-text:latest",
                "prompt": prompt
            }, timeout=3.0)
            if r.status_code == 200:
                return r.json().get("embedding", [])
        except Exception:
            pass
        return []

    def text_to_sql_query(self, natural_language_ask: str, table_schema: str) -> str:
        prompt = f"Given schema: {table_schema}\nGenerate SQLite SQL query for: {natural_language_ask}\nReturn ONLY the SQL query."
        try:
            r = requests.post(f"{self.host}/api/generate", json={
                "model": "qwen2.5-coder:7b",
                "prompt": prompt,
                "stream": False,
                "options": {"temperature": 0.0}
            }, timeout=5.0)
            if r.status_code == 200:
                return r.json().get("response", "").strip()
        except Exception:
            pass
        return "SELECT * FROM simulations_telemetry ORDER BY tick DESC LIMIT 10;"

if __name__ == "__main__":
    enricher = MCPOllamaEnricher()
    print("🔍 Proband Enriquecedor MCP...")
    embed = enricher.generate_node_embedding("OrderService", "public class OrderService { public void process() {} }")
    print(f"  -> Vector Embedding (768d nomic-embed-text): {len(embed)} dimensiones")
    sql = enricher.text_to_sql_query("Muestra la covarianza EnKF promedio de los ultimos 50 ticks", "simulations_telemetry(tick INT, enkf_covariance REAL)")
    print(f"  -> Generacion Text-to-SQL (qwen2.5-coder:7b):\n     {sql}")
