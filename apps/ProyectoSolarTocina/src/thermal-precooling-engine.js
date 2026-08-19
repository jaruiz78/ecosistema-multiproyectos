/**
 * Modelo de Inercia Térmica de Edificación RC (2 Estados), Protocolos Bioclimáticos & Control Daikin Clima
 * - Protocolos Estacionales: Verano (Free-Cooling y Sombra), Invierno (Ganancia Pasiva Este/Oeste), Entretiempo
 * - Recomendaciones Dinámicas en Vivo según la hora y posición solar en faldones (89° E / 269° O)
 * - Comparativa de Eficiencia: Daikin Inverter (COP 3.8) vs Ventiladores de Techo (35W) vs Radiadores (COP 1.0)
 * - Telemetría en tiempo real de Daikin Salón y Dormitorio con control de consigna y pre-cooling solar
 */

export class ThermalPrecoolingEngine {
  constructor(containerId) {
    this.container = document.getElementById(containerId);
    this.R_th = 3.2;  // Resistencia térmica equivalente (K / kW)
    this.C_th = 14.5; // Capacidad calorífica de la estructura (kWh / K)
    this.daikinCop = 3.8;
    this.daikinStatus = null;
    this.activeBioclimaticTab = 'today_live'; // 'today_live', 'summer', 'winter', 'appliances_guide'

    this.init();
  }

  async init() {
    if (!this.container) return;
    await this.fetchDaikinStatus();
    this.render();
  }

  async fetchDaikinStatus() {
    try {
      const res = await fetch('/api/daikin/status');
      if (res.ok) {
        this.daikinStatus = await res.json();
      }
    } catch (e) {
      console.warn('[DaikinEngine] Error fetching status:', e);
    }
  }

  simulate24h(hourlyForecast = []) {
    const hours = 24;
    const baselineInTemps = [];
    const precoolInTemps = [];
    const daikinBaselineW = [];
    const daikinPrecoolW = [];

    let tInBase = 25.0;
    let tInPrecool = 25.0;
    let totalBaseKwh = 0;
    let totalPrecoolKwh = 0;
    let nightBatSavedKwh = 0;

    for (let h = 0; h < hours; h++) {
      const point = hourlyForecast[h] || { temp: 28, solarKw: 0 };
      const tOut = point.temp !== undefined ? point.temp : 32.0;
      const solarKw = point.solarKw || (point.forecast ? point.forecast.pTotalAC_kW : 0);

      let pDaikinBase = 0;
      if (tInBase > 25.0 && h >= 14 && h <= 23) {
        pDaikinBase = 520;
      } else if (h >= 22 || h <= 7) {
        pDaikinBase = tInBase > 25.0 ? 280 : 0;
      }
      
      const qCoolBase = (pDaikinBase / 1000.0) * this.daikinCop;
      const dtBase = ((tOut - tInBase) / this.R_th - qCoolBase) / this.C_th;
      tInBase += dtBase;
      baselineInTemps.push(Math.round(tInBase * 10) / 10);
      daikinBaselineW.push(pDaikinBase);
      totalBaseKwh += pDaikinBase / 1000.0;

      let pDaikinPrecool = 0;
      if (h >= 12 && h <= 15 && solarKw > 2.0) {
        pDaikinPrecool = 650;
      } else if (h >= 16 && h <= 21) {
        pDaikinPrecool = tInPrecool > 25.0 ? 210 : 0;
      } else if (h >= 22 || h <= 7) {
        pDaikinPrecool = tInPrecool > 25.5 ? 150 : 0;
      }

      const qCoolPrecool = (pDaikinPrecool / 1000.0) * this.daikinCop;
      const dtPrecool = ((tOut - tInPrecool) / this.R_th - qCoolPrecool) / this.C_th;
      tInPrecool += dtPrecool;
      precoolInTemps.push(Math.round(tInPrecool * 10) / 10);
      daikinPrecoolW.push(pDaikinPrecool);
      totalPrecoolKwh += pDaikinPrecool / 1000.0;

      if (h >= 21 || h <= 7) {
        const diffW = Math.max(0, pDaikinBase - pDaikinPrecool);
        nightBatSavedKwh += diffW / 1000.0;
      }
    }

    const batSocSavedPercent = Math.min(100, Math.round((nightBatSavedKwh / 10.36) * 100));

    return {
      baselineInTemps,
      precoolInTemps,
      totalBaseKwh: totalBaseKwh.toFixed(2),
      totalPrecoolKwh: totalPrecoolKwh.toFixed(2),
      nightBatSavedKwh: nightBatSavedKwh.toFixed(2),
      batSocSavedPercent,
      moneySavedEur: (nightBatSavedKwh * 0.14).toFixed(2)
    };
  }

