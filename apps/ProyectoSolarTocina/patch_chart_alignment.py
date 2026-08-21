import re

with open("src/app.js", "r") as f:
    code = f.read()

# Replace the 96-slot branch to ensure strict 1-to-1 array alignment for realMeasuredEv
old_loop_block = """        if (i < currentSlot) {
          const slotTimeline = this.todayHighRes?.timeline?.filter(t => {
            const tSlot = t.hour * 4 + Math.floor(t.minute / 15);
            return tSlot === i;
          }) || [];

          if (slotTimeline.length > 0) {
            const avgSol = slotTimeline.reduce((acc, cur) => acc + cur.avg_solar_kw, 0) / slotTimeline.length;
            const avgHm = slotTimeline.reduce((acc, cur) => acc + cur.avg_home_kw, 0) / slotTimeline.length;
            const avgGrd = slotTimeline.reduce((acc, cur) => acc + cur.avg_grid_import_kw, 0) / slotTimeline.length;
            realMeasuredSolar.push(Number(avgSol.toFixed(3)));
            realMeasuredHome.push(Number(avgHm.toFixed(3)));
            realMeasuredGrid.push(Number(avgGrd.toFixed(3)));
            const evPwr = (avgHm >= 1.8 && slotH >= 15) ? Math.max(0, avgHm - 0.65) : null;
            realMeasuredEv.push(evPwr ? Number(evPwr.toFixed(3)) : null);
          } else {
            const hrData = this.todayHourlyReal?.hourly?.find(h => h.hour === slotH);
            realMeasuredSolar.push(hrData ? hrData.avg_solar_kw : 0.0);
            realMeasuredHome.push(hrData ? (hrData.avg_home_kw ?? hrData.avg_grid_kw) : 0.25);
            realMeasuredGrid.push(hrData ? (hrData.avg_grid_import_kw ?? 0.0) : 0.0);
          }
        } else if (i === currentSlot) {
          const liveSolar = this.latestTelemetry?.solar_total_kw ?? (realMeasuredSolar[i-1] ?? 0.0);
          const liveHome = this.latestTelemetry?.grid?.home_load_kw ?? (realMeasuredHome[i-1] ?? 0.25);
          const liveGrid = this.latestTelemetry?.grid?.grid_import_kw ?? (realMeasuredGrid[i-1] ?? 0.0);
          realMeasuredSolar.push(Number(liveSolar.toFixed(3)));
          realMeasuredHome.push(Number(liveHome.toFixed(3)));
          realMeasuredGrid.push(Number(liveGrid.toFixed(3)));
          const isEvActive = this.latestTelemetry?.ev_status?.is_charging || (liveHome >= 1.8);
          const liveEvPwr = this.latestTelemetry?.ev_status?.ev_power_kw || (liveHome >= 1.8 ? liveHome - 0.65 : 0);
          realMeasuredEv.push(isEvActive && liveEvPwr > 0.5 ? Number(liveEvPwr.toFixed(3)) : null);
        } else {
          realMeasuredSolar.push(null);
          realMeasuredHome.push(null);
          realMeasuredGrid.push(null);
          realMeasuredEv.push(null);
        }"""

new_loop_block = """        if (i < currentSlot) {
          const slotTimeline = this.todayHighRes?.timeline?.filter(t => {
            const tSlot = t.hour * 4 + Math.floor(t.minute / 15);
            return tSlot === i;
          }) || [];

          if (slotTimeline.length > 0) {
            const avgSol = slotTimeline.reduce((acc, cur) => acc + cur.avg_solar_kw, 0) / slotTimeline.length;
            const avgHm = slotTimeline.reduce((acc, cur) => acc + cur.avg_home_kw, 0) / slotTimeline.length;
            const avgGrd = slotTimeline.reduce((acc, cur) => acc + cur.avg_grid_import_kw, 0) / slotTimeline.length;
            realMeasuredSolar.push(Number(avgSol.toFixed(3)));
            realMeasuredHome.push(Number(avgHm.toFixed(3)));
            realMeasuredGrid.push(Number(avgGrd.toFixed(3)));
            // Detección precisa de recarga VE entre 14:45 y 17:30
            const isEvSlot = (avgHm >= 1.6 && ((slotH === 14 && slotM >= 45) || (slotH >= 15 && slotH <= 17)));
            const evPwr = isEvSlot ? Math.max(0, avgHm - 0.55) : null;
            realMeasuredEv.push(evPwr ? Number(evPwr.toFixed(3)) : null);
          } else {
            const hrData = this.todayHourlyReal?.hourly?.find(h => h.hour === slotH);
            realMeasuredSolar.push(hrData ? hrData.avg_solar_kw : 0.0);
            realMeasuredHome.push(hrData ? (hrData.avg_home_kw ?? hrData.avg_grid_kw) : 0.25);
            realMeasuredGrid.push(hrData ? (hrData.avg_grid_import_kw ?? 0.0) : 0.0);
            const isEvSlot = (hrData && hrData.avg_home_kw >= 1.6 && slotH >= 15 && slotH <= 17);
            const evPwr = isEvSlot ? Math.max(0, hrData.avg_home_kw - 0.55) : null;
            realMeasuredEv.push(evPwr ? Number(evPwr.toFixed(3)) : null);
          }
        } else if (i === currentSlot) {
          const liveSolar = this.latestTelemetry?.solar_total_kw ?? (realMeasuredSolar[i-1] ?? 0.0);
          const liveHome = this.latestTelemetry?.grid?.home_load_kw ?? (realMeasuredHome[i-1] ?? 0.25);
          const liveGrid = this.latestTelemetry?.grid?.grid_import_kw ?? (realMeasuredGrid[i-1] ?? 0.0);
          realMeasuredSolar.push(Number(liveSolar.toFixed(3)));
          realMeasuredHome.push(Number(liveHome.toFixed(3)));
          realMeasuredGrid.push(Number(liveGrid.toFixed(3)));
          const isEvActive = this.latestTelemetry?.ev_status?.is_charging;
          const liveEvPwr = this.latestTelemetry?.ev_status?.ev_power_kw || 0;
          realMeasuredEv.push(isEvActive && liveEvPwr > 0.5 ? Number(liveEvPwr.toFixed(3)) : null);
        } else {
          realMeasuredSolar.push(null);
          realMeasuredHome.push(null);
          realMeasuredGrid.push(null);
          realMeasuredEv.push(null);
        }"""

code = code.replace(old_loop_block, new_loop_block)

# In 24-hour mode, also initialize realMeasuredEv
code = code.replace(
    "realMeasuredGrid = new Array(24).fill(null);",
    "realMeasuredGrid = new Array(24).fill(null);\n      realMeasuredEv = new Array(24).fill(null);"
)

with open("src/app.js", "w") as f:
    f.write(code)

print("Chart alignment patched successfully.")
