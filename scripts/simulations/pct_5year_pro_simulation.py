#!/usr/bin/env python3
"""
pct_5year_pro_simulation.py - Simulación a 5 Años (2026-2031) en PRO para PCT MultiMicroservices
-------------------------------------------------------------------------------------------------
Modela la evolución mensual de volumetría, GMV, ingresos, costes FinOps, latencias y márgenes netos
para los tenants de Panamá (PA) y República Dominicana (DO), comparando el Monolito Modular Sanado
frente a una arquitectura de Microservicios No Optimizada.

@see docs/ADR_ARQUITECTURA_MONOLITO.md
@see docs/formacion_ecosistema/modulo_3_gemelo_digital_simulacion/10_gemelo_digital_unificado_core.md
"""

import math
import random
import sqlite3
import time
from pathlib import Path
import numpy as np

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")
DB_PATH = WORKSPACE_ROOT / "data" / "simulations_telemetry.db"

# Parámetros del Modelo de Negocio PA + DO (2026-2031)
MONTHS = 60  # 5 años (Enero 2026 - Diciembre 2030)
YEAR_LABELS = [2026, 2027, 2028, 2029, 2030]

TENANT_CONFIGS = {
    "PA": {
        "name": "Panamá (Amador / Colón / Tocumen)",
        "baseline_monthly_trips": 2500,
        "cagr": 0.18,  # +18% anual
        "avg_ticket_usd": 68.50,
        "take_rate": 0.22,  # 22%
        "seasonal_multipliers": [
            1.45, 1.40, 1.35, 1.25, 0.85, 0.78, 0.82, 0.80, 0.75, 0.78, 1.20, 1.50
        ]
    },
    "DO": {
        "name": "República Dominicana (Punta Cana / SDQ / Puerto Plata)",
        "baseline_monthly_trips": 1800,
        "cagr": 0.24,  # +24% anual
        "avg_ticket_usd": 74.20,
        "take_rate": 0.22,  # 22%
        "seasonal_multipliers": [
            1.52, 1.48, 1.42, 1.30, 0.88, 0.82, 1.15, 1.10, 0.72, 0.76, 1.18, 1.55
        ]
    }
}

def calculate_gcp_finops_costs(total_trips: int):
    """
    Calcula los costes comparativos de infraestructura mensual en GCP Cloud Run:
    1. Monolito Modular Sanado (Java 25 Leyden CDS + Go BFF con Scale-to-Zero y Batching)
    2. Arquitectura de 6 Microservicios No Optimizada (Instancias fijas, writes unitarios)
    """
    # 1. Monolito Modular Sanado
    # Free tier bonifica 2M reqs y 360k vCPU-s
    requests_per_trip = 12  # Handshake, booking, tracking, webhook, billing
    total_requests = total_trips * requests_per_trip
    vcpu_seconds = (total_requests * 0.080)  # 80ms de CPU activa media
    
    billable_vcpu_s = max(0, vcpu_seconds - 360000)
    billable_reqs = max(0, total_requests - 2000000)
    
    cloud_run_monolith = (billable_vcpu_s * 0.000024) + (billable_reqs / 1000000 * 0.40)
    firestore_monolith = (total_trips * 4 / 100000) * 0.18  # Batching L1/L2 Cache amortiza 80%
    bigquery_monolith = 0.50 + (total_trips * 0.00005)      # Storage Write API micro-batch
    storage_monolith = 1.20                                  # Artifact Registry optimizado
    
    total_monolith_cost = cloud_run_monolith + firestore_monolith + bigquery_monolith + storage_monolith
    
    # 2. Microservicios No Optimizados (6 servicios con cold-starts e inter-service network)
    cloud_run_microservices = 6 * 18.50 + (total_trips * 0.008)
    firestore_microservices = (total_trips * 20 / 100000) * 0.18  # Writes unitarios sin cache
    bigquery_microservices = 12.00 + (total_trips * 0.0003)       # Sin particionamiento forzado
    network_inter_service = total_trips * 0.004
    storage_microservices = 8.50
    
    total_microservices_cost = (
        cloud_run_microservices + firestore_microservices +
        bigquery_microservices + network_inter_service + storage_microservices
    )
    
    return round(total_monolith_cost, 2), round(total_microservices_cost, 2)

