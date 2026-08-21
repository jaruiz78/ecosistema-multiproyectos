import re

with open("src/app.js", "r") as f:
    code = f.read()

# 1. Initialize realMeasuredEv
code = code.replace(
    "let realMeasuredGrid = [];",
    "let realMeasuredGrid = [];\n    let realMeasuredEv = [];"
)

# 2. In 96-slot loop, calculate realMeasuredEv
old_slot_loop = """            const avgSol = slotTimeline.reduce((acc, cur) => acc + cur.avg_solar_kw, 0) / slotTimeline.length;
            const avgHm = slotTimeline.reduce((acc, cur) => acc + cur.avg_home_kw, 0) / slotTimeline.length;
            const avgGrd = slotTimeline.reduce((acc, cur) => acc + cur.avg_grid_import_kw, 0) / slotTimeline.length;
            realMeasuredSolar.push(Number(avgSol.toFixed(3)));
            realMeasuredHome.push(Number(avgHm.toFixed(3)));
            realMeasuredGrid.push(Number(avgGrd.toFixed(3)));"""

new_slot_loop = """            const avgSol = slotTimeline.reduce((acc, cur) => acc + cur.avg_solar_kw, 0) / slotTimeline.length;
            const avgHm = slotTimeline.reduce((acc, cur) => acc + cur.avg_home_kw, 0) / slotTimeline.length;
            const avgGrd = slotTimeline.reduce((acc, cur) => acc + cur.avg_grid_import_kw, 0) / slotTimeline.length;
            realMeasuredSolar.push(Number(avgSol.toFixed(3)));
            realMeasuredHome.push(Number(avgHm.toFixed(3)));
            realMeasuredGrid.push(Number(avgGrd.toFixed(3)));
            const evPwr = (avgHm >= 1.8 && slotH >= 15) ? Math.max(0, avgHm - 0.65) : null;
            realMeasuredEv.push(evPwr ? Number(evPwr.toFixed(3)) : null);"""

code = code.replace(old_slot_loop, new_slot_loop)

# 3. Handle currentSlot for EV
old_current_slot = """          const liveSolar = this.latestTelemetry?.solar_total_kw ?? (realMeasuredSolar[i-1] ?? 0.0);
          const liveHome = this.latestTelemetry?.grid?.home_load_kw ?? (realMeasuredHome[i-1] ?? 0.25);
          const liveGrid = this.latestTelemetry?.grid?.grid_import_kw ?? (realMeasuredGrid[i-1] ?? 0.0);
          realMeasuredSolar.push(Number(liveSolar.toFixed(3)));
          realMeasuredHome.push(Number(liveHome.toFixed(3)));
          realMeasuredGrid.push(Number(liveGrid.toFixed(3)));"""

new_current_slot = """          const liveSolar = this.latestTelemetry?.solar_total_kw ?? (realMeasuredSolar[i-1] ?? 0.0);
          const liveHome = this.latestTelemetry?.grid?.home_load_kw ?? (realMeasuredHome[i-1] ?? 0.25);
          const liveGrid = this.latestTelemetry?.grid?.grid_import_kw ?? (realMeasuredGrid[i-1] ?? 0.0);
          realMeasuredSolar.push(Number(liveSolar.toFixed(3)));
          realMeasuredHome.push(Number(liveHome.toFixed(3)));
          realMeasuredGrid.push(Number(liveGrid.toFixed(3)));
          const isEvActive = this.latestTelemetry?.ev_status?.is_charging || (liveHome >= 1.8);
          const liveEvPwr = this.latestTelemetry?.ev_status?.ev_power_kw || (liveHome >= 1.8 ? liveHome - 0.65 : 0);
          realMeasuredEv.push(isEvActive && liveEvPwr > 0.5 ? Number(liveEvPwr.toFixed(3)) : null);"""

code = code.replace(old_current_slot, new_current_slot)

# 4. Handle future slots for EV
code = code.replace(
    "realMeasuredGrid.push(null);\n        }",
    "realMeasuredGrid.push(null);\n          realMeasuredEv.push(null);\n        }"
)

# 5. Add dataset for EV in overview datasets
ev_dataset_entry = """          {
            label: '🚗 Carga Omoda 7 PHEV (76% SoC • Solar Directa)',
            data: realMeasuredEv,
            borderColor: '#e879f9',
            backgroundColor: 'rgba(232, 121, 249, 0.35)',
            borderWidth: 2.8,
            fill: true,
            tension: 0.25,
            pointRadius: 4,
            pointBackgroundColor: '#e879f9'
          },"""

code = code.replace(
    "label: '🏠 Consumo Hogar Real (Smart Meter)',",
    ev_dataset_entry + "\n          {\n            label: '🏠 Consumo Hogar Real (Smart Meter)',"
)

with open("src/app.js", "w") as f:
    f.write(code)

print("Patched app.js with EV chart datasets.")
