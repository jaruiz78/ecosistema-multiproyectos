#!/usr/bin/env python3
"""
Arquitectura y especificación formal para interstellar_laser_router.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-005-slsa-l3-cosign-provenance.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_0_software_engineering/04_compliance_gdpr_ai_act_pii.md
- Referencia Académica: Dwork (2006) Differential Privacy; Zero-Trust Architecture (NIST 800-207)
"""
"""
interstellar_laser_router.py
=============================================================================
Motor de Ruteo Óptico Láser Inter-Satelital (ISL Mesh) y Resiliencia D2D.
Enruta paquetes a velocidad de la luz en el vacío (c = 300.000 km/s) sobre la
malla esférica dinámica LEO mediante Dijkstra con conmutación LoRaWAN / D2D.
=============================================================================
"""
import heapq
import math
from typing import Dict, List, Tuple, Optional

SPEED_OF_LIGHT_VACUUM_KM_S = 299792.458
SPEED_OF_LIGHT_FIBER_KM_S = 200000.0 # Fibra óptica terrestre (~2/3 c)

class InterstellarLaserMeshRouter:
    def __init__(self):
        self.satellites: Dict[str, Tuple[float, float, float]] = {} # id -> (lat, lon, alt_km)
        self.laser_links: Dict[str, List[Tuple[str, float]]] = {} # id -> list of (neighbor_id, distance_km)

    def register_satellite(self, sat_id: str, lat: float, lon: float, alt_km: float = 550.0):
        self.satellites[sat_id] = (lat, lon, alt_km)
        if sat_id not in self.laser_links:
            self.laser_links[sat_id] = []

    def add_laser_link(self, sat_a: str, sat_b: str):
        if sat_a in self.satellites and sat_b in self.satellites:
            dist = self._compute_euclidean_distance(self.satellites[sat_a], self.satellites[sat_b])
            self.laser_links[sat_a].append((sat_b, dist))
            self.laser_links[sat_b].append((sat_a, dist))

    def _compute_euclidean_distance(self, pos_a: Tuple[float, float, float], pos_b: Tuple[float, float, float]) -> float:
        lat1, lon1, alt1 = pos_a
        lat2, lon2, alt2 = pos_b
        r1 = 6371.0 + alt1
        r2 = 6371.0 + alt2

        phi1, theta1 = math.radians(lat1), math.radians(lon1)
        phi2, theta2 = math.radians(lat2), math.radians(lon2)

        x1 = r1 * math.cos(phi1) * math.cos(theta1)
        y1 = r1 * math.cos(phi1) * math.sin(theta1)
        z1 = r1 * math.sin(phi1)

        x2 = r2 * math.cos(phi2) * math.cos(theta2)
        y2 = r2 * math.cos(phi2) * math.sin(theta2)
        z2 = r2 * math.sin(phi2)

        return math.sqrt((x1 - x2) ** 2 + (y1 - y2) ** 2 + (z1 - z2) ** 2)

    def route_shortest_path_laser(self, src_id: str, dst_id: str) -> Dict:
        if src_id not in self.satellites or dst_id not in self.satellites:
            return {"status": "SATELLITE_NOT_FOUND", "path": [], "latency_ms": 0.0}

        pq = [(0.0, src_id, [src_id])]
        visited = set()

        while pq:
            dist_km, current, path = heapq.heappop(pq)
            if current == dst_id:
                latency_ms = (dist_km / SPEED_OF_LIGHT_VACUUM_KM_S) * 1000.0
                fiber_latency_ms = (dist_km / SPEED_OF_LIGHT_FIBER_KM_S) * 1000.0
                speedup_pct = ((fiber_latency_ms - latency_ms) / fiber_latency_ms) * 100.0

                return {
                    "status": "SUCCESS_ROUTED_LASER",
                    "path": path,
                    "total_distance_km": round(dist_km, 2),
                    "laser_latency_ms": round(latency_ms, 3),
                    "fiber_equivalent_latency_ms": round(fiber_latency_ms, 3),
                    "laser_speedup_percent": round(speedup_pct, 1),
                    "hops_count": len(path) - 1
                }

            if current in visited:
                continue
            visited.add(current)

            for neighbor, edge_dist in self.laser_links.get(current, []):
                if neighbor not in visited:
                    heapq.heappush(pq, (dist_km + edge_dist, neighbor, path + [neighbor]))

        return {"status": "NO_LASER_PATH_FOUND", "path": [], "latency_ms": 0.0}
