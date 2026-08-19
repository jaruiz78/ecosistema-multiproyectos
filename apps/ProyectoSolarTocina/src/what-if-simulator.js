/**
 * Simulador "What-If" Interactivo de Consumos de Verano Avanzado
 * Cruza datos teóricos con telemetría en vivo Modbus TCP (192.168.1.66),
 * evalúa la viabilidad técnica total (límite ICP 4.6 kW + Batería Fox-ESS + Sol),
 * calcula el coste exacto de red por hora y ciclo,
 * y permite sincronizar y calibrar en caliente con el Smart Meter real para afinar la IA.
 */

import { APPLIANCE_CATALOG } from './appliance-recommender.js';

export class WhatIfSimulator {
  constructor(containerId, onStateChange = null) {
    this.container = document.getElementById(containerId);
    this.onStateChange = onStateChange;

    // Estado inicial: Electrodomésticos habituales de una tarde de verano en Tocina
    this.activeStates = {
      daikin_salon: true,
      daikin_bedroom: false,
      midea_fridge: true,
      beko_washer: false,
      fagor_dishwasher: false,
      teka_oven: false,
      cecofry_airfryer: false,
      grunkel_toaster: false,
      digital_microwave: false,
      superser_dryer: false,
      telework_laptops: true,
      living_tv: true,
      taurus_fan: false,
      home_lights_wifi: true,
      solar_thermal_acs: true,
      omoda7_ev_charge: false
    };

    // Modos de simulación
    this.currentMode = 'live'; // 'live', 'peak_solar', 'night_zero', 'morning_early'
    this.liveSolarW = 2450;
    this.liveBatSoc = 100;
    this.liveGridPriceEurKwh = 0.135;
    this.liveHomeLoadW = 831; // Medido real del Smart Meter

    this.simSolarW = 2450;
    this.simBatterySoc = 100;
    this.batteryCapacityKwh = 10.36;
    this.batteryMaxDischargeKw = 5.00; // Máxima descarga continua Fox-ESS EP5 HV
    this.contractedPowerKw = 4.60;     // Potencia contratada en factura (4.6 kW)
    this.lastCalibration = null;

    this.initUI();
    this.fetchLatestCalibration();
  }

  async fetchLatestCalibration() {
    try {
      const res = await fetch('/api/whatif/state');
      if (res.ok) {
        const data = await res.json();
        if (data.latest_calibration) {
          this.lastCalibration = data.latest_calibration;
          if (data.latest_calibration.active_appliances) {
            this.activeStates = { ...this.activeStates, ...data.latest_calibration.active_appliances };
            this.updateToggleCards();
          }
        }
        if (data.live_measured_home_load_w) {
          this.liveHomeLoadW = data.live_measured_home_load_w;
        }
        this.renderCalibrationBanner();
        this.renderResults();
      }
    } catch (e) {
      console.warn('[WhatIf] Error fetching state:', e);
    }
  }

  updateLiveTelemetry(solarW, batSoc, priceEurKwh = 0.135, homeLoadW = null) {
    this.liveSolarW = solarW;
    this.liveBatSoc = batSoc;
    if (priceEurKwh) this.liveGridPriceEurKwh = priceEurKwh;
    if (homeLoadW !== null && homeLoadW !== undefined) {
      this.liveHomeLoadW = homeLoadW;
    }

    if (this.currentMode === 'live') {
      this.simSolarW = this.liveSolarW;
      this.simBatterySoc = this.liveBatSoc;
      this.renderResults();
      this.renderCalibrationBanner();
    }
  }

  setSimulationMode(mode) {
    this.currentMode = mode;
    if (mode === 'live') {
      this.simSolarW = this.liveSolarW;
      this.simBatterySoc = this.liveBatSoc;
    } else if (mode === 'peak_solar') {
      this.simSolarW = 4600; // 4.6 kW pico de mediodía
      this.simBatterySoc = 100;
    } else if (mode === 'night_zero') {
      this.simSolarW = 0;    // Noche 0W
      this.simBatterySoc = 80;
    } else if (mode === 'morning_early') {
      this.simSolarW = 750;  // 08:00 AM sol este
      this.simBatterySoc = 50;
    }
    this.updateModeButtons();
    this.renderResults();
  }

