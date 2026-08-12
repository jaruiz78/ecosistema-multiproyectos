import logging
from typing import Dict, Any, Tuple

logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")

class FirestoreRLSAuditor:
    """
    Auditor de Reglas de Seguridad Firestore y Aislamiento Celular Multi-Tenant (RLS).
    Valida que los accesos a datos estén estrictamente restringidos al tenant_id autenticado en el token JWT.
    """

    def __init__(self):
        pass

    def evaluate_access(self, auth_context: Dict[str, Any], resource_path: str, request_type: str = "read") -> Tuple[bool, str]:
        """
        Simula la evaluación de reglas Firestore RLS.
        Regla: match /databases/{database}/documents/tenants/{tenantId}/{document=**} {
            allow read, write: if request.auth != null && request.auth.token.tenant_id == tenantId;
        }
        """
        if not auth_context or not auth_context.get("authenticated"):
            return False, "RECHAZADO: Solicitud sin autenticación (Unauthenticated)."

        user_tenant_id = auth_context.get("token", {}).get("tenant_id")
        if not user_tenant_id:
            return False, "RECHAZADO: El token no contiene claim de 'tenant_id'."

        # Extraer tenantId de la ruta de Firestore
        parts = resource_path.strip("/").split("/")
        if len(parts) >= 2 and parts[0] == "tenants":
            target_tenant_id = parts[1]
            if user_tenant_id == target_tenant_id:
                return True, f"PERMITIDO: Acceso {request_type} concedido para tenant_id '{user_tenant_id}'."
            else:
                return False, f"RECHAZADO MULTI-TENANT VIOLATION: Usuario de tenant '{user_tenant_id}' intentó acceder a datos del tenant '{target_tenant_id}'."

        return False, "RECHAZADO: Ruta fuera del esquema de seguridad multi-tenant registrado."

if __name__ == "__main__":
    auditor = FirestoreRLSAuditor()

    auth_tenant_a = {"authenticated": True, "token": {"uid": "usr_1", "tenant_id": "regantes_almeria"}}
    auth_tenant_b = {"authenticated": True, "token": {"uid": "usr_2", "tenant_id": "regantes_murcia"}}

    # Test 1: Acceso legítimo
    allowed, msg = auditor.evaluate_access(auth_tenant_a, "/tenants/regantes_almeria/parcelas/p101")
    logging.info(f"Test Legítimo: {msg}")
    assert allowed is True

    # Test 2: Intento de fuga multi-tenant
    allowed, msg = auditor.evaluate_access(auth_tenant_a, "/tenants/regantes_murcia/parcelas/p202")
    logging.info(f"Test Fuga Multi-Tenant: {msg}")
    assert allowed is False
    logging.info("✅ Auditor de Firestore RLS verificado correctamente.")