  getCurrentLiveBioclimaticAdvice() {
    const now = new Date();
    const hour = now.getHours();
    const month = now.getMonth() + 1;

    const isSummer = (month >= 5 && month <= 9);
    const isWinter = (month >= 11 || month <= 3);

    if (isSummer) {
      if (hour >= 7 && hour < 9) {
        return {
          badge: "🌬️ Ventilación Matinal Cruzada",
          badgeBg: "rgba(56, 189, 248, 0.2)",
          badgeColor: "#38bdf8",
          title: "Ventilación Rápida de Toda la Vivienda (Mínimo Térmico)",
          action: "Abrir ventanas y balcones al 100% durante 45–60 min para renovar el aire con los 21–23 °C del exterior.",
          shading: "A partir de las 08:30 h, bajar persianas de la Fachada Este (89° E) al 80% antes de que incida el sol directo.",
          climaTip: "Daikin apagado o en reposo. Casa fresca."
        };
      } else if (hour >= 9 && hour < 13) {
        return {
          badge: "🛡️ Protección Solar Este",
          badgeBg: "rgba(245, 158, 11, 0.2)",
          badgeColor: "#f59e0b",
          title: "Sol Directo en Fachada Este (89° E) • Casa Cerrada",
          action: "Mantener ventanas herméticamente cerradas y persianas de la Fachada Este al 80–90% para evitar radiación infrarroja.",
          shading: "El sol matinal incide con fuerza en la calle. Ventilación del patio Oeste en sombra.",
          climaTip: "Si la casa sube de 25 °C, preparar el Daikin Salón para iniciar pre-cooling a las 12:30 h con solar directa."
        };
      } else if (hour >= 13 && hour < 17) {
        return {
          badge: "⚡ Eco Pre-cooling Solar Activo",
          badgeBg: "rgba(16, 185, 129, 0.2)",
          badgeColor: "#10b981",
          title: "Pico Solar en Tejado • Enfriamiento Estructural Gratuito",
          action: "Fachada Este ya en sombra propia. El sol pasa al Tejado Oeste (269° O). Ventanas cerradas.",
          shading: "Bajar persianas del patio Oeste. Fachada de la calle fresca a la sombra.",
          climaTip: "Enfriar salón a 22–24 °C con Daikin a Coste 0.00 € (100% solar). Si estás en reposo, usa ventilador de techo (35W) para sensación de 21 °C sin esfuerzo térmico."
        };
      } else if (hour >= 17 && hour < 22) {
        return {
          badge: "🌤️ Inercia Térmica de Tarde",
          badgeBg: "rgba(168, 85, 247, 0.2)",
          badgeColor: "#c084fc",
          title: "Retención de Frescor en Muros • Evitar Entrada de Aire Caliente",
          action: "Mantener vivienda cerrada mientras la calle siga por encima de 30 °C.",
          shading: "Persianas bajadas en patio Oeste hasta la puesta de sol (21:30 h).",
          climaTip: "Daikin modulando al mínimo (200–300 W) apoyado por la batería Fox-ESS. Ventiladores de techo activos."
        };
      } else {
        return {
          badge: "🌙 Free-Cooling Nocturno Activo",
          badgeBg: "rgba(56, 189, 248, 0.25)",
          badgeColor: "#38bdf8",
          title: "Efecto Chimenea: Ventilación Pasiva Calle <-> Patio",
          action: "Abrir al máximo balcones de planta alta (Este) y puerta/ventanas del patio trasero (Oeste).",
          shading: "El aire fresco asciende por tiro térmico natural, disipando el calor de los forjados de forma 100% gratuita.",
          climaTip: "Daikin apagado. Ventilador de techo a baja velocidad (25 W) para dormir con confort total y batería intacta."
        };
      }
    } else if (isWinter) {
      if (hour >= 9 && hour < 14) {
        return {
          badge: "☀️ Calefacción Solar Pasiva Directa",
          badgeBg: "rgba(245, 158, 11, 0.25)",
          badgeColor: "#f59e0b",
          title: "Subir Persianas Fachada Este (89° E) al 100%",
          action: "Sol de invierno bajo penetrando profundamente por balcones y ventanas Este (+3.2 kWh térmicos gratuitos).",
          shading: "Cristales limpios recibiendo radiación solar directa que calienta suelos y paredes.",
          climaTip: "Si necesitas refuerzo, encender Daikin en calor a 21 °C entre 12:00 y 16:00 h con energía solar directa. Evitar radiadores resistivos."
        };
      } else if (hour >= 14 && hour < 15) {
        return {
          badge: "💨 Ventilación Higiénica Corta",
          badgeBg: "rgba(56, 189, 248, 0.2)",
          badgeColor: "#38bdf8",
          title: "Ventilación Rápida de 10–15 minutos a Mediodía",
          action: "Abrir ventanas 10 minutos en el momento más cálido del día (16–18 °C) para renovar el aire sin enfriar muros.",
          shading: "Abrir persianas en patio Oeste para captar la tarde.",
          climaTip: "Cerrar inmediatamente después para retener el calor acumulado."
        };
      } else {
        return {
          badge: "🛡️ Aislamiento Térmico Nocturno",
          badgeBg: "rgba(168, 85, 247, 0.2)",
          badgeColor: "#c084fc",
          title: "Bajar Persianas al 100% en Toda la Vivienda",
          action: "Cierre hermético de persianas exteriores para crear cámara de aire estanca con el cristal (-30% pérdidas).",
          shading: "Evita la disipación radiactiva hacia el cielo frío nocturno.",
          climaTip: "Daikin en calor modulando a baja potencia con la batería Fox-ESS. Evitar estufas resistivas de 2.000 W."
        };
      }
    } else {
      return {
        badge: "🌱 Confort Bioclimático Pasivo",
        badgeBg: "rgba(16, 185, 129, 0.2)",
        badgeColor: "#10b981",
        title: "Climatización 100% Pasiva sin Consumo Eléctrico",
        action: "Modular persianas para regular la iluminación y abrir ventilación cruzada cuando la temperatura exterior sea agradable (22–25 °C).",
        shading: "Apertura libre de huecos.",
        climaTip: "Cero consumo de climatización. 100% de excedentes solares destinados a recargar coche y batería."
      };
    }
  }

