# ProyectoDefensa — Sistemas Tácticos Air-Gapped & Cifrado Post-Cuántico Kyber

Módulo empresarial de defensa táctica, enrutamiento mesh en entornos desconectados (Air-Gapped) y protección de comunicaciones mediante esquemas criptográficos post-cuánticos (PQC Kyber / ML-KEM).

---

## 1. Arquitectura Hexagonal y Stack Técnico
- **Lenguaje:** Java 25 LTS (`record` inmutables, Virtual Threads Loom, Zero-Mockito).
- **Framework Base:** Spring Boot 4.1.0 (hereda de `corp-spring-boot-starter-parent`).
- **Seguridad:** Zero-Trust BeyondCorp, protección Anti-Replay en \(O(1)\), sobres tácticos cifrados.

## 2. Agregados de Dominio y Modelos
1. **`Defensa`**: Agregado raíz para operaciones de defensa y respuesta táctica.
2. **`TacticalSensorNode`**: Nodo sensor táctico (Radar, Sonar, ESM) con potencia de señal RF en dBm (\(\text{signalStrengthDbm} \le 0.0\)).
3. **`KyberSecurityEnvelope`**: Sobre de seguridad PQC con ciphertext Base64, resumen de secreto compartido, origen/destino y conteo de saltos air-gapped (\(\text{hopCount} \ge 0\)).

## 3. Servicios de Negocio
- **`KyberMeshRelayService`**:
  - Enrutamiento táctico oportunista entre nodos de la malla en \(O(1)\).
  - Protección estricta contra ataques de repetición (Anti-Replay) mediante registro atómico en memoria.
  - Límite de salto táctico (\(\text{TTL} \le 16\)) para evitar tormentas de retransmisión en redes radio tácticas.

## 4. Pruebas y Certificación
- **100% Zero-Mockito**: Tests de concurrencia y validación criptográfica herméticos in-memory.
- **Ejecución:** `mvn clean test`