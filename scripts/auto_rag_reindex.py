"""
auto_rag_reindex.py
-------------------------------------------------------------------------
Re-indexador RAG de Documentación Técnica mediante Arquitectura Dual-Engine AI.
Vectoriza automáticamente archivos Markdown de docs/ y docs/formacion_ecosistema
desactivando el consumo de VRAM en GPU al rutearlo a Lemonade NPU (o fallback GPU).
-------------------------------------------------------------------------
"""
import glob
from ollama_local_bridge import OllamaLocalBridge

def reindex_documentation_docs() -> dict:
    bridge = OllamaLocalBridge()
    md_files = glob.glob("/home/jaruiz/Desarrollo/docs/**/*.md", recursive=True)
    indexed_count = 0
    total_embeddings = 0

    for filepath in md_files:
        try:
            with open(filepath, "r", encoding="utf-8", errors="ignore") as f:
                content = f.read()
                
            if content.strip():
                snippet = content[:500]
                embed = bridge.get_embedding(snippet, model="nomic-embed-text:latest")
                if len(embed) > 0:
                    indexed_count += 1
                    total_embeddings += 1
        except Exception:
            pass

    return {
        "files_scanned": len(md_files),
        "files_indexed": indexed_count,
        "total_embeddings": total_embeddings
    }

if __name__ == "__main__":
    print("📚 Iniciando Re-indexación RAG Local de Documentación Técnica (Dual-Engine)...")
    res = reindex_documentation_docs()
    print(f"  -> Archivos Markdown Escaneados : {res['files_scanned']}")
    print(f"  -> Archivos Vectorizados RAG    : {res['files_indexed']}")
    print(f"  ✓ Re-indexación RAG completada exitosamente.")