  setPreset(presetName) {
    if (presetName === 'summer_afternoon') {
      this.activeStates = {
        daikin_salon: true,
        daikin_bedroom: false,
        midea_fridge: true,
        beko_washer: false,
        fagor_dishwasher: false,
        teka_oven: false,
        cecofry_airfryer: false,
        grunkel_toaster: false,
        digital_microwave: false,
        superser_dryer: false,
        telework_laptops: true,
        living_tv: true,
        taurus_fan: false,
        home_lights_wifi: true,
        solar_thermal_acs: true,
        omoda7_ev_charge: false
      };
    } else if (presetName === 'max_solar_lunch') {
      // 14:00 - 15:00 Almuerzo con máximo sol
      this.activeStates = {
        daikin_salon: true,
        daikin_bedroom: false,
        midea_fridge: true,
        beko_washer: true,
        fagor_dishwasher: false,
        teka_oven: false,
        cecofry_airfryer: true,
        grunkel_toaster: false,
        digital_microwave: false,
        superser_dryer: false,
        telework_laptops: true,
        living_tv: true,
        taurus_fan: false,
        home_lights_wifi: true,
        solar_thermal_acs: true,
        omoda7_ev_charge: true
      };
    } else if (presetName === 'night_quiet') {
      // 00:00 - 07:00 Noche en reposo
      this.activeStates = {
        daikin_salon: false,
        daikin_bedroom: true,
        midea_fridge: true,
        beko_washer: false,
        fagor_dishwasher: false,
        teka_oven: false,
        cecofry_airfryer: false,
        grunkel_toaster: false,
        digital_microwave: false,
        superser_dryer: false,
        telework_laptops: false,
        living_tv: false,
        taurus_fan: false,
        home_lights_wifi: true,
        solar_thermal_acs: true,
        omoda7_ev_charge: false
      };
    } else if (presetName === 'breakfast_routine') {
      // 08:00 - 09:00 Desayuno + Teletrabajo
      this.activeStates = {
        daikin_salon: false,
        daikin_bedroom: false,
        midea_fridge: true,
        beko_washer: false,
        fagor_dishwasher: false,
        teka_oven: false,
        cecofry_airfryer: false,
        grunkel_toaster: true,
        coffee_maker: true,
        digital_microwave: false,
        superser_dryer: false,
        telework_laptops: true,
        wife_study_station: false,
        user_study_overtime: false,
        living_tv: false,
        taurus_fan: false,
        home_lights_wifi: true,
        solar_thermal_acs: true,
        omoda7_ev_charge: false
      };
    } else if (presetName === 'dual_study_telework') {
      // Teletrabajo Usuario + Estudios Mujer
      this.activeStates = {
        daikin_salon: true,
        daikin_bedroom: false,
        midea_fridge: true,
        beko_washer: false,
        fagor_dishwasher: false,
        teka_oven: false,
        cecofry_airfryer: false,
        grunkel_toaster: false,
        coffee_maker: false,
        digital_microwave: false,
        superser_dryer: false,
        telework_laptops: true,
        wife_study_station: true,
        user_study_overtime: true,
        living_tv: false,
        taurus_fan: true,
        home_lights_wifi: true,
        solar_thermal_acs: true,
        omoda7_ev_charge: false
      };
    } else if (presetName === 'dinner_routine') {
      // 20:30 - 21:00 Cena en familia
      this.activeStates = {
        daikin_salon: true,
        daikin_bedroom: false,
        midea_fridge: true,
        beko_washer: false,
        fagor_dishwasher: false,
        teka_oven: false,
        cecofry_airfryer: true,
        grunkel_toaster: true,
        coffee_maker: false,
        digital_microwave: true,
        superser_dryer: false,
        telework_laptops: false,
        wife_study_station: false,
        user_study_overtime: true,
        living_tv: true,
        taurus_fan: true,
        home_lights_wifi: true,
        solar_thermal_acs: true,
        omoda7_ev_charge: false
      };
    } else if (presetName === 'stress_test_all') {
      // Todo encendido
      Object.keys(this.activeStates).forEach(k => {
        this.activeStates[k] = true;
      });
    }
    this.updateToggleCards();
    this.renderResults();
    if (this.onStateChange) this.onStateChange(this.getCalculatedResults());
  }

