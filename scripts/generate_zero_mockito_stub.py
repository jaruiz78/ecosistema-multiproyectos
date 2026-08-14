"""
Arquitectura y especificación formal para generate_zero_mockito_stub.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-twin-peps-enkf.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_unified_twin_math/10_gemelo_digital_unificado_core.md
- Referencia Académica: Verstraete, Murg, Cirac (2008) PEPS Tensor Networks; Evensen (2003) EnKF
"""
"""
generate_zero_mockito_stub.py
-------------------------------------------------------------------------
Generador de Stubs Herméticos TDD (Zero-Mockito) mediante Dual-Engine AI
(Ollama GPU qwen2.5-coder:7b + NPU Router).
Genera clases in-memory en Java 25 o Go a partir de la interfaz de un puerto DDD.
-------------------------------------------------------------------------
"""
import sys
from ollama_local_bridge import OllamaLocalBridge

def generate_java_stub(interface_name: str, package_name: str):
    bridge = OllamaLocalBridge()
    prompt = f"""
    Write a pure Java 25 in-memory stub implementation for interface '{interface_name}' in package '{package_name}'.
    Follow Zero-Mockito policy. Use a ConcurrentHashMap or ArrayList for in-memory storage.
    Do NOT include Spring, Mockito, or infrastructure annotations.
    Return ONLY valid Java code.
    """
    code, metrics = bridge.generate_completion(prompt, model="qwen2.5-coder:7b", temperature=0.1)
    return code, metrics

if __name__ == "__main__":
    interface_n = sys.argv[1] if len(sys.argv) > 1 else "PaymentGatewayPort"
    pkg_n = sys.argv[2] if len(sys.argv) > 2 else "com.corp.domain.port.out"
    print(f"🚀 Generando Stub Zero-Mockito en Java 25 para '{interface_n}'...")
    stub_code, metrics = generate_java_stub(interface_n, pkg_n)
    print(f"⚡ Métricas de Inferencia ({metrics.get('engine', 'GPU')}): {metrics.get('tokens_generated', 0)} tokens @ {metrics.get('tokens_per_sec', 0.0)} tokens/s ({metrics.get('latency_ms', 0)} ms)")
    print("\n--- STUB JAVA 25 GENERADO EN LOCAL (qwen2.5-coder:7b) ---")
    print(stub_code[:400] + "\n...")
