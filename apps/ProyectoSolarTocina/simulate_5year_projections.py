import numpy as np
import pandas as pd
import json

# -----------------------------------------------------------------------------
# 1. PARÁMETROS CALIBRADOS DEL ECOSISTEMA TOCINA (5.0 kWp + 10.36 kWh + Omoda 7)
# -----------------------------------------------------------------------------

# Producción solar mensual base en Tocina (Sevilla) para 5.0 kWp (3kWp Este + 2kWp Oeste, 30° Tilt, TOPCon + Albedo)
# Total año 1: ~7,880 kWh (1,576 kWh/kWp)
monthly_solar_base_kwh = [
    395.0,  # Ene
    485.0,  # Feb
    670.0,  # Mar
    760.0,  # Abr
    845.0,  # May
    895.0,  # Jun
    930.0,  # Jul
    890.0,  # Ago
    720.0,  # Sep
    565.0,  # Oct
    415.0,  # Nov
    310.0   # Dic
]

# Consumo base hogar + Daikin (kWh/mes)
# Verano e invierno más altos por A/C y bomba de calor
monthly_home_base_kwh = [
    380.0,  # Ene (Calefacción Daikin)
    340.0,  # Feb
    290.0,  # Mar (Primavera suave)
    270.0,  # Abr
    310.0,  # May
    420.0,  # Jun (Inicio A/C)
    540.0,  # Jul (Pico A/C 42°C Tocina)
    520.0,  # Ago (Pico A/C 41°C Tocina)
    380.0,  # Sep
    290.0,  # Oct
    310.0,  # Nov
    370.0   # Dic (Calefacción Daikin)
]

# Consumo Omoda 7 PHEV (~15,000 km/año, 17 kWh/100km = 2,550 kWh/año = 212.5 kWh/mes)
monthly_ev_kwh = 212.5

# Tarifas Naturgy
price_energy_import_eur = 0.118  # Media ponderada P1/P2/P3 con impuestos
price_surplus_export_eur = 0.082 # Compensación de excedentes (€/kWh neto)
fixed_power_term_eur = 17.50     # Término fijo potencia 4.6 kW + alquiler contador + impuestos

# Degradación anual
solar_degradation_rate = 0.0040  # -0.40% / año (Jinko N-Type TOPCon)
battery_degradation_rate = 0.012 # -1.20% / año (Fox-ESS LFP)

# -----------------------------------------------------------------------------
# 2. SIMULACIÓN MENSUAL A 5 AÑOS (60 MESES: Sept 2026 a Agosto 2031)
# -----------------------------------------------------------------------------

records = []
virtual_battery_wallet_eur = 0.0 # Monedero virtual acumulado

months_names = ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"]

# Comenzamos en Septiembre 2026 (Mes 0)
start_month = 8 # 0-indexed (8 = Septiembre)
start_year = 2026

