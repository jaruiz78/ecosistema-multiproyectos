"""
auto_adr_curator.py
-------------------------------------------------------------------------
Curador Autónomo de Decisiones de Arquitectura (ADRs) mediante Dual-Engine AI
(Ollama GPU qwen2.5-coder:7b + NPU Router).
Autogenera documentos Markdown en docs/adr/ a partir de los cambios estructurales.
-------------------------------------------------------------------------
"""
import os
from ollama_local_bridge import OllamaLocalBridge

def generate_adr(title: str, context: str, decision: str) -> str:
    bridge = OllamaLocalBridge()
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
    code, metrics = bridge.generate_completion(prompt, model="qwen2.5-coder:7b", temperature=0.1)
    return code.strip() if code else f"# ADR: {title}\n\n## Context\n{context}\n\n## Decision\n{decision}"

if __name__ == "__main__":
    title = "Hybrid Dual-Engine NPU-GPU Execution Loop with Lemonade and Ollama"
    context = "VRAM contention and latency optimization when performing continuous vector embeddings and high-frequency code generation."
    decision = "Offload vector embeddings to Lemonade NPU and code generation/stubs to GPU RTX 5060 Ollama container."
    
    print(f"📝 Curando Registro ADR autónomo: '{title}'...")
    adr_md = generate_adr(title, context, decision)
    
    adr_path = "/home/jaruiz/Desarrollo/docs/adr/0010-hybrid-local-llm-architecture.md"
    os.makedirs(os.path.dirname(adr_path), exist_ok=True)
    with open(adr_path, "w", encoding="utf-8") as f:
        f.write(adr_md)
        
    print(f"✅ ADR guardado exitosamente en: {adr_path}")
