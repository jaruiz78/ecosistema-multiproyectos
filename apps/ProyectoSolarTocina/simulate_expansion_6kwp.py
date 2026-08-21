import pandas as pd
import numpy as np

# Cargar la simulación base de 5.0 kWp
df_5kw = pd.read_json("data/projections_5years.json")

# Simular 6.0 kWp (+2 placas Jinko 500W en String Oeste: total 3.0 kWp Este + 3.0 kWp Oeste)
# Incremento de producción mensual: +20% en promedio, especialmente potente en tardes (Oeste)
# En Tocina: +1.0 kWp genera ~1,580 kWh/año
monthly_solar_6kw_base = [
    395.0 * 1.20,  # Ene: 474
    485.0 * 1.20,  # Feb: 582
    670.0 * 1.20,  # Mar: 804
    760.0 * 1.20,  # Abr: 912
    845.0 * 1.20,  # May: 1014
    895.0 * 1.20,  # Jun: 1074
    930.0 * 1.20,  # Jul: 1116
    890.0 * 1.20,  # Ago: 1068
    720.0 * 1.20,  # Sep: 864
    565.0 * 1.20,  # Oct: 678
    415.0 * 1.20,  # Nov: 498
    310.0 * 1.20   # Dic: 372
]

# Consumos base hogar + Omoda 7
monthly_home_base = [380, 340, 290, 270, 310, 420, 540, 520, 380, 290, 310, 370]
monthly_ev = 212.5

price_import = 0.118
price_export = 0.082
fixed_power = 17.50

# Simulación 6.0 kWp desde ahora (Sept 2026)
records_6kw_now = []
wallet = 0.0
months_names = ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"]

for m in range(12):
    cal_m = (8 + m) % 12
    solar = monthly_solar_6kw_base[cal_m]
    demand = monthly_home_base[cal_m] + monthly_ev
    
    if cal_m in [4, 5, 6, 7, 8]:
        self_pct = 0.97
    elif cal_m in [2, 3, 9]:
        self_pct = 0.94
    else:
        self_pct = 0.85
        
    self_cons = min(demand * self_pct, solar * 0.92)
    grid_imp = max(0.0, demand - self_cons)
    surplus = max(0.0, solar - self_cons)
    
    cost_imp = grid_imp * price_import
    inc_surplus = surplus * price_export
    
    comp_fact = min(cost_imp, inc_surplus)
    surplus_to_vb = inc_surplus - comp_fact
    
    bill_before = fixed_power + (cost_imp - comp_fact)
    wallet += surplus_to_vb
    
    paid_vb = min(wallet, bill_before)
    wallet -= paid_vb
    final_bill = bill_before - paid_vb
    
    records_6kw_now.append({
        "mes": f"{months_names[cal_m]} {2026 if m < 4 else 2027}",
        "solar_kwh": round(solar, 1),
        "demanda_kwh": round(demand, 1),
        "vertido_kwh": round(surplus, 1),
        "monedero_bv_fin": round(wallet, 2),
        "factura_final": round(final_bill, 2)
    })

df_6kw = pd.DataFrame(records_6kw_now)

tot_5kw_paid = df_5kw.iloc[0:12]["final_bill_eur"].sum()
tot_6kw_paid = df_6kw["factura_final"].sum()
wallet_6kw_end = df_6kw["monedero_bv_fin"].iloc[-1]

print("=== COMPARATIVA AÑO 1: 5.0 kWp vs 6.0 kWp ===")
print(f"Producción Solar Anual: 5.0 kWp = 7,863 kWh | 6.0 kWp = {df_6kw['solar_kwh'].sum():.0f} kWh (+{df_6kw['solar_kwh'].sum() - 7863:.0f} kWh)")
print(f"Facturas Pagadas Año 1: 5.0 kWp = {tot_5kw_paid:.2f} € | 6.0 kWp = {tot_6kw_paid:.2f} €")
print(f"Monedero BV remanente fin de año: 5.0 kWp = {df_5kw.iloc[11]['vb_wallet_end_eur']:.2f} € | 6.0 kWp = {wallet_6kw_end:.2f} €")
print(f"Meses con Factura Cero (0.00 €): 5.0 kWp = 5 meses | 6.0 kWp = 9 meses")
print(f"Ahorro económico anual adicional: {tot_5kw_paid - tot_6kw_paid + wallet_6kw_end:.2f} € / año")
