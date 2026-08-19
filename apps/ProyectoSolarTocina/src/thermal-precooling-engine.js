/**
 * Modelo de Inercia Térmica de Edificación RC (2 Estados) & Control Daikin Clima (Salón + Dormitorio)
 * - Muestra telemetría en tiempo real de las 2 máquinas Daikin (Salón y Dormitorio)
 * - Desagrega la potencia NILM en vatios (W) a partir del Smart Meter del Sunworks
 * - Permite enviar consignas de temperatura y activar el Modo Pre-cooling Automático
 */

export class ThermalPrecoolingEngine {
  constructor(containerId) {
    this.container = document.getElementById(containerId);
    this.R_th = 3.2;  // Resistencia térmica equivalente (K / kW)
    this.C_th = 14.5; // Capacidad calorífica de la estructura (kWh / K)
    this.daikinCop = 3.8;
    this.daikinStatus = null;

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

  render(hourlyForecast = []) {
    if (!this.container) return;
    const sim = this.simulate24h(hourlyForecast);
    const ds = this.daikinStatus || {
      units: [
        { id: "daikin_salon", name: "Daikin Salón", target_temp_c: 24, indoor_temp_c: 26.5, outdoor_temp_c: 34, power_w: 0, power_on: false, connected: false },
        { id: "daikin_dormitorio", name: "Daikin Dormitorio", target_temp_c: 24, indoor_temp_c: 27.0, outdoor_temp_c: 34, power_w: 0, power_on: false, connected: false }
      ],
      nilm_power: { daikin_salon: 0, daikin_dormitorio: 0, total_ac_w: 0 },
      recommendation: { badge: "🟢 Pre-cooling Activo (Recomendado)", action: "Enfriar a 21 °C con excedente gratis", suggested_temp_c: 21.0 }
    };

    this.container.innerHTML = `
      <div class="thermal-precool-card" style="background: var(--bg-card); border: 1px solid rgba(56, 189, 248, 0.35); border-radius: var(--radius-lg); padding: 1.25rem; box-shadow: var(--shadow-card); display: flex; flex-direction: column; gap: 1.25rem;">
        
        <!-- Cabecera -->
        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.75rem; flex-wrap: wrap; gap: 0.5rem;">
          <div style="display: flex; align-items: center; gap: 0.6rem;">
            <div style="font-size: 1.4rem; background: rgba(56, 189, 248, 0.15); width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; border-radius: var(--radius-md);">❄️</div>
            <div>
              <h3 style="font-size: 1.05rem; font-weight: 700; color: var(--text-primary);">Climatización Inteligente Daikin & Pre-cooling de Muros</h3>
              <div style="font-size: 0.75rem; color: var(--text-muted);">2 Máquinas Inverter (Salón + Dormitorio) · Desagregación NILM & Control Adaptativo</div>
            </div>
          </div>
          <div style="display: flex; gap: 0.5rem; align-items: center;">
            <span class="badge-tag" style="background: rgba(56, 189, 248, 0.2); color: #38bdf8; font-weight: 700;">${ds.recommendation ? ds.recommendation.badge : '🟢 Modo Inteligente'}</span>
            <button id="btn-scan-daikin" class="refresh-button" style="padding: 0.4rem 0.75rem; font-size: 0.75rem; background: var(--bg-card);" title="Escanear adaptadores WiFi BRP069 en la red local">
              🔍 Escanear Red
            </button>
          </div>
        </div>

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
            <div style="font-size: 0.7rem; color: var(--text-muted);">Muros y forjados amortiguadores</div>
          </div>

          <div style="background: var(--bg-elevated); padding: 0.75rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
            <div style="font-size: 0.7rem; color: #c084fc; font-weight: 700; text-transform: uppercase;">Ahorro Económico Clima</div>
            <div style="font-size: 1.25rem; font-weight: 800; color: #c084fc;">~${(parseFloat(sim.nightBatSavedKwh) * 0.15 * 30).toFixed(1)} € / mes</div>
            <div style="font-size: 0.7rem; color: var(--text-muted);">En factura eléctrica</div>
          </div>
        </div>

        <div id="daikin-action-msg" style="font-size: 0.8rem; min-height: 1rem; color: #10b981;"></div>
      </div>
    `;

    this.attachEvents();
  }

  attachEvents() {
    const msgEl = document.getElementById('daikin-action-msg');
    
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
