import sys
import os
import pytest

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))
from pqc_engine import PostQuantumCryptoEngine

def test_pqc_attestation_signing():
    engine = PostQuantumCryptoEngine(security_level=3)
    payload = b"GENESIS_BLOCK_TENANT_REGANTES_01"
    
    result = engine.sign_attestation(payload)
    
    assert result["algorithm"] == "Dilithium-3 / Kyber-768"
    assert result["security_level"] == 3
    assert result["valid"] is True
    assert result["pqc_signature"].startswith("PQC_DILITHIUM3_")
    assert len(result["pqc_signature"]) > 20

def test_pqc_deterministic_signature_for_same_payload():
    engine = PostQuantumCryptoEngine()
    payload = b"CRITICAL_SCADA_COMMAND_TRIP_BREAKER"
    
    sig1 = engine.sign_attestation(payload)
    sig2 = engine.sign_attestation(payload)
    
    assert sig1["pqc_signature"] == sig2["pqc_signature"]
