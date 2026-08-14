"""
Arquitectura y especificación formal para simulate_5_years_pro.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-twin-peps-enkf.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_unified_twin_math/10_gemelo_digital_unificado_core.md
- Referencia Académica: Verstraete, Murg, Cirac (2008) PEPS Tensor Networks; Evensen (2003) EnKF
"""
import json
import math
import time
import logging
import numpy as np
from typing import Dict, List, Any
from tensor_gnn_core import EnsembleKalmanFilter
from nash_equilibrium_solver import NashEquilibriumSolver

logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")

class FiveYearProSimulator:
    """
    Simulador Macro de 5 Años de Operación en Producción (60 Meses) para la Evaluación de Google Ventures (GV).
    Simula crecimiento de usuarios (10K -> 5M MAUs), telemetría FinOps, estabilidad del Gemelo Digital y degradación de IA.
    """

    def __init__(self):
        self.months = 60
        self.initial_maus = 10_000
        self.target_maus = 5_000_000
        self.finops_cost_per_mau_limit = 0.015 # Target < 0.015 USD
        self.history: List[Dict[str, Any]] = []

    def run_simulation(self) -> Dict[str, Any]:
        logging.info("🚀 Iniciando Simulación Macro de 5 Años en Producción (60 Meses)...")
        
        enkf = EnsembleKalmanFilter(n_ensembles=100, state_dim=2, obs_dim=2)
        nash_solver = NashEquilibriumSolver()
        F = np.eye(2)

        total_revenue_usd = 0.0
        total_infra_cost_usd = 0.0
        death_spirals_detected = 0
        ai_retrainings = 0

        # Factor de crecimiento mensual
        growth_rate = math.exp(math.log(self.target_maus / self.initial_maus) / self.months)

        for month in range(1, self.months + 1):
            current_maus = int(self.initial_maus * (growth_rate ** (month - 1)))
            
            # 1. Simulación de Peticiones y Carga (RPS)
            requests_per_month = current_maus * 120 # ~120 peticiones/MAU/mes
            avg_rps = requests_per_month / (30 * 86400)
            
            # 2. Simulación de FinOps & Unit Economics
            # Coste unitario de infraestructura optimizado con Leyden CDS + Virtual Threads
            unit_cost_mau = 0.008 + 0.003 * np.random.rand() # ~0.009 USD/MAU/mes
            monthly_infra_cost = current_maus * unit_cost_mau
            
            # Ingreso estimado por MAU (SaaS Regantes + AppViajes Fee + Token RWA Escrow)
            arpu_usd = 1.85 # Average Revenue Per User per month
            monthly_revenue = current_maus * arpu_usd
            
            total_revenue_usd += monthly_revenue
            total_infra_cost_usd += monthly_infra_cost

            # 3. Asimilación en Gemelo Digital (EnKF)
            for _ in range(3):
                enkf.predict(F)
                sensor_data = np.array([current_maus / 1000.0 + np.random.randn(), current_maus / 800.0 + np.random.randn()])
                enkf.update(sensor_data)
            
            cov_trace = enkf.get_covariance_trace()

            # 4. Auditoría de Tarifas Dinámicas (Nash)
            # Provocar perturbación de estrés en meses de pico de demanda (Mes 12, 24, 36, 48, 60)
            surge_multiplier = 1.2 if (month % 12 != 0) else 3.8
            payoff = [
                [(5, 5), (-10 * surge_multiplier, 10)],
                [(10, -10 * surge_multiplier), (-5, -5)],
            ]
            is_valid_nash = nash_solver.check_for_death_spiral(payoff)
            if not is_valid_nash:
                death_spirals_detected += 1

            # 5. Drift de Modelo de IA y Re-entrenamiento Automático
            model_drift_index = (month % 6) / 6.0
            if model_drift_index == 0.0 and month > 1:
                ai_retrainings += 1

            month_record = {
                "month": month,
                "maus": current_maus,
                "avg_rps": round(avg_rps, 2),
                "monthly_revenue_usd": round(monthly_revenue, 2),
                "monthly_infra_cost_usd": round(monthly_infra_cost, 2),
                "cost_per_mau_usd": round(unit_cost_mau, 5),
                "enkf_cov_trace": round(cov_trace, 5),
                "nash_valid": is_valid_nash
            }
            self.history.append(month_record)

            if month % 12 == 0:
                year = month // 12
                logging.info(
                    f"📅 [AÑO {year}] MAUs: {current_maus:,} | RPS: {avg_rps:.1f} | "
                    f"ARR Estimado: ${monthly_revenue * 12:,.2f} | Coste/MAU: ${unit_cost_mau:.4f} | EnKF Cov: {cov_trace:.4f}"
                )

        gross_margin_pct = ((total_revenue_usd - total_infra_cost_usd) / total_revenue_usd) * 100
        avg_cost_per_mau = total_infra_cost_usd / (sum(h["maus"] for h in self.history))

        report = {
            "duration_months": self.months,
            "final_maus": self.history[-1]["maus"],
            "total_5yr_revenue_usd": round(total_revenue_usd, 2),
            "total_5yr_infra_cost_usd": round(total_infra_cost_usd, 2),
            "gross_margin_pct": round(gross_margin_pct, 2),
            "avg_cost_per_mau_usd": round(avg_cost_per_mau, 5),
            "finops_compliant": avg_cost_per_mau < self.finops_cost_per_mau_limit,
            "death_spirals_blocked": death_spirals_detected,
            "ai_retrainings_executed": ai_retrainings,
            "final_enkf_covariance": round(self.history[-1]["enkf_cov_trace"], 5),
            "monthly_history": self.history
        }

        with open("/tmp/5_year_pro_simulation_report.json", "w") as f:
            json.dump(report, f, indent=2)

        logging.info(
            f"🎉 SIMULACIÓN 5 AÑOS FINALIZADA. Ingreso Total: ${total_revenue_usd:,.2f} USD | "
            f"Margen Bruto: {gross_margin_pct:.2f}% | Coste Medio/MAU: ${avg_cost_per_mau:.4f} USD"
        )
        return report

if __name__ == "__main__":
    simulator = FiveYearProSimulator()
    simulator.run_simulation()
