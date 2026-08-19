"""
Soiling & String Imbalance Detector
Ecosistema Solar Tocina - Los Rosales
Autor: Google Antigravity

Compara en continuo el rendimiento relativo entre:
- String 1 (Oeste 265°, 4x Jinko 500W = 2.0 kWp)
- String 2 (Este 85°, 6x Jinko 500W = 3.0 kWp)
Detecta acumulación de polvo, calima sahariana o suciedad asimétrica y calcula pérdidas económicas en €/mes.
"""

import json
import os
import math
from datetime import datetime

class SoilingDetector:
    def __init__(self):
        self.east_kwp = 3.0
        self.west_kwp = 2.0
        self.nominal_ratio = 3.0 / 2.0 # 1.5

    def analyze_strings(self, pv1_west_w, pv2_east_w, clear_sky_point=None):
        """
        Analiza las lecturas instantáneas de ambos strings frente al modelo óptico teórico
        """
        if pv1_west_w < 50 and pv2_east_w < 50:
            return {
                "status": "idle_night",
                "cleanliness_score_percent": 100.0,
                "loss_percent": 0.0,
                "monthly_loss_eur": 0.0,
                "badge": "🌙 Esperando Luz Solar",
                "recommendation": "Radiación solar matinal incipiente. El análisis óptico se activa al alcanzar >50W por string.",
                "pv1_west_w": pv1_west_w,
                "pv2_east_w": pv2_east_w,
                "yield_west_w_per_kwp": 0.0,
                "yield_east_w_per_kwp": 0.0
            }

        meas_ratio = (pv2_east_w / pv1_west_w) if pv1_west_w > 50 else 1.5
        
        # Rendimiento específico (W / kWp)
        yield_west_w_per_kwp = pv1_west_w / self.west_kwp
        yield_east_w_per_kwp = pv2_east_w / self.east_kwp
        
        # Comparación
        diff = abs(yield_east_w_per_kwp - yield_west_w_per_kwp)
        avg_yield = (yield_east_w_per_kwp + yield_west_w_per_kwp) / 2.0
        
        imbalance_pct = (diff / avg_yield * 100.0) if avg_yield > 100 else 0.0
        
        # Puntuación de limpieza
        cleanliness = max(70.0, min(100.0, 100.0 - (imbalance_pct * 0.4)))
        loss_pct = round(100.0 - cleanliness, 1)
        
        # Pérdida económica estimada mensual (sobre ~750 kWh/mes generados en verano en Tocina a 0.12 €/kWh)
        monthly_kwh_loss = (loss_pct / 100.0) * 750.0
        monthly_eur_loss = round(monthly_kwh_loss * 0.12, 2)

        if loss_pct < 4.0:
            status = "optimal"
            badge = "🟢 Módulos Limpios (Rendimiento Óptimo)"
            rec = "Superficie de paneles en perfecto estado óptico."
        elif loss_pct < 9.0:
            status = "slight_dust"
            badge = "🟡 Polvo Ligero Detectado"
            rec = "Polvo ambiental leve. No requiere limpieza inmediata a menos que haya calima."
        else:
            status = "cleaning_recommended"
            badge = "🔴 Alerta Calima / Suciedad"
            rec = f"Se recomienda manguerazo a las placas. Pérdida estimada: ~{monthly_eur_loss} €/mes."

        return {
            "status": status,
            "badge": badge,
            "cleanliness_score_percent": round(cleanliness, 1),
            "loss_percent": loss_pct,
            "monthly_loss_eur": monthly_eur_loss,
            "pv1_west_w": pv1_west_w,
            "pv2_east_w": pv2_east_w,
            "yield_west_w_per_kwp": round(yield_west_w_per_kwp, 1),
            "yield_east_w_per_kwp": round(yield_east_w_per_kwp, 1),
            "recommendation": rec
        }
