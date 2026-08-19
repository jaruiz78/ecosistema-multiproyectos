"""
naturgy_virtual_battery_controller.py
Centro de Control Financiero y Monedero de Batería Virtual Naturgy Clientes S.A.U.
Ecosistema Solar Tocina - Los Rosales
Autor: Google Antigravity

Características:
1. Soporta 2 estados de operación:
   - STANDBY_CONTRACTED: Contratada formalmente en tarifa Noche Luz ECO pero aún no activa en facturación real (Modo Simulación y Proyección).
   - ACTIVE_BILLING: Activa formalmente y contabilizando excedentes/compensaciones reales.
2. Contabilidad FIFO de saldo con caducidad a 5 Años (60 meses) según contrato Naturgy.
3. Compensación integral de facturas: Energía Importada + Término Fijo de Potencia (4.60 kW) + Alquiler de Contador + Impuestos (IEE 5.1127% + IVA 21%).
4. Tarifa de Compensación: 0.072600 €/kWh (0.060000 €/kWh base + IVA).
"""

import json
import os
from datetime import datetime, date, timedelta
from typing import Dict, Any, List, Optional, Tuple

NATURGY_VB_CONFIG_PATH = os.path.join(os.path.dirname(os.path.abspath(__file__)), "data", "naturgy_virtual_battery_config.json")

DEFAULT_NATURGY_VB_CONFIG = {
    "status": "STANDBY_CONTRACTED", # "STANDBY_CONTRACTED", "ACTIVE_BILLING"
    "contract_info": {
        "company": "Naturgy Clientes S.A.U.",
        "tariff_name": "Tarifa Noche Luz ECO 2.0TD con Batería Virtual",
        "cups": "ES0031104638423001VV0F",
        "holder": "José Antonio Ruiz Arribas",
        "contract_date": "2026-08-19",
        "activation_date": None,
        "surplus_rate_eur_kwh": 0.072600, # 0.06 € + IVA
        "grid_import_valley_eur_kwh": 0.093991, # 0.075 € + Imp
        "contracted_power_p1_kw": 4.60,
        "contracted_power_p2_kw": 4.60,
        "monthly_fixed_cost_eur": 33.87, # Término fijo con impuestos y alquiler contador
        "wallet_validity_years": 5,
        "applies_to_fixed_term": True
    },
    "current_wallet_balance_eur": 0.00,
    "virtual_simulated_wallet_eur": 52.30,
    "ledger_entries": [],
    "last_updated": ""
}

