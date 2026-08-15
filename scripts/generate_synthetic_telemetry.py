#!/usr/bin/env python3
"""
Generador de Telemetría Sintética (Zero-Cost Local Reality)
-----------------------------------------------------------
Emite eventos JSON de alta frecuencia (movilidad, sensores IoT) hacia
el emulador local de Pub/Sub para forzar el procesamiento concurrente real 
en los workers de Go y probar los Ring-Buffers.

Uso:
  python3 generate_synthetic_telemetry.py --topic <topic-name> --rate 1000
"""

import os
import json
import time
import uuid
import random
import argparse
import threading
from typing import Dict, Any

# Simulamos el cliente de PubSub con una librería estándar HTTP (para no forzar a tener google-cloud-pubsub en local para el test rápido, 
# aunque en PRO usaríamos el SDK oficial). El Emulador PubSub soporta la API REST de Google.
import urllib.request
import urllib.parse

PUBSUB_EMULATOR_HOST = os.environ.get("PUBSUB_EMULATOR_HOST", "localhost:8085")
PROJECT_ID = "local-ecosystem-project"

def create_topic_if_not_exists(topic_name: str):
    url = f"http://{PUBSUB_EMULATOR_HOST}/v1/projects/{PROJECT_ID}/topics/{topic_name}"
    req = urllib.request.Request(url, method="PUT", data=b"{}")
    try:
        urllib.request.urlopen(req)
        print(f"Topic {topic_name} creado en el emulador.")
    except Exception as e:
        # Ignore if it already exists
        pass

def publish_messages(topic_name: str, messages: list[Dict[str, Any]]):
    url = f"http://{PUBSUB_EMULATOR_HOST}/v1/projects/{PROJECT_ID}/topics/{topic_name}:publish"
    
    payload = {
        "messages": [
            {
                "data": urllib.parse.quote(json.dumps(msg)).encode("utf-8").hex(), # Base64/Hex encoding simplified for emulator
                "attributes": {"source": "synthetic-generator"}
            } for msg in messages
        ]
    }
    
    req = urllib.request.Request(
        url,
        data=json.dumps(payload).encode("utf-8"),
        headers={"Content-Type": "application/json"},
        method="POST"
    )
    
    try:
        urllib.request.urlopen(req)
    except Exception as e:
        print(f"Error publicando: {e}")

def generate_telemetry_payload() -> Dict[str, Any]:
    return {
        "device_id": f"sensor-{random.randint(1000, 9999)}",
        "timestamp": time.time(),
        "lat": 40.4168 + random.uniform(-0.05, 0.05),
        "lon": -3.7038 + random.uniform(-0.05, 0.05),
        "temperature": random.gauss(20.0, 5.0),
        "battery_level": random.uniform(10.0, 100.0)
    }

def worker(topic_name: str, batch_size: int, rate: float):
    delay = 1.0 / (rate / batch_size) if rate > 0 else 0
    while True:
        batch = [generate_telemetry_payload() for _ in range(batch_size)]
        publish_messages(topic_name, batch)
        time.sleep(delay)

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--topic", type=str, default="telemetry-events")
    parser.add_argument("--rate", type=int, default=100, help="Eventos por segundo")
    parser.add_argument("--workers", type=int, default=4, help="Número de hilos de publicación")
    
    args = parser.parse_args()
    
    print(f"🔥 Iniciando Ingesta Sintética Local hacia {PUBSUB_EMULATOR_HOST}")
    print(f"   Topic: {args.topic}")
    print(f"   Rate esperado: {args.rate} msg/sec usando {args.workers} workers")
    
    create_topic_if_not_exists(args.topic)
    
    batch_size = max(1, args.rate // args.workers)
    
    threads = []
    for _ in range(args.workers):
        t = threading.Thread(target=worker, args=(args.topic, batch_size, args.rate), daemon=True)
        t.start()
        threads.append(t)
        
    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        print("\nDeteniendo ingesta...")