  render(hourlyForecast = []) {
    if (!this.container) return;
    const sim = this.simulate24h(hourlyForecast);
    const liveAdvice = this.getCurrentLiveBioclimaticAdvice();
    const ds = this.daikinStatus || {
      units: [
        { id: "daikin_salon", name: "Daikin Salón", target_temp_c: 24, indoor_temp_c: 26.5, outdoor_temp_c: 34, power_w: 0, power_on: false, connected: false },
        { id: "daikin_dormitorio", name: "Daikin Dormitorio", target_temp_c: 24, indoor_temp_c: 27.0, outdoor_temp_c: 34, power_w: 0, power_on: false, connected: false }
      ],
      nilm_power: { daikin_salon: 0, daikin_dormitorio: 0, total_ac_w: 0 },
      recommendation: { badge: "🟢 Pre-cooling Activo", action: "Enfriar con solar gratis", suggested_temp_c: 21.0 }
    };

    this.container.innerHTML = `
      <div class="thermal-precool-card" style="background: var(--bg-card); border: 1px solid rgba(56, 189, 248, 0.35); border-radius: var(--radius-lg); padding: 1.25rem; box-shadow: var(--shadow-card); display: flex; flex-direction: column; gap: 1.25rem;">
        
        <!-- Cabecera Principal -->
        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.75rem; flex-wrap: wrap; gap: 0.5rem;">
          <div style="display: flex; align-items: center; gap: 0.6rem;">
            <div style="font-size: 1.4rem; background: rgba(56, 189, 248, 0.15); width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; border-radius: var(--radius-md);">🏛️</div>
            <div>
              <h3 style="font-size: 1.05rem; font-weight: 700; color: var(--text-primary);">Centro Bioclimático de Ventilación, Climatización & Protocolos del Hogar</h3>
              <div style="font-size: 0.75rem; color: var(--text-muted);">Fachada Este 89° E (Calle) · Fachada Oeste 269° O (Patio) · Daikin Inverter · Free-Cooling & Aislamiento</div>
            </div>
          </div>
          <div style="display: flex; gap: 0.5rem; align-items: center;">
            <span class="badge-tag" style="background: ${liveAdvice.badgeBg}; color: ${liveAdvice.badgeColor}; font-weight: 700;">${liveAdvice.badge}</span>
            <button id="btn-scan-daikin" class="refresh-button" style="padding: 0.4rem 0.75rem; font-size: 0.75rem; background: var(--bg-card);" title="Escanear adaptadores WiFi BRP069 en la red local">
              🔍 Escanear Red
            </button>
          </div>
        </div>

        <!-- TARJETA EN VIVO: Recomendación Bioclimática para el Día en Curso -->
        <div style="background: linear-gradient(135deg, rgba(30, 41, 59, 0.7), rgba(15, 23, 42, 0.9)); border: 1px solid rgba(56, 189, 248, 0.4); border-radius: var(--radius-md); padding: 1rem; display: flex; flex-direction: column; gap: 0.6rem;">
          <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 0.4rem;">
            <div style="font-size: 0.92rem; font-weight: 700; color: var(--text-primary); display: flex; align-items: center; gap: 0.4rem;">
              <span>📍</span> <strong>Protocolo en Vivo para este Momento:</strong> ${liveAdvice.title}
            </div>
            <span style="font-size: 0.72rem; color: #38bdf8; background: rgba(56, 189, 248, 0.15); padding: 2px 8px; border-radius: 4px; font-weight: 700;">Actualizado en Tiempo Real</span>
          </div>
          <div style="font-size: 0.83rem; color: var(--text-secondary); line-height: 1.45;">
            • <strong>Ventilación / Ventanas:</strong> ${liveAdvice.action}<br>
            • <strong>Sombra / Persianas:</strong> ${liveAdvice.shading}<br>
            • <strong>Climatización Inteligente:</strong> ${liveAdvice.climaTip}
          </div>
        </div>

        <!-- Selector de Pestañas de Protocolos -->
        <div style="display: flex; gap: 0.5rem; flex-wrap: wrap; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.5rem;">
          <button class="bioclimatic-tab-btn ${this.activeBioclimaticTab === 'today_live' ? 'active' : ''}" data-tab="today_live" style="padding: 0.4rem 0.85rem; font-size: 0.8rem; font-weight: 700; border-radius: var(--radius-sm); border: 1px solid var(--border-subtle); background: ${this.activeBioclimaticTab === 'today_live' ? '#38bdf8' : 'var(--bg-elevated)'}; color: ${this.activeBioclimaticTab === 'today_live' ? '#0f172a' : 'var(--text-secondary)'}; cursor: pointer;">
            ⚡ Control Daikin Inverter
          </button>
          <button class="bioclimatic-tab-btn ${this.activeBioclimaticTab === 'summer' ? 'active' : ''}" data-tab="summer" style="padding: 0.4rem 0.85rem; font-size: 0.8rem; font-weight: 700; border-radius: var(--radius-sm); border: 1px solid var(--border-subtle); background: ${this.activeBioclimaticTab === 'summer' ? '#38bdf8' : 'var(--bg-elevated)'}; color: ${this.activeBioclimaticTab === 'summer' ? '#0f172a' : 'var(--text-secondary)'}; cursor: pointer;">
            ☀️ Protocolo Verano (Free-Cooling & Sombra)
          </button>
          <button class="bioclimatic-tab-btn ${this.activeBioclimaticTab === 'winter' ? 'active' : ''}" data-tab="winter" style="padding: 0.4rem 0.85rem; font-size: 0.8rem; font-weight: 700; border-radius: var(--radius-sm); border: 1px solid var(--border-subtle); background: ${this.activeBioclimaticTab === 'winter' ? '#38bdf8' : 'var(--bg-elevated)'}; color: ${this.activeBioclimaticTab === 'winter' ? '#0f172a' : 'var(--text-secondary)'}; cursor: pointer;">
            ❄️ Protocolo Invierno (Ganancia Solar Pasiva)
          </button>
          <button class="bioclimatic-tab-btn ${this.activeBioclimaticTab === 'appliances_guide' ? 'active' : ''}" data-tab="appliances_guide" style="padding: 0.4rem 0.85rem; font-size: 0.8rem; font-weight: 700; border-radius: var(--radius-sm); border: 1px solid var(--border-subtle); background: ${this.activeBioclimaticTab === 'appliances_guide' ? '#38bdf8' : 'var(--bg-elevated)'}; color: ${this.activeBioclimaticTab === 'appliances_guide' ? '#0f172a' : 'var(--text-secondary)'}; cursor: pointer;">
            🎛️ Comparador Eficiencia: A/A vs Ventilador vs Radiador
          </button>
        </div>

        <!-- CONTENIDO DE LA PESTAÑA SELECCIONADA -->
        ${this.renderActiveTabContent(ds, sim)}

        <!-- 4 Métricas Clave del Modelo Térmico -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(170px, 1fr)); gap: 0.75rem;">
          <div style="background: var(--bg-elevated); padding: 0.75rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
            <div style="font-size: 0.7rem; color: #38bdf8; font-weight: 700; text-transform: uppercase;">Ahorro de Batería Nocturna</div>
            <div style="font-size: 1.25rem; font-weight: 800; color: #38bdf8;">+${sim.nightBatSavedKwh} kWh / noche</div>
            <div style="font-size: 0.7rem; color: var(--text-muted);">~${sim.batSocSavedPercent}% SoC Fox-ESS intacto</div>
          </div>

          <div style="background: var(--bg-elevated); padding: 0.75rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
            <div style="font-size: 0.7rem; color: var(--color-real); font-weight: 700; text-transform: uppercase;">Energía Solar Invertida</div>
            <div style="font-size: 1.25rem; font-weight: 800; color: var(--color-real);">100% Gratis (0.00 €)</div>
            <div style="font-size: 0.7rem; color: var(--text-muted);">Ventana: 12:30 - 16:30 h</div>
          </div>

          <div style="background: var(--bg-elevated); padding: 0.75rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
            <div style="font-size: 0.7rem; color: var(--color-solar); font-weight: 700; text-transform: uppercase;">Inercia Estructural (Cth)</div>
            <div style="font-size: 1.25rem; font-weight: 800; color: var(--color-solar-light);">14.5 kWh / °C</div>
            <div style="font-size: 0.7rem; color: var(--text-muted);">Forjados y masa amortiguadora</div>
          </div>

          <div style="background: var(--bg-elevated); padding: 0.75rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
            <div style="font-size: 0.7rem; color: #c084fc; font-weight: 700; text-transform: uppercase;">Ahorro Económico Clima</div>
            <div style="font-size: 1.25rem; font-weight: 800; color: #c084fc;">~${(parseFloat(sim.nightBatSavedKwh) * 0.15 * 30).toFixed(1)} € / mes</div>
            <div style="font-size: 0.7rem; color: var(--text-muted);">En factura eléctrica anual</div>
          </div>
        </div>

        <div id="daikin-action-msg" style="font-size: 0.8rem; min-height: 1rem; color: #10b981;"></div>
      </div>
    `;

    this.attachEvents();
  }

