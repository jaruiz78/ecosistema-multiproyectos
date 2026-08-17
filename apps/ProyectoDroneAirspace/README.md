# 🚁 ProyectoDroneAirspace: Orquestador U-Space 4D & Desconflicción Aérea

Vertical corporativo de alta precisión para la gestión de espacio aéreo de aeronaves no tripuladas (UAS / Drones comerciales y eVTOL) en espacio aéreo de muy baja cota (VLL - Very Low Level Airspace), desconflicción de trayectorias 4D indexadas en mallas espaciales Uber H3 y cumplimiento de normativas europeas U-Space (EU 2021/664) y FAA UTM.

---

## 🏛️ 1. Arquitectura Hexagonal y Flujo de Desconflicción 4D

```mermaid
flowchart TD
    subgraph Entrada["1. Ingesta Telemétrica y Planes de Vuelo"]
        A["Operador de Dron / UTM Ground Station"] -->|Plan de Vuelo 4D| API["UspaceFlightController (REST / gRPC)"]
        GPS["Telemetría de Vuelo ADS-B / IoT"] -->|Coordenadas GPS + Altitud AGL| API
    end

    subgraph Dominio["2. Capa de Dominio Puro (Zero-Mockito / Java 25)"]
        API --> Port["UspaceConflictResolutionPort"]
        Port --> Svc["UspaceConflictResolutionService"]
        Svc --> Engine["core-geogrid-h3 (H3 3D Spatial Grid Engine)"]
        Svc --> MPC["core-mpc-control (MPC Trajectory Optimizer)"]
    end

    subgraph Verificacion["3. Verificación de Seguridad y Resolución"]
        Engine --> Check{"¿Conflicto Espacio-Temporal 4D?"}
        Check -- Sí (< 50m H, < 30m V) --> Resolve["Reroute Dinámico O(N) / Maniobra Evasiva"]
        Check -- No --> Clear["Vuelo Autorizado / Cleared Route"]
        Resolve --> Ledger["Audit Log Inmutable (SHA-256)"]
        Clear --> Ledger
    end

    subgraph Twin["4. Gemelo Digital Unificado"]
        Ledger --> GT["tensor_gnn_core.py (Cluster 11: Drone Airspace)"]
    end
```

---

## ⚡ 2. Especificación Técnica y Rendimiento

- **Separación Mínima de Seguridad**: `50 metros` horizontal, `30 metros` vertical (AGL).
- **Indexación Espacial**: Prismas volumétricos 3D Uber H3 Resolución 9 (`~100m`) con estratos de altitud de 20 metros.
- **Complejidad Algorítmica**: \(O(N)\) para detección y resolución de colisiones por celda espacial.
- **Runtimes & Standards**: Java 25 LTS, Spring Boot 4.1, Virtual Threads Loom (cero Carrier Thread Pinning).

---

## 📚 3. Trazabilidad Académica y Referencias

- **ADR Relacionado**: [`ADR-024: U-Space Drone Airspace 3D H3`](file:///home/jaruiz/Desarrollo/docs/adr/adr-024-u-space-drone-airspace-3d-h3.md).
- **Universidad Privada**: [`FACULTAD_VIII: Ingeniería Industrial, Operaciones y HCI`](file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md).
