"""
generate_zero_mockito_stub.py
-------------------------------------------------------------------------
Generador de Stubs Herméticos TDD (Zero-Mockito) mediante Ollama GPU (qwen2.5-coder:7b).
Genera clases in-memory en Java 25 o Go a partir de la interfaz de un puerto DDD.
-------------------------------------------------------------------------
"""
import requests
import sys

OLLAMA_HOST = "http://localhost:11434"

def generate_java_stub(interface_name: str, package_name: str) -> str:
    prompt = f"""
    Write a pure Java 25 in-memory stub implementation for interface '{interface_name}' in package '{package_name}'.
    Follow Zero-Mockito policy. Use a ConcurrentHashMap or ArrayList for in-memory storage.
    Do NOT include Spring, Mockito, or infrastructure annotations.
    Return ONLY valid Java code.
    """
    try:
        r = requests.post(f"{OLLAMA_HOST}/api/generate", json={
            "model": "qwen2.5-coder:7b",
            "prompt": prompt,
            "stream": False,
            "options": {"temperature": 0.1}
        }, timeout=8.0)
        if r.status_code == 200:
            return r.json().get("response", "").strip()
    except Exception as e:
        return f"// Error generating stub: {str(e)}"
    return ""

if __name__ == "__main__":
    interface_n = sys.argv[1] if len(sys.argv) > 1 else "PaymentGatewayPort"
    pkg_n = sys.argv[2] if len(sys.argv) > 2 else "com.corp.domain.port.out"
    print(f"🚀 Generando Stub Zero-Mockito en Java 25 para '{interface_n}'...")
    stub_code = generate_java_stub(interface_n, pkg_n)
    print("\n--- STUB JAVA 25 GENERADO EN LOCAL (qwen2.5-coder:7b) ---")
    print(stub_code[:400] + "\n...")
