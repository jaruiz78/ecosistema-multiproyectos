#!/usr/bin/env python3
"""
run_master_ecosystem_suite.py
=============================================================================
BATERÍA DE PRUEBAS GLOBAL, SIMULACIONES, ENTRENAMIENTOS Y AUDITORÍA
Ecosistema MultiProyectos & Google Antigravity (2026-2031)
=============================================================================
"""
import sys
import os
import time
import subprocess
import sqlite3

def run_command(cmd, desc):
    print(f"\n--- [EJECUTANDO]: {desc} ---")
    start = time.perf_counter()
    res = subprocess.run(cmd, shell=True, capture_output=True, text=True)
    dur = time.perf_counter() - start
    if res.returncode == 0:
        print(f"  ✓ {desc} completado en {dur:.2f}s")
        return True, res.stdout
    else:
        print(f"  ❌ Error en {desc} ({dur:.2f}s):")
        print(res.stderr or res.stdout)
        return False, res.stderr or res.stdout

def main():
    print("==============================================================================")
    print("  INICIANDO SUITE MAESTRA DE VALIDACIÓN GLOBAL DE ECOSISTEMA MULTIPROYECTOS")
    print("==============================================================================")
    
    start_total = time.perf_counter()
    all_ok = True
    
    # 1. Ejecutar Simulaciones y Entrenamientos de los 10 nuevos proyectos
    sim_scripts = [
        ("python3 /home/jaruiz/Desarrollo/scripts/train_carbon_ledger_dpp.py", "Simulación CarbonLedger DPP"),
        ("python3 /home/jaruiz/Desarrollo/scripts/train_fleet_cold_chain_vrp.py", "Simulación FleetColdChain VRP"),
        ("python3 /home/jaruiz/Desarrollo/scripts/train_agro_energy_vpp_mpc.py", "Simulación AgroEnergyVPP MPC"),
        ("python3 /home/jaruiz/Desarrollo/scripts/train_gov_procure_rag_matcher.py", "Simulación GovProcureMatch RAG"),
        ("python3 /home/jaruiz/Desarrollo/scripts/train_presa_twin_scada_enkf.py", "Simulación PresaTwinSCADA EnKF"),
        ("python3 /home/jaruiz/Desarrollo/scripts/train_smart_destination_dti.py", "Simulación SmartDestination DTI"),
        ("python3 /home/jaruiz/Desarrollo/scripts/train_hotel_twin_revpar_mpc.py", "Simulación HotelTwinRevPAR MPC"),
        ("python3 /home/jaruiz/Desarrollo/scripts/train_eco_tourism_passport_zk.py", "Simulación EcoTourismPassport ZK"),
        ("python3 /home/jaruiz/Desarrollo/scripts/train_intermodal_transfer_hub_vrp.py", "Simulación IntermodalTransferHub VRP"),
        ("python3 /home/jaruiz/Desarrollo/scripts/train_regenerative_experience_escrow.py", "Simulación RegenerativeExperience Escrow"),
        ("python3 /home/jaruiz/Desarrollo/scripts/train_pharma_cold_chain_kinetics.py", "Simulación PharmaColdChain Kinetics"),
        ("python3 /home/jaruiz/Desarrollo/scripts/train_critical_minerals_mrv.py", "Simulación CriticalMineralsMRV"),
        ("python3 /home/jaruiz/Desarrollo/scripts/train_emergency_geogrid_rothermel.py", "Simulación EmergencyGeoGrid"),
        ("python3 /home/jaruiz/Desarrollo/scripts/train_zerotrust_ot_mesh.py", "Simulación ZeroTrustOTMesh"),
        ("python3 /home/jaruiz/Desarrollo/scripts/train_green_hydrogen_desal_mpc.py", "Simulación GreenHydrogenDesal MPC"),
        ("python3 /home/jaruiz/Desarrollo/scripts/run_1m_master_pro_simulation_suite.py", "Ejecución Masiva 1M PRO Simulation")
    ]
    
    for cmd, desc in sim_scripts:
        ok, out = run_command(cmd, desc)
        if not ok:
            all_ok = False
            
    # 2. Ejecutar Benchmark Continuo de los 25 Módulos
    ok, out = run_command("python3 /home/jaruiz/Desarrollo/scripts/run_nightly_continuous_benchmark.py --ticks 1000", "Benchmark Continuo Nocturno (25 Módulos)")
    if not ok:
        all_ok = False
        
    # 3. Auditoría de Seguridad SAST
    if os.path.exists("/home/jaruiz/Desarrollo/scripts/run_sast_audit.py"):
        ok, out = run_command("python3 /home/jaruiz/Desarrollo/scripts/run_sast_audit.py", "Auditoría Estática de Seguridad SAST")
        if not ok:
            all_ok = False
            
    # 4. Verificación de Telemetría en SQLite
    db_path = "/home/jaruiz/Desarrollo/simulations_telemetry.db"
    if os.path.exists(db_path):
        conn = sqlite3.connect(db_path)
        cur = conn.cursor()
        cur.execute("SELECT name FROM sqlite_master WHERE type='table';")
        tables = [r[0] for r in cur.fetchall()]
        print(f"\n  ✓ Tablas analíticas registradas en simulations_telemetry.db: {len(tables)}")
        for t in tables:
            cur.execute(f"SELECT COUNT(*) FROM {t};")
            cnt = cur.fetchone()[0]
            print(f"    • {t}: {cnt:,} registros")
        conn.close()
        
    dur_total = time.perf_counter() - start_total
    print("\n==============================================================================")
    if all_ok:
        print(f"🟢 SUITE GLOBAL MULTIPROYECTOS FINALIZADA CON ÉXITO EN {dur_total:.2f}s (0 ERRORES)")
    else:
        print(f"❌ LA SUITE GLOBAL DETECTÓ ERRORES (TIEMPO: {dur_total:.2f}s)")
    print("==============================================================================")
    
    return 0 if all_ok else 1

if __name__ == "__main__":
    sys.exit(main())
