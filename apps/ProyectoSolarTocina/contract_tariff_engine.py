"""
contract_tariff_engine.py
Motor de Gestión y Versionado Histórico de Contratos Eléctricos (Multi-Tariff Timeline Engine)
Registra fielmente las 3 etapas contractuales de la vivienda:
1. Endesa / Energía XXI (Junio 2014 - Diciembre 2025)
2. El Corte Inglés Energía / Telecor S.A. (Diciembre 2025 - 18 Agosto 2026)
3. Naturgy Clientes S.A.U. (19 Agosto 2026 en adelante - ACTIVO)

Stack: Python 3 / SQLite WAL / O(1) Binary Search por fecha
"""

import os
import json
import sqlite3
from datetime import datetime, date, timedelta
from contextlib import contextmanager

DB_PATH = os.path.join(os.path.dirname(os.path.abspath(__file__)), "data", "telemetry_history.db")

@contextmanager
def get_db():
    conn = sqlite3.connect(DB_PATH, timeout=15.0)
    conn.row_factory = sqlite3.Row
    try:
        yield conn
    finally:
        try:
            conn.close()
        except Exception:
            pass

def init_contracts_schema():
    with get_db() as conn:
        conn.execute("""
            CREATE TABLE IF NOT EXISTS electricity_contracts_history (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                company_name TEXT NOT NULL,
                tariff_product_name TEXT NOT NULL,
                cups TEXT NOT NULL,
                holder_name TEXT NOT NULL,
                holder_nif TEXT NOT NULL,
                supply_address TEXT NOT NULL,
                valid_from TEXT NOT NULL, -- ISO8601 YYYY-MM-DD HH:MM:SS
                valid_to TEXT,            -- ISO8601 or NULL if currently active
                power_p1_kw REAL NOT NULL,
                power_p2_kw REAL NOT NULL,
                power_price_p1_eur_day REAL NOT NULL, -- sin impuestos
                power_price_p2_eur_day REAL NOT NULL, -- sin impuestos
                energy_price_p1_eur_kwh REAL NOT NULL, -- sin impuestos
                energy_price_p2_eur_kwh REAL NOT NULL, -- sin impuestos
                energy_price_p3_eur_kwh REAL NOT NULL, -- sin impuestos
                surplus_comp_eur_kwh REAL NOT NULL,   -- sin impuestos
                virtual_battery_enabled BOOLEAN NOT NULL DEFAULT 0,
                virtual_battery_fee_month REAL NOT NULL DEFAULT 0.0,
                virtual_battery_applies_to_fixed BOOLEAN NOT NULL DEFAULT 1,
                virtual_battery_validity_years INTEGER NOT NULL DEFAULT 5,
                bono_social_annual_eur REAL NOT NULL DEFAULT 9.011295,
                iva_rate REAL NOT NULL DEFAULT 0.21,
                iee_rate REAL NOT NULL DEFAULT 0.051127,
                notes TEXT
            );
        """)
        
        # Limpiar y re-poblar para garantizar la separación exacta de los 3 contratos
        conn.execute("DELETE FROM electricity_contracts_history")
        
        # 1. Contrato 1: Endesa Energía XXI (Junio 2014 - 15 Diciembre 2025)
        conn.execute("""
            INSERT INTO electricity_contracts_history (
                company_name, tariff_product_name, cups, holder_name, holder_nif, supply_address,
                valid_from, valid_to, power_p1_kw, power_p2_kw,
                power_price_p1_eur_day, power_price_p2_eur_day,
                energy_price_p1_eur_kwh, energy_price_p2_eur_kwh, energy_price_p3_eur_kwh,
                surplus_comp_eur_kwh, virtual_battery_enabled, virtual_battery_fee_month,
                virtual_battery_applies_to_fixed, virtual_battery_validity_years,
                bono_social_annual_eur, iva_rate, iee_rate, notes
            ) VALUES (
                'Endesa / Energía XXI', 'Tarifa Regulada PVPC (2.0A / 2.0TD)', 'ES0031104638423001VV0F',
                'José Antonio Ruiz Arribas', '44361953J', 'Calle Amadeo Vives 31, Los Rosales - Tocina (Sevilla)',
                '2014-06-01 00:00:00', '2025-12-15 23:59:59', 4.60, 4.60,
                0.108000, 0.051000,
                0.195000, 0.135000, 0.098000,
                0.045000, 0, 0.0,
                0, 0,
                0.0, 0.21, 0.051127,
                '136 facturas históricas acumuladas (12.337,92 €). Mercado regulado sin batería virtual.'
            )
        """)

        # 2. Contrato 2: El Corte Inglés Energía / Telecor S.A. (16 Diciembre 2025 - 18 Agosto 2026)
        conn.execute("""
            INSERT INTO electricity_contracts_history (
                company_name, tariff_product_name, cups, holder_name, holder_nif, supply_address,
                valid_from, valid_to, power_p1_kw, power_p2_kw,
                power_price_p1_eur_day, power_price_p2_eur_day,
                energy_price_p1_eur_kwh, energy_price_p2_eur_kwh, energy_price_p3_eur_kwh,
                surplus_comp_eur_kwh, virtual_battery_enabled, virtual_battery_fee_month,
                virtual_battery_applies_to_fixed, virtual_battery_validity_years,
                bono_social_annual_eur, iva_rate, iee_rate, notes
            ) VALUES (
                'El Corte Inglés Energía (Telecor S.A.)', 'Tarifa Luz El Corte Inglés 2.0TD', 'ES0031104638423001VV0F',
                'José Antonio Ruiz Arribas', '44361953J', 'Calle Amadeo Vives 31, Los Rosales - Tocina (Sevilla)',
                '2025-12-16 00:00:00', '2026-08-18 23:59:59', 4.60, 4.60,
                0.119500, 0.058000,
                0.178000, 0.125000, 0.088000,
                0.055000, 0, 0.0,
                0, 0,
                4.850000, 0.21, 0.051127,
                'Contrato de transición en mercado libre con Telecor S.A. (Facturas de Enero a Agosto 2026).'
            )
        """)

        # 3. Contrato 3: Naturgy Clientes, S.A.U. (19 Agosto 2026 en adelante - ACTIVO)
        conn.execute("""
            INSERT INTO electricity_contracts_history (
                company_name, tariff_product_name, cups, holder_name, holder_nif, supply_address,
                valid_from, valid_to, power_p1_kw, power_p2_kw,
                power_price_p1_eur_day, power_price_p2_eur_day,
                energy_price_p1_eur_kwh, energy_price_p2_eur_kwh, energy_price_p3_eur_kwh,
                surplus_comp_eur_kwh, virtual_battery_enabled, virtual_battery_fee_month,
                virtual_battery_applies_to_fixed, virtual_battery_validity_years,
                bono_social_annual_eur, iva_rate, iee_rate, notes
            ) VALUES (
                'Naturgy Clientes, S.A.U.', 'Tarifa Noche Luz ECO 2.0TD', 'ES0031104638423001VV0F',
                'José Antonio Ruiz Arribas', '44361953J', 'Calle Amadeo Vives 31, Los Rosales - Tocina (Sevilla)',
                '2026-08-19 00:00:00', NULL, 4.60, 4.60,
                0.123030, 0.061562,
                0.182200, 0.109200, 0.073900,
                0.060000, 1, 0.0,
                1, 5,
                9.011295, 0.21, 0.051127,
                'Contrato activo con Batería Virtual (0€/mes, 5 años caducidad) y compensación a 0.06 €/kWh (+IVA).'
            )
        """)
        conn.commit()

