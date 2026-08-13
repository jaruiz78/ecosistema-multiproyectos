"""
auto_adr_curator.py
-------------------------------------------------------------------------
Curador Autónomo de Decisiones de Arquitectura (ADRs) mediante Ollama GPU (gemma4:26b).
Autogenera documentos Markdown en docs/adr/ a partir de los cambios estructurales.
-------------------------------------------------------------------------
"""
import requests
import os
import time

OLLAMA_HOST = "http://localhost:11434"

def generate_adr(title: str, context: str, decision: str) -> str:
    prompt = f"""
    Title: {title}
    Context: {context}
    Decision: {decision}

    Format a markdown ADR document adhering to Nygard template:
    # ADR: {title}
    ## Context
    ## Decision
    ## Consequences
    Return ONLY valid Markdown.
    """
    try:
        r = requests.post(f"{OLLAMA_HOST}/api/generate", json={
            "model": "gemma4:26b",
            "prompt": prompt,
            "stream": False,
            "options": {"temperature": 0.1}
        }, timeout=12.0)
        if r.status_code == 200:
            return r.json().get("response", "").strip()
    except Exception as e:
        return f"# ADR: {title}\n\nError: {str(e)}"
    return ""

if __name__ == "__main__":
    title = "Hybrid Edge-Cloud AI Execution Loop with Ollama Local GPU"
    context = "Cloud LLM API costs and network latency for high-frequency simulation ticks."
    decision = "Offload synthetic data generation, Zero-Mockito TDD stubs, and RAG embeddings to local RTX 5060 Ollama container."
    
    print(f"📝 Curando Registro ADR autónomo: '{title}'...")
    adr_md = generate_adr(title, context, decision)
    
    adr_path = "/home/jaruiz/Desarrollo/docs/adr/0010-hybrid-local-llm-architecture.md"
    os.makedirs(os.path.dirname(adr_path), exist_ok=True)
    with open(adr_path, "w", encoding="utf-8") as f:
        f.write(adr_md)
        
    print(f"✅ ADR guardado exitosamente en: {adr_path}")