for m_idx in range(60):
    year_num = (m_idx + start_month) // 12
    curr_year = start_year + (m_idx // 12)
    cal_month = (start_month + m_idx) % 12
    month_name = months_names[cal_month]
    
    # Factor de degradación
    deg_factor = (1.0 - solar_degradation_rate) ** (m_idx / 12.0)
    
    solar_gen = monthly_solar_base_kwh[cal_month] * deg_factor
    home_demand = monthly_home_base_kwh[cal_month]
    ev_demand = monthly_ev_kwh
    
    # En septiembre 2026 (mes 0), 15 días con Schuko y 15 días con Wallbox
    # A partir de Octubre 2026 (mes 1), Wallbox 7.4kW modula el 92% de la recarga en horas solares
    ev_solar_capture_pct = 0.88 if m_idx == 0 else 0.94
    
    # Autoconsumo directo + batería Fox-ESS
    # La batería Fox-ESS (10.36 kWh) cicla ~280-300 días/año cubriendo el 90-95% de la noche
    total_consumption = home_demand + ev_demand
    
    # Autoconsumo solar directo + Batería Fox-ESS
    # En meses solares (Marzo-Octubre) el autoconsumo es 95-98%. En invierno 75-85%.
    if cal_month in [4, 5, 6, 7, 8]: # Mayo a Septiembre
        self_consumption_pct = 0.95
    elif cal_month in [2, 3, 9]:     # Marzo, Abril, Octubre
        self_consumption_pct = 0.90
    else:                            # Invierno (Nov, Dic, Ene, Feb)
        self_consumption_pct = 0.78
        
    energy_self_consumed = min(total_consumption * self_consumption_pct, solar_gen * 0.90)
    grid_import_kwh = max(0.0, total_consumption - energy_self_consumed)
    
    # Excedente vertido a la red
    # Antes del 15 de septiembre 2026 el vertido está a cero; a partir de ahí vertido al 100%
    if m_idx == 0: # Septiembre 2026 (medio mes vertiendo)
        surplus_exported_kwh = max(0.0, (solar_gen - energy_self_consumed) * 0.50)
    else:
        surplus_exported_kwh = max(0.0, solar_gen - energy_self_consumed)
        
    # Finanzas de la factura
    cost_import_eur = grid_import_kwh * price_energy_import_eur
    income_surplus_eur = surplus_exported_kwh * price_surplus_export_eur
    
    # Factura antes de Batería Virtual
    # La compensación simplificada en factura tradicional solo compensa el término de energía
    comp_en_factura = min(cost_import_eur, income_surplus_eur)
    surplus_to_virtual_battery = income_surplus_eur - comp_en_factura
    
    bill_before_vb = fixed_power_term_eur + (cost_import_eur - comp_en_factura)
    
    # Aplicación de Batería Virtual Naturgy (compensa término fijo + impuestos)
    virtual_battery_wallet_eur += surplus_to_virtual_battery
    
    amount_paid_from_vb = min(virtual_battery_wallet_eur, bill_before_vb)
    virtual_battery_wallet_eur -= amount_paid_from_vb
    
    final_bill_to_pay = bill_before_vb - amount_paid_from_vb
    
    records.append({
        "month_index": m_idx + 1,
        "date_str": f"{month_name} {curr_year}",
        "year": curr_year,
        "cal_month": cal_month + 1,
        "solar_gen_kwh": round(solar_gen, 1),
        "total_demand_kwh": round(total_consumption, 1),
        "self_consumed_kwh": round(energy_self_consumed, 1),
        "grid_import_kwh": round(grid_import_kwh, 1),
        "surplus_export_kwh": round(surplus_exported_kwh, 1),
        "bill_before_vb_eur": round(bill_before_vb, 2),
        "surplus_income_eur": round(income_surplus_eur, 2),
        "vb_wallet_end_eur": round(virtual_battery_wallet_eur, 2),
        "final_bill_eur": round(final_bill_to_pay, 2)
    })

df = pd.DataFrame(records)

# Resumen anual
print("=== RESUMEN ANUAL A 5 AÑOS (2026 - 2031) ===")
for y in range(2026, 2032):
    sub = df[df["year"] == y]
    if len(sub) > 0:
        tot_solar = sub["solar_gen_kwh"].sum()
        tot_dem = sub["total_demand_kwh"].sum()
        tot_surplus = sub["surplus_export_kwh"].sum()
        tot_paid = sub["final_bill_eur"].sum()
        max_wallet = sub["vb_wallet_end_eur"].max()
        end_wallet = sub["vb_wallet_end_eur"].iloc[-1]
        print(f"Año {y} ({len(sub)} meses): Solar={tot_solar:.0f} kWh | Demanda={tot_dem:.0f} kWh | Vertido={tot_surplus:.0f} kWh | Total Facturas Pagadas={tot_paid:.2f} € | Monedero BV Fin={end_wallet:.2f} €")

# Exportar tabla completa mes a mes para formateo markdown
df.to_json("data/projections_5years.json", orient="records", indent=2)
print("\nSimulación completada y guardada en data/projections_5years.json")
