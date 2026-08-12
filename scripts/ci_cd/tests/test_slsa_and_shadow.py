import json
import time
import urllib.request
import pytest
from slsa_provenance_verifier import SLSAProvenanceVerifier
from shadow_traffic_mirror import run_demo_proxy, GLOBAL_METRICS

def test_slsa_l3_valid_provenance_authorized():
    verifier = SLSAProvenanceVerifier()
    artifact = b"JAR_SPRING_BOOT_NATIVE_AOT_4.0_RELEASE"
    
    envelope = verifier.generate_provenance(
        artifact_name="eu.gcr.io/pct/microservice:v2.0",
        artifact_content=artifact,
        repo_url="https://github.com/jaruiz78/pctMultiMicroservices.git",
        commit_sha="fedcba9876543210fedcba9876543210fedcba98"
    )
    
    authorized, message = verifier.verify_provenance(artifact, envelope)
    assert authorized is True
    assert "SLSA L3 Criptográficamente Válida" in message

def test_slsa_l3_tampered_artifact_rejected():
    verifier = SLSAProvenanceVerifier()
    artifact = b"JAR_SPRING_BOOT_NATIVE_AOT_4.0_RELEASE"
    tampered_artifact = b"JAR_SPRING_BOOT_NATIVE_AOT_4.0_RELEASE_WITH_MALWARE"
    
    envelope = verifier.generate_provenance(
        artifact_name="eu.gcr.io/pct/microservice:v2.0",
        artifact_content=artifact,
        repo_url="https://github.com/jaruiz78/pctMultiMicroservices.git",
        commit_sha="fedcba9876543210fedcba9876543210fedcba98"
    )
    
    authorized, message = verifier.verify_provenance(tampered_artifact, envelope)
    assert authorized is False
    assert "RECHAZADO" in message

def test_slsa_l3_invalid_signature_rejected():
    verifier = SLSAProvenanceVerifier()
    artifact = b"JAR_SPRING_BOOT_NATIVE_AOT_4.0_RELEASE"
    
    envelope = verifier.generate_provenance(
        artifact_name="eu.gcr.io/pct/microservice:v2.0",
        artifact_content=artifact,
        repo_url="https://github.com/jaruiz78/pctMultiMicroservices.git",
        commit_sha="fedcba9876543210fedcba9876543210fedcba98"
    )
    
    # Tamper with signature
    envelope["signatures"][0]["sig"] = "0000000000000000000000000000000000000000000000000000000000000000"
    
    authorized, message = verifier.verify_provenance(artifact, envelope)
    assert authorized is False
    assert "Firma criptográfica INVÁLIDA" in message

def test_shadow_traffic_mirror_proxy_flow():
    p_srv, prim_srv, shad_srv = run_demo_proxy(port=8890, primary_port=9093, shadow_port=9094)
    
    try:
        req = urllib.request.Request(
            "http://127.0.0.1:8890/api/v1/sensors",
            data=b'{"sensor_id": "H3-8828308281ffff", "temp": 24.5}',
            headers={"Content-Type": "application/json"},
            method="POST"
        )
        
        start_time = time.time()
        with urllib.request.urlopen(req) as resp:
            status = resp.status
            body = json.loads(resp.read().decode("utf-8"))
            client_lat = time.time() - start_time

        assert status == 200
        assert body["service"] == "PRIMARY_SERVICE"
        assert body["is_shadow"] is False
        
        # Give background shadow thread time to record metrics
        time.sleep(0.2)
        summary = GLOBAL_METRICS.get_summary()
        assert summary["total_requests"] >= 1
        assert summary["shadow_requests_sent"] >= 1

    finally:
        p_srv.shutdown()
        prim_srv.shutdown()
        shad_srv.shutdown()