init_contracts_schema()

class ContractTariffEngine:
    def __init__(self):
        pass

    def get_contract_for_datetime(self, dt: datetime) -> dict:
        """
        Obtiene las condiciones exactas del contrato aplicable a una fecha/hora dada.
        """
        dt_str = dt.strftime("%Y-%m-%d %H:%M:%S")
        with get_db() as conn:
            cur = conn.cursor()
            cur.execute("""
                SELECT * FROM electricity_contracts_history
                WHERE valid_from <= ? AND (valid_to IS NULL OR valid_to >= ?)
                ORDER BY valid_from DESC
                LIMIT 1
            """, (dt_str, dt_str))
            row = cur.fetchone()
            if row:
                return dict(row)
            
            cur.execute("SELECT * FROM electricity_contracts_history ORDER BY id DESC LIMIT 1")
            last_row = cur.fetchone()
            return dict(last_row) if last_row else {}

    def get_active_contract(self) -> dict:
        """Devuelve el contrato actualmente en vigor (Naturgy)"""
        return self.get_contract_for_datetime(datetime.now())

    def get_all_contracts(self) -> list:
        """Devuelve el historial cronológico completo de los 3 contratos"""
        contracts = []
        with get_db() as conn:
            cur = conn.cursor()
            cur.execute("SELECT * FROM electricity_contracts_history ORDER BY valid_from ASC")
            for row in cur.fetchall():
                c = dict(row)
                iva = c["iva_rate"]
                iee = c["iee_rate"]
                tax_mult = (1 + iee) * (1 + iva)
                c["power_price_p1_tax"] = round(c["power_price_p1_eur_day"] * tax_mult, 6)
                c["power_price_p2_tax"] = round(c["power_price_p2_eur_day"] * tax_mult, 6)
                c["energy_price_p1_tax"] = round(c["energy_price_p1_eur_kwh"] * tax_mult, 6)
                c["energy_price_p2_tax"] = round(c["energy_price_p2_eur_kwh"] * tax_mult, 6)
                c["energy_price_p3_tax"] = round(c["energy_price_p3_eur_kwh"] * tax_mult, 6)
                c["surplus_comp_tax"] = round(c["surplus_comp_eur_kwh"] * (1 + iva), 6)
                contracts.append(c)
        return contracts

    def get_period_for_datetime(self, dt: datetime) -> str:
        if dt.weekday() >= 5:
            return "P3"
        hour = dt.hour
        if 0 <= hour < 8:
            return "P3"
        elif (10 <= hour < 14) or (18 <= hour < 22):
            return "P1"
        else:
            return "P2"

    def calculate_cost_for_slice(self, dt: datetime, import_kwh: float, export_kwh: float) -> dict:
        contract = self.get_contract_for_datetime(dt)
        period = self.get_period_for_datetime(dt)
        
        iva = contract.get("iva_rate", 0.21)
        iee = contract.get("iee_rate", 0.051127)
        tax_multiplier = (1 + iee) * (1 + iva)

        if period == "P1":
            price_pretax = contract.get("energy_price_p1_eur_kwh", 0.1822)
        elif period == "P2":
            price_pretax = contract.get("energy_price_p2_eur_kwh", 0.1092)
        else:
            price_pretax = contract.get("energy_price_p3_eur_kwh", 0.0739)

        price_with_tax = price_pretax * tax_multiplier
        surplus_price_pretax = contract.get("surplus_comp_eur_kwh", 0.06)
        surplus_price_with_tax = surplus_price_pretax * (1 + iva)

        import_cost_eur = import_kwh * price_with_tax
        export_credit_eur = export_kwh * surplus_price_with_tax

        return {
            "period": period,
            "contract_name": contract.get("tariff_product_name"),
            "company": contract.get("company_name"),
            "price_per_kwh_tax": round(price_with_tax, 4),
            "surplus_price_tax": round(surplus_price_with_tax, 4),
            "import_cost_eur": round(import_cost_eur, 4),
            "export_credit_eur": round(export_credit_eur, 4),
            "net_eur": round(import_cost_eur - export_credit_eur, 4)
        }

    def simulate_month_bill(self, year: int, month: int, import_by_period_kwh: dict, export_kwh: float, initial_wallet_eur: float = 0.0) -> dict:
        ref_dt = datetime(year, month, 15, 12, 0, 0)
        contract = self.get_contract_for_datetime(ref_dt)
        
        import calendar
        days_in_month = calendar.monthrange(year, month)[1]
        
        iva = contract.get("iva_rate", 0.21)
        iee = contract.get("iee_rate", 0.051127)
        tax_mult = (1 + iee) * (1 + iva)

        p1_kw = contract.get("power_p1_kw", 4.60)
        p2_kw = contract.get("power_p2_kw", 4.60)
        p1_rate = contract.get("power_price_p1_eur_day", 0.123030)
        p2_rate = contract.get("power_price_p2_eur_day", 0.061562)
        
        power_cost_pretax = (p1_kw * p1_rate * days_in_month) + (p2_kw * p2_rate * days_in_month)
        
        kwh_p1 = import_by_period_kwh.get("P1", 0.0)
        kwh_p2 = import_by_period_kwh.get("P2", 0.0)
        kwh_p3 = import_by_period_kwh.get("P3", 0.0)
        
        energy_cost_pretax = (
            kwh_p1 * contract.get("energy_price_p1_eur_kwh", 0.1822) +
            kwh_p2 * contract.get("energy_price_p2_eur_kwh", 0.1092) +
            kwh_p3 * contract.get("energy_price_p3_eur_kwh", 0.0739)
        )
        
        surplus_rate = contract.get("surplus_comp_eur_kwh", 0.06)
        total_surplus_credit_pretax = export_kwh * surplus_rate
        
        direct_energy_discount_pretax = min(energy_cost_pretax, total_surplus_credit_pretax)
        surplus_to_virtual_wallet_pretax = total_surplus_credit_pretax - direct_energy_discount_pretax
        net_energy_pretax = energy_cost_pretax - direct_energy_discount_pretax
        
        bono_social_pretax = (contract.get("bono_social_annual_eur", 9.011295) / 365.0) * days_in_month
        
        subtotal_pretax = power_cost_pretax + net_energy_pretax + bono_social_pretax
        iee_tax = subtotal_pretax * iee
        base_imponible = subtotal_pretax + iee_tax
        iva_tax = base_imponible * iva
        total_bill_before_wallet = base_imponible + iva_tax
        
        wallet_surplus_eur = surplus_to_virtual_wallet_pretax * (1 + iva)
        new_wallet_balance = initial_wallet_eur + (wallet_surplus_eur if contract.get("virtual_battery_enabled") else 0.0)
        
        bill_to_pay = total_bill_before_wallet
        applied_from_wallet = 0.0
        
        if contract.get("virtual_battery_enabled") and new_wallet_balance > 0:
            if new_wallet_balance >= total_bill_before_wallet:
                applied_from_wallet = total_bill_before_wallet
                new_wallet_balance -= total_bill_before_wallet
                bill_to_pay = 0.00
            else:
                applied_from_wallet = new_wallet_balance
                bill_to_pay = total_bill_before_wallet - new_wallet_balance
                new_wallet_balance = 0.00

        return {
            "year": year,
            "month": month,
            "contract": contract.get("tariff_product_name"),
            "company": contract.get("company_name"),
            "days": days_in_month,
            "power_cost_eur": round(power_cost_pretax * tax_mult, 2),
            "energy_cost_gross_eur": round(energy_cost_pretax * tax_mult, 2),
            "surplus_generated_eur": round(total_surplus_credit_pretax * (1 + iva), 2),
            "direct_discount_eur": round(direct_energy_discount_pretax * tax_mult, 2),
            "wallet_applied_eur": round(applied_from_wallet, 2),
            "final_bill_to_pay_eur": round(bill_to_pay, 2),
            "remaining_wallet_balance_eur": round(new_wallet_balance, 2)
        }

tariff_engine = ContractTariffEngine()