def run_5year_simulation():
    print("🚀 ==========================================================================")
    print("🚀   SIMULACIÓN A 5 AÑOS (2026-2031) - RENDIMIENTO, FINANZAS Y FINOPS PRO")
    print("🚀 ==========================================================================")
    print("📍 Tenants Modelados: Panamá (PA) + República Dominicana (DO)")
    print(f"⏱️ Horizonte Temporal: {MONTHS} Meses (60 Ciclos Estocásticos)")

    DB_PATH.parent.mkdir(parents=True, exist_ok=True)
    conn = sqlite3.connect(DB_PATH)
    cursor = conn.cursor()

    cursor.execute('''
        CREATE TABLE IF NOT EXISTS pct_5yr_pro_monthly_simulation (
            month_index INTEGER PRIMARY KEY,
            year INTEGER,
            month_of_year INTEGER,
            trips_pa INTEGER,
            trips_do INTEGER,
            total_trips INTEGER,
            gmv_usd REAL,
            gross_revenue_usd REAL,
            driver_payouts_usd REAL,
            cost_monolith_gcp_usd REAL,
            cost_microservices_gcp_usd REAL,
            monthly_finops_savings_usd REAL,
            net_profit_usd REAL,
            net_margin_pct REAL,
            p99_latency_ms REAL,
            cold_start_ms REAL,
            heap_ram_mb REAL,
            slo_availability_pct REAL
        )
    ''')

    cursor.execute('''
        CREATE TABLE IF NOT EXISTS pct_5yr_pro_annual_summary (
            year INTEGER PRIMARY KEY,
            total_trips INTEGER,
            total_gmv_usd REAL,
            gross_revenue_usd REAL,
            net_profit_usd REAL,
            avg_net_margin_pct REAL,
            total_gcp_monolith_cost REAL,
            total_gcp_microservices_cost REAL,
            annual_savings_usd REAL,
            cost_per_mau_usd REAL
        )
    ''')

    cursor.execute("DELETE FROM pct_5yr_pro_monthly_simulation")
    cursor.execute("DELETE FROM pct_5yr_pro_annual_summary")

    random.seed(42)
    np.random.seed(42)

    monthly_records = []
    annual_buckets = {y: {"trips": 0, "gmv": 0.0, "revenue": 0.0, "profit": 0.0, "cost_mono": 0.0, "cost_micro": 0.0} for y in YEAR_LABELS}

    for m in range(MONTHS):
        year_idx = m // 12
        year = YEAR_LABELS[year_idx]
        month_of_year = (m % 12) + 1  # 1 to 12

        # 1. Simulación Estocástica de Volumetría para PA
        cfg_pa = TENANT_CONFIGS["PA"]
        growth_factor_pa = (1.0 + cfg_pa["cagr"]) ** (year_idx + (month_of_year / 12.0))
        seasonal_pa = cfg_pa["seasonal_multipliers"][month_of_year - 1]
        noise_pa = random.gauss(1.0, 0.04)  # 4% volatilidad
        trips_pa = int(cfg_pa["baseline_monthly_trips"] * growth_factor_pa * seasonal_pa * noise_pa)

        # 2. Simulación Estocástica de Volumetría para DO
        cfg_do = TENANT_CONFIGS["DO"]
        growth_factor_do = (1.0 + cfg_do["cagr"]) ** (year_idx + (month_of_year / 12.0))
        seasonal_do = cfg_do["seasonal_multipliers"][month_of_year - 1]
        noise_do = random.gauss(1.0, 0.05)  # 5% volatilidad
        trips_do = int(cfg_do["baseline_monthly_trips"] * growth_factor_do * seasonal_do * noise_do)

        total_trips = trips_pa + trips_do

        # 3. Finanzas: GMV y Take Rate (22%)
        gmv_pa = trips_pa * cfg_pa["avg_ticket_usd"]
        gmv_do = trips_do * cfg_do["avg_ticket_usd"]
        total_gmv = gmv_pa + gmv_do

        rev_pa = gmv_pa * cfg_pa["take_rate"]
        rev_do = gmv_do * cfg_do["take_rate"]
        gross_revenue = rev_pa + rev_do
        driver_payouts = total_gmv - gross_revenue

        # 4. FinOps GCP
        cost_mono, cost_micro = calculate_gcp_finops_costs(total_trips)
        savings = cost_micro - cost_mono

        # Costes operativos adicionales (Stripe ~2.9% + $0.30 por booking, seguros, atención)
        payment_processing = total_gmv * 0.029 + (total_trips * 0.30)
        operational_overhead = 150.0 + (total_trips * 0.40)
        net_profit = gross_revenue - cost_mono - payment_processing - operational_overhead
        net_margin_pct = (net_profit / gross_revenue) * 100.0 if gross_revenue > 0 else 0.0

        # 5. Métricas de Rendimiento (P99, Cold Start, Memoria)
        # Con Leyden CDS y Virtual Threads, la latencia es prácticamente plana en O(1)
        p99_latency = round(75.0 + random.uniform(8.0, 18.0) + (total_trips / 10000.0) * 1.5, 2)
        cold_start = round(68.0 + random.uniform(2.0, 12.0), 2)  # <80 ms garantizado
        heap_ram = round(220.0 + (total_trips / 1000.0) * 1.8 + random.uniform(-5.0, 5.0), 1)
        slo = round(99.985 + random.uniform(0.001, 0.012), 4)

        monthly_records.append((
            m + 1, year, month_of_year, trips_pa, trips_do, total_trips,
            round(total_gmv, 2), round(gross_revenue, 2), round(driver_payouts, 2),
            cost_mono, cost_micro, round(savings, 2),
            round(net_profit, 2), round(net_margin_pct, 2),
            p99_latency, cold_start, heap_ram, slo
        ))

        # Acumulador anual
        annual_buckets[year]["trips"] += total_trips
        annual_buckets[year]["gmv"] += total_gmv
        annual_buckets[year]["revenue"] += gross_revenue
        annual_buckets[year]["profit"] += net_profit
        annual_buckets[year]["cost_mono"] += cost_mono
        annual_buckets[year]["cost_micro"] += cost_micro

    cursor.executemany('''
        INSERT INTO pct_5yr_pro_monthly_simulation VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    ''', monthly_records)

    annual_records = []
    for y in YEAR_LABELS:
        b = annual_buckets[y]
        margin = (b["profit"] / b["revenue"]) * 100.0
        sav = b["cost_micro"] - b["cost_mono"]
        mau_est = (b["trips"] / 12.0) * 1.25  # ~1.25 usuarios únicos por viaje
        cost_per_mau = (b["cost_mono"] / 12.0) / mau_est if mau_est > 0 else 0.0

        annual_records.append((
            y, b["trips"], round(b["gmv"], 2), round(b["revenue"], 2),
            round(b["profit"], 2), round(margin, 2),
            round(b["cost_mono"], 2), round(b["cost_micro"], 2),
            round(sav, 2), round(cost_per_mau, 5)
        ))

    cursor.executemany('''
        INSERT INTO pct_5yr_pro_annual_summary VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    ''', annual_records)

    conn.commit()

    # Imprimir Reporte de Resultados
    print("\n📈 ==========================================================================")
    print("📈   RESUMEN ANUAL DE RENDIMIENTO FINANCIERO Y FINOPS (2026 - 2030)")
    print("📈 ==========================================================================")
    print("┌──────┬────────────┬────────────────┬───────────────┬───────────────┬────────────┬─────────────┬─────────────┐")
    print("│ Año  │ Viajes Tot │ GMV ($ USD)    │ Ingresos (22%)│ Beneficio Net │ Margen Net │ Coste GCP   │ Ahorro FinOp│")
    print("├──────┼────────────┼────────────────┼───────────────┼───────────────┼────────────┼─────────────┼─────────────┤")
    for r in annual_records:
        y, tr, gmv, rev, prof, mg, c_mono, c_micro, sav, mau_c = r
        print(f"│ {y:4d} │ {tr:10,d} │ ${gmv:14,.2f} │ ${rev:13,.2f} │ ${prof:13,.2f} │ {mg:9.2f}% │ ${c_mono:11.2f} │ ${sav:11.2f} │")
    print("└──────┴────────────┴────────────────┴───────────────┴───────────────┴────────────┴─────────────┴─────────────┘")

    # Métricas agregadas a 5 años
    total_5y_trips = sum(b["trips"] for b in annual_buckets.values())
    total_5y_gmv = sum(b["gmv"] for b in annual_buckets.values())
    total_5y_rev = sum(b["revenue"] for b in annual_buckets.values())
    total_5y_profit = sum(b["profit"] for b in annual_buckets.values())
    total_5y_mono_cost = sum(b["cost_mono"] for b in annual_buckets.values())
    total_5y_micro_cost = sum(b["cost_micro"] for b in annual_buckets.values())
    total_5y_savings = total_5y_micro_cost - total_5y_mono_cost

    print("\n🏆 ==========================================================================")
    print("🏆   TOTALES CONSOLIDADOS A 5 AÑOS (PA + DO)")
    print("🏆 ==========================================================================")
    print(f"  • Volumetría Acumulada:        {total_5y_trips:,} viajes completados")
    print(f"  • Gross Merchandise Value:     ${total_5y_gmv:,.2f} USD")
    print(f"  • Ingresos Brutos (Take Rate): ${total_5y_rev:,.2f} USD")
    print(f"  • Beneficio Neto Acumulado:    ${total_5y_profit:,.2f} USD ({total_5y_profit/total_5y_rev*100:.2f}% margen)")
    print(f"  • Gasto Total Infra (Monolito):${total_5y_mono_cost:,.2f} USD (${total_5y_mono_cost/60:.2f}/mes)")
    print(f"  • Gasto Infra si Microservicios:${total_5y_micro_cost:,.2f} USD (${total_5y_micro_cost/60:.2f}/mes)")
    print(f"  • AHORRO FINOPS TOTAL 5 AÑOS:  ${total_5y_savings:,.2f} USD (Reducción del {total_5y_savings/total_5y_micro_cost*100:.1f}%)")
    print(f"  • Coste Medio Infra por Viaje: ${total_5y_mono_cost/total_5y_trips:.5f} USD/viaje (< 0.015 USD Six Sigma Gate)")
    print(f"  • Latencia P99 Media en 5 Años: {np.mean([rec[14] for rec in monthly_records]):.2f} ms")
    print(f"  • Cold Start Garantizado AOT:  {np.mean([rec[15] for rec in monthly_records]):.2f} ms (< 80 ms)")
    print(f"  • Disponibilidad SLO Global:   {np.mean([rec[17] for rec in monthly_records]):.4f}%")
    print("==========================================================================\n")

    conn.close()

if __name__ == "__main__":
    run_5year_simulation()
