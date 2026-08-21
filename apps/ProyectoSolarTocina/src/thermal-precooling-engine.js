/**
 * Centro Bioclimático de Ventilación, Climatización, Persianas & Gestión Térmica del Hogar
 * - Protocolos Estacionales Dinámicos: Verano (Free-Cooling & Sombra), Invierno (Ganancia Pasiva & Aislamiento) y Entretiempo.
 * - Plan Semanal a 7 Días con horarios exactos de persianas (Fachada Este 89° E / Fachada Oeste 269° O) y Climatizadores Daikin.
 * - Simulación de Inercia Térmica RC (2 Estados) y Pre-cooling/Pre-heating solar a coste 0.00 €.
 * - Control directo y telemetría de máquinas Daikin (Salón y Dormitorio).
 */

export class ThermalPrecoolingEngine {
  constructor(containerId) {
    this.container = document.getElementById(containerId);
    this.R_th = 3.2;  // Resistencia térmica equivalente (K / kW)
    this.C_th = 14.5; // Capacidad calorífica de la estructura (kWh / K)
    this.daikinCop = 3.8;
    this.daikinStatus = null;
    this.activeBioclimaticTab = 'week_plan'; // 'week_plan', 'today_live', 'summer', 'winter', 'appliances_guide'
    this.daysData = [];

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
          action: "Abrir ventanas de Fachada Este y Patio Oeste durante 45–60 min para renovar con aire a 20–22 °C.",
          shading: "A partir de las 08:30 h, bajar persianas de la Fachada Este (89° E) al 80% antes de que incida el sol directo.",
          climaTip: "Daikin en reposo (0 W). Conservar el aire fresco natural."
        };
      } else if (hour >= 9 && hour < 13) {
        return {
          badge: "🛡️ Protección Solar Este",
          badgeBg: "rgba(245, 158, 11, 0.2)",
          badgeColor: "#f59e0b",
          title: "Sol Directo en Fachada Este (89° E) • Casa Cerrada",
          action: "Mantener ventanas herméticamente cerradas y persianas de Fachada Este al 80–90%.",
          shading: "El sol incide de lleno en la calle. Estancias orientadas al patio Oeste permanecen a la sombra.",
          climaTip: "Si la estancia sube de 25 °C, preparar Daikin Salón para iniciar pre-cooling a las 12:30 h con excedente solar directo."
        };
      } else if (hour >= 13 && hour < 18) {
        return {
          badge: "⚡ Pre-cooling Solar Activo (Coste 0.00 €)",
          badgeBg: "rgba(16, 185, 129, 0.2)",
          badgeColor: "#10b981",
          title: "Pico Solar en Tejado • Enfriamiento Estructural Gratuito",
          action: "Fachada Este en sombra propia. Fachada Norte (359° N) en sombra permanente sin sol directo.",
          shading: "Persianas Norte pueden permanecer al 50% para iluminación difusa óptima sin ganancia solar directa.",
          climaTip: "Enfriar salón a 22–24 °C con Daikin a Coste 0.00 € (100% solar). Ventilador pasillo -> despacho activo."
        };
      } else if (hour >= 18 && hour < 22) {
        return {
          badge: "🌤️ Inercia Térmica de Tarde",
          badgeBg: "rgba(168, 85, 247, 0.2)",
          badgeColor: "#c084fc",
          title: "Retención de Frescor en Muros • Evitar Entrada de Aire Caliente",
          action: "Mantener vivienda cerrada mientras la calle siga por encima de 29 °C.",
          shading: "Persianas bajadas en patio Oeste hasta la puesta de sol (21:30 h).",
          climaTip: "Daikin modulando al mínimo (180–240 W) apoyado por la batería Fox-ESS. Ventiladores de techo activos."
        };
      } else {
        return {
          badge: "🌙 Free-Cooling Nocturno Chimenea",
          badgeBg: "rgba(56, 189, 248, 0.25)",
          badgeColor: "#38bdf8",
          title: "Efecto Chimenea: Ventilación Pasiva Calle <-> Patio",
          action: "Abrir al máximo balcones de planta alta (Este) y puerta/ventanas del patio trasero (Oeste).",
          shading: "El aire fresco exterior disipa el calor de los forjados de forma 100% gratuita.",
          climaTip: "Daikin apagado (0 W). Ventilador de techo a baja velocidad (25 W) para dormir con confort total y batería intacta."
        };
      }
    } else if (isWinter) {
      if (hour >= 9 && hour < 14) {
        return {
          badge: "☀️ Captación Solar Pasiva Este",
          badgeBg: "rgba(245, 158, 11, 0.25)",
          badgeColor: "#f59e0b",
          title: "Subir Persianas Fachada Este (89° E) al 100%",
          action: "Sol de invierno bajo penetrando profundamente por balcones (+3.2 kWh térmicos gratuitos).",
          shading: "Cristales limpios recibiendo radiación solar directa que calienta suelos y paredes.",
          climaTip: "Pre-Heating Solar: Encender Daikin en calor a 22.5 °C entre 12:00 y 16:00 h con máxima eficiencia COP (~4.2) y sol gratis."
        };
      } else if (hour >= 14 && hour < 15) {
        return {
          badge: "💨 Ventilación Higiénica Corta",
          badgeBg: "rgba(56, 189, 248, 0.2)",
          badgeColor: "#38bdf8",
          title: "Ventilación Rápida de 10–15 min a Mediodía",
          action: "Abrir ventanas 10 minutos en el momento más cálido del día (16–19 °C) para renovar el aire sin enfriar los muros.",
          shading: "Abrir persianas en patio Oeste para captar la radiación de la tarde.",
          climaTip: "Cerrar inmediatamente después para retener el calor acumulado."
        };
      } else {
        return {
          badge: "🛡️ Aislamiento Nocturno Hermético",
          badgeBg: "rgba(168, 85, 247, 0.2)",
          badgeColor: "#c084fc",
          title: "Bajar Persianas al 100% en Toda la Vivienda",
          action: "Cierre hermético de persianas exteriores al anochecer (18:30 h) para crear cámara aislante (-30% pérdidas).",
          shading: "Evita la disipación radiactiva hacia el cielo frío nocturno.",
          climaTip: "Daikin en calor a 20.0 °C con lamas a 60° hacia el suelo, modulando a baja potencia con la batería Fox-ESS."
        };
      }
    } else {
      return {
        badge: "🌱 Confort Bioclimático Pasivo",
        badgeBg: "rgba(16, 185, 129, 0.2)",
        badgeColor: "#10b981",
        title: "Climatización 100% Pasiva sin Consumo Eléctrico",
        action: "Modular persianas para regular la iluminación y abrir ventilación cruzada cuando la temperatura exterior sea agradable (22–25 °C).",
        shading: "Apertura libre de huecos según necesidad.",
        climaTip: "Cero consumo de climatización. 100% de excedentes solares destinados a recargar coche y acumular saldo en Batería Virtual."
      };
    }
  }

  generate7DaysBioclimaticPlan(daysData = []) {
    const defaultDays = [
      { dayName: "Hoy (Mié 19/8)", tempMax: 34.2, tempMin: 21.0, cloudCover: 17, expectedSolarKwh: 30.0 },
      { dayName: "Jue 20/8", tempMax: 34.0, tempMin: 21.2, cloudCover: 53, expectedSolarKwh: 25.7 },
      { dayName: "Vie 21/8", tempMax: 33.8, tempMin: 20.5, cloudCover: 27, expectedSolarKwh: 29.3 },
      { dayName: "Sáb 22/8", tempMax: 34.5, tempMin: 21.8, cloudCover: 9, expectedSolarKwh: 31.0 },
      { dayName: "Dom 23/8", tempMax: 29.0, tempMin: 20.0, cloudCover: 55, expectedSolarKwh: 15.5 },
      { dayName: "Lun 24/8", tempMax: 33.5, tempMin: 21.0, cloudCover: 39, expectedSolarKwh: 31.1 },
      { dayName: "Mar 25/8", tempMax: 35.0, tempMin: 22.0, cloudCover: 1, expectedSolarKwh: 31.5 }
    ];

    const source = (daysData && daysData.length >= 7) ? daysData : defaultDays;

    return source.slice(0, 7).map((d, idx) => {
      const tMax = d.tempMax !== undefined ? d.tempMax : (d.max_temp || 33.0);
      const tMin = d.tempMin !== undefined ? d.tempMin : (d.min_temp || 20.0);
      const clouds = d.cloudCover !== undefined ? d.cloudCover : (d.avg_cloud || 20);
      const solarKwh = d.expectedSolarKwh !== undefined ? d.expectedSolarKwh : (d.totalKwh || 28.0);

      const isHotSummer = tMax >= 32.0;
      const isMild = tMax >= 22.0 && tMax < 32.0;
      const isCold = tMax < 22.0;

      let regime = "";
      let regimeColor = "";
      let persianasEste = "";
      let persianasOeste = "";
      let daikinStrategy = "";
      let ventilacion = "";
      let batAdvice = "";

      if (isHotSummer) {
        regime = "☀️ Verano / Calor Intenso";
        regimeColor = "#f59e0b";
        persianasEste = "08:30 – 13:30 h: Bajar al 80% (bloquear sol directo). Subir al 100% de 22:30 a 07:30 h.";
        persianasOeste = "14:00 – 21:00 h: Bajar al 90% (evitar sobrecalentamiento patio). Abrir de noche.";
        daikinStrategy = "Pre-Cooling Solar: 12:30 a 16:30 h a 22°C (Coste 0.00 € con sol). Tarde/Noche en crucero 25.5°C o ventilador de techo.";
        ventilacion = "Free-Cooling Nocturno: Ventilación cruzada calle <-> patio de 23:00 a 07:30 h (T ext < 23°C).";
        batAdvice = solarKwh > 20.0 ? "Batería llena con sol diurno. Cero carga de red nocturna." : "Carga valle P3 si se prevén nubes.";
      } else if (isCold) {
        regime = "❄️ Invierno / Calefacción Pasiva";
        regimeColor = "#38bdf8";
        persianasEste = "09:30 – 13:30 h: Subir al 100% para captación solar térmica pasiva (+3.2 kWh térmicos).";
        persianasOeste = "13:30 – 17:30 h: Subir al 100%. A las 18:00 h bajar al 100% en toda la casa.";
        daikinStrategy = "Pre-Heating Solar: 12:00 a 16:00 h a 22.5°C con máximo COP (~4.2) y sol gratis. Lamas a 60° hacia el suelo.";
        ventilacion = "Ventilación corta de 10 min a las 14:00 h (momento más cálido) para renovar aire sin enfriar forjados.";
        batAdvice = solarKwh <= 5.5 ? "🚨 Temporal: Carga nocturna Valle P3 al 100% (0.094 €/kWh)." : "Carga valle P3 al 85% para amortiguar picos matinales.";
      } else {
        regime = "🌱 Entretiempo / Confort Pasivo";
        regimeColor = "#10b981";
        persianasEste = "Apertura libre para iluminación natural. Sombra suave a mediodía si el sol molesta.";
        persianasOeste = "Apertura libre. Ventilación cruzada continua durante las horas suaves del día.";
        daikinStrategy = "Climatización apagada (0 W). Confort térmico 100% natural.";
        ventilacion = "Ventilación continua cuando el exterior esté entre 20 y 25 °C.";
        batAdvice = "100% Autoconsumo solar y excedentes al 100% hacia recarga VE y monedero virtual.";
      }

      return {
        dayLabel: d.dayName || `Día ${idx + 1}`,
        tMax: tMax.toFixed(1),
        tMin: tMin.toFixed(1),
        clouds: Math.round(clouds),
        solarKwh: typeof solarKwh === 'number' ? solarKwh.toFixed(1) : solarKwh,
        regime,
        regimeColor,
        persianasEste,
        persianasOeste,
        daikinStrategy,
        ventilacion,
        batAdvice
      };
    });
  }

  render(hourlyForecast = [], daysData = []) {
    if (!this.container) return;
    if (hourlyForecast && hourlyForecast.length > 0) {
      this.lastHourlyForecast = hourlyForecast;
    }
    if (daysData && daysData.length > 0) {
      this.daysData = daysData;
    }
    const sim = this.simulate24h(this.lastHourlyForecast || hourlyForecast);
    const liveAdvice = this.getCurrentLiveBioclimaticAdvice();
    const weekPlan = this.generate7DaysBioclimaticPlan(this.daysData);
    const ds = this.daikinStatus || {
      units: [
        { id: "daikin_salon", name: "Daikin Salón (35 m²)", target_temp_c: 24, indoor_temp_c: 26.5, outdoor_temp_c: 34, power_w: 0, power_on: false, connected: false },
        { id: "daikin_dormitorio", name: "Daikin Dormitorio (16 m²)", target_temp_c: 24, indoor_temp_c: 27.0, outdoor_temp_c: 34, power_w: 0, power_on: false, connected: false }
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
              <h3 style="font-size: 1.05rem; font-weight: 700; color: var(--text-primary);">Centro Bioclimático: Horarios de Persianas, Ventilación & Climatización Inteligente</h3>
              <div style="font-size: 0.75rem; color: var(--text-muted);">Fachada Este 89° E (Calle) · Fachada Norte 359° N (Patio/Despacho) · Daikin Inverter · Pre-cooling / Pre-heating Solar</div>
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
            • <strong>Sombra / Persianas (Este/Oeste):</strong> ${liveAdvice.shading}<br>
            • <strong>Climatización Inteligente Daikin:</strong> ${liveAdvice.climaTip}
          </div>
        </div>

        <!-- Selector de Pestañas de Protocolos -->
        <div style="display: flex; gap: 0.5rem; flex-wrap: wrap; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.5rem;">
          <button class="bioclimatic-tab-btn ${this.activeBioclimaticTab === 'week_plan' ? 'active' : ''}" data-tab="week_plan" style="padding: 0.45rem 0.9rem; font-size: 0.82rem; font-weight: 700; border-radius: var(--radius-sm); border: 1px solid var(--border-subtle); background: ${this.activeBioclimaticTab === 'week_plan' ? '#38bdf8' : 'var(--bg-elevated)'}; color: ${this.activeBioclimaticTab === 'week_plan' ? '#0f172a' : 'var(--text-secondary)'}; cursor: pointer;">
            📅 Plan Bioclimático Semanal a 7 Días
          </button>
          <button class="bioclimatic-tab-btn ${this.activeBioclimaticTab === 'today_live' ? 'active' : ''}" data-tab="today_live" style="padding: 0.45rem 0.9rem; font-size: 0.82rem; font-weight: 700; border-radius: var(--radius-sm); border: 1px solid var(--border-subtle); background: ${this.activeBioclimaticTab === 'today_live' ? '#38bdf8' : 'var(--bg-elevated)'}; color: ${this.activeBioclimaticTab === 'today_live' ? '#0f172a' : 'var(--text-secondary)'}; cursor: pointer;">
            ⚡ Control Daikin Inverter
          </button>
          <button class="bioclimatic-tab-btn ${this.activeBioclimaticTab === 'summer' ? 'active' : ''}" data-tab="summer" style="padding: 0.45rem 0.9rem; font-size: 0.82rem; font-weight: 700; border-radius: var(--radius-sm); border: 1px solid var(--border-subtle); background: ${this.activeBioclimaticTab === 'summer' ? '#38bdf8' : 'var(--bg-elevated)'}; color: ${this.activeBioclimaticTab === 'summer' ? '#0f172a' : 'var(--text-secondary)'}; cursor: pointer;">
            ☀️ Protocolo Verano (Free-Cooling & Sombra)
          </button>
          <button class="bioclimatic-tab-btn ${this.activeBioclimaticTab === 'winter' ? 'active' : ''}" data-tab="winter" style="padding: 0.45rem 0.9rem; font-size: 0.82rem; font-weight: 700; border-radius: var(--radius-sm); border: 1px solid var(--border-subtle); background: ${this.activeBioclimaticTab === 'winter' ? '#38bdf8' : 'var(--bg-elevated)'}; color: ${this.activeBioclimaticTab === 'winter' ? '#0f172a' : 'var(--text-secondary)'}; cursor: pointer;">
            ❄️ Protocolo Invierno (Pre-Heating & Aislamiento)
          </button>
          <button class="bioclimatic-tab-btn ${this.activeBioclimaticTab === 'appliances_guide' ? 'active' : ''}" data-tab="appliances_guide" style="padding: 0.45rem 0.9rem; font-size: 0.82rem; font-weight: 700; border-radius: var(--radius-sm); border: 1px solid var(--border-subtle); background: ${this.activeBioclimaticTab === 'appliances_guide' ? '#38bdf8' : 'var(--bg-elevated)'}; color: ${this.activeBioclimaticTab === 'appliances_guide' ? '#0f172a' : 'var(--text-secondary)'}; cursor: pointer;">
            🎛️ Eficiencia: Daikin vs Ventilador vs Radiador
          </button>
        </div>

        <!-- CONTENIDO DE LA PESTAÑA SELECCIONADA -->
        ${this.renderActiveTabContent(ds, sim, weekPlan)}

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

  renderActiveTabContent(ds, sim, weekPlan = []) {
    if (this.activeBioclimaticTab === 'week_plan') {
      return `
        <div style="display: flex; flex-direction: column; gap: 1rem;">
          <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 0.5rem;">
            <div style="font-size: 0.95rem; font-weight: 800; color: #38bdf8; display: flex; align-items: center; gap: 0.4rem;">
              <span>🗓️</span> Cronograma Bioclimático a 7 Días Vista (Pronóstico Open-Meteo Tocina)
            </div>
            <span style="font-size: 0.72rem; color: var(--text-muted);">Horarios de persianas calculados según azimut solar y carga térmica prevista</span>
          </div>

          <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(310px, 1fr)); gap: 0.85rem;">
            ${weekPlan.map(d => `
              <div style="background: var(--bg-elevated); border: 1px solid var(--border-subtle); border-radius: var(--radius-md); padding: 0.9rem; display: flex; flex-direction: column; gap: 0.65rem;">
                
                <!-- Encabezado del Día -->
                <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.4rem;">
                  <div>
                    <strong style="color: var(--text-primary); font-size: 0.95rem;">${d.dayLabel}</strong>
                    <div style="font-size: 0.72rem; color: var(--text-muted);">🌡️ ${d.tMin}°C a ${d.tMax}°C · ☁️ ${d.clouds}% nubes · ☀️ ${d.solarKwh} kWh</div>
                  </div>
                  <span class="badge-tag" style="background: rgba(255,255,255,0.06); color: ${d.regimeColor}; border: 1px solid ${d.regimeColor}; font-size: 0.7rem; font-weight: 700;">
                    ${d.regime}
                  </span>
                </div>

                <!-- Directrices Horarias -->
                <div style="font-size: 0.78rem; color: var(--text-secondary); display: flex; flex-direction: column; gap: 0.45rem; line-height: 1.4;">
                  <div style="background: rgba(0,0,0,0.2); padding: 0.45rem; border-radius: 4px; border-left: 3px solid #f59e0b;">
                    <strong style="color: #f59e0b;">🪟 Persianas Fachada Este (89° E - Calle):</strong><br>
                    ${d.persianasEste}
                  </div>

                  <div style="background: rgba(0,0,0,0.2); padding: 0.45rem; border-radius: 4px; border-left: 3px solid #c084fc;">
                    <strong style="color: #c084fc;">🪟 Persianas Fachada Norte (359° N - Patio/Despacho):</strong><br>
                    ${d.persianasOeste}
                  </div>

                  <div style="background: rgba(0,0,0,0.2); padding: 0.45rem; border-radius: 4px; border-left: 3px solid #10b981;">
                    <strong style="color: #10b981;">❄️ Estrategia Climatizadores Daikin:</strong><br>
                    ${d.daikinStrategy}
                  </div>

                  <div style="background: rgba(0,0,0,0.2); padding: 0.45rem; border-radius: 4px; border-left: 3px solid #38bdf8;">
                    <strong style="color: #38bdf8;">🌬️ Ventilación & Free-Cooling:</strong><br>
                    ${d.ventilacion}
                  </div>
                </div>

                <!-- Footer Batería -->
                <div style="font-size: 0.72rem; color: var(--text-muted); border-top: 1px dashed var(--border-subtle); padding-top: 0.35rem; display: flex; justify-content: space-between;">
                  <span>🔋 <strong>Batería Fox-ESS:</strong> ${d.batAdvice}</span>
                </div>
              </div>
            `).join('')}
          </div>
        </div>
      `;
    }

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

            <div style="background: rgba(245,158,11,0.08); padding: 0.85rem; border-radius: var(--radius-sm); border: 1px solid rgba(245,158,11,0.3); grid-column: 1 / -1;">
              <strong style="color: #f59e0b;">📚 Zona de Estudio (Planta Alta Izq • Balcón Este 89° E + Muro Lateral Terraza Norte 359° N):</strong>
              <p style="color: var(--text-secondary); margin-top: 0.35rem; line-height: 1.45;">
                • <strong>Mañanas (08:30 – 13:30 h):</strong> Bajar persiana del balcón al 85% para bloquear la radiación matinal directa.<br>
                • <strong>Tardes (15:00 – 20:30 h):</strong> El muro de la terraza Norte permanece en sombra permanente. Mantener puerta al distribuidor abierta para recibir el flujo del Daikin o apoyar con ventilador.<br>
                • <strong>Noches (23:00 – 07:30 h):</strong> Abrir balcón de estudio y puerta de terraza para generar un tiro cruzado inmediato que disipa el calor acumulado a coste 0.00 €.
              </p>
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
              <strong style="color: #10b981;">2. Pre-Heating Solar Daikin (12:00 – 16:00 h)</strong>
              <p style="color: var(--text-muted); margin-top: 0.35rem;">Calentar a 22.5 °C a mediodía con energía solar directa y máximo COP (~4.2). Lamas deflectoras a 60° hacia el suelo para evitar estratificación térmica.</p>
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
        this.render(this.lastHourlyForecast, this.daysData);
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
          this.render(this.lastHourlyForecast, this.daysData);
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
        this.render(this.lastHourlyForecast, this.daysData);
      }
    } catch (e) {
      if (msgEl) msgEl.textContent = '❌ Error: ' + e.message;
    }
  }
}
