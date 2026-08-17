#!/usr/bin/env python3
"""
Add Strategic 2nd Aggregate Tests for Top 10 Verticals
------------------------------------------------------
Añade tests unitarios Zero-Mockito para los segundos agregados de dominio
en los 10 verticales estratégicos.
"""

from pathlib import Path

APPS_DIR = Path("/home/jaruiz/Desarrollo/apps")

TESTS = {
    "ProyectoB2G": {
        "pkg": "com.corp.proyectob2g",
        "entity": "PublicProcurementContract",
        "valid_call": 'new PublicProcurementContract("CNT-01", "Ministerio de Transición", 500000.0, "PUBLISHED", Instant.now())',
        "invalid_call": 'new PublicProcurementContract("CNT-01", "Ministerio de Transición", -100.0, "PUBLISHED", Instant.now())',
    },
    "ProyectoEnergia": {
        "pkg": "com.corp.proyectoenergia",
        "entity": "GridSubstationNode",
        "valid_call": 'new GridSubstationNode("SUB-01", "Zone-North", 15000.0, 8500.0, Instant.now())',
        "invalid_call": 'new GridSubstationNode("SUB-01", "Zone-North", -500.0, 8500.0, Instant.now())',
    },
    "ProyectoVPP": {
        "pkg": "com.corp.proyectovpp",
        "entity": "BatteryEnergyStorageUnit",
        "valid_call": 'new BatteryEnergyStorageUnit("BAT-01", "LFP", 85.5, 250.0, Instant.now())',
        "invalid_call": 'new BatteryEnergyStorageUnit("BAT-01", "LFP", 150.0, 250.0, Instant.now())',
    },
    "ProyectoLogistica": {
        "pkg": "com.corp.proyectologistica",
        "entity": "AutonomousFleetRoute",
        "valid_call": 'new AutonomousFleetRoute("RT-01", "88390cb643fffff", "88390cb647fffff", 12.5, Instant.now())',
        "invalid_call": 'new AutonomousFleetRoute("RT-01", "88390cb643fffff", "88390cb647fffff", -2.0, Instant.now())',
    },
    "ProyectoTokenRWA": {
        "pkg": "com.corp.proyectotokenrwa",
        "entity": "EscrowAssetVault",
        "valid_call": 'new EscrowAssetVault("VLT-01", "RWA-GOLD-01", 1250000.0, true, Instant.now())',
        "invalid_call": 'new EscrowAssetVault("VLT-01", "RWA-GOLD-01", -50.0, true, Instant.now())',
    },
    "ProyectoDefensa": {
        "pkg": "com.corp.proyectodefensa",
        "entity": "TacticalSensorNode",
        "valid_call": 'new TacticalSensorNode("SNS-01", "RADAR_L", -45.0, true, Instant.now())',
        "invalid_call": 'new TacticalSensorNode("SNS-01", "RADAR_L", 15.0, true, Instant.now())',
    },
    "ProyectoCircular": {
        "pkg": "com.corp.proyectocircular",
        "entity": "DigitalProductPassport",
        "valid_call": 'new DigitalProductPassport("DPP-01", "BATCH-2026-A", 65.0, "https://passport.corp/dpp-01", Instant.now())',
        "invalid_call": 'new DigitalProductPassport("DPP-01", "BATCH-2026-A", 150.0, "https://passport.corp/dpp-01", Instant.now())',
    },
    "ProyectoAgua": {
        "pkg": "com.corp.proyectoagua",
        "entity": "WaterPressureValve",
        "valid_call": 'new WaterPressureValve("VLV-01", "SEG-CANAL-4", 4.2, 120.0, Instant.now())',
        "invalid_call": 'new WaterPressureValve("VLV-01", "SEG-CANAL-4", -1.0, 120.0, Instant.now())',
    },
    "ProyectoSalud": {
        "pkg": "com.corp.proyectosalud",
        "entity": "ZkClinicalStudyCohort",
        "valid_call": 'new ZkClinicalStudyCohort("COH-01", "0xdeadbeef1234", 1500, true, Instant.now())',
        "invalid_call": 'new ZkClinicalStudyCohort("COH-01", "0xdeadbeef1234", -10, true, Instant.now())',
    },
    "ProyectoEmergencyGeoGrid": {
        "pkg": "com.corp.proyectoemergencygeogrid",
        "entity": "EmergencyDisasterCell",
        "valid_call": 'new EmergencyDisasterCell("88390cb643fffff", "WILDFIRE", 4, 2500, Instant.now())',
        "invalid_call": 'new EmergencyDisasterCell("88390cb643fffff", "WILDFIRE", 9, 2500, Instant.now())',
    },
}

for proj, info in TESTS.items():
    test_dir = APPS_DIR / proj / "src/test/java" / info["pkg"].replace(".", "/") / "domain"
    test_dir.mkdir(parents=True, exist_ok=True)
    entity = info["entity"]
    
    code = f"""package {info["pkg"]}.domain;

import {info["pkg"]}.domain.model.{entity};
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test de Dominio para el segundo agregado estratégico {entity}.
 * Zero-Mockito Policy.
 */
class {entity}DomainTest {{

    @Test
    @DisplayName("Debe instanciar {entity} válidamente cumpliendo invariantes")
    void shouldCreateValid() {{
        {entity} agg = {info["valid_call"]};
        assertThat(agg).isNotNull();
    }}

    @Test
    @DisplayName("Debe lanzar excepción si se violan invariantes de negocio en {entity}")
    void shouldRejectInvalid() {{
        assertThatThrownBy(() -> {{
            {info["invalid_call"]};
        }}).isInstanceOf(IllegalArgumentException.class);
    }}
}}
"""
    (test_dir / f"{entity}DomainTest.java").write_text(code, encoding="utf-8")
    print(f"  ✓ Added test for {proj} -> {entity}DomainTest")
