import os
import json
import pytest
from check_spring_aot_hints import SpringAotHintChecker
from pubsub_cache_invalidator import DistributedL1CacheInvalidator
from audit_firestore_rls import FirestoreRLSAuditor
from predictive_h3_vector_manager import PredictiveH3VectorManager
from shadow_traffic_mirror import run_demo_proxy, GLOBAL_METRICS

def test_spring_aot_hint_checker():
    checker = SpringAotHintChecker("/home/jaruiz/Desarrollo/corp-spring-boot-starter")
    res = checker.scan()
    assert res["scanned_files"] > 0
    assert res["is_clean"] is True

def test_pubsub_l1_cache_invalidation_sync():
    invalidator = DistributedL1CacheInvalidator()
    invalidator.register_subscriber("spring-boot-rep-1")
    invalidator.register_subscriber("go-worker-rep-1")

    key = "tenant:almeria:config"
    invalidator.put_cache("spring-boot-rep-1", key)
    invalidator.put_cache("go-worker-rep-1", key)

    event = invalidator.publish_invalidation_event("spring-boot-rep-1", "configs", key)
    assert event["source"] == "spring-boot-rep-1"
    assert invalidator.is_cached("go-worker-rep-1", key) is False

def test_firestore_rls_multi_tenant_isolation():
    auditor = FirestoreRLSAuditor()
    auth_a = {"authenticated": True, "token": {"uid": "user_a", "tenant_id": "tenant_alpha"}}
    auth_b = {"authenticated": True, "token": {"uid": "user_b", "tenant_id": "tenant_beta"}}

    # Permite propio tenant
    ok_a, msg_a = auditor.evaluate_access(auth_a, "/tenants/tenant_alpha/data/101")
    assert ok_a is True

    # Deniega tenant ajeno (Fuga bloqueada)
    ok_b, msg_b = auditor.evaluate_access(auth_b, "/tenants/tenant_alpha/data/101")
    assert ok_b is False
    assert "MULTI-TENANT VIOLATION" in msg_b

def test_predictive_h3_vector_trajectory():
    manager = PredictiveH3VectorManager(default_resolution=8)
    
    # 0 km/h -> Sin transición
    still_res = manager.calculate_prefetch_cells(36.838, -2.459, speed_kmh=0.0, heading_deg=0.0)
    assert still_res["is_transitioning"] is False

    # 100 km/h -> Transición activa con precarga
    moving_res = manager.calculate_prefetch_cells(36.838, -2.459, speed_kmh=100.0, heading_deg=90.0)
    assert moving_res["is_transitioning"] is True
    assert len(moving_res["cells_to_prefetch"]) == 2

def test_gitops_presync_slsa_manifest_exists():
    hook_path = "/home/jaruiz/Desarrollo/infra/gitops/argocd/presync-slsa-l3-verifier-hook.yaml"
    assert os.path.exists(hook_path)
