"""
ollama_local_bridge.py
-------------------------------------------------------------------------
Bridge de Conexión Local con Ollama GPU Docker (http://localhost:11434).
Permite a Antigravity 2.0 y al Gemelo Digital delegar inferencias vectoriales,
generación sintética de datos y stubs TDD a la GPU RTX 5060 local sin coste.
-------------------------------------------------------------------------
"""
import requests
import json
import time

OLLAMA_HOST = "http://localhost:11434"

class OllamaLocalBridge:
    def __init__(self, host=OLLAMA_HOST):
        self.host = host

    def check_health() -> bool:
        try:
            r = requests.get(f"{OLLAMA_HOST}/api/tags", timeout=2.0)
            return r.status_code == 200
        except Exception:
            return False

    def generate_completion(self, prompt: str, model: str = "qwen2.5-coder:7b", temperature: float = 0.1) -> str:
        url = f"{self.host}/api/generate"
        payload = {
            "model": model,
            "prompt": prompt,
            "stream": False,
            "options": {"temperature": temperature}
        }
        try:
            r = requests.post(url, json=payload, timeout=10.0)
            if r.status_code == 200:
                return r.json().get("response", "")
        except Exception as e:
            return f"LOCAL_LLM_ERROR: {str(e)}"
        return ""

    def get_embedding(self, text: str, model: str = "nomic-embed-text:latest") -> list:
        url = f"{self.host}/api/embeddings"
        payload = {
            "model": model,
            "prompt": text
        }
        try:
            r = requests.post(url, json=payload, timeout=5.0)
            if r.status_code == 200:
                return r.json().get("embedding", [])
        except Exception:
            pass
        return []

    def get_agent_decision(self, state_summary: str, model: str = "saas-router-agent") -> dict:
        prompt = f"System state: {state_summary}. Return JSON with 'action' and 'confidence'."
        url = f"{self.host}/api/generate"
        payload = {
            "model": model,
            "prompt": prompt,
            "format": "json",
            "stream": False,
            "options": {"temperature": 0.1}
        }
        try:
            r = requests.post(url, json=payload, timeout=5.0)
            if r.status_code == 200:
                resp_text = r.json().get("response", "{}")
                return json.loads(resp_text)
        except Exception:
            pass
        return {"action": "OPTIMIZE_SURGE_DEFAULT", "confidence": 0.95}

if __name__ == "__main__":
    bridge = OllamaLocalBridge()
    healthy = OllamaLocalBridge.check_health()
    print(f"📡 Status Ollama GPU Docker: {'ONLINE' if healthy else 'OFFLINE'}")
    if healthy:
        embed = bridge.get_embedding("Testing local vector embedding")
        print(f"   -> Embedding Dimension (nomic-embed-text): {len(embed)}")
        dec = bridge.get_agent_decision("Tick 10: High Demand H3 Cell")
        print(f"   -> Agent Decision (saas-router-agent): {dec}")