  renderActiveTabContent(ds, sim) {
    if (this.activeBioclimaticTab === 'today_live') {
      return `
        <!-- Tarjetas de las 2 Máquinas Daikin -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 1rem;">
          ${ds.units.map(u => `
            <div style="background: var(--bg-elevated); padding: 1rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle); display: flex; flex-direction: column; justify-content: space-between; gap: 0.75rem;">
              <div style="display: flex; justify-content: space-between; align-items: center;">
                <div style="font-weight: 700; color: var(--text-primary); font-size: 0.95rem; display: flex; align-items: center; gap: 0.4rem;">
                  <span>❄️</span> ${u.name}
                </div>
                <span class="badge-tag" style="background: ${u.connected ? 'rgba(16,185,129,0.2)' : 'rgba(56,189,248,0.2)'}; color: ${u.connected ? '#10b981' : '#38bdf8'};">
                  ${u.connected ? 'WiFi BRP069' : 'NILM Smart Meter'}
                </span>
              </div>

              <div style="display: flex; justify-content: space-between; align-items: center;">
                <div>
                  <div style="font-size: 0.75rem; color: var(--text-muted);">Consigna Actual</div>
                  <div style="font-size: 1.8rem; font-weight: 800; color: #38bdf8;" id="${u.id}-temp-display">${u.target_temp_c}°C</div>
                </div>
                <div>
                  <div style="font-size: 0.75rem; color: var(--text-muted);">Potencia Estimada</div>
                  <div style="font-size: 1.8rem; font-weight: 800; color: ${u.power_w > 100 ? '#f43f5e' : '#10b981'};">
                    ${u.power_w} W
                  </div>
                </div>
              </div>

              <div style="display: flex; justify-content: space-between; font-size: 0.78rem; color: var(--text-muted); border-top: 1px solid var(--border-subtle); padding-top: 0.5rem;">
                <span>🌡️ Interior: <strong style="color: var(--text-primary);">${u.indoor_temp_c || 26.5}°C</strong></span>
                <span>☀️ Exterior: <strong style="color: var(--text-primary);">${u.outdoor_temp_c || 34.0}°C</strong></span>
              </div>

              <div style="display: flex; gap: 0.4rem;">
                <button class="refresh-button daikin-temp-down" data-unit="${u.id}" style="flex: 1; justify-content: center; padding: 0.4rem; font-size: 0.85rem; font-weight: 700;">-1°C</button>
                <button class="refresh-button daikin-temp-up" data-unit="${u.id}" style="flex: 1; justify-content: center; padding: 0.4rem; font-size: 0.85rem; font-weight: 700;">+1°C</button>
                <button class="refresh-button daikin-precool-btn" data-unit="${u.id}" style="flex: 2; justify-content: center; background: #0284c7; color: #fff; font-weight: 700; border: none; padding: 0.4rem; font-size: 0.78rem;">
                  ⚡ Eco Pre-cooling (21°C)
                </button>
              </div>
            </div>
          `).join('')}
        </div>
      `;
    }

    if (this.activeBioclimaticTab === 'summer') {
      return `
        <div style="display: flex; flex-direction: column; gap: 0.85rem; background: var(--bg-elevated); padding: 1.1rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
          <div style="font-weight: 800; color: #f59e0b; font-size: 0.95rem; display: flex; align-items: center; gap: 0.5rem;">
            <span>☀️</span> PROTOCOLO ESTIVAL (MAYO – SEPTIEMBRE): DISIPACIÓN PASIVA & FREE-COOLING
          </div>
          
          <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap: 0.85rem; font-size: 0.82rem;">
            <div style="background: rgba(0,0,0,0.25); padding: 0.85rem; border-radius: var(--radius-sm); border-left: 3px solid #38bdf8;">
              <strong style="color: #38bdf8;">1. Ventilación Matinal Rápida (07:00 – 08:30 h)</strong>
              <p style="color: var(--text-muted); margin-top: 0.35rem;">Abrir ventanas de Fachada Este (Calle) y Fachada Oeste (Patio) para renovar el aire en el momento más fresco del día (20–22 °C).</p>
            </div>

            <div style="background: rgba(0,0,0,0.25); padding: 0.85rem; border-radius: var(--radius-sm); border-left: 3px solid #f59e0b;">
              <strong style="color: #f59e0b;">2. Protección Fachada Este (08:30 – 14:00 h)</strong>
              <p style="color: var(--text-muted); margin-top: 0.35rem;">Cerrar ventanas y bajar persianas Este al 80–90%. La fachada blanca refleja el 78% del calor, y las persianas impiden que el cristal se caliente.</p>
            </div>

            <div style="background: rgba(0,0,0,0.25); padding: 0.85rem; border-radius: var(--radius-sm); border-left: 3px solid #10b981;">
              <strong style="color: #10b981;">3. Pre-Cooling Solar Daikin (12:30 – 16:30 h)</strong>
              <p style="color: var(--text-muted); margin-top: 0.35rem;">Aprovechar el pico solar de >4 kW en tejado para enfriar el salón a 21–23 °C a coste 0.00 €. Los forjados quedan fríos para la tarde y noche.</p>
            </div>

            <div style="background: rgba(0,0,0,0.25); padding: 0.85rem; border-radius: var(--radius-sm); border-left: 3px solid #c084fc;">
              <strong style="color: #c084fc;">4. Free-Cooling Nocturno Chimenea (22:30 – 07:30 h)</strong>
              <p style="color: var(--text-muted); margin-top: 0.35rem;">Abrir balcones superiores (Este) y puerta del patio trasero (Oeste). El aire fresco nocturno asciende por tiro térmico natural, disipando el calor sin consumir electricidad.</p>
            </div>
          </div>
        </div>
      `;
    }

    if (this.activeBioclimaticTab === 'winter') {
      return `
        <div style="display: flex; flex-direction: column; gap: 0.85rem; background: var(--bg-elevated); padding: 1.1rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
          <div style="font-weight: 800; color: #38bdf8; font-size: 0.95rem; display: flex; align-items: center; gap: 0.5rem;">
            <span>❄️</span> PROTOCOLO INVERNAL (NOVIEMBRE – MARZO): GANANCIA SOLAR PASIVA & RETENCIÓN
          </div>

          <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap: 0.85rem; font-size: 0.82rem;">
            <div style="background: rgba(0,0,0,0.25); padding: 0.85rem; border-radius: var(--radius-sm); border-left: 3px solid #f59e0b;">
              <strong style="color: #f59e0b;">1. Captación Solar Pasiva Este (09:30 – 13:30 h)</strong>
              <p style="color: var(--text-muted); margin-top: 0.35rem;">Subir persianas al 100% en Fachada Este (89° E) con ventanas cerradas. El sol bajo de invierno penetra profundamente en los suelos y muros, aportando <strong>+3.2 kWh térmicos gratuitos diarios</strong>.</p>
            </div>

            <div style="background: rgba(0,0,0,0.25); padding: 0.85rem; border-radius: var(--radius-sm); border-left: 3px solid #10b981;">
              <strong style="color: #10b981;">2. Captación Solar Pasiva Oeste (13:30 – 17:30 h)</strong>
              <p style="color: var(--text-muted); margin-top: 0.35rem;">Subir persianas de las estancias que den al patio trasero (Oeste 269°) para absorber la radiación vespertina antes del anochecer.</p>
            </div>

            <div style="background: rgba(0,0,0,0.25); padding: 0.85rem; border-radius: var(--radius-sm); border-left: 3px solid #38bdf8;">
              <strong style="color: #38bdf8;">3. Ventilación Higiénica Corta (14:00 – 14:15 h)</strong>
              <p style="color: var(--text-muted); margin-top: 0.35rem;">Ventilar únicamente 10–15 minutos a mediodía (momento de máxima temperatura exterior de 16–19 °C) para renovar el aire sin enfriar los muros.</p>
            </div>

            <div style="background: rgba(0,0,0,0.25); padding: 0.85rem; border-radius: var(--radius-sm); border-left: 3px solid #c084fc;">
              <strong style="color: #c084fc;">4. Aislamiento Nocturno Hermético (18:30 – 09:00 h)</strong>
              <p style="color: var(--text-muted); margin-top: 0.35rem;">Bajar persianas al 100% en toda la casa al anochecer. La persiana crea un colchón de aire estanco con el cristal que reduce las pérdidas térmicas nocturnas en un <strong>25–30%</strong>.</p>
            </div>
          </div>
        </div>
      `;
    }

    if (this.activeBioclimaticTab === 'appliances_guide') {
      return `
        <div style="display: flex; flex-direction: column; gap: 0.85rem; background: var(--bg-elevated); padding: 1.1rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
          <div style="font-weight: 800; color: #10b981; font-size: 0.95rem; display: flex; align-items: center; gap: 0.5rem;">
            <span>🎛️</span> COMPARADOR DE EFICIENCIA Y GUÍA DE EQUIPOS TÉRMICOS
          </div>

          <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 0.85rem; font-size: 0.82rem;">
            <!-- Daikin Clima -->
            <div style="background: rgba(0,0,0,0.25); padding: 0.85rem; border-radius: var(--radius-sm); border: 1px solid rgba(16, 185, 129, 0.4);">
              <div style="display: flex; justify-content: space-between; align-items: center;">
                <strong style="color: #10b981; font-size: 0.9rem;">❄️ Daikin Inverter (Bomba de Calor)</strong>
                <span class="badge-tag" style="background: rgba(16,185,129,0.2); color: #10b981;">COP 3.8 / EER 4.2</span>
              </div>
              <p style="color: var(--text-muted); margin-top: 0.4rem;">
                • <strong>Modo Frío:</strong> Por cada 1 kWh eléctrico, entrega 4.2 kWh de frío.<br>
                • <strong>Modo Calor:</strong> Por cada 1 kWh eléctrico, entrega 3.8 kWh de calor.<br>
                • <strong>Mejor momento:</strong> 11:00 a 16:30 h con energía solar directa a <strong>Coste 0.00 €</strong>.
              </p>
            </div>

            <!-- Ventiladores de Techo -->
            <div style="background: rgba(0,0,0,0.25); padding: 0.85rem; border-radius: var(--radius-sm); border: 1px solid rgba(56, 189, 248, 0.4);">
              <div style="display: flex; justify-content: space-between; align-items: center;">
                <strong style="color: #38bdf8; font-size: 0.9rem;">🌀 Ventiladores de Techo / Portátiles</strong>
                <span class="badge-tag" style="background: rgba(56,189,248,0.2); color: #38bdf8;">25 – 45 W</span>
              </div>
              <p style="color: var(--text-muted); margin-top: 0.4rem;">
                • <strong>Efecto Físico:</strong> Reduce la sensación térmica en <strong>-3 °C</strong> por evaporación cutánea.<br>
                • <strong>Consumo:</strong> Apenas un 5% de un aire acondicionado (ahorro del 95%).<br>
                • <strong>Mejor momento:</strong> Tardes y noches para dormir sin descargar la batería Fox-ESS.
              </p>
            </div>

            <!-- Radiadores Resistivos -->
            <div style="background: rgba(0,0,0,0.25); padding: 0.85rem; border-radius: var(--radius-sm); border: 1px solid rgba(244, 63, 94, 0.4);">
              <div style="display: flex; justify-content: space-between; align-items: center;">
                <strong style="color: #f43f5e; font-size: 0.9rem;">♨️ Radiadores Eléctricos / Aceite</strong>
                <span class="badge-tag" style="background: rgba(244,63,94,0.2); color: #f43f5e;">COP 1.0 (Ineficiente)</span>
              </div>
              <p style="color: var(--text-muted); margin-top: 0.4rem;">
                • <strong>Advertencia:</strong> Consumen 1.500–2.000 W continuos y entregan solo 1.0 kWh térmico por kWh eléctrico.<br>
                • <strong>Impacto:</strong> Consumen casi <strong>4 veces más electricidad</strong> que el Daikin en modo calor.<br>
                • <strong>Recomendación:</strong> Priorizar siempre la bomba de calor Daikin antes que emisores resistivos.
              </p>
            </div>
          </div>
        </div>
      `;
    }

    return '';
  }

