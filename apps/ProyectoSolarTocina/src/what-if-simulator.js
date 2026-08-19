/**
 * Simulador "What-If" Interactivo de Consumos de Verano Avanzado
 * Cruza datos teóricos con telemetría en vivo Modbus TCP (192.168.1.66),
 * evalúa la viabilidad técnica total (límite ICP 4.6 kW + Batería Fox-ESS + Sol)
 * y calcula el coste exacto de red por hora y ciclo.
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
    this.currentMode = 'live'; // 'live', 'peak_solar', 'night_zero', 'custom_mid'
    this.liveSolarW = 2450;
    this.liveBatSoc = 99;
    this.liveGridPriceEurKwh = 0.135; // Precio PVPC / OMIE actual

    this.simSolarW = 2450;
    this.simBatterySoc = 99;
    this.batteryCapacityKwh = 10.36;
    this.batteryMaxDischargeKw = 5.00; // Máxima descarga continua Fox-ESS EP5 HV
    this.contractedPowerKw = 4.60;     // Potencia contratada en factura (4.6 kW)

    this.initUI();
  }

  updateLiveTelemetry(solarW, batSoc, priceEurKwh = 0.135) {
    this.liveSolarW = solarW;
    this.liveBatSoc = batSoc;
    if (priceEurKwh) this.liveGridPriceEurKwh = priceEurKwh;

    if (this.currentMode === 'live') {
      this.simSolarW = this.liveSolarW;
      this.simBatterySoc = this.liveBatSoc;
      this.renderResults();
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
    } else if (presetName === 'stress_test_all') {
      // ENCENDER ABSOLUTAMENTE TODO SIMULTÁNEAMENTE
      APPLIANCE_CATALOG.forEach(app => {
        this.activeStates[app.id] = true;
      });
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
    // Capacidad técnica simultánea = Sol + Batería (5kW) + Red Contratada (4.6kW)
    const maxSystemCapacityKw = solarKw + this.batteryMaxDischargeKw + this.contractedPowerKw;
    const icpHeadroomKw = this.contractedPowerKw - gridImportKw;

    let viabilityStatus = 'green'; // 'green', 'yellow', 'red'
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

    // Autonomía estimada de batería si hay descarga
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

  initUI() {
    if (!this.container) return;

    this.container.innerHTML = `
      <!-- Barra Superior: Modos de Escenario Físico & Presets -->
      <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 0.75rem; margin-bottom: 1rem; background: rgba(0,0,0,0.25); padding: 0.75rem 1rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
        
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

      <!-- Tarjeta de Diagnóstico de Viabilidad Técnica & Coste de Red -->
      <div id="whatif-viability-card" style="margin-bottom: 1.25rem; border-radius: var(--radius-md); padding: 1.15rem; transition: all 0.25s ease; border: 1px solid rgba(16, 185, 129, 0.4); background: rgba(16, 185, 129, 0.08);">
        <!-- Se llena con renderResults() -->
      </div>

      <!-- Resumen Dinámico Superior de 4 KPIs -->
      <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 0.75rem; margin-bottom: 1.25rem;">
        <div style="background: var(--bg-elevated); padding: 0.8rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
          <div style="font-size: 0.72rem; color: #f43f5e; font-weight: 700; text-transform: uppercase;">Consumo Demandado Total</div>
          <div style="font-size: 1.25rem; font-weight: 800; color: #f43f5e;" id="whatif-total-load">0.90 kW</div>
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
          <div style="font-size: 0.72rem; color: var(--text-muted);" id="whatif-bat-sub">99% SoC (10.36 kWh)</div>
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
          <span>☀️ Sol Directo: <strong id="lbl-bar-solar" style="color: var(--color-solar-light);">0.90 kW</strong></span>
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
    this.renderResults();
  }

  setupEventListeners() {
    // Botones de entorno físico
    const envBtns = this.container.querySelectorAll('#whatif-env-modes .tab-btn');
    envBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        envBtns.forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        this.setSimulationMode(btn.dataset.env);
      });
    });

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

    const loadEl = this.container.querySelector('#whatif-total-load');
    const activeSubEl = this.container.querySelector('#whatif-active-sub');
    const solarValEl = this.container.querySelector('#whatif-solar-val');
    const solarSubEl = this.container.querySelector('#whatif-solar-sub');
    const batValEl = this.container.querySelector('#whatif-bat-val');
    const batSubEl = this.container.querySelector('#whatif-bat-sub');
    const costValEl = this.container.querySelector('#whatif-cost-val');
    const costSubEl = this.container.querySelector('#whatif-cost-sub');
    const viabilityCard = this.container.querySelector('#whatif-viability-card');

    if (loadEl) loadEl.textContent = `${res.totalLoadKw} kW`;
    if (activeSubEl) activeSubEl.textContent = `${res.activeCount} aparatos encendidos`;
    
    if (solarValEl) solarValEl.textContent = `${(this.simSolarW / 1000).toFixed(2)} kW`;
    if (solarSubEl) {
      solarSubEl.textContent = this.currentMode === 'live' ? 'En Vivo (Modbus TCP)' : 'Simulación Escenario';
    }

    if (batValEl) {
      batValEl.textContent = res.batDischargeKw > 0 ? `-${res.batDischargeKw} kW` : `0.00 kW`;
      batValEl.style.color = res.batDischargeKw > 0 ? '#c084fc' : 'var(--text-muted)';
    }
    if (batSubEl) {
      batSubEl.textContent = `${this.simBatterySoc}% SoC • Autonomía: ${res.autonomyHours} h`;
    }

    if (costValEl) {
      if (res.is100PercentClean) {
        costValEl.textContent = `0.000 € / h`;
        costValEl.style.color = 'var(--color-real)';
        if (costSubEl) costSubEl.textContent = `100% Solar / Batería (GRATIS)`;
      } else {
        costValEl.textContent = `${res.costPerHourEur} € / h`;
        costValEl.style.color = '#f43f5e';
        if (costSubEl) costSubEl.textContent = `Importando ${res.gridImportKw} kW de red (${res.costPer2HoursEur} € por 2 horas)`;
      }
    }

    // Actualizar Tarjeta de Diagnóstico de Viabilidad
    if (viabilityCard) {
      let borderColor = 'rgba(16, 185, 129, 0.4)';
      let bgColor = 'rgba(16, 185, 129, 0.08)';
      let titleColor = '#10b981';

      if (res.viabilityStatus === 'yellow') {
        borderColor = 'rgba(245, 158, 11, 0.4)';
        bgColor = 'rgba(245, 158, 11, 0.08)';
        titleColor = '#fbbf24';
      } else if (res.viabilityStatus === 'red') {
        borderColor = 'rgba(244, 63, 94, 0.5)';
        bgColor = 'rgba(244, 63, 94, 0.12)';
        titleColor = '#f43f5e';
      }

      viabilityCard.style.borderColor = borderColor;
      viabilityCard.style.background = bgColor;

      viabilityCard.innerHTML = `
        <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 0.5rem;">
          <div style="font-size: 0.95rem; font-weight: 800; color: ${titleColor}; letter-spacing: 0.02em;">
            ${res.viabilityTitle}
          </div>
          <div style="font-size: 0.78rem; font-weight: 700; color: var(--text-secondary);">
            Límite ICP Contratado: <strong style="color: var(--text-primary);">${this.contractedPowerKw} kW</strong>
          </div>
        </div>

        <div style="font-size: 0.82rem; color: var(--text-primary); margin-top: 0.45rem; line-height: 1.45;">
          ${res.viabilityDesc}
        </div>

        <!-- Desglose numérico rápido -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(130px, 1fr)); gap: 0.5rem; margin-top: 0.75rem; border-top: 1px solid rgba(255,255,255,0.08); padding-top: 0.6rem;">
          <div>
            <div style="font-size: 0.68rem; color: var(--text-muted); text-transform: uppercase;">Demanda Total</div>
            <div style="font-size: 0.95rem; font-weight: 800; color: #f43f5e;">${res.totalLoadKw} kW</div>
          </div>
          <div>
            <div style="font-size: 0.68rem; color: var(--text-muted); text-transform: uppercase;">Cubierto por Sol</div>
            <div style="font-size: 0.95rem; font-weight: 800; color: var(--color-solar-light);">${res.solarCoveredKw} kW</div>
          </div>
          <div>
            <div style="font-size: 0.68rem; color: var(--text-muted); text-transform: uppercase;">Cubierto por Batería</div>
            <div style="font-size: 0.95rem; font-weight: 800; color: #c084fc;">${res.batDischargeKw} kW</div>
          </div>
          <div>
            <div style="font-size: 0.68rem; color: var(--text-muted); text-transform: uppercase;">Consumo de Red</div>
            <div style="font-size: 0.95rem; font-weight: 800; color: ${res.gridImportKw > 0 ? '#f43f5e' : 'var(--color-real)'};">${res.gridImportKw} kW</div>
          </div>
          <div>
            <div style="font-size: 0.68rem; color: var(--text-muted); text-transform: uppercase;">Coste por Hora</div>
            <div style="font-size: 0.95rem; font-weight: 800; color: ${res.gridImportKw > 0 ? '#f43f5e' : 'var(--color-real)'};">${res.costPerHourEur} €/h</div>
          </div>
        </div>
      `;
    }

    // Actualizar Barra Proporcional
    const total = parseFloat(res.totalLoadKw) || 0.001;
    const solPct = Math.min(100, (parseFloat(res.solarCoveredKw) / total) * 100);
    const batPct = Math.min(100 - solPct, (parseFloat(res.batDischargeKw) / total) * 100);
    const gridPct = Math.max(0, 100 - solPct - batPct);

    const barSol = this.container.querySelector('#bar-solar-part');
    const barBat = this.container.querySelector('#bar-bat-part');
    const barGrid = this.container.querySelector('#bar-grid-part');
    const lblClean = this.container.querySelector('#whatif-clean-pct-label');
    const lblSol = this.container.querySelector('#lbl-bar-solar');
    const lblBat = this.container.querySelector('#lbl-bar-bat');
    const lblGrid = this.container.querySelector('#lbl-bar-grid');

    if (barSol) barSol.style.width = `${solPct}%`;
    if (barBat) barBat.style.width = `${batPct}%`;
    if (barGrid) barGrid.style.width = `${gridPct}%`;

    if (lblClean) {
      lblClean.textContent = `${res.cleanPercent}% Limpia (${res.is100PercentClean ? '100% Coste 0.00 €' : `Red: ${res.costPerHourEur} €/h`})`;
      lblClean.style.color = res.is100PercentClean ? 'var(--color-real)' : '#fbbf24';
    }

    if (lblSol) lblSol.textContent = `${res.solarCoveredKw} kW (${Math.round(solPct)}%)`;
    if (lblBat) lblBat.textContent = `${res.batDischargeKw} kW (${Math.round(batPct)}%)`;
    if (lblGrid) lblGrid.textContent = `${res.gridImportKw} kW (${Math.round(gridPct)}%)`;
  }
}
