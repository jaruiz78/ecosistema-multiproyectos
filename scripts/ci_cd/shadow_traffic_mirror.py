import asyncio
import json
import logging
import time
from typing import Dict, Any, Tuple
from http.server import HTTPServer, BaseHTTPRequestHandler
import threading
import urllib.request
import urllib.error

logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")

class ShadowTrafficMetrics:
    def __init__(self):
        self.lock = threading.Lock()
        self.total_requests = 0
        self.primary_latencies = []
        self.shadow_latencies = []
        self.shadow_requests_sent = 0
        self.discrepancies = 0

    def record(self, primary_lat: float, shadow_lat: float, has_discrepancy: bool):
        with self.lock:
            self.total_requests += 1
            self.shadow_requests_sent += 1
            self.primary_latencies.append(primary_lat)
            self.shadow_latencies.append(shadow_lat)
            if has_discrepancy:
                self.discrepancies += 1

    def get_summary(self) -> Dict[str, Any]:
        with self.lock:
            avg_primary = sum(self.primary_latencies) / len(self.primary_latencies) if self.primary_latencies else 0
            avg_shadow = sum(self.shadow_latencies) / len(self.shadow_latencies) if self.shadow_latencies else 0
            summary = {
                "total_requests": self.total_requests,
                "shadow_requests_sent": self.shadow_requests_sent,
                "avg_primary_latency_ms": round(avg_primary * 1000, 2),
                "avg_shadow_latency_ms": round(avg_shadow * 1000, 2),
                "discrepancies": self.discrepancies
            }
            # Guardar auditoría persistente
            try:
                with open("/tmp/shadow_traffic_metrics.json", "w") as f:
                    json.dump(summary, f, indent=2)
            except Exception:
                pass
            return summary

GLOBAL_METRICS = ShadowTrafficMetrics()

class ShadowProxyHandler(BaseHTTPRequestHandler):
    PRIMARY_URL = "http://127.0.0.1:9091"
    SHADOW_URL = "http://127.0.0.1:9092"

    def do_GET(self):
        self._handle_proxy("GET", None)

    def do_POST(self):
        content_length = int(self.headers.get("Content-Length", 0))
        body = self.rfile.read(content_length) if content_length > 0 else None
        self._handle_proxy("POST", body)

    def _handle_proxy(self, method: str, body: bytes):
        start_primary = time.time()
        
        # 1. Enviar solicitud sincrónica al servicio Primario
        prim_status, prim_headers, prim_body = self._forward_request(
            self.PRIMARY_URL + self.path, method, body, dict(self.headers), is_shadow=False
        )
        primary_latency = time.time() - start_primary

        # Responder INMEDIATAMENTE al cliente para no penalizar la latencia
        self.send_response(prim_status)
        for k, v in prim_headers.items():
            if k.lower() not in ["transfer-encoding", "content-length"]:
                self.send_header(k, v)
        self.send_header("Content-Length", str(len(prim_body)))
        self.send_header("X-Proxy-Handled-By", "Antigravity-Shadow-Mirror")
        self.end_headers()
        self.wfile.write(prim_body)

        # 2. Espejear ASÍNCRONAMENTE en segundo plano al servicio Shadow
        threading.Thread(
            target=self._mirror_to_shadow,
            args=(method, self.path, body, dict(self.headers), primary_latency, prim_body),
            daemon=True
        ).start()

    def _mirror_to_shadow(self, method: str, path: str, body: bytes, headers: dict, prim_lat: float, prim_body: bytes):
        start_shadow = time.time()
        shadow_headers = headers.copy()
        shadow_headers["X-Shadow-Mode"] = "true"
        shadow_headers["X-Shadow-Source"] = "Antigravity-Local-Proxy"

        shad_status, shad_headers, shad_body = self._forward_request(
            self.SHADOW_URL + path, method, body, shadow_headers, is_shadow=True
        )
        shadow_latency = time.time() - start_shadow

        has_discrepancy = (prim_body != shad_body)
        GLOBAL_METRICS.record(prim_lat, shadow_latency, has_discrepancy)
        
        logging.info(
            f"👥 [SHADOW MIRROR] Path: {path} | Primary Latency: {prim_lat*1000:.1f}ms | "
            f"Shadow Latency: {shadow_latency*1000:.1f}ms | Shadow Status: {shad_status} | "
            f"Payload Match: {not has_discrepancy}"
        )

    def _forward_request(self, url: str, method: str, body: bytes, headers: dict, is_shadow: bool) -> Tuple[int, dict, bytes]:
        req = urllib.request.Request(url, data=body, headers=headers, method=method)
        try:
            with urllib.request.urlopen(req, timeout=5) as response:
                return response.status, dict(response.headers), response.read()
        except urllib.error.HTTPError as e:
            return e.code, dict(e.headers), e.read()
        except Exception as e:
            err_msg = json.dumps({"error": str(e), "is_shadow": is_shadow}).encode("utf-8")
            return 503, {"Content-Type": "application/json"}, err_msg

    def log_message(self, format, *args):
        # Deshabilitar logs por defecto de BaseHTTPRequestHandler
        pass

class DummyBackendHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        self._respond()

    def do_POST(self):
        self._respond()

    def _respond(self):
        is_shadow = self.headers.get("X-Shadow-Mode") == "true"
        service_name = "SHADOW_SERVICE" if is_shadow else "PRIMARY_SERVICE"
        
        # Simular pequeño delay en procesamiento
        time.sleep(0.01)
        
        response_data = {
            "service": service_name,
            "status": "OK",
            "is_shadow": is_shadow,
            "received_headers": {
                "x-shadow-mode": self.headers.get("X-Shadow-Mode", "false"),
                "x-shadow-source": self.headers.get("X-Shadow-Source", "none")
            }
        }
        
        body = json.dumps(response_data).encode("utf-8")
        self.send_response(200)
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        self.wfile.write(body)

    def log_message(self, format, *args):
        pass

def run_demo_proxy(port=8880, primary_port=9091, shadow_port=9092):
    logging.info("🚀 Iniciando Servidores Backend Simulados (Primario en :9091, Shadow en :9092)...")
    primary_server = HTTPServer(("127.0.0.1", primary_port), DummyBackendHandler)
    shadow_server = HTTPServer(("127.0.0.1", shadow_port), DummyBackendHandler)

    threading.Thread(target=primary_server.serve_forever, daemon=True).start()
    threading.Thread(target=shadow_server.serve_forever, daemon=True).start()

    ShadowProxyHandler.PRIMARY_URL = f"http://127.0.0.1:{primary_port}"
    ShadowProxyHandler.SHADOW_URL = f"http://127.0.0.1:{shadow_port}"

    proxy_server = HTTPServer(("127.0.0.1", port), ShadowProxyHandler)
    logging.info(f"🔄 Proxy de Tráfico Sombra escuchando en http://127.0.0.1:{port}...")
    
    proxy_thread = threading.Thread(target=proxy_server.serve_forever, daemon=True)
    proxy_thread.start()

    return proxy_server, primary_server, shadow_server

if __name__ == "__main__":
    p_srv, prim_srv, shad_srv = run_demo_proxy(port=8880)
    
    logging.info("🧪 Enviando solicitud de prueba a través del Proxy Espejo...")
    req = urllib.request.Request("http://127.0.0.1:8880/api/v1/trips", data=b'{"user_id": 123}', headers={"Content-Type": "application/json"}, method="POST")
    
    start_time = time.time()
    with urllib.request.urlopen(req) as resp:
        body = json.loads(resp.read().decode("utf-8"))
        client_latency = time.time() - start_time
        logging.info(f"📦 Respuesta recibida por el cliente (Latencia: {client_latency*1000:.2f}ms):")
        logging.info(json.dumps(body, indent=2))

    # Esperar brevemente a que el thread del shadow mirror finalice
    time.sleep(0.2)
    summary = GLOBAL_METRICS.get_summary()
    logging.info(f"📊 Resumen de Métrica Shadow: {json.dumps(summary, indent=2)}")

    p_srv.shutdown()
    prim_srv.shutdown()
    shad_srv.shutdown()
