"""
Arquitectura y especificación formal para ollama_local_bridge.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-twin-peps-enkf.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_unified_twin_math/10_gemelo_digital_unificado_core.md
- Referencia Académica: Verstraete, Murg, Cirac (2008) PEPS Tensor Networks; Evensen (2003) EnKF
"""
"""
ollama_local_bridge.py
-------------------------------------------------------------------------
Bridge Híbrido Dual-Engine (Ollama GPU Docker + Lemonade NPU Server).
Permite a Antigravity 2.0 y al Gemelo Digital delegar inferencias vectoriales
y micro-decisiones a la NPU (Lemonade) mientras reserva la GPU (Ollama)
exclusivamente para la generación de código y razonamiento complejo.
-------------------------------------------------------------------------
"""
import requests
import json
import time
from typing import Dict, List, Any, Tuple
from concurrent.futures import ThreadPoolExecutor
from lemonade_npu_bridge import LemonadeNPUBridge

OLLAMA_HOST = "http://localhost:11434"

class OllamaLocalBridge:
    def __init__(self, host: str = OLLAMA_HOST):
        self.host = host
        self.npu_bridge = LemonadeNPUBridge()
        self.npu_available = self.npu_bridge.check_health()

    @staticmethod
    def check_health() -> bool:
        try:
            r = requests.get(f"{OLLAMA_HOST}/api/tags", timeout=2.0)
            return r.status_code == 200
        except Exception:
            return False

    def generate_completion(self, prompt: str, model: str = "qwen2.5-coder:7b", temperature: float = 0.1) -> Tuple[str, Dict[str, Any]]:
        """Genera código/razonamiento en Ollama GPU con métricas de tokens/s."""
        url = f"{self.host}/api/generate"
        payload = {
            "model": model,
            "prompt": prompt,
            "stream": False,
            "options": {"temperature": temperature}
        }
        t0 = time.time()
        try:
            r = requests.post(url, json=payload, timeout=10.0)
            elapsed = time.time() - t0
            if r.status_code == 200:
                resp_json = r.json()
                response_text = resp_json.get("response", "")
                eval_count = resp_json.get("eval_count", len(response_text.split()))
                eval_duration_ns = resp_json.get("eval_duration", int(elapsed * 1e9))
                tokens_per_sec = (eval_count / (eval_duration_ns / 1e9)) if eval_duration_ns > 0 else 0.0
                
                metrics = {
                    "engine": "GPU_OLLAMA",
                    "latency_ms": round(elapsed * 1000, 2),
                    "tokens_generated": eval_count,
                    "tokens_per_sec": round(tokens_per_sec, 2)
                }
                return response_text, metrics
        except Exception as e:
            return f"LOCAL_LLM_ERROR: {str(e)}", {"engine": "ERROR", "latency_ms": 0, "tokens_generated": 0, "tokens_per_sec": 0.0}
        return "", {"engine": "GPU_OLLAMA", "latency_ms": 0, "tokens_generated": 0, "tokens_per_sec": 0.0}

    def get_embedding(self, text: str, model: str = "nomic-embed-text:latest") -> List[float]:
        """Genera embeddings ruteando a NPU (Lemonade) con fallback a GPU (Ollama)."""
        if self.npu_available:
            npu_model = model.replace(":latest", "")
            embed = self.npu_bridge.get_embedding(text, model=npu_model)
            if embed:
                return embed
        
        # Fallback transparente a Ollama GPU
        url = f"{self.host}/api/embeddings"
        payload = {
            "model": model,
            "prompt": text
        }
        try:
            r = requests.post(url, json=payload, timeout=10.0)
            if r.status_code == 200:
                return r.json().get("embedding", [])
        except Exception:
            pass
        return []

    def get_agent_decision(self, state_summary: str, model: str = "saas-router-agent") -> Dict[str, Any]:
        """Efectúa micro-decisiones ruteando a NPU (Lemonade) con fallback a GPU (Ollama)."""
        if self.npu_available:
            decision = self.npu_bridge.get_agent_decision(state_summary, model=model)
            if decision and decision.get("engine") != "NPU_FALLBACK":
                decision["engine"] = "NPU_LEMONADE"
                return decision

        # Fallback transparente a Ollama GPU
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
                res = json.loads(resp_text)
                res["engine"] = "GPU_OLLAMA"
                return res
        except Exception:
            pass
        return {"action": "OPTIMIZE_SURGE_DEFAULT", "confidence": 0.95, "engine": "GPU_FALLBACK"}

    def run_concurrent_npu_gpu_task(self, embedding_text: str, code_prompt: str) -> Dict[str, Any]:
        """Dispara en paralelo la vectorización RAG en NPU y la generación de código en GPU."""
        t0 = time.time()
        with ThreadPoolExecutor(max_workers=2) as executor:
            future_embed = executor.submit(self.get_embedding, embedding_text)
            future_code = executor.submit(self.generate_completion, code_prompt)
            
            embed_res = future_embed.result()
            code_res, code_metrics = future_code.result()

        total_elapsed = time.time() - t0
        return {
            "total_elapsed_ms": round(total_elapsed * 1000, 2),
            "embedding_dims": len(embed_res),
            "code_metrics": code_metrics,
            "parallel_execution": True
        }

if __name__ == "__main__":
    bridge = OllamaLocalBridge()
    gpu_healthy = OllamaLocalBridge.check_health()
    npu_healthy = bridge.npu_available
    
    print(f"📡 Status Ollama GPU Docker: {'ONLINE' if gpu_healthy else 'OFFLINE'}")
    print(f"🍋 Status Lemonade NPU Server: {'ONLINE' if npu_healthy else 'OFFLINE (Fallback a GPU Activo)'}")
    
    if gpu_healthy or npu_healthy:
        embed = bridge.get_embedding("Testing local vector embedding")
        print(f"   -> Embedding Dimension (nomic-embed-text): {len(embed)}")
        dec = bridge.get_agent_decision("Tick 10: High Demand H3 Cell")
        print(f"   -> Agent Decision ({dec.get('engine', 'GPU')}): {dec}")
        comp, metrics = bridge.generate_completion("Write a simple Java 25 record for User(String id, String name)")
        print(f"   -> Code Generation ({metrics['engine']}): {metrics['tokens_generated']} tokens @ {metrics['tokens_per_sec']} tokens/s ({metrics['latency_ms']} ms)")
        
        print("\n⚡ Probando Ejecución Concurrente en Paralelo (NPU + GPU)...")
        par_res = bridge.run_concurrent_npu_gpu_task(
            "Vectorize this documentation paragraph for codebase RAG",
            "Write a Java 25 interface OrderPort with method void saveOrder(String id)"
        )
        print(f"   -> Parallel Task Completed: {par_res['total_elapsed_ms']} ms | Code: {par_res['code_metrics']['tokens_generated']} tokens @ {par_res['code_metrics']['tokens_per_sec']} tokens/s")
