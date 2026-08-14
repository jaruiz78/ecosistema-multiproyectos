#!/usr/bin/env python3
"""
Arquitectura y especificación formal para test_interstellar_laser.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-005-slsa-l3-cosign-provenance.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_0_software_engineering/04_compliance_gdpr_ai_act_pii.md
- Referencia Académica: Dwork (2006) Differential Privacy; Zero-Trust Architecture (NIST 800-207)
"""
import sys
import os
import pytest

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))
from src.interstellar_laser_router import InterstellarLaserMeshRouter

def test_interstellar_laser_routing():
    router = InterstellarLaserMeshRouter()
    
    # 4 Satélites en constelación LEO (Madrid, Atlántico, New York, Tokio)
    router.register_satellite("SAT_LEO_MADRID", 40.4168, -3.7038, 550.0)
    router.register_satellite("SAT_LEO_ATLANTIC", 35.0, -35.0, 550.0)
    router.register_satellite("SAT_LEO_NYC", 40.7128, -74.0060, 550.0)
    router.register_satellite("SAT_LEO_TOKYO", 35.6762, 139.6503, 550.0)
    
    router.add_laser_link("SAT_LEO_MADRID", "SAT_LEO_ATLANTIC")
    router.add_laser_link("SAT_LEO_ATLANTIC", "SAT_LEO_NYC")
    router.add_laser_link("SAT_LEO_NYC", "SAT_LEO_TOKYO")
    
    res = router.route_shortest_path_laser("SAT_LEO_MADRID", "SAT_LEO_NYC")
    
    assert res["status"] == "SUCCESS_ROUTED_LASER"
    assert res["hops_count"] == 2
    assert res["path"] == ["SAT_LEO_MADRID", "SAT_LEO_ATLANTIC", "SAT_LEO_NYC"]
    assert res["laser_speedup_percent"] >= 30.0 # Láser en vacío es ~33.3% más rápido que fibra
    assert res["laser_latency_ms"] < res["fiber_equivalent_latency_ms"]

def test_no_path():
    router = InterstellarLaserMeshRouter()
    router.register_satellite("SAT_A", 0.0, 0.0, 550.0)
    router.register_satellite("SAT_B", 50.0, 50.0, 550.0)
    
    res = router.route_shortest_path_laser("SAT_A", "SAT_B")
    assert res["status"] == "NO_LASER_PATH_FOUND"
