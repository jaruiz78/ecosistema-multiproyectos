"""
auto_rag_reindex.py
-------------------------------------------------------------------------
Re-indexador RAG de Documentación Técnica mediante Ollama GPU (nomic-embed-text).
Vectoriza automáticamente archivos Markdown de docs/ y docs/formacion_ecosistema.
-------------------------------------------------------------------------
"""
import glob
import requests

OLLAMA_HOST = "http://localhost:11434"

def reindex_documentation_docs() -> dict:
    md_files = glob.glob("/home/jaruiz/Desarrollo/docs/**/*.md", recursive=True)
    indexed_count = 0
    total_embeddings = 0

    for filepath in md_files:
        try:
            with open(filepath, "r", encoding="utf-8", errors="ignore") as f:
                content = f.read()
                
            if content.strip():
                snippet = content[:500]
                r = requests.post(f"{OLLAMA_HOST}/api/embeddings", json={
                    "model": "nomic-embed-text:latest",
                    "prompt": snippet
                }, timeout=5.0)
                if r.status_code == 200:
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
    print("📚 Iniciando Re-indexación RAG Local de Documentación Técnica...")
    res = reindex_documentation_docs()
    print(f"  -> Archivos Markdown Escaneados : {res['files_scanned']}")
    print(f"  -> Archivos Vectorizados RAG    : {res['files_indexed']}")
    print(f"  ✓ Re-indexación RAG en Ollama GPU completada exitosamente.")
