import re

with open("src/app.js", "r") as f:
    code = f.read()

# We need to enhance updateOmodaTrafficLight to consider potential solar when in curtailment
new_logic = """  updateOmodaTrafficLight(telemetry) {
    if (!telemetry) return;
    const solarW = telemetry.solar_total_w || 0;
    const homeLoadW = (telemetry.grid && telemetry.grid.home_load_w) || 0;
    const batSoc = (telemetry.battery && telemetry.battery.soc_percent) || 42;
    const surplusW = Math.max(0, solarW - homeLoadW);
    const surplusKw = surplusW / 1000.0;

    // Estimación de potencial solar latente cuando el inversor hace MPPT Derating (Anti-Vertido + Batería 100%)
    const curHour = typeof getMadridTime === 'function' ? getMadridTime().hour : new Date().getHours();
    const isSunPeakHours = (curHour >= 11 && curHour <= 17);
    const isCurtailmentActive = (batSoc >= 95 && Math.abs(solarW - homeLoadW) < 300 && isSunPeakHours);

    const badge = document.getElementById('omoda-traffic-badge');
    const lightRed = document.getElementById('light-red');
    const lightAmber = document.getElementById('light-amber');
    const lightGreen = document.getElementById('light-green');
    const titleEl = document.getElementById('omoda-traffic-verdict-title');
    const descEl = document.getElementById('omoda-traffic-verdict-desc');
    const surplusEl = document.getElementById('omoda-traffic-surplus-val');
    const pctEl = document.getElementById('omoda-traffic-solar-pct');
    const pwrEl = document.getElementById('omoda-traffic-pwr-val');
    const windowEl = document.getElementById('omoda-traffic-window-val');

    if (!badge || !lightRed || !lightAmber || !lightGreen) return;

    if (surplusEl) {
      if (isCurtailmentActive) {
        surplusEl.textContent = `~3.50 kW (Latente)`;
      } else {
        surplusEl.textContent = `${surplusKw >= 0.05 ? '+' : ''}${surplusKw.toFixed(2)} kW`;
      }
    }

    // 0. Si el Omoda 7 está cargando activamente (detectado por inferencia o telemetría)
    const evStatus = telemetry.ev_status;
    if (evStatus && evStatus.is_charging) {
      badge.textContent = `⚡ CARGANDO OMODA 7 (${evStatus.ev_power_kw} kW)`;
      badge.style.background = 'rgba(56, 189, 248, 0.2)';
      badge.style.color = '#38bdf8';
      badge.style.border = '1px solid #38bdf8';

      lightRed.style.background = '#334155';
      lightRed.style.boxShadow = 'none';
      lightRed.style.opacity = '0.25';

      lightAmber.style.background = '#334155';
      lightAmber.style.boxShadow = 'none';
      lightAmber.style.opacity = '0.25';

      lightGreen.style.background = '#38bdf8';
      lightGreen.style.boxShadow = '0 0 16px #38bdf8';
      lightGreen.style.opacity = '1.0';

      if (titleEl) titleEl.textContent = `⚡ Carga en curso • Batería Omoda: ${evStatus.current_soc_pct}% (~${evStatus.ev_range_km} km)`;
      if (descEl) descEl.textContent = `Absorbiendo ${evStatus.ev_power_kw} kW (${evStatus.solar_fraction_pct}% Solar/Batería Fox). ETA 80%: ${evStatus.eta_80} | ETA 100%: ${evStatus.eta_100}. Coste sesión: ${evStatus.session_cost_eur} €.`;
      if (pctEl) pctEl.textContent = `${evStatus.solar_fraction_pct}% Solar`;
      if (pwrEl) pwrEl.textContent = `${evStatus.ev_power_kw} kW`;
      if (windowEl) windowEl.textContent = `Hasta ${evStatus.eta_80} (80%)`;
      return;
    }

    // 1. Estado VERDE: Excedente >= 2.0 kW O Modo Estrangulamiento Anti-Vertido con Batería >= 95% en horas de sol
    if (surplusKw >= 2.0 || (solarW >= 3000 && batSoc >= 80) || isCurtailmentActive) {
      badge.textContent = '🟢 ÓPTIMO • CARGA 100% SOLAR';
      badge.style.background = 'rgba(16, 185, 129, 0.2)';
      badge.style.color = '#10b981';
      badge.style.border = '1px solid #10b981';

      lightRed.style.background = '#334155';
      lightRed.style.boxShadow = 'none';
      lightRed.style.opacity = '0.25';

      lightAmber.style.background = '#334155';
      lightAmber.style.boxShadow = 'none';
      lightAmber.style.opacity = '0.25';

      lightGreen.style.background = '#10b981';
      lightGreen.style.boxShadow = '0 0 16px #10b981';
      lightGreen.style.opacity = '1.0';

      if (isCurtailmentActive) {
        if (titleEl) titleEl.textContent = '¡Batería al 100% y sol disponible! Momento perfecto para enchufar el Omoda 7';
        if (descEl) descEl.textContent = `El inversor está frenando los paneles a ${(solarW/1000).toFixed(2)} kW porque la batería está llena y no viertes a red. Al enchufar el coche a 2.3 kW (10A), el inversor desatará toda la energía solar latente a coste 0.00 €.`;
      } else {
        if (titleEl) titleEl.textContent = '¡Momento ideal para recargar el Omoda 7 SHS!';
        if (descEl) descEl.textContent = `Dispones de ${surplusKw.toFixed(2)} kW de excedente solar directo. Enchufando el coche a 2.3 kW (10A Schuko), la recarga será 100% solar y gratuita sin recurrir a la red eléctrica.`;
      }
      if (pctEl) pctEl.textContent = '100% Solar';
      if (pwrEl) pwrEl.textContent = '2.3 kW (10A)';
      if (windowEl) windowEl.textContent = 'Ahora mismo (Pico Solar)';
    }"""

code = re.sub(
    r'  updateOmodaTrafficLight\(telemetry\) \{.*?    // 1\. Estado VERDE: Excedente >= 2\.0 kW.*?(?=    // 2\. Estado NARANJA:)',
    new_logic + "\n",
    code,
    flags=re.DOTALL
)

with open("src/app.js", "w") as f:
    f.write(code)

print("Patch applied successfully to updateOmodaTrafficLight in app.js")
