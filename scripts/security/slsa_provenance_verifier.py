import json
import hashlib
import hmac
import time
import logging
from typing import Dict, Any, Tuple

logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")

class SLSAProvenanceVerifier:
    """
    Simulador de Atestaciones SLSA L3 (Supply-chain Levels for Software Artifacts v1.0)
    y Verificador de Binary Authorization para Artifact Registry / Cloud Run.
    """

    DEFAULT_SECRET_KEY = b"consilium_romano_slsa_l3_secret_signing_key"

    def __init__(self, signing_key: bytes = DEFAULT_SECRET_KEY):
        self.signing_key = signing_key

    def generate_provenance(
        self,
        artifact_name: str,
        artifact_content: bytes,
        repo_url: str,
        commit_sha: str,
        builder_id: str = "https://cloudbuild.googleapis.com/GoogleAntigravityL3Builder@v1"
    ) -> Dict[str, Any]:
        """
        Genera la proveniencia in-toto / SLSA v1.0 en formato JSON estandarizado.
        """
        artifact_digest = hashlib.sha256(artifact_content).hexdigest()
        
        statement = {
            "_type": "https://in-toto.io/Statement/v1",
            "subject": [
                {
                    "name": artifact_name,
                    "digest": {
                        "sha256": artifact_digest
                    }
                }
            ],
            "predicateType": "https://slsa.dev/provenance/v1",
            "predicate": {
                "buildDefinition": {
                    "buildType": "https://slsa.dev/container-build/v1",
                    "externalParameters": {
                        "repository": repo_url,
                        "ref": f"refs/heads/main@{commit_sha}"
                    },
                    "internalParameters": {
                        "slsaLevel": "SLSA_BUILD_LEVEL_3"
                    },
                    "resolvedDependencies": [
                        {
                            "uri": repo_url,
                            "digest": {
                                "gitCommit": commit_sha
                            }
                        }
                    ]
                },
                "runDetails": {
                    "builder": {
                        "id": builder_id
                    },
                    "metadata": {
                        "invocationId": f"build-{int(time.time())}",
                        "startedOn": time.strftime("%Y-%m-%dT%H:%M:%SZ", time.gmtime()),
                        "completeness": {
                            "parameters": True,
                            "environment": True,
                            "materials": True
                        }
                    }
                }
            }
        }

        # Generar firma criptográfica sobre el enunciado estandarizado
        canonical_json = json.dumps(statement, sort_keys=True).encode("utf-8")
        signature = hmac.new(self.signing_key, canonical_json, hashlib.sha256).hexdigest()

        envelope = {
            "payloadType": "application/vnd.in-toto+json",
            "payload": statement,
            "signatures": [
                {
                    "keyid": "key-antigravity-l3-kms",
                    "sig": signature
                }
            ]
        }
        return envelope

    def verify_provenance(self, artifact_content: bytes, envelope: Dict[str, Any]) -> Tuple[bool, str]:
        """
        Actúa como simulador de Binary Authorization en GCP:
        Verifica la autenticidad criptográfica de la firma y la conformidad con SLSA Level 3.
        """
        try:
            payload = envelope.get("payload", {})
            signatures = envelope.get("signatures", [])

            if not signatures:
                return False, "RECHAZADO: No se encontraron firmas en la proveniencia (SLSA Violation)."

            sig_entry = signatures[0]
            provided_sig = sig_entry.get("sig", "")

            # Re-verificar firma HMAC/ECDSA
            canonical_json = json.dumps(payload, sort_keys=True).encode("utf-8")
            expected_sig = hmac.new(self.signing_key, canonical_json, hashlib.sha256).hexdigest()

            if not hmac.compare_digest(provided_sig, expected_sig):
                return False, "RECHAZADO: Firma criptográfica INVÁLIDA (Artefacto o Proveniencia manipulados)."

            # Verificar coincidencia del Digest del Artefacto
            artifact_digest = hashlib.sha256(artifact_content).hexdigest()
            subjects = payload.get("subject", [])
            if not subjects or subjects[0]["digest"].get("sha256") != artifact_digest:
                return False, f"RECHAZADO: El hash SHA-256 del binario ({artifact_digest[:8]}...) no coincide con la atestación."

            # Verificar Nivel SLSA
            predicate = payload.get("predicate", {})
            build_def = predicate.get("buildDefinition", {})
            slsa_level = build_def.get("internalParameters", {}).get("slsaLevel")
            
            if slsa_level != "SLSA_BUILD_LEVEL_3":
                return False, f"RECHAZADO: El artefacto no cumple con SLSA L3 (Nivel actual: {slsa_level})."

            return True, "APROBADO: Proveniencia SLSA L3 Criptográficamente Válida. Despliegue en Cloud Run Autorizado."

        except Exception as e:
            return False, f"ERROR DE VERIFICACIÓN: {str(e)}"

if __name__ == "__main__":
    verifier = SLSAProvenanceVerifier()
    
    # 1. Simular binario legítimo
    sample_binary = b"BINARY_IMAGE_CONTENT_SPRING_BOOT_APP_V1.0"
    provenance = verifier.generate_provenance(
        artifact_name="eu.gcr.io/corp-project/app:v1.0",
        artifact_content=sample_binary,
        repo_url="https://github.com/jaruiz78/corp-spring-boot-starter.git",
        commit_sha="a1b2c3d4e5f67890123456789abcdef012345678"
    )
    
    logging.info("📄 Proveniencia Generada:")
    logging.info(json.dumps(provenance, indent=2))
    
    # 2. Verificación legítima
    success, msg = verifier.verify_provenance(sample_binary, provenance)
    logging.info(f"VEREDICTO BINARY AUTH: {msg}")
    assert success is True

    # 3. Simular manipulación de artefacto (Ataque de cadena de suministro)
    tampered_binary = b"BINARY_IMAGE_CONTENT_WITH_MALICIOUS_BACKDOOR"
    success, msg = verifier.verify_provenance(tampered_binary, provenance)
    logging.info(f"VEREDICTO ATAQUE (Binario Alterado): {msg}")
    assert success is False
