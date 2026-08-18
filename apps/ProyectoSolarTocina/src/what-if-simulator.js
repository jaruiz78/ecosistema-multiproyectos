/**
 * Simulador "What-If" Interactivo de Consumos de Verano
 * Permite activar y desactivar cualquier combinación de electrodomésticos reales
 * del hogar en Tocina para comprobar en tiempo real la cobertura solar, autonomía
 * de la batería Fox-ESS y el coste 0.00 €.
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

    this.currentSolarW = 3600; // Se actualiza dinámicamente con Modbus
    this.batterySocPercent = 100;
    this.batteryCapacityKwh = 10.36;
    this.batteryMaxPowerKw = 5.18;

    this.initUI();
  }

  updateLiveSolar(solarW, batSoc = 100) {
    this.currentSolarW = solarW;
    this.batterySocPercent = batSoc;
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
        taurus_fan: true,
        home_lights_wifi: true,
        solar_thermal_acs: true,
        omoda7_ev_charge: false
      };
    } else if (presetName === 'max_solar_lunch') {
      this.activeStates = {
        daikin_salon: true,
        daikin_bedroom: false,
        midea_fridge: true,
        beko_washer: true,
        fagor_dishwasher: true,
        teka_oven: false,
        cecofry_airfryer: true,
        grunkel_toaster: false,
        digital_microwave: false,
        superser_dryer: false,
        telework_laptops: true,
        living_tv: false,
        taurus_fan: false,
        home_lights_wifi: true,
        solar_thermal_acs: true,
        omoda7_ev_charge: true
      };
    } else if (presetName === 'night_quiet') {
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
        taurus_fan: true,
        home_lights_wifi: true,
        solar_thermal_acs: true,
        omoda7_ev_charge: false
      };
    } else if (presetName === 'eco_minimum') {
      this.activeStates = {
        daikin_salon: false,
        daikin_bedroom: false,
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
    }
    this.updateToggleCards();
    this.renderResults();
    if (this.onStateChange) this.onStateChange(this.getCalculatedResults());
  }

  toggleAppliance(applianceId) {
    this.activeStates[applianceId] = !this.activeStates[applianceId];
    this.updateToggleCards();
    this.renderResults();
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

    const netW = this.currentSolarW - totalLoadW;
    const currentBatKwh = (this.batterySocPercent / 100) * this.batteryCapacityKwh;
    
    let solarCoveredW = 0;
    let batDischargeW = 0;
    let gridImportW = 0;
    let surplusExportW = 0;
    let batteryStatusStr = '';
    let autonomyStr = '';

    if (netW >= 0) {
      // 100% Cubierto por Sol Directo
      solarCoveredW = totalLoadW;
      surplusExportW = netW;
      batteryStatusStr = this.batterySocPercent >= 98 ? 'Batería 100% llena (Excedente a BV)' : 'Batería Cargando con Sol';
      autonomyStr = 'Autonomía Ilimitada (Generación > Consumo)';
    } else {
      // Déficit cubierto por batería Fox-ESS
      const deficitW = -netW;
      solarCoveredW = this.currentSolarW;
      
      const maxBatW = this.batteryMaxPowerKw * 1000;
      if (currentBatKwh > 0.5 && deficitW <= maxBatW) {
        batDischargeW = deficitW;
        gridImportW = 0;
        const hoursLeft = currentBatKwh / (deficitW / 1000);
        batteryStatusStr = `Descarga Fox-ESS: ${(deficitW / 1000).toFixed(2)} kW`;
        autonomyStr = `Autonomía batería: ~${hoursLeft.toFixed(1)} horas continuas`;
      } else if (currentBatKwh > 0.5) {
        batDischargeW = maxBatW;
        gridImportW = deficitW - maxBatW;
        batteryStatusStr = `Descarga Máxima: 5.18 kW`;
        autonomyStr = `Apoyo de red necesario por pico de potencia`;
      } else {
        gridImportW = deficitW;
        batteryStatusStr = `Batería descargada`;
        autonomyStr = `Consumo directo de red`;
      }
    }

    const is100PercentClean = gridImportW === 0;
    const hourlyCostEur = (gridImportW / 1000) * 0.12;

    return {
      totalLoadW,
      totalLoadKw: (totalLoadW / 1000).toFixed(2),
      activeCount: activeList.length,
      solarCoveredW,
      batDischargeW,
      gridImportW,
      surplusExportW,
      is100PercentClean,
      hourlyCostEur: hourlyCostEur.toFixed(2),
      batteryStatusStr,
      autonomyStr
    };
  }

  initUI() {
    if (!this.container) return;

    this.container.innerHTML = `
      <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 0.75rem; margin-bottom: 1rem;">
        <div style="display: flex; gap: 0.5rem; flex-wrap: wrap;" id="preset-buttons-bar">
          <button class="whatif-preset-btn active" data-preset="summer_afternoon">☀️ Tarde de Verano</button>
          <button class="whatif-preset-btn" data-preset="max_solar_lunch">⚡ Almuerzo & Sol Máximo</button>
          <button class="whatif-preset-btn" data-preset="night_quiet">🌙 Noche Silenciosa</button>
          <button class="whatif-preset-btn" data-preset="eco_minimum">🌱 Mínimo Base</button>
        </div>
        <div class="badge-tag" id="whatif-clean-badge" style="background: rgba(16, 185, 129, 0.2); color: #10b981; font-weight: 700;">
          🟢 100% Autoconsumo (Coste 0,00 €/h)
        </div>
      </div>

      <!-- Resumen Dinámico Superior -->
      <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 0.75rem; margin-bottom: 1.25rem;">
        <div style="background: var(--bg-elevated); padding: 0.8rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
          <div style="font-size: 0.72rem; color: #f43f5e; font-weight: 700; text-transform: uppercase;">Consumo Simulado Total</div>
          <div style="font-size: 1.25rem; font-weight: 800; color: #f43f5e;" id="whatif-total-load">0.90 kW</div>
          <div style="font-size: 0.72rem; color: var(--text-muted);" id="whatif-active-sub">5 aparatos activos</div>
        </div>

        <div style="background: var(--bg-elevated); padding: 0.8rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
          <div style="font-size: 0.72rem; color: var(--color-solar); font-weight: 700; text-transform: uppercase;">Generación Solar Activa</div>
          <div style="font-size: 1.25rem; font-weight: 800; color: var(--color-solar-light);" id="whatif-solar-val">3.60 kW</div>
          <div style="font-size: 0.72rem; color: var(--text-muted);">10x Jinko 500W</div>
        </div>

        <div style="background: var(--bg-elevated); padding: 0.8rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
          <div style="font-size: 0.72rem; color: #c084fc; font-weight: 700; text-transform: uppercase;">Estado Batería Fox-ESS</div>
          <div style="font-size: 1.25rem; font-weight: 800; color: #c084fc;" id="whatif-bat-val">100% SoC</div>
          <div style="font-size: 0.72rem; color: var(--text-muted);" id="whatif-bat-sub">10.36 kWh disponibles</div>
        </div>

        <div style="background: var(--bg-elevated); padding: 0.8rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
          <div style="font-size: 0.72rem; color: var(--color-real); font-weight: 700; text-transform: uppercase;">Excedente a Batería Virtual</div>
          <div style="font-size: 1.25rem; font-weight: 800; color: var(--color-real);" id="whatif-surplus-val">+2.70 kW</div>
          <div style="font-size: 0.72rem; color: var(--text-muted);" id="whatif-cost-val">Coste: 0.00 € / hora</div>
        </div>
      </div>

      <!-- Cuadrícula de Interruptores Interactivos de Electrodomésticos -->
      <div class="whatif-cards-grid" id="whatif-grid-container" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 0.75rem;">
        <!-- Se llena con renderCards() -->
      </div>
    `;

    this.setupEventListeners();
    this.renderCards();
    this.renderResults();
  }

  setupEventListeners() {
    // Botones de presets
    const presetBtns = this.container.querySelectorAll('.whatif-preset-btn');
    presetBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        presetBtns.forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        this.setPreset(btn.dataset.preset);
      });
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

    // Añadir listeners a las tarjetas
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

    const loadEl = this.container.querySelector('#whatif-total-load');
    const activeSubEl = this.container.querySelector('#whatif-active-sub');
    const solarValEl = this.container.querySelector('#whatif-solar-val');
    const batValEl = this.container.querySelector('#whatif-bat-val');
    const batSubEl = this.container.querySelector('#whatif-bat-sub');
    const surplusValEl = this.container.querySelector('#whatif-surplus-val');
    const costValEl = this.container.querySelector('#whatif-cost-val');
    const cleanBadgeEl = this.container.querySelector('#whatif-clean-badge');

    if (loadEl) loadEl.textContent = `${res.totalLoadKw} kW`;
    if (activeSubEl) activeSubEl.textContent = `${res.activeCount} aparatos encendidos`;
    if (solarValEl) solarValEl.textContent = `${(this.currentSolarW / 1000).toFixed(2)} kW`;
    if (batValEl) batValEl.textContent = `${this.batterySocPercent}% SoC`;
    if (batSubEl) batSubEl.textContent = res.batteryStatusStr;

    if (surplusValEl) {
      if (res.surplusExportW > 0) {
        surplusValEl.textContent = `+${(res.surplusExportW / 1000).toFixed(2)} kW`;
        surplusValEl.style.color = 'var(--color-real)';
      } else {
        surplusValEl.textContent = `0.00 kW`;
        surplusValEl.style.color = 'var(--text-muted)';
      }
    }

    if (costValEl) {
      costValEl.textContent = res.is100PercentClean ? `Coste: 0.00 € / h` : `Coste red: ${res.hourlyCostEur} € / h`;
    }

    if (cleanBadgeEl) {
      if (res.is100PercentClean) {
        cleanBadgeEl.style.background = 'rgba(16, 185, 129, 0.2)';
        cleanBadgeEl.style.color = '#10b981';
        cleanBadgeEl.innerHTML = `🟢 100% Autoconsumo Solar/Batería (Coste 0,00 €/h) • ${res.autonomyStr}`;
      } else {
        cleanBadgeEl.style.background = 'rgba(244, 63, 94, 0.2)';
        cleanBadgeEl.style.color = '#f43f5e';
        cleanBadgeEl.innerHTML = `🔴 Importando ${(res.gridImportW / 1000).toFixed(2)} kW de Red (${res.hourlyCostEur} €/h)`;
      }
    }
  }
}