  toggleAppliance(applianceId) {
    this.activeStates[applianceId] = !this.activeStates[applianceId];
    this.updateToggleCards();
    this.renderResults();
    this.renderCalibrationBanner();
    if (this.onStateChange) this.onStateChange(this.getCalculatedResults());
  }

  getCalculatedResults() {
    let totalLoadW = 0;
    const activeList = [];

    APPLIANCE_CATALOG.forEach(app => {
      if (this.activeStates[app.id]) {
        totalLoadW += app.realPowerW;
        activeList.push(app);
      }
    });

    const totalLoadKw = totalLoadW / 1000.0;
    const solarKw = this.simSolarW / 1000.0;
    const currentBatKwh = (this.simBatterySoc / 100.0) * this.batteryCapacityKwh;
    
    // 1. Cobertura Solar Directa
    const solarCoveredKw = Math.min(totalLoadKw, solarKw);
    const deficitAfterSolarKw = Math.max(0, totalLoadKw - solarCoveredKw);

    // 2. Cobertura Batería Fox-ESS
    let batDischargeKw = 0;
    let deficitAfterBatteryKw = 0;

    if (deficitAfterSolarKw > 0) {
      if (currentBatKwh > 0.5) { // Batería con carga
        batDischargeKw = Math.min(deficitAfterSolarKw, this.batteryMaxDischargeKw);
        deficitAfterBatteryKw = Math.max(0, deficitAfterSolarKw - batDischargeKw);
      } else {
        batDischargeKw = 0;
        deficitAfterBatteryKw = deficitAfterSolarKw;
      }
    }

    // 3. Importación de Red Eléctrica
    const gridImportKw = deficitAfterBatteryKw;
    const surplusExportKw = Math.max(0, solarKw - totalLoadKw);

    // 4. Diagnóstico de Viabilidad Técnica & Seguridad ICP
    const maxSystemCapacityKw = solarKw + this.batteryMaxDischargeKw + this.contractedPowerKw;
    const icpHeadroomKw = this.contractedPowerKw - gridImportKw;

    let viabilityStatus = 'green';
    let viabilityTitle = '';
    let viabilityDesc = '';

    if (gridImportKw === 0) {
      viabilityStatus = 'green';
      viabilityTitle = '🟢 100% AUTOSUFICIENTE (COSTE 0,00 €)';
      viabilityDesc = surplusExportKw > 0 
        ? `Todos los ${activeList.length} aparatos funcionan con Sol Directo y sobran +${surplusExportKw.toFixed(2)} kW para cargar batería o verter a Batería Virtual.`
        : `Consumo cubierto íntegramente por Sol (${solarCoveredKw.toFixed(2)} kW) + Batería Fox-ESS (${batDischargeKw.toFixed(2)} kW). Cero coste de red.`;
    } else if (gridImportKw <= this.contractedPowerKw) {
      viabilityStatus = 'yellow';
      viabilityTitle = '🟡 TÉCNICAMENTE VIABLE CON APOYO DE RED (ICP SEGURO)';
      viabilityDesc = `Los ${activeList.length} aparatos funcionan sin problema. El ICP de 4.6 kW NO saltará (margen libre de red: +${icpHeadroomKw.toFixed(2)} kW). Sol y Batería cubren ${(solarCoveredKw + batDischargeKw).toFixed(2)} kW y la red aporta ${gridImportKw.toFixed(2)} kW.`;
    } else {
      viabilityStatus = 'red';
      viabilityTitle = '🔴 RIESGO DE DISPARO DEL ICP (SOBRECARGA DE RED)';
      viabilityDesc = `La demanda a la red (${gridImportKw.toFixed(2)} kW) supera tu potencia contratada de ${this.contractedPowerKw} kW en +${(gridImportKw - this.contractedPowerKw).toFixed(2)} kW. El automático podría saltar tras unos minutos. Se recomienda apagar o desfasar 1 electrodoméstico pesado.`;
    }

    // 5. Cómputo de Costes Económicos
    const costPerHourEur = gridImportKw * this.liveGridPriceEurKwh;
    const costPer2HoursEur = costPerHourEur * 2.0;
    const cleanPercent = totalLoadKw > 0 ? Math.round(((solarCoveredKw + batDischargeKw) / totalLoadKw) * 100) : 100;

    let autonomyHours = 99;
    if (batDischargeKw > 0) {
      autonomyHours = currentBatKwh / batDischargeKw;
    }

    return {
      totalLoadW,
      totalLoadKw: totalLoadKw.toFixed(2),
      activeCount: activeList.length,
      solarCoveredKw: solarCoveredKw.toFixed(2),
      batDischargeKw: batDischargeKw.toFixed(2),
      gridImportKw: gridImportKw.toFixed(2),
      surplusExportKw: surplusExportKw.toFixed(2),
      cleanPercent,
      is100PercentClean: gridImportKw === 0,
      costPerHourEur: costPerHourEur.toFixed(3),
      costPer2HoursEur: costPer2HoursEur.toFixed(2),
      viabilityStatus,
      viabilityTitle,
      viabilityDesc,
      icpHeadroomKw: icpHeadroomKw.toFixed(2),
      autonomyHours: autonomyHours < 48 ? autonomyHours.toFixed(1) : '> 48'
    };
  }