class NaturgyVirtualBatteryController:
    def __init__(self, config_path: str = NATURGY_VB_CONFIG_PATH):
        self.config_path = config_path
        self.config = self.load_config()

    def load_config(self) -> Dict[str, Any]:
        if os.path.exists(self.config_path):
            try:
                with open(self.config_path, "r", encoding="utf-8") as f:
                    data = json.load(f)
                    cfg = json.loads(json.dumps(DEFAULT_NATURGY_VB_CONFIG))
                    cfg.update(data)
                    return cfg
            except Exception as e:
                print(f"[NaturgyVB] Error cargando config: {e}")
        return json.loads(json.dumps(DEFAULT_NATURGY_VB_CONFIG))

    def save_config(self, new_data: Optional[Dict[str, Any]] = None) -> bool:
        if new_data:
            self.config.update(new_data)
        self.config["last_updated"] = datetime.now().isoformat()
        os.makedirs(os.path.dirname(self.config_path), exist_ok=True)
        try:
            with open(self.config_path, "w", encoding="utf-8") as f:
                json.dump(self.config, f, indent=2, ensure_ascii=False)
            return True
        except Exception as e:
            print(f"[NaturgyVB] Error guardando config: {e}")
            return False

    def toggle_activation_status(self, is_active: bool, activation_date: Optional[str] = None) -> Dict[str, Any]:
        new_status = "ACTIVE_BILLING" if is_active else "STANDBY_CONTRACTED"
        self.config["status"] = new_status
        if is_active:
            self.config["contract_info"]["activation_date"] = activation_date or datetime.now().strftime("%Y-%m-%d")
        else:
            self.config["contract_info"]["activation_date"] = None
        
        self.save_config()
        return {
            "success": True,
            "status": new_status,
            "is_active": is_active,
            "activation_date": self.config["contract_info"]["activation_date"],
            "message": "Batería Virtual activada en modo facturación real" if is_active else "Batería Virtual en modo Standby / En Espera"
        }

    def add_monthly_surplus_entry(self, month_year: str, surplus_kwh: float, raw_bill_eur: float) -> Dict[str, Any]:
        surplus_rate = self.config["contract_info"]["surplus_rate_eur_kwh"]
        credit_generated_eur = round(surplus_kwh * surplus_rate, 2)
        
        dt_entry = datetime.strptime(f"{month_year}-01", "%Y-%m-%d")
        expires_at = (dt_entry + timedelta(days=5*365.25)).strftime("%Y-%m-%d")

        current_balance = self.config["current_wallet_balance_eur"] if self.config["status"] == "ACTIVE_BILLING" else self.config["virtual_simulated_wallet_eur"]
        
        available_funds = round(current_balance + credit_generated_eur, 2)
        offset_applied = min(available_funds, raw_bill_eur)
        final_bill_eur = round(max(0.0, raw_bill_eur - offset_applied), 2)
        new_balance = round(available_funds - offset_applied, 2)

        entry = {
            "id": len(self.config["ledger_entries"]) + 1,
            "month_year": month_year,
            "surplus_kwh": surplus_kwh,
            "credit_generated_eur": credit_generated_eur,
            "raw_bill_eur": raw_bill_eur,
            "offset_applied_eur": offset_applied,
            "final_bill_paid_eur": final_bill_eur,
            "wallet_balance_after_eur": new_balance,
            "expires_at": expires_at,
            "is_zero_bill": final_bill_eur == 0.0,
            "recorded_at": datetime.now().isoformat()
        }

        self.config["ledger_entries"].append(entry)
        if self.config["status"] == "ACTIVE_BILLING":
            self.config["current_wallet_balance_eur"] = new_balance
        else:
            self.config["virtual_simulated_wallet_eur"] = new_balance

        self.save_config()
        return entry

    def generate_annual_projection(self) -> Dict[str, Any]:
        months = [
            ("2026-01", "Enero", 716, 460),
            ("2026-02", "Febrero", 588, 540),
            ("2026-03", "Marzo", 406, 720),
            ("2026-04", "Abril", 380, 820),
            ("2026-05", "Mayo", 390, 920),
            ("2026-06", "Junio", 460, 960),
            ("2026-07", "Julio", 580, 990),
            ("2026-08", "Agosto", 590, 950),
            ("2026-09", "Septiembre", 450, 810),
            ("2026-10", "Octubre", 390, 650),
            ("2026-11", "Noviembre", 480, 480),
            ("2026-12", "Diciembre", 650, 410)
        ]

        fixed_cost = self.config["contract_info"]["monthly_fixed_cost_eur"]
        import_rate = self.config["contract_info"]["grid_import_valley_eur_kwh"]
        surplus_rate = self.config["contract_info"]["surplus_rate_eur_kwh"]

        projection = []
        running_wallet = 0.0
        total_paid_year = 0.0
        zero_bill_months_count = 0

        for code, name, home_kwh, solar_kwh in months:
            # Modelo de autarquía con batería Fox-ESS 10 kWh
            is_summer_half = code in ["2026-03", "2026-04", "2026-05", "2026-06", "2026-07", "2026-08", "2026-09", "2026-10"]
            autarky_pct = 0.98 if is_summer_half else 0.72

            self_consumed_kwh = min(home_kwh * autarky_pct, solar_kwh * 0.85)
            import_kwh = max(0, home_kwh - self_consumed_kwh)
            export_kwh = max(0, solar_kwh - self_consumed_kwh)

            energy_cost = import_kwh * import_rate
            raw_bill = round(energy_cost + fixed_cost, 2)
            export_credit = round(export_kwh * surplus_rate, 2)

            available = round(running_wallet + export_credit, 2)
            offset = min(available, raw_bill)
            final_bill = round(max(0.0, raw_bill - offset), 2)
            running_wallet = round(available - offset, 2)

            if final_bill == 0.0:
                zero_bill_months_count += 1
            total_paid_year += final_bill

            projection.append({
                "code": code,
                "month_name": name,
                "home_consumption_kwh": home_kwh,
                "solar_generation_kwh": solar_kwh,
                "grid_import_kwh": round(import_kwh, 1),
                "grid_export_kwh": round(export_kwh, 1),
                "raw_bill_eur": raw_bill,
                "export_credit_eur": export_credit,
                "wallet_balance_end_eur": running_wallet,
                "final_bill_eur": final_bill,
                "is_zero_bill": final_bill == 0.0
            })

        return {
            "status": self.config["status"],
            "is_active": self.config["status"] == "ACTIVE_BILLING",
            "current_wallet_balance_eur": self.config["current_wallet_balance_eur"],
            "virtual_simulated_wallet_eur": self.config["virtual_simulated_wallet_eur"],
            "zero_bill_months_count": zero_bill_months_count,
            "total_paid_year_eur": round(total_paid_year, 2),
            "monthly_average_paid_eur": round(total_paid_year / 12.0, 2),
            "year_end_remaining_wallet_eur": running_wallet,
            "projection_months": projection
        }

    def get_full_system_status(self) -> Dict[str, Any]:
        proj = self.generate_annual_projection()
        return {
            "config": self.config,
            "status": self.config["status"],
            "is_active": self.config["status"] == "ACTIVE_BILLING",
            "contract_info": self.config["contract_info"],
            "projection": proj
        }

naturgy_vb_engine = NaturgyVirtualBatteryController()
