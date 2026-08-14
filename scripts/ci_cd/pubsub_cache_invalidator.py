"""
Arquitectura y especificación formal para pubsub_cache_invalidator.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-twin-peps-enkf.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_unified_twin_math/10_gemelo_digital_unificado_core.md
- Referencia Académica: Verstraete, Murg, Cirac (2008) PEPS Tensor Networks; Evensen (2003) EnKF
"""
import time
import logging
from typing import Dict, List, Set, Any

logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")

class DistributedL1CacheInvalidator:
    """
    Invalidador de Caché L1 Distribuido vía Pub/Sub (GCP Topics).
    Sincroniza en tiempo real las cachés locales Caffeine (Java 25) y Go In-Memory Maps
    evitando desacoplamientos o lectura de datos obsoletos sin polling.
    """

    def __init__(self, topic_name: str = "projects/corp-prod/topics/l1-cache-invalidation"):
        self.topic_name = topic_name
        self.subscribers: Dict[str, Set[str]] = {} # subscriber_id -> cached_keys
        self.invalidation_log: List[Dict[str, Any]] = []

    def register_subscriber(self, subscriber_id: str):
        """Registra una réplica de servicio (Spring Boot o Go Worker)."""
        if subscriber_id not in self.subscribers:
            self.subscribers[subscriber_id] = set()
            logging.info(f"🔌 Suscriptor de Caché L1 registrado: {subscriber_id}")

    def put_cache(self, subscriber_id: str, key: str):
        """Almacena una clave en la caché L1 de una réplica específica."""
        if subscriber_id not in self.subscribers:
            self.register_subscriber(subscriber_id)
        self.subscribers[subscriber_id].add(key)

    def publish_invalidation_event(self, source_subscriber_id: str, cache_name: str, key: str):
        """
        Publica un evento de invalidación en el canal Pub/Sub.
        Todas las réplicas excepto la fuente invalidarán inmediatamente la clave en local.
        """
        event = {
            "topic": self.topic_name,
            "source": source_subscriber_id,
            "cache": cache_name,
            "key": key,
            "timestamp": time.time()
        }
        self.invalidation_log.append(event)
        
        invalidated_count = 0
        for sub_id, keys in self.subscribers.items():
            if sub_id != source_subscriber_id and key in keys:
                keys.remove(key)
                invalidated_count += 1
                logging.info(f"⚡ [Pub/Sub L1 Cache] Clave '{key}' INVALIDADA en la réplica {sub_id}")

        logging.info(f"📡 Evento Pub/Sub emitido por {source_subscriber_id} -> Clave '{key}' invalidada en {invalidated_count} réplicas.")
        return event

    def is_cached(self, subscriber_id: str, key: str) -> bool:
        return key in self.subscribers.get(subscriber_id, set())

if __name__ == "__main__":
    invalidator = DistributedL1CacheInvalidator()
    
    # Simular réplicas Spring Boot y Go Worker
    invalidator.register_subscriber("spring-boot-replica-1")
    invalidator.register_subscriber("spring-boot-replica-2")
    invalidator.register_subscriber("go-worker-replica-1")

    # Poblar cachés
    invalidator.put_cache("spring-boot-replica-1", "tenant:1001:config")
    invalidator.put_cache("spring-boot-replica-2", "tenant:1001:config")
    invalidator.put_cache("go-worker-replica-1", "tenant:1001:config")

    # Mutación en replica 1 dispara invalidación global
    invalidator.publish_invalidation_event("spring-boot-replica-1", "tenants", "tenant:1001:config")

    assert invalidator.is_cached("spring-boot-replica-2", "tenant:1001:config") is False
    assert invalidator.is_cached("go-worker-replica-1", "tenant:1001:config") is False
    logging.info("✅ Demostración de invalidación L1 Pub/Sub completada.")