  async calibrateWithLiveMeter() {
    const btn = this.container.querySelector('#btn-whatif-sync-live');
    const msgEl = this.container.querySelector('#whatif-sync-msg');
    
    try {
      if (btn) {
        btn.disabled = true;
        btn.textContent = '🔄 Sincronizando con Smart Meter...';
      }
      
      const currentRes = this.getCalculatedResults();
      const payload = {
        active_states: this.activeStates,
        simulated_load_w: currentRes.totalLoadW,
        notes: `Calibración interactiva usuario (${currentRes.activeCount} aparatos activos)`
      };

      const res = await fetch('/api/whatif/calibrate-live', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      const data = await res.json();
      if (data.success) {
        this.lastCalibration = data;
        this.liveHomeLoadW = data.measured_home_load_w;
        this.renderCalibrationBanner();
        if (msgEl) {
          msgEl.innerHTML = `<span style="color: #10b981; font-weight: 700;">✅ ¡Calibración exitosa! Smart Meter: ${data.measured_home_load_w} W vs What-If: ${data.simulated_load_w} W (Precisión ${data.accuracy_pct}%). Datos asimilados en el Gemelo Digital.</span>`;
          setTimeout(() => { if (msgEl) msgEl.innerHTML = ''; }, 6000);
        }
        if (this.onStateChange) this.onStateChange(this.getCalculatedResults());
      } else {
        if (msgEl) msgEl.innerHTML = `<span style="color: #f43f5e;">❌ Error: ${data.error || 'No se pudo calibrar'}</span>`;
      }
    } catch (e) {
      if (msgEl) msgEl.innerHTML = `<span style="color: #f43f5e;">❌ Error de red: ${e.message}</span>`;
    } finally {
      if (btn) {
        btn.disabled = false;
        btn.innerHTML = `🎯 Sincronizar & Calibrar con Smart Meter`;
      }
    }
  }

  renderCalibrationBanner() {
    const banner = this.container.querySelector('#whatif-live-calibration-banner');
    if (!banner) return;

    const calc = this.getCalculatedResults();
    const simW = calc.totalLoadW;
    const realW = this.liveHomeLoadW || 831;
    const deltaW = Math.round(realW - simW);
    const accuracy = Math.max(0, Math.min(100, Math.round((1 - Math.abs(deltaW) / Math.max(1, realW)) * 1000) / 10));

    const timeStr = this.lastCalibration?.timestamp 
      ? new Date(this.lastCalibration.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) 
      : 'En vivo';

    banner.innerHTML = `
      <div style="display: flex; align-items: center; gap: 0.75rem; flex-wrap: wrap;">
        <div style="font-size: 1.3rem;">🧠</div>
        <div>
          <div style="font-size: 0.88rem; font-weight: 800; color: var(--text-primary); display: flex; align-items: center; gap: 0.4rem;">
            <span>Conciliación What-If con Telemetría Real en Vivo</span>
            <span class="badge-tag" style="background: rgba(16, 185, 129, 0.2); color: #10b981; font-weight: 700; font-size: 0.7rem;">
              Precisión: ${accuracy}%
            </span>
          </div>
          <div style="font-size: 0.76rem; color: var(--text-muted); margin-top: 0.15rem;">
            • <strong>Smart Meter Real Medido:</strong> <strong style="color: #38bdf8;">${realW} W</strong> | 
            • <strong>Simulado What-If:</strong> <strong style="color: #f59e0b;">${simW} W</strong> | 
            • <strong>Delta Residual:</strong> <strong style="color: ${Math.abs(deltaW) < 50 ? '#10b981' : '#f43f5e'};">${deltaW > 0 ? `+${deltaW}` : deltaW} W</strong>
          </div>
        </div>
      </div>

      <div style="display: flex; align-items: center; gap: 0.5rem;">
        <button id="btn-whatif-sync-live" class="whatif-preset-btn" style="background: linear-gradient(135deg, #0284c7, #0369a1); color: #ffffff; border: 1px solid #38bdf8; font-weight: 800; padding: 0.45rem 0.9rem; font-size: 0.8rem; display: flex; align-items: center; gap: 0.4rem; cursor: pointer; box-shadow: 0 2px 8px rgba(2, 132, 199, 0.35);">
          🎯 Sincronizar & Calibrar con Smart Meter
        </button>
      </div>
    `;

    const btn = banner.querySelector('#btn-whatif-sync-live');
    if (btn) {
      btn.addEventListener('click', () => this.calibrateWithLiveMeter());
    }
  }

  initUI() {
    if (!this.container) return;

    this.container.innerHTML = `
      <!-- Barra Superior: Modos de Escenario Físico & Presets -->
      <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 0.75rem; margin-bottom: 0.75rem; background: rgba(0,0,0,0.25); padding: 0.75rem 1rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
        
        <!-- Modos Físicos de Radiación -->
        <div style="display: flex; align-items: center; gap: 0.5rem; flex-wrap: wrap;">
          <span style="font-size: 0.75rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase;">Entorno:</span>
          <div class="tabs-bar" id="whatif-env-modes" style="margin: 0;">
            <button class="tab-btn active" data-env="live">🔴 Telemetría Real (Modbus)</button>
            <button class="tab-btn" data-env="peak_solar">☀️ Mediodía (Pico 4.6 kW)</button>
            <button class="tab-btn" data-env="morning_early">🌅 Mañana 08:00 (750 W)</button>
            <button class="tab-btn" data-env="night_zero">🌙 Noche (0 W Sol)</button>
          </div>
        </div>

        <!-- Presets de Carga -->
        <div style="display: flex; align-items: center; gap: 0.5rem; flex-wrap: wrap;">
          <span style="font-size: 0.75rem; color: var(--text-muted); font-weight: 700; text-transform: uppercase;">Presets:</span>
          <div style="display: flex; gap: 0.35rem; flex-wrap: wrap;" id="preset-buttons-bar">
            <button class="whatif-preset-btn" data-preset="breakfast_routine">🍞 Desayuno 08:00</button>
            <button class="whatif-preset-btn" data-preset="max_solar_lunch">🍲 Almuerzo 14:00</button>
            <button class="whatif-preset-btn active" data-preset="summer_afternoon">☀️ Tarde Verano</button>
            <button class="whatif-preset-btn" data-preset="dual_study_telework">💻 Teletrabajo + Estudio</button>
            <button class="whatif-preset-btn" data-preset="dinner_routine">🍽️ Cena 20:30</button>
            <button class="whatif-preset-btn" data-preset="night_quiet">🌙 Noche</button>
            <button class="whatif-preset-btn" data-preset="stress_test_all" style="background: rgba(244, 63, 94, 0.15); color: #f43f5e; border-color: rgba(244, 63, 94, 0.4); font-weight: 800;">🔥 TODO</button>
          </div>
        </div>

      </div>

      <!-- Banner de Conciliación y Calibración en Vivo -->
      <div id="whatif-live-calibration-banner" style="background: rgba(2, 132, 199, 0.1); border: 1px solid rgba(56, 189, 248, 0.35); border-radius: var(--radius-md); padding: 0.85rem 1rem; margin-bottom: 1rem; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 0.6rem;">
        <!-- Se llena con renderCalibrationBanner() -->
      </div>
      <div id="whatif-sync-msg" style="font-size: 0.8rem; margin-bottom: 0.75rem; min-height: 1.2rem;"></div>

      <!-- Tarjeta de Diagnóstico de Viabilidad Técnica & Coste de Red -->
      <div id="whatif-viability-card" style="margin-bottom: 1.25rem; border-radius: var(--radius-md); padding: 1.15rem; transition: all 0.25s ease; border: 1px solid rgba(16, 185, 129, 0.4); background: rgba(16, 185, 129, 0.08);">
        <!-- Se llena con renderResults() -->
      </div>

      <!-- Resumen Dinámico Superior de 4 KPIs -->
      <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 0.75rem; margin-bottom: 1.25rem;">
        <div style="background: var(--bg-elevated); padding: 0.8rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
          <div style="font-size: 0.72rem; color: #f43f5e; font-weight: 700; text-transform: uppercase;">Consumo Demandado Total</div>
          <div style="font-size: 1.25rem; font-weight: 800; color: #f43f5e;" id="whatif-total-load">0.83 kW</div>
          <div style="font-size: 0.72rem; color: var(--text-muted);" id="whatif-active-sub">5 aparatos activos</div>
        </div>

        <div style="background: var(--bg-elevated); padding: 0.8rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
          <div style="font-size: 0.72rem; color: var(--color-solar); font-weight: 700; text-transform: uppercase;">Generación Solar Activa</div>
          <div style="font-size: 1.25rem; font-weight: 800; color: var(--color-solar-light);" id="whatif-solar-val">2.45 kW</div>
          <div style="font-size: 0.72rem; color: var(--text-muted);" id="whatif-solar-sub">Medido en Vivo</div>
        </div>

        <div style="background: var(--bg-elevated); padding: 0.8rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
          <div style="font-size: 0.72rem; color: #c084fc; font-weight: 700; text-transform: uppercase;">Aporte Batería Fox-ESS</div>
          <div style="font-size: 1.25rem; font-weight: 800; color: #c084fc;" id="whatif-bat-val">0.00 kW</div>
          <div style="font-size: 0.72rem; color: var(--text-muted);" id="whatif-bat-sub">100% SoC (10.36 kWh)</div>
        </div>

        <div style="background: var(--bg-elevated); padding: 0.8rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
          <div style="font-size: 0.72rem; color: var(--color-real); font-weight: 700; text-transform: uppercase;">Coste Eléctrico de Red</div>
          <div style="font-size: 1.25rem; font-weight: 800; color: var(--color-real);" id="whatif-cost-val">0.000 € / h</div>
          <div style="font-size: 0.72rem; color: var(--text-muted);" id="whatif-cost-sub">100% Gratis</div>
        </div>
      </div>

      <!-- Barra Visual Proporcional de Origen de Energía -->
      <div style="background: rgba(0,0,0,0.3); padding: 0.75rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle); margin-bottom: 1.25rem;">
        <div style="display: flex; justify-content: space-between; font-size: 0.75rem; font-weight: 700; margin-bottom: 0.4rem;">
          <span style="color: var(--text-secondary);">ORIGEN DE LA ENERGÍA SIMULADA:</span>
          <span id="whatif-clean-pct-label" style="color: var(--color-real);">100% Limpia (Sol + Batería)</span>
        </div>
        <div style="display: flex; height: 12px; border-radius: 9999px; overflow: hidden; background: rgba(255,255,255,0.05);">
          <div id="bar-solar-part" style="background: var(--color-solar); width: 100%; transition: width 0.3s;" title="Sol Directo"></div>
          <div id="bar-bat-part" style="background: #c084fc; width: 0%; transition: width 0.3s;" title="Batería Fox-ESS"></div>
          <div id="bar-grid-part" style="background: #f43f5e; width: 0%; transition: width 0.3s;" title="Red Eléctrica"></div>
        </div>
        <div style="display: flex; justify-content: space-between; font-size: 0.7rem; color: var(--text-muted); margin-top: 0.35rem;">
          <span>☀️ Sol Directo: <strong id="lbl-bar-solar" style="color: var(--color-solar-light);">0.83 kW</strong></span>
          <span>🔋 Batería Fox-ESS: <strong id="lbl-bar-bat" style="color: #c084fc;">0.00 kW</strong></span>
          <span>🔌 Red Eléctrica: <strong id="lbl-bar-grid" style="color: #f43f5e;">0.00 kW</strong></span>
        </div>
      </div>

      <!-- Cuadrícula de Interruptores Interactivos de Electrodomésticos -->
      <div class="whatif-cards-grid" id="whatif-grid-container" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 0.75rem;">
        <!-- Se llena con renderCards() -->
      </div>
    `;

    this.setupEventListeners();
    this.renderCards();
    this.renderCalibrationBanner();
    this.renderResults();
  }

  setupEventListeners() {
    const envBtns = this.container.querySelectorAll('#whatif-env-modes .tab-btn');
    envBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        envBtns.forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        this.setSimulationMode(btn.dataset.env);
      });
    });

    const presetBtns = this.container.querySelectorAll('.whatif-preset-btn');
    presetBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        if (btn.id === 'btn-whatif-sync-live') return;
        presetBtns.forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        this.setPreset(btn.dataset.preset);
      });
    });
  }

  updateModeButtons() {
    const envBtns = this.container.querySelectorAll('#whatif-env-modes .tab-btn');
    envBtns.forEach(btn => {
      btn.classList.toggle('active', btn.dataset.env === this.currentMode);
    });
  }

  renderCards() {
    const grid = this.container.querySelector('#whatif-grid-container');
    if (!grid) return;

    grid.innerHTML = APPLIANCE_CATALOG.map(app => {
      const isActive = this.activeStates[app.id];
      const isSolarAcs = app.id === 'solar_thermal_acs';

      return `
        <div class="whatif-card ${isActive ? 'active' : ''} ${isSolarAcs ? 'solar-acs-card' : ''}" 
             data-id="${app.id}" 
             style="background: var(--bg-card); border: 1px solid ${isActive ? 'var(--color-real)' : 'var(--border-subtle)'}; border-radius: var(--radius-md); padding: 0.8rem; cursor: pointer; transition: all 0.2s ease; display: flex; flex-direction: column; justify-content: space-between; gap: 0.5rem; position: relative;">
          
          <div style="display: flex; justify-content: space-between; align-items: flex-start;">
            <span style="font-size: 1.4rem;">${app.icon}</span>
            <div style="font-size: 0.75rem; font-weight: 800; font-family: var(--font-mono); color: ${isActive ? 'var(--color-real)' : 'var(--text-muted)'}; background: ${isActive ? 'rgba(16, 185, 129, 0.15)' : 'rgba(255,255,255,0.05)'}; padding: 0.15rem 0.4rem; border-radius: var(--radius-sm);">
              ${app.realPowerW === 0 ? '0 W (Solar)' : `${app.realPowerW} W`}
            </div>
          </div>

          <div>
            <div style="font-size: 0.85rem; font-weight: 700; color: var(--text-primary);">${app.name}</div>
            <div style="font-size: 0.72rem; color: var(--text-muted); margin-top: 0.2rem; line-height: 1.3;">${app.description}</div>
          </div>

          <div style="display: flex; justify-content: space-between; align-items: center; border-top: 1px solid var(--border-subtle); padding-top: 0.4rem; margin-top: 0.2rem;">
            <span style="font-size: 0.7rem; color: var(--text-secondary); text-transform: uppercase; font-weight: 600;">${app.category}</span>
            <span style="font-size: 0.75rem; font-weight: 700; color: ${isActive ? 'var(--color-real)' : 'var(--text-muted)'};">
              ${isActive ? '🟢 ACTIVO' : '⚪ APAGADO'}
            </span>
          </div>

        </div>
      `;
    }).join('');

    const cards = grid.querySelectorAll('.whatif-card');
    cards.forEach(card => {
      card.addEventListener('click', () => {
        const id = card.dataset.id;
        this.toggleAppliance(id);
      });
    });
  }

  updateToggleCards() {
    const cards = this.container.querySelectorAll('.whatif-card');
    cards.forEach(card => {
      const id = card.dataset.id;
      const isActive = this.activeStates[id];
      card.classList.toggle('active', isActive);
      card.style.borderColor = isActive ? 'var(--color-real)' : 'var(--border-subtle)';
      const statusSpan = card.querySelector('div:last-child span:last-child');
      if (statusSpan) {
        statusSpan.textContent = isActive ? '🟢 ACTIVO' : '⚪ APAGADO';
        statusSpan.style.color = isActive ? 'var(--color-real)' : 'var(--text-muted)';
      }
    });
  }

  renderResults() {
    const res = this.getCalculatedResults();

    const viabilityCard = this.container.querySelector('#whatif-viability-card');
    if (viabilityCard) {
      let bg = 'rgba(16, 185, 129, 0.08)';
      let border = 'rgba(16, 185, 129, 0.4)';
      let titleColor = '#10b981';

      if (res.viabilityStatus === 'yellow') {
        bg = 'rgba(245, 158, 11, 0.08)';
        border = 'rgba(245, 158, 11, 0.4)';
        titleColor = '#f59e0b';
      } else if (res.viabilityStatus === 'red') {
        bg = 'rgba(244, 63, 94, 0.08)';
        border = 'rgba(244, 63, 94, 0.4)';
        titleColor = '#f43f5e';
      }

      viabilityCard.style.background = bg;
      viabilityCard.style.borderColor = border;
      viabilityCard.innerHTML = `
        <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 0.5rem;">
          <div style="font-weight: 800; font-size: 0.95rem; color: ${titleColor};">${res.viabilityTitle}</div>
          <div style="font-size: 0.75rem; color: var(--text-muted); font-family: var(--font-mono);">
            Margen ICP (4.6 kW): <strong style="color: ${parseFloat(res.icpHeadroomKw) > 1.0 ? '#10b981' : '#f43f5e'};">+${res.icpHeadroomKw} kW libre</strong>
          </div>
        </div>
        <div style="font-size: 0.83rem; color: var(--text-secondary); margin-top: 0.4rem; line-height: 1.45;">
          ${res.viabilityDesc}
        </div>
      `;
    }

    const setTxt = (id, val) => {
      const el = this.container.querySelector(`#${id}`);
      if (el) el.textContent = val;
    };

    setTxt('whatif-total-load', `${res.totalLoadKw} kW`);
    setTxt('whatif-active-sub', `${res.activeCount} aparatos activos`);
    setTxt('whatif-solar-val', `${(this.simSolarW / 1000).toFixed(2)} kW`);
    setTxt('whatif-bat-val', `${res.batDischargeKw} kW`);
    setTxt('whatif-bat-sub', `${this.simBatterySoc}% SoC (Autonomía: ${res.autonomyHours} h)`);
    setTxt('whatif-cost-val', `${res.costPerHourEur} € / h`);
    setTxt('whatif-cost-sub', res.is100PercentClean ? '100% Gratis (0.00 €)' : `2 horas: ${res.costPer2HoursEur} €`);

    const cleanPctLabel = this.container.querySelector('#whatif-clean-pct-label');
    if (cleanPctLabel) {
      cleanPctLabel.textContent = `${res.cleanPercent}% Limpia (Sol + Batería)`;
      cleanPctLabel.style.color = res.cleanPercent === 100 ? 'var(--color-real)' : '#f59e0b';
    }

    const totalW = parseFloat(res.totalLoadW);
    const solarW = parseFloat(res.solarCoveredKw) * 1000;
    const batW = parseFloat(res.batDischargeKw) * 1000;
    const gridW = parseFloat(res.gridImportKw) * 1000;

    const solarPct = totalW > 0 ? (solarW / totalW) * 100 : 100;
    const batPct = totalW > 0 ? (batW / totalW) * 100 : 0;
    const gridPct = totalW > 0 ? (gridW / totalW) * 100 : 0;

    const barSolar = this.container.querySelector('#bar-solar-part');
    const barBat = this.container.querySelector('#bar-bat-part');
    const barGrid = this.container.querySelector('#bar-grid-part');

    if (barSolar) barSolar.style.width = `${solarPct}%`;
    if (barBat) barBat.style.width = `${batPct}%`;
    if (barGrid) barGrid.style.width = `${gridPct}%`;

    setTxt('lbl-bar-solar', `${res.solarCoveredKw} kW (${Math.round(solarPct)}%)`);
    setTxt('lbl-bar-bat', `${res.batDischargeKw} kW (${Math.round(batPct)}%)`);
    setTxt('lbl-bar-grid', `${res.gridImportKw} kW (${Math.round(gridPct)}%)`);
  }
}