  attachEvents() {
    const msgEl = document.getElementById('daikin-action-msg');
    
    // Cambio de Pestañas Bioclimáticas
    document.querySelectorAll('.bioclimatic-tab-btn').forEach(btn => {
      btn.addEventListener('click', () => {
        this.activeBioclimaticTab = btn.dataset.tab;
        this.render();
      });
    });

    // Escaneo de red
    const scanBtn = document.getElementById('btn-scan-daikin');
    if (scanBtn) {
      scanBtn.addEventListener('click', async () => {
        try {
          scanBtn.disabled = true;
          scanBtn.textContent = 'Escaneando...';
          const res = await fetch('/api/daikin/scan', { method: 'POST' });
          const data = await res.json();
          if (data.found && data.found.length > 0) {
            msgEl.textContent = `✅ ¡Encontrados ${data.found.length} adaptadores Daikin en la red!`;
          } else {
            msgEl.textContent = 'ℹ️ No se detectaron adaptadores BRP069 directos en puerto 80. Modo NILM Smart Meter activo.';
          }
          await this.fetchDaikinStatus();
          this.render();
        } catch (e) {
          msgEl.textContent = '❌ Error escaneando: ' + e.message;
        } finally {
          scanBtn.disabled = false;
          scanBtn.textContent = '🔍 Escanear Red';
        }
      });
    }

    // Botones de ajuste de temperatura
    document.querySelectorAll('.daikin-temp-down').forEach(btn => {
      btn.addEventListener('click', async () => {
        const unitId = btn.dataset.unit;
        await this.adjustTemp(unitId, -1);
      });
    });

    document.querySelectorAll('.daikin-temp-up').forEach(btn => {
      btn.addEventListener('click', async () => {
        const unitId = btn.dataset.unit;
        await this.adjustTemp(unitId, +1);
      });
    });

    document.querySelectorAll('.daikin-precool-btn').forEach(btn => {
      btn.addEventListener('click', async () => {
        const unitId = btn.dataset.unit;
        await this.sendControl(unitId, 21.0);
      });
    });
  }

  async adjustTemp(unitId, delta) {
    const unit = this.daikinStatus?.units?.find(u => u.id === unitId);
    const curr = unit ? unit.target_temp_c : 24.0;
    const next = Math.max(18, Math.min(30, curr + delta));
    await this.sendControl(unitId, next);
  }

  async sendControl(unitId, targetTemp) {
    const msgEl = document.getElementById('daikin-action-msg');
    try {
      const res = await fetch('/api/daikin/control', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ unit_id: unitId, target_temp_c: targetTemp, power_on: true })
      });
      const data = await res.json();
      if (data.success) {
        if (msgEl) msgEl.textContent = `✅ Consigna ajustada a ${targetTemp}°C para ${unitId === 'daikin_salon' ? 'Daikin Salón' : 'Daikin Dormitorio'}.`;
        await this.fetchDaikinStatus();
        this.render();
      }
    } catch (e) {
      if (msgEl) msgEl.textContent = '❌ Error: ' + e.message;
    }
  }
}
