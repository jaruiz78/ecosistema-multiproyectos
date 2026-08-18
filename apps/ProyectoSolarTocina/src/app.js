import { SolarEngine } from './solar-engine.js';
import { WeatherApiClient } from './weather-api.js';
import { SolarKalmanTwin } from './kalman-filter.js';
import { MicrogridMpcOptimizer } from './mpc-optimizer.js';
import { VirtualBatteryManager } from './virtual-battery.js';
import { ApplianceRecommender } from './appliance-recommender.js';
import { MarketPricingService } from './market-pricing.js';
import { H3SpatialMicroclimate } from './h3-spatial-grid.js';
import { GreenEnergyLedger } from './green-ledger.js';
import { MobilityPlanner, PRESET_ROUTES } from './mobility-planner.js';
import { PowerFlowCanvas } from './power-flow-canvas.js';
import { WhatIfSimulator } from './what-if-simulator.js';

class SolarApp {
  constructor() {
    this.engine = new SolarEngine();
    this.weatherApi = new WeatherApiClient();
    this.kalmanTwin = new SolarKalmanTwin();
    this.mpcOptimizer = new MicrogridMpcOptimizer();
    this.virtualBattery = new VirtualBatteryManager();
    this.applianceRecommender = new ApplianceRecommender(this.engine);
    this.marketPricing = new MarketPricingService();
    this.h3Microclimate = new H3SpatialMicroclimate();
    this.greenLedger = new GreenEnergyLedger();
    this.mobilityPlanner = new MobilityPlanner();
    
    this.forecastData = null;
    this.daysData = [];
    this.marketPrices = [];
    this.mpcResult = null;
    this.annualFinance = null;
    this.sqliteHistory = [];
    this.sseEventSource = null;
    
    this.selectedDayIndex = 0;
    this.activeChartTab = 'overview';

    this.chartToday = null;
    this.chartWeek = null;
    this.annualForecastChart = null;
    this.annualForecastData = null;

    // Componentes visuales interactivos
    this.powerFlow = new PowerFlowCanvas('powerFlowCanvas');
    this.sankey = window.SankeyFlowChart ? new window.SankeyFlowChart('sankeyCanvas') : null;
    this.whatIf = new WhatIfSimulator('whatif-simulator-container', (res) => {
      // Callback opcional de What-If
    });

    this.init();
    this.setupSseStream();
    this.startBackgroundSync();
  }

  async init() {
    this.bindEvents();
    this.updateConfigFromUI();
    
    // Carga paralela de meteo, precios de mercado, proyecciones anuales y pre-cooling
    await Promise.all([
      this.loadWeatherData(),
      this.loadMarketPrices(),
      this.loadAnnualForecast('12m'),
      this.loadThermalPrecooling()
    ]);
    
    this.runInitialTelemetryAssimilation();
    this.recalculate();
  }

  setupSseStream() {
    try {
      if (this.sseEventSource) {
        this.sseEventSource.close();
      }
      this.sseEventSource = new EventSource('/api/stream');
      
      this.sseEventSource.addEventListener('telemetry', (e) => {
        try {
          const data = JSON.parse(e.data);
          this.processTelemetry(data);
        } catch (err) {}
      });

      this.sseEventSource.onerror = () => {
        // En caso de corte, fallback suave a polling
        this.sseEventSource.close();
        setTimeout(() => this.setupSseStream(), 5000);
      };
    } catch (e) {
      console.warn('SSE stream error:', e);
    }
  }

  startBackgroundSync() {
    this.fetchModbusTelemetry();
    this.fetchSqliteHistory();
    this.fetchLearningInsights();
    setInterval(() => {
      if (!this.sseEventSource || this.sseEventSource.readyState !== EventSource.OPEN) {
        this.fetchModbusTelemetry();
      }
      this.fetchSqliteHistory();
      this.fetchLearningInsights();
    }, 4000);
  }

  async fetchLearningInsights() {
    try {
      const resp = await fetch('/api/learning/insights');
      if (!resp.ok) return;
      const data = await resp.json();
      
      const badge = document.getElementById('learning-confidence-badge');
      if (badge) badge.textContent = `Confianza IA: ${data.confidence_score}%`;

      const soilVal = document.getElementById('learning-soiling-val');
      const soilSub = document.getElementById('learning-soiling-sub');
      if (soilVal) soilVal.textContent = `${(data.soiling_factor * 100).toFixed(1)}% Limpio`;
      if (soilSub) soilSub.textContent = `Pérdida por suciedad: ${data.soiling_loss_percent}%`;

      const strVal = document.getElementById('learning-strings-val');
      if (strVal) strVal.textContent = `${data.east_optical_yield}% / ${data.west_optical_yield}%`;

      const hvacVal = document.getElementById('learning-hvac-val');
      if (hvacVal) hvacVal.textContent = `${data.hvac_sensitivity} W/°C`;

      const sampVal = document.getElementById('learning-samples-val');
      if (sampVal) sampVal.textContent = `${data.samples_assimilated} lecturas`;

      const recText = document.getElementById('learning-rec-text');
      if (recText) recText.textContent = data.recommendation;
    } catch (e) {}
  }

  async loadMarketPrices() {
    this.marketPrices = await this.marketPricing.fetchHourlyPrices();
  }

  async fetchSqliteHistory() {
    try {
      const resp = await fetch('/api/history');
      if (resp.ok) {
        this.sqliteHistory = await resp.json();
        if (this.activeChartTab === 'history_db') {
          this.renderHourlyChart();
        }
      }
    } catch (e) {}
  }

  async fetchModbusTelemetry() {
    try {
      const resp = await fetch('/api/telemetry');
      if (!resp.ok) return;
      const data = await resp.json();
      this.processTelemetry(data);
    } catch (e) {
      console.warn('Modbus polling error:', e);
    }
  }

  processTelemetry(data) {
    if (!data) return;

    if (!data.online) {
      const badge = document.getElementById('modbus-live-badge');
      if (badge) {
        badge.textContent = `🔴 Offline (Reintentando...)`;
        badge.style.background = 'rgba(239, 68, 68, 0.2)';
        badge.style.color = '#ef4444';
      }
      return;
    }

    // Update UI elements
    const badge = document.getElementById('modbus-live-badge');
    if (badge) {
      badge.textContent = `🟢 En Vivo SSE: ${data.ip}:502 (${data.model})`;
      badge.style.background = 'rgba(16, 185, 129, 0.2)';
      badge.style.color = '#10b981';
    }

    const pv1El = document.getElementById('live-pv1-val');
    const pv1Sub = document.getElementById('live-pv1-sub');
    if (pv1El && data.pv1_east) pv1El.textContent = `${data.pv1_east.power_kw.toFixed(2)} kW`;
    if (pv1Sub && data.pv1_east) pv1Sub.textContent = `${data.pv1_east.voltage_v.toFixed(1)} V • ${data.pv1_east.current_a.toFixed(1)} A`;

    const pv2El = document.getElementById('live-pv2-val');
    const pv2Sub = document.getElementById('live-pv2-sub');
    if (pv2El && data.pv2_west) pv2El.textContent = `${data.pv2_west.power_kw.toFixed(2)} kW`;
    if (pv2Sub && data.pv2_west) pv2Sub.textContent = `${data.pv2_west.voltage_v.toFixed(1)} V • ${data.pv2_west.current_a.toFixed(1)} A`;

    const solEl = document.getElementById('live-solar-val');
    if (solEl && data.solar_total_kw !== undefined) solEl.textContent = `${data.solar_total_kw.toFixed(2)} kW`;

    const homeLoadEl = document.getElementById('live-home-load-val');
    if (homeLoadEl && data.grid && data.grid.home_load_kw !== undefined) {
      homeLoadEl.textContent = `${data.grid.home_load_kw.toFixed(2)} kW`;
    }

    const exportEl = document.getElementById('live-export-val');
    if (exportEl && data.grid && data.grid.grid_export_kw !== undefined) {
      exportEl.textContent = `${data.grid.grid_export_kw.toFixed(2)} kW`;
    }

    const batEl = document.getElementById('live-bat-val');
    const batSub = document.getElementById('live-bat-sub');
    if (batEl && data.battery) batEl.textContent = `${data.battery.soc_percent}% SoC`;
    if (batSub && data.battery) batSub.textContent = `${data.battery.voltage_v.toFixed(1)} V (10.36 kWh)`;

    const invEl = document.getElementById('live-inv-val');
    if (invEl && data.inverter) invEl.textContent = `${data.inverter.temperature_c.toFixed(1)} °C`;

    // Actualizar Diagrama Unifilar Interactivo y Simulador What-If
    if (this.powerFlow) {
      this.powerFlow.updateTelemetry(data);
    }
    if (this.whatIf) {
      const solarW = data.solar_total_w !== undefined ? data.solar_total_w : (data.solar_total_kw ? data.solar_total_kw * 1000 : 0);
      const batSoc = data.battery ? data.battery.soc_percent : 100;
      this.whatIf.updateLiveSolar(solarW, batSoc);
    }

    // Auto-asimilación continua en el Filtro de Kalman EnKF
    if (this.daysData.length > 0 && data.grid && data.grid.ac_power_kw > 0.1) {
      const curHour = new Date().getHours();
      const theoPoint = this.daysData[0].hourly.find(p => p.hour === curHour) || this.daysData[0].hourly[13];
      const theoKw = theoPoint.clearSky.pTotalAC_kW;
      
      const kResult = this.kalmanTwin.assimilate(data.grid.ac_power_kw, theoKw);
      this.renderKalmanMetrics(kResult);
    }
  }

  runInitialTelemetryAssimilation() {
    this.kalmanTwin.assimilate(3.23, 3.42);
    this.kalmanTwin.assimilate(3.72, 3.98);
    this.kalmanTwin.assimilate(4.18, 4.10);
    this.kalmanTwin.assimilate(3.84, 3.82);
  }

  bindEvents() {
    // Navegación Maestra por Pestañas Compartimentadas
    const masterTabBtns = document.querySelectorAll('.master-tab-btn');
    masterTabBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        const targetId = btn.getAttribute('data-target');
        if (!targetId) return;

        masterTabBtns.forEach(b => b.classList.remove('active'));
        btn.classList.add('active');

        document.querySelectorAll('.master-tab-pane').forEach(pane => {
          pane.classList.remove('active');
        });
        const activePane = document.getElementById(targetId);
        if (activePane) {
          activePane.classList.add('active');
        }

        // Redimensionar gráficos y canvas según la pestaña activa
        window.dispatchEvent(new Event('resize'));
        if (targetId === 'tab-live' && this.powerFlow) {
          setTimeout(() => this.powerFlow.resize(), 50);
        }
        if (targetId === 'tab-forecast') {
          setTimeout(() => {
            if (this.chartToday) this.chartToday.resize();
            if (this.chartWeek) this.chartWeek.resize();
          }, 50);
        }
        if (targetId === 'tab-annual-ai' && this.annualForecastChart) {
          setTimeout(() => this.annualForecastChart.resize(), 50);
        }
      });
    });

    // Sliders de Configuración de la Instalación
    const inputs = [
      'panelWp', 'eastPanels', 'eastAzimuth', 'eastTilt',
      'westPanels', 'westAzimuth', 'westTilt', 'inverterKw',
      'batteryKwh', 'homeLoadW'
    ];

    inputs.forEach(id => {
      const el = document.getElementById(id);
      if (el) {
        el.addEventListener('input', () => {
          this.updateValueLabels();
          this.updateConfigFromUI();
          this.recalculate();
        });
      }
    });

    // Controles del Planificador Personalizado de VE (Omoda 7)
    const evPlanInputs = ['evStartHour', 'evEndHour', 'evCurrentSoc', 'evTargetSoc'];
    evPlanInputs.forEach(id => {
      const el = document.getElementById(id);
      if (el) {
        el.addEventListener('input', () => {
          this.updateEvPlannerLabels();
          this.renderEvPlanner();
        });
      }
    });

    const evPlanModeSelect = document.getElementById('evPlanMode');
    if (evPlanModeSelect) {
      evPlanModeSelect.addEventListener('change', () => this.renderEvPlanner());
    }

    // Pestañas de Gráficos Horarios
    document.querySelectorAll('.section-box[aria-label="Curva Horaria Detallada"] .tab-btn').forEach(btn => {
      btn.addEventListener('click', (e) => {
        document.querySelectorAll('.section-box[aria-label="Curva Horaria Detallada"] .tab-btn').forEach(b => b.classList.remove('active'));
        e.currentTarget.classList.add('active');
        this.activeChartTab = e.currentTarget.dataset.tab;
        this.renderHourlyChart();
      });
    });

    // Pestañas de Predicción Anual & Scorecard
    document.querySelectorAll('#annual-forecast-tabs .tab-btn').forEach(btn => {
      btn.addEventListener('click', (e) => {
        document.querySelectorAll('#annual-forecast-tabs .tab-btn').forEach(b => b.classList.remove('active'));
        e.currentTarget.classList.add('active');
        const view = e.currentTarget.dataset.view;
        this.switchAnnualForecastView(view);
      });
    });

    // Botón de Actualizar Datos
    const refreshBtn = document.getElementById('refresh-btn');
    if (refreshBtn) {
      refreshBtn.addEventListener('click', async () => {
        refreshBtn.classList.add('rotating');
        await this.loadWeatherData(true);
        await this.loadMarketPrices();
        this.recalculate();
        setTimeout(() => refreshBtn.classList.remove('rotating'), 800);
      });
    }

    // FoxCloud Configuration & Sync
    const foxSaveBtn = document.getElementById('foxcloud-save-btn');
    if (foxSaveBtn) {
      foxSaveBtn.addEventListener('click', async () => {
        const apiKey = document.getElementById('foxcloud-apikey-input')?.value || '';
        const sn = document.getElementById('foxcloud-sn-input')?.value || '';
        const msgEl = document.getElementById('foxcloud-msg');

        if (!apiKey && !sn) {
          if (msgEl) {
            msgEl.textContent = '❌ Por favor introduce al menos el SN del Inversor';
            msgEl.style.color = '#ef4444';
          }
          return;
        }

        try {
          const resp = await fetch('/api/foxcloud/config', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ api_key: apiKey, device_sn: sn, username: "Jose Antonio Ruiz" })
          });
          const res = await resp.json();
          if (msgEl) {
            msgEl.textContent = res.success ? '✅ Credenciales guardadas correctamente' : `❌ ${res.error}`;
            msgEl.style.color = res.success ? '#10b981' : '#ef4444';
          }
          this.checkFoxCloudStatus();
        } catch (e) {
          if (msgEl) { msgEl.textContent = `❌ Error: ${e.message}`; msgEl.style.color = '#ef4444'; }
        }
      });
    }

    const foxSyncBtn = document.getElementById('foxcloud-sync-btn');
    if (foxSyncBtn) {
      foxSyncBtn.addEventListener('click', async () => {
        const msgEl = document.getElementById('foxcloud-msg');
        foxSyncBtn.textContent = '⏳ Sincronizando...';
        try {
          const resp = await fetch('/api/foxcloud/sync', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ hours: 24 })
          });
          const res = await resp.json();
          if (msgEl) {
            msgEl.textContent = res.success ? `✅ ${res.message}` : `⚠️ ${res.message || res.error}`;
            msgEl.style.color = res.success ? '#10b981' : '#fbbf24';
          }
          this.updateSqliteStats();
        } catch (e) {
          if (msgEl) { msgEl.textContent = `❌ Error de red: ${e.message}`; msgEl.style.color = '#ef4444'; }
        } finally {
          foxSyncBtn.textContent = '🔄 Sincronizar 24h';
        }
      });
    }

    this.checkFoxCloudStatus();
    this.updateSqliteStats();
  }

  async checkFoxCloudStatus() {
    try {
      const resp = await fetch('/api/foxcloud/status');
      if (!resp.ok) return;
      const data = await resp.json();
      const badge = document.getElementById('foxcloud-status-badge');
      if (badge) {
        if (data.configured) {
          badge.textContent = `FoxCloud 2.0: Conectado (${data.username || data.device_sn || 'OK'})`;
          badge.style.background = 'rgba(56, 189, 248, 0.2)';
          badge.style.color = '#38bdf8';
        } else {
          badge.textContent = `FoxCloud: No configurado`;
          badge.style.background = 'rgba(148, 163, 184, 0.2)';
          badge.style.color = '#94a3b8';
        }
      }
    } catch (e) {}
  }

  async updateSqliteStats() {
    try {
      const resp = await fetch('/api/history/stats');
      if (!resp.ok) return;
      const stats = await resp.json();
      
      const badge = document.getElementById('sqlite-records-badge');
      if (badge) badge.textContent = `${stats.total_records || 0} lecturas en SQLite`;

      const firstRec = document.getElementById('sqlite-first-rec');
      if (firstRec && stats.first_record) {
        const d = new Date(stats.first_record);
        firstRec.textContent = d.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' });
      }

      const maxPow = document.getElementById('sqlite-max-pow');
      if (maxPow) {
        maxPow.textContent = stats.max_solar_w ? `${(stats.max_solar_w / 1000).toFixed(2)} kW` : '-- kW';
      }
    } catch (e) {}
  }

  updateValueLabels() {
    const map = {
      'panelWp': val => `${val} Wp`,
      'eastPanels': val => `${val} uds`,
      'eastAzimuth': val => `${val}° (${this.getCompassLabel(val)})`,
      'eastTilt': val => `${val}°`,
      'westPanels': val => `${val} uds`,
      'westAzimuth': val => `${val}° (${this.getCompassLabel(val)})`,
      'westTilt': val => `${val}°`,
      'inverterKw': val => `${val} kW`,
      'batteryKwh': val => `${val} kWh`,
      'homeLoadW': val => `${val} W`
    };

    for (const [id, formatter] of Object.entries(map)) {
      const input = document.getElementById(id);
      const label = document.getElementById(`${id}-val`);
      if (input && label) {
        label.textContent = formatter(input.value);
      }
    }
  }

  updateEvPlannerLabels() {
    const formatHour = h => `${h.toString().padStart(2, '0')}:00 h`;
    
    const startH = document.getElementById('evStartHour')?.value || 13;
    const endH = document.getElementById('evEndHour')?.value || 18;
    const curSoc = document.getElementById('evCurrentSoc')?.value || 25;
    const tgtSoc = document.getElementById('evTargetSoc')?.value || 85;

    const startLbl = document.getElementById('evStartHour-val');
    if (startLbl) startLbl.textContent = formatHour(startH);

    const endLbl = document.getElementById('evEndHour-val');
    if (endLbl) endLbl.textContent = formatHour(endH);

    const curLbl = document.getElementById('evCurrentSoc-val');
    if (curLbl) curLbl.textContent = `${curSoc}% (${((curSoc/100)*18.7).toFixed(1)} kWh)`;

    const tgtLbl = document.getElementById('evTargetSoc-val');
    if (tgtLbl) tgtLbl.textContent = `${tgtSoc}% (${((tgtSoc/100)*18.7).toFixed(1)} kWh)`;
  }

  getCompassLabel(deg) {
    deg = (Number(deg) % 360 + 360) % 360;
    if (deg >= 337.5 || deg < 22.5) return 'Norte';
    if (deg >= 22.5 && deg < 67.5) return 'Noreste';
    if (deg >= 67.5 && deg < 112.5) return 'Este';
    if (deg >= 112.5 && deg < 157.5) return 'Sureste';
    if (deg >= 157.5 && deg < 202.5) return 'Sur';
    if (deg >= 202.5 && deg < 247.5) return 'Suroeste';
    if (deg >= 247.5 && deg < 292.5) return 'Oeste';
    return 'Noroeste';
  }

  updateConfigFromUI() {
    const getVal = (id, def) => {
      const el = document.getElementById(id);
      return el ? parseFloat(el.value) : def;
    };

    const eastPanels = getVal('eastPanels', 6);
    const westPanels = getVal('westPanels', 4);
    const panelWp = getVal('panelWp', 500);

    this.engine.updateConfig({
      panelWp,
      stringEastPanels: eastPanels,
      stringEastAzimuth: getVal('eastAzimuth', 85),
      stringEastTilt: getVal('eastTilt', 20),
      stringWestPanels: westPanels,
      stringWestAzimuth: getVal('westAzimuth', 265),
      stringWestTilt: getVal('westTilt', 20),
      inverterMaxKw: getVal('inverterKw', 10),
      batteryCapacityKwh: getVal('batteryKwh', 10.36),
      baseHomeLoadW: getVal('homeLoadW', 750)
    });

    this.mpcOptimizer.config.batteryCapacityKwh = getVal('batteryKwh', 10.36);

    const totalWp = (eastPanels + westPanels) * panelWp;
    const eastWp = eastPanels * panelWp;
    const westWp = westPanels * panelWp;

    const totalKwBadge = document.getElementById('total-kwp-badge');
    if (totalKwBadge) totalKwBadge.textContent = `${(totalWp / 1000).toFixed(2)} kWp`;

    const eastWpBadge = document.getElementById('east-wp-badge');
    if (eastWpBadge) eastWpBadge.textContent = `${(eastWp / 1000).toFixed(2)} kWp (${eastPanels}x)`;

    const westWpBadge = document.getElementById('west-wp-badge');
    if (westWpBadge) westWpBadge.textContent = `${(westWp / 1000).toFixed(2)} kWp (${westPanels}x)`;
  }

  async loadWeatherData(forceRefresh = false) {
    this.forecastData = await this.weatherApi.fetchHourlyForecast(7, forceRefresh);
  }

  recalculate() {
    if (!this.forecastData) return;

    this.daysData = [];
    const hourly = this.forecastData.hourly;
    const daysMap = new Map();

    hourly.forEach(item => {
      const dayKey = item.date.toISOString().slice(0, 10);
      if (!daysMap.has(dayKey)) {
        daysMap.set(dayKey, []);
      }
      daysMap.get(dayKey).push(item);
    });

    let dayIdx = 0;
    for (const [dayKey, items] of daysMap.entries()) {
      if (items.length < 24) continue;

      let hourlyCalcs = items.map(weatherItem => 
        this.engine.calculateHourlyPoint(weatherItem.date, weatherItem)
      );

      if (dayIdx === 0) {
        hourlyCalcs = this.kalmanTwin.applyToForecast(hourlyCalcs);
      }

      const simulation = this.engine.simulateBatteryDispatch(hourlyCalcs, 5.18, 4.0);

      const totalKwhReal = hourlyCalcs.reduce((acc, p) => acc + p.forecast.pTotalAC_kW, 0);
      const totalKwhClear = hourlyCalcs.reduce((acc, p) => acc + p.clearSky.pTotalAC_kW, 0);
      const eastKwhReal = hourlyCalcs.reduce((acc, p) => acc + p.forecast.pEast_kW, 0);
      const westKwhReal = hourlyCalcs.reduce((acc, p) => acc + p.forecast.pWest_kW, 0);
      
      const peakKwReal = Math.max(...hourlyCalcs.map(p => p.forecast.pTotalAC_kW));
      const peakKwClear = Math.max(...hourlyCalcs.map(p => p.clearSky.pTotalAC_kW));
      const peakHour = hourlyCalcs.find(p => p.forecast.pTotalAC_kW === peakKwReal)?.hour || 14;

      const evTotalCharged = simulation.reduce((acc, p) => acc + (p.ev.chargePowerW / 1000), 0);

      const avgCloud = items.reduce((acc, p) => acc + p.cloudCover, 0) / items.length;
      const maxTemp = Math.max(...items.map(p => p.temp));
      const minTemp = Math.min(...items.map(p => p.temp));

      this.daysData.push({
        index: dayIdx++,
        dateStr: dayKey,
        date: items[0].date,
        hourly: simulation,
        kwhReal: totalKwhReal,
        kwhClear: totalKwhClear,
        eastKwh: eastKwhReal,
        westKwh: westKwhReal,
        peakKw: peakKwReal,
        peakKwClear: peakKwClear,
        peakHour,
        evChargedKwh: evTotalCharged,
        avgCloud: Math.round(avgCloud),
        maxTemp: Math.round(maxTemp),
        minTemp: Math.round(minTemp),
        weatherDesc: this.weatherApi.getWeatherDescription(items[12].weatherCode),
        batteryCapKwh: this.engine.config.batteryCapacityKwh
      });
    }

    const selectedDay = this.daysData[this.selectedDayIndex] || this.daysData[0];
    if (selectedDay) {
      this.mpcResult = this.mpcOptimizer.optimize24Hours(selectedDay.hourly, {
        homeBatSocKwh: 5.18,
        evSocKwh: 4.0
      });
    }

    this.annualFinance = this.virtualBattery.simulateAnnualBalance(280);

    this.render();
  }

  render() {
    this.renderKPIs();
    this.renderDailyEnergyBalance();
    this.renderDaySelector();
    this.renderHourlyChart();
    this.renderWeekChart();
    this.renderAppliances();
    this.renderMobilityRoutes();
    this.renderEvPlanner();
    this.renderGreenPassport();
    this.renderMicroclimateH3();
    this.renderKalmanMetrics();
    this.renderLiveSensors();
  }

  renderDailyEnergyBalance() {
    const selectedDay = this.daysData[this.selectedDayIndex] || this.daysData[0];
    if (!selectedDay) return;

    let totalSolarKwh = 0;
    let totalHomeKwh = 0;
    let totalBatChargedKwh = 0;
    let totalBatDischargedKwh = 0;
    let totalExportKwh = 0;
    let totalImportKwh = 0;

    const tbody = document.getElementById('hourly-table-body');
    if (tbody) tbody.innerHTML = '';

    selectedDay.hourly.forEach(p => {
      const solarKw = p.forecast.pTotalAC_kW;
      const homeKw = p.battery.homeLoadW / 1000.0;
      const batPowerKw = p.battery.powerW / 1000.0; // Positivo carga, negativo descarga
      const expKw = p.battery.gridExportW / 1000.0;
      const impKw = p.battery.gridImportW / 1000.0;

      totalSolarKwh += solarKw;
      totalHomeKwh += homeKw;
      if (batPowerKw > 0) totalBatChargedKwh += batPowerKw;
      else if (batPowerKw < 0) totalBatDischargedKwh += Math.abs(batPowerKw);
      totalExportKwh += expKw;
      totalImportKwh += impKw;

      if (tbody) {
        const row = document.createElement('tr');
        row.style.borderBottom = '1px solid rgba(255, 255, 255, 0.05)';

        let stateBadge = `<span style="color: var(--color-real); font-weight: 600;">☀️ 100% Solar</span>`;
        if (solarKw < 0.1 && batPowerKw < 0) {
          stateBadge = `<span style="color: #c084fc; font-weight: 600;">🔋 Batería EP5</span>`;
        } else if (expKw > 0.5) {
          stateBadge = `<span style="color: var(--color-east); font-weight: 600;">⚡ Exportando</span>`;
        }

        let batFlowHtml = '--';
        if (batPowerKw > 0.05) {
          batFlowHtml = `<span style="color: #10b981; font-weight: 600;">+${batPowerKw.toFixed(2)} kW (Carga)</span>`;
        } else if (batPowerKw < -0.05) {
          batFlowHtml = `<span style="color: #c084fc; font-weight: 600;">-${Math.abs(batPowerKw).toFixed(2)} kW (Descarga)</span>`;
        }

        row.innerHTML = `
          <td style="padding: 0.5rem 0.8rem; text-align: left; font-weight: 700; color: var(--text-primary);">${p.hour.toString().padStart(2, '0')}:00 h</td>
          <td style="padding: 0.5rem 0.8rem; color: var(--color-solar-light); font-weight: 600;">${solarKw.toFixed(2)}</td>
          <td style="padding: 0.5rem 0.8rem; color: #f43f5e; font-weight: 600;">${homeKw.toFixed(2)}</td>
          <td style="padding: 0.5rem 0.8rem;">${batFlowHtml}</td>
          <td style="padding: 0.5rem 0.8rem; color: #38bdf8; font-weight: 700;">${Math.round(p.battery.socPercent)}%</td>
          <td style="padding: 0.5rem 0.8rem; color: var(--color-real); font-weight: 600;">${expKw > 0.01 ? expKw.toFixed(2) : '0.00'}</td>
          <td style="padding: 0.5rem 0.8rem; text-align: center;">${stateBadge}</td>
        `;
        tbody.appendChild(row);
      }
    });

    const solEl = document.getElementById('bal-solar-total');
    if (solEl) solEl.textContent = `${totalSolarKwh.toFixed(2)} kWh`;

    const homeEl = document.getElementById('bal-home-total');
    if (homeEl) homeEl.textContent = `${totalHomeKwh.toFixed(2)} kWh`;

    const batChEl = document.getElementById('bal-bat-charged');
    const batDchEl = document.getElementById('bal-bat-discharged');
    if (batChEl) batChEl.textContent = `+${totalBatChargedKwh.toFixed(1)} kWh`;
    if (batDchEl) batDchEl.textContent = `Descarga: -${totalBatDischargedKwh.toFixed(1)} kWh`;

    const expEl = document.getElementById('bal-grid-export');
    const impEl = document.getElementById('bal-grid-import');
    if (expEl) expEl.textContent = `${totalExportKwh.toFixed(2)} kWh`;
    if (impEl) impEl.textContent = `Importación Red: ${totalImportKwh.toFixed(2)} kWh`;

    const autEl = document.getElementById('energy-autonomy-badge');
    if (autEl) {
      const autonomy = totalHomeKwh > 0 ? Math.min(100, Math.round(((totalHomeKwh - totalImportKwh) / totalHomeKwh) * 100)) : 100;
      autEl.textContent = `Autosuficiencia: ${autonomy}%`;
    }
  }

  renderKPIs() {
    const selectedDay = this.daysData[this.selectedDayIndex] || this.daysData[0];
    if (!selectedDay) return;

    document.getElementById('kpi-kwh-real').textContent = `${selectedDay.kwhReal.toFixed(2)} kWh`;
    document.getElementById('kpi-kwh-clear').textContent = `${selectedDay.kwhClear.toFixed(2)} kWh`;

    const efficiency = selectedDay.kwhClear > 0 
      ? Math.round((selectedDay.kwhReal / selectedDay.kwhClear) * 100) 
      : 100;
    document.getElementById('kpi-efficiency').textContent = `${efficiency}% del máximo teórico`;

    document.getElementById('kpi-peak-kw').textContent = `${selectedDay.peakKw.toFixed(2)} kW`;
    document.getElementById('kpi-peak-hour').textContent = `Pico a las ${selectedDay.peakHour.toString().padStart(2, '0')}:00 h`;

    const maxSoc = Math.max(...selectedDay.hourly.map(p => p.battery.socPercent));
    document.getElementById('kpi-battery-max').textContent = `${Math.round(maxSoc)}%`;

    const evKwh = selectedDay.evChargedKwh || 0;
    const evKmAdded = ((evKwh / 18.7) * 95).toFixed(0);
    document.getElementById('kpi-ev-charged').textContent = `Omoda 7: +${evKwh.toFixed(1)} kWh (~${evKmAdded} km)`;

    const solarElecSavingsEur = selectedDay.kwhReal * 0.12;
    const gasolineSavingsEur = (evKmAdded / 100) * 7.4 * 1.62;
    const totalDailySavings = solarElecSavingsEur + gasolineSavingsEur;

    document.getElementById('kpi-savings').textContent = `${totalDailySavings.toFixed(2)} €/día`;
    document.getElementById('kpi-savings-detail').textContent = `Luz: ${solarElecSavingsEur.toFixed(2)}€ | Gasolina: ${gasolineSavingsEur.toFixed(2)}€`;

    const dayLabel = this.selectedDayIndex === 0 
      ? 'Hoy (Día en curso)' 
      : new Intl.DateTimeFormat('es-ES', { weekday: 'long', day: 'numeric', month: 'long' }).format(selectedDay.date);
    
    document.getElementById('selected-day-title').textContent = dayLabel;
    document.getElementById('selected-day-weather').textContent = `${selectedDay.weatherDesc} • Temp: ${selectedDay.minTemp}°C - ${selectedDay.maxTemp}°C`;
  }

  renderAppliances() {
    const container = document.getElementById('appliances-container');
    if (!container) return;

    const selectedDay = this.daysData[this.selectedDayIndex] || this.daysData[0];
    if (!selectedDay) return;

    const recommendations = this.applianceRecommender.getRecommendationsForDay(selectedDay.hourly);
    container.innerHTML = '';

    recommendations.forEach(item => {
      const card = document.createElement('div');
      card.className = 'appliance-card';

      const isFree = item.bestWindow.is100PercentFree;
      const badgeHtml = isFree 
        ? `<span class="free-tag">100% SOLAR • 0.00 €</span>`
        : `<span class="badge-tag" style="background: rgba(245, 158, 11, 0.2); color: #fbbf24;">${item.bestWindow.solarCoveragePercent}% Solar</span>`;

      card.innerHTML = `
        <div class="appliance-header">
          <div class="appliance-icon">${item.icon}</div>
          <div>
            <div class="appliance-name">${item.name}</div>
            <div class="appliance-specs">${item.powerKw} kW pico • ${item.durationHours}h ciclo (${item.totalEnergyKwh} kWh)</div>
          </div>
        </div>

        <div class="best-window-box">
          <div class="window-label">Mejor Franja Horaria Recomendada:</div>
          <div class="window-time">${item.bestWindow.timeRangeStr}</div>
        </div>

        <div class="appliance-footer">
          <div>${badgeHtml}</div>
          <div>Ahorro: <strong>+${item.bestWindow.savingsEur.toFixed(2)} €</strong></div>
        </div>
      `;

      container.appendChild(card);
    });
  }

  renderMobilityRoutes() {
    const container = document.getElementById('routes-container');
    if (!container) return;

    const selectedDay = this.daysData[this.selectedDayIndex] || this.daysData[0];
    const ambTemp = selectedDay ? selectedDay.maxTemp : 32;

    container.innerHTML = '';

    PRESET_ROUTES.forEach(route => {
      const calc = this.mobilityPlanner.calculateRouteEnergyNeed(route, ambTemp);
      const card = document.createElement('div');
      card.className = 'route-card';

      card.innerHTML = `
        <div class="route-header">
          <div>
            <div class="route-name">${calc.routeName}</div>
            <div class="route-specs">${route.description}</div>
          </div>
          <div class="route-dist">${calc.distanceKm} km</div>
        </div>

        <div style="font-size: 0.78rem; color: var(--text-secondary); display: flex; flex-direction: column; gap: 0.25rem;">
          <div>• Batería Requerida: <strong>${calc.kwhNeeded.toFixed(1)} kWh</strong> (${calc.electricCoveragePercent}% EV Puro)</div>
          <div>• Coste en Gasolina: <del style="color: #ef4444;">${calc.gasolineCostEur.toFixed(2)} €</del></div>
          <div>• Coste con Excedente Solar: <strong style="color: var(--color-real);">0.00 €</strong></div>
        </div>

        <div class="route-savings-box">
          <span style="font-size: 0.75rem; color: var(--text-muted); font-weight: 600;">Ahorro por Viaje:</span>
          <span class="route-savings-val">+${calc.savingsPerTripEur.toFixed(2)} €</span>
        </div>
      `;

      container.appendChild(card);
    });
  }

  renderGreenPassport() {
    const selectedDay = this.daysData[this.selectedDayIndex] || this.daysData[0];
    if (!selectedDay) return;

    const evKm = ((selectedDay.evChargedKwh || 0) / 18.7) * 95;
    const stats = this.greenLedger.calculateEmissionsAvoided(selectedDay.kwhReal, evKm);
    const cert = this.greenLedger.generateGreenCertificate(selectedDay);

    const co2TodayEl = document.getElementById('co2-saved-today');
    if (co2TodayEl) co2TodayEl.textContent = `${stats.totalCo2SavedKg} kg`;

    const treesTodayEl = document.getElementById('co2-trees-today');
    if (treesTodayEl) treesTodayEl.textContent = `🌳 Equiv. a ${stats.equivalentTreesPlanted} árboles/día`;

    const hashEl = document.getElementById('green-cert-hash');
    if (hashEl) hashEl.textContent = cert.hashSignature;

    const certIdEl = document.getElementById('green-cert-id');
    if (certIdEl) certIdEl.textContent = cert.certificateId;
  }

  renderMicroclimateH3() {
    const selectedDay = this.daysData[this.selectedDayIndex] || this.daysData[0];
    const profile = this.h3Microclimate.getMicroclimateProfile(
      selectedDay ? selectedDay.date.getMonth() + 1 : 8,
      selectedDay ? selectedDay.maxTemp : 32
    );

    const descEl = document.getElementById('h3-microclimate-desc');
    if (descEl) {
      descEl.innerHTML = `
        <strong>${profile.regionName}</strong>: ${profile.description}
        <br/><small style="color: var(--text-muted);">Albedo Urbano: ${profile.urbanAlbedo} • Factor Aerosol: ${profile.aerosolOpticalDepth}</small>
      `;
    }
  }

  renderEvPlanner() {
    const selectedDay = this.daysData[this.selectedDayIndex] || this.daysData[0];
    if (!selectedDay) return;

    const startHour = document.getElementById('evStartHour')?.value || 13;
    const endHour = document.getElementById('evEndHour')?.value || 18;
    const currentSoc = document.getElementById('evCurrentSoc')?.value || 25;
    const targetSoc = document.getElementById('evTargetSoc')?.value || 85;
    const mode = document.getElementById('evPlanMode')?.value || 'solar_only';

    const plan = this.applianceRecommender.calculateCustomEvChargePlan({
      startHour,
      endHour,
      currentSocPercent: currentSoc,
      targetSocPercent: targetSoc,
      maxKw: 3.7,
      mode
    }, selectedDay.hourly);

    const kwhEl = document.getElementById('ev-plan-kwh');
    if (kwhEl) kwhEl.textContent = `${plan.deliveredKwh.toFixed(1)} kWh`;

    const kmEl = document.getElementById('ev-plan-km');
    if (kmEl) kmEl.textContent = `+${plan.equivalentKmAdded} km de autonomía eléctrica`;

    const solarPctEl = document.getElementById('ev-plan-solar-pct');
    if (solarPctEl) {
      solarPctEl.textContent = `${plan.solarPercent}% Solar`;
      solarPctEl.style.color = plan.solarPercent >= 90 ? '#10b981' : '#c084fc';
    }

    const costEl = document.getElementById('ev-plan-cost');
    if (costEl) costEl.textContent = `Coste en factura: ${plan.electricCostEur.toFixed(2)} €`;

    const savingsEl = document.getElementById('ev-plan-savings');
    if (savingsEl) savingsEl.textContent = `+${plan.gasolineCostSavedEur.toFixed(2)} €`;
  }

  renderKalmanMetrics(entry) {
    const kFactorEl = document.getElementById('kalman-factor-badge');
    const kCovEl = document.getElementById('kalman-cov-badge');
    if (kFactorEl) {
      const factor = this.kalmanTwin.state[0] * this.kalmanTwin.state[1];
      kFactorEl.textContent = `${(factor * 100).toFixed(1)}%`;
    }
    if (kCovEl) {
      kCovEl.textContent = `Cov: ${this.kalmanTwin.getCovarianceNorm().toFixed(3)}`;
    }
  }

  renderDaySelector() {
    const container = document.getElementById('day-selector-container');
    if (!container) return;

    container.innerHTML = '';

    this.daysData.forEach((day, idx) => {
      const card = document.createElement('div');
      card.className = `day-card ${idx === this.selectedDayIndex ? 'active' : ''}`;
      
      const weekday = idx === 0 
        ? 'Hoy' 
        : new Intl.DateTimeFormat('es-ES', { weekday: 'short' }).format(day.date).toUpperCase();
      
      const dayNum = day.date.getDate();
      const monthNum = day.date.getMonth() + 1;

      card.innerHTML = `
        <div class="day-header">${weekday}</div>
        <div class="day-date">${dayNum}/${monthNum}</div>
        <div class="day-kwh">${day.kwhReal.toFixed(1)} <small>kWh</small></div>
        <div class="day-kwh-clear">Máx: ${day.kwhClear.toFixed(1)} kWh</div>
        <div class="day-cloud-badge ${day.avgCloud < 20 ? 'sunny' : (day.avgCloud < 60 ? 'mixed' : 'cloudy')}">
          ${day.avgCloud}% nubes
        </div>
      `;

      card.addEventListener('click', () => {
        this.selectedDayIndex = idx;
        this.recalculate();
      });

      container.appendChild(card);
    });
  }

  renderHourlyChart() {
    const selectedDay = this.daysData[this.selectedDayIndex] || this.daysData[0];
    if (!selectedDay) return;

    const ctx = document.getElementById('hourlyChart');
    const sankeyCanvas = document.getElementById('sankeyCanvas');
    if (!ctx) return;

    if (this.activeChartTab === 'sankey_view') {
      ctx.style.display = 'none';
      if (sankeyCanvas) {
        sankeyCanvas.style.display = 'block';
        this.loadSankeyData();
      }
      return;
    } else {
      ctx.style.display = 'block';
      if (sankeyCanvas) sankeyCanvas.style.display = 'none';
    }

    if (this.chartToday) {
      this.chartToday.destroy();
    }

    const labels = selectedDay.hourly.map(p => `${p.hour.toString().padStart(2, '0')}:00`);
    const clearSkyData = selectedDay.hourly.map(p => p.clearSky.pTotalAC_kW);
    const realForecastData = selectedDay.hourly.map(p => p.forecast.pTotalAC_kW);
    const eastRealData = selectedDay.hourly.map(p => p.forecast.pEast_kW);
    const westRealData = selectedDay.hourly.map(p => p.forecast.pWest_kW);
    const homeLoadData = selectedDay.hourly.map(p => (p.battery.homeLoadW / 1000));

    let datasets = [];

    if (this.activeChartTab === 'overview') {
      datasets = [
        {
          label: 'Máximo Teórico Despejado (Clear-Sky)',
          data: clearSkyData,
          borderColor: '#f59e0b',
          backgroundColor: 'rgba(245, 158, 11, 0.10)',
          borderWidth: 2.5,
          borderDash: [6, 4],
          fill: true,
          tension: 0.35,
          yAxisID: 'y'
        },
        {
          label: 'Generación Prevista Real (Asimilada EnKF)',
          data: realForecastData,
          borderColor: '#10b981',
          backgroundColor: 'rgba(16, 185, 129, 0.35)',
          borderWidth: 3,
          fill: true,
          tension: 0.35,
          yAxisID: 'y'
        },
        {
          label: 'Consumo Hogar (Facturas Reales)',
          data: homeLoadData,
          borderColor: '#94a3b8',
          borderWidth: 1.8,
          borderDash: [3, 3],
          fill: false,
          tension: 0.2,
          yAxisID: 'y'
        }
      ];
    } else if (this.activeChartTab === 'strings') {
      datasets = [
        {
          label: 'String 1 Este (6x500W @ 85° - Pico Mañana)',
          data: eastRealData,
          borderColor: '#38bdf8',
          backgroundColor: 'rgba(56, 189, 248, 0.25)',
          borderWidth: 2.5,
          fill: true,
          tension: 0.35,
          yAxisID: 'y'
        },
        {
          label: 'String 2 Oeste (4x500W @ 265° - Pico Tarde)',
          data: westRealData,
          borderColor: '#fb923c',
          backgroundColor: 'rgba(251, 146, 60, 0.25)',
          borderWidth: 2.5,
          fill: true,
          tension: 0.35,
          yAxisID: 'y'
        },
        {
          label: 'Total Combinado Inversor AC',
          data: realForecastData,
          borderColor: '#10b981',
          borderWidth: 2,
          fill: false,
          tension: 0.35,
          yAxisID: 'y'
        }
      ];
    } else if (this.activeChartTab === 'prices') {
      const priceData = this.marketPrices.length === 24 
        ? this.marketPrices.map(p => (p.priceEurKwh * 100))
        : [8.5, 7.8, 7.2, 7.0, 7.1, 7.9, 9.8, 13.5, 14.8, 14.2, 9.5, 6.5, 4.5, 3.8, 3.5, 4.0, 5.5, 8.5, 14.0, 19.5, 22.5, 21.0, 15.5, 10.5];

      datasets = [
        {
          label: 'Precio Electricidad Pool OMIE/PVPC (c€/kWh)',
          data: priceData,
          borderColor: '#ec4899',
          backgroundColor: 'rgba(236, 72, 153, 0.2)',
          borderWidth: 2.5,
          fill: true,
          tension: 0.2,
          yAxisID: 'y'
        },
        {
          label: 'Generación Solar (kW)',
          data: realForecastData,
          borderColor: '#10b981',
          borderWidth: 2,
          borderDash: [3, 3],
          fill: false,
          yAxisID: 'y1'
        }
      ];

      this.chartToday = new Chart(ctx, {
        type: 'line',
        data: { labels, datasets },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { labels: { color: '#cbd5e1' } } },
          scales: {
            x: { ticks: { color: '#94a3b8' } },
            y: { title: { display: true, text: 'Precio Mercado (c€/kWh)', color: '#ec4899' }, min: 0, ticks: { color: '#ec4899' } },
            y1: { position: 'right', title: { display: true, text: 'Solar (kW)', color: '#10b981' }, min: 0, grid: { drawOnChartArea: false }, ticks: { color: '#10b981' } }
          }
        }
      });
      return;
    } else if (this.activeChartTab === 'history_db') {
      const history = [...this.sqliteHistory].reverse();
      const histLabels = history.map(h => {
        const d = new Date(h.timestamp);
        return `${d.getHours().toString().padStart(2,'0')}:${d.getMinutes().toString().padStart(2,'0')}:${d.getSeconds().toString().padStart(2,'0')}`;
      });
      const pv1Hist = history.map(h => h.pv1_power_w / 1000.0);
      const pv2Hist = history.map(h => h.pv2_power_w / 1000.0);
      const totalHist = history.map(h => h.solar_total_kw);
      const socHist = history.map(h => h.battery_soc_percent);

      datasets = [
        {
          label: 'String Este (kW)',
          data: pv1Hist,
          borderColor: '#38bdf8',
          borderWidth: 2,
          fill: false,
          yAxisID: 'y'
        },
        {
          label: 'String Oeste (kW)',
          data: pv2Hist,
          borderColor: '#fb923c',
          borderWidth: 2,
          fill: false,
          yAxisID: 'y'
        },
        {
          label: 'Total Real Inversor Modbus (kW)',
          data: totalHist,
          borderColor: '#10b981',
          backgroundColor: 'rgba(16, 185, 129, 0.2)',
          borderWidth: 2.5,
          fill: true,
          yAxisID: 'y'
        },
        {
          label: 'SoC Batería EP5 (%)',
          data: socHist,
          borderColor: '#c084fc',
          borderWidth: 2,
          fill: false,
          yAxisID: 'y1'
        }
      ];

      this.chartToday = new Chart(ctx, {
        type: 'line',
        data: { labels: histLabels.length ? histLabels : labels, datasets },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { labels: { color: '#cbd5e1' } } },
          scales: {
            x: { ticks: { color: '#94a3b8', maxTicksLimit: 12 } },
            y: { title: { display: true, text: 'Potencia Real (kW)', color: '#10b981' }, min: 0, ticks: { color: '#10b981' } },
            y1: { position: 'right', title: { display: true, text: 'SoC (%)', color: '#c084fc' }, min: 0, max: 100, grid: { drawOnChartArea: false }, ticks: { color: '#c084fc' } }
          }
        }
      });
      return;
    } else if (this.activeChartTab === 'mpc' && this.mpcResult) {
      const mpcSched = this.mpcResult.schedule;
      const mpcBatKw = mpcSched.map(p => p.homeBatPowerKw);
      const mpcEvKw = mpcSched.map(p => p.evChargePowerKw);
      const mpcGridExp = mpcSched.map(p => p.gridExportKw);
      const mpcBatSoc = mpcSched.map(p => p.homeBatSocPercent);

      datasets = [
        {
          label: 'Generación Solar (kW)',
          data: realForecastData,
          borderColor: '#10b981',
          borderWidth: 2,
          borderDash: [3, 3],
          fill: false,
          yAxisID: 'y'
        },
        {
          label: 'Despacho Batería Casa Fox-ESS (kW)',
          data: mpcBatKw,
          borderColor: '#8b5cf6',
          backgroundColor: 'rgba(139, 92, 246, 0.25)',
          borderWidth: 2.5,
          fill: true,
          yAxisID: 'y'
        },
        {
          label: 'Carga Wallbox Omoda 7 SHS (kW)',
          data: mpcEvKw,
          borderColor: '#c084fc',
          backgroundColor: 'rgba(192, 132, 252, 0.35)',
          borderWidth: 2.5,
          fill: true,
          yAxisID: 'y'
        },
        {
          label: 'Exportación a Batería Virtual (kW)',
          data: mpcGridExp,
          borderColor: '#ec4899',
          borderWidth: 2,
          fill: false,
          yAxisID: 'y'
        },
        {
          label: 'SOC Batería Casa (%)',
          data: mpcBatSoc,
          borderColor: '#38bdf8',
          borderWidth: 2,
          fill: false,
          yAxisID: 'y1'
        }
      ];
    } else if (this.activeChartTab === 'kalman') {
      const history = this.kalmanTwin.history;
      const kalmanLabels = history.map((h, i) => `Medición ${i+1}`);
      const measured = history.map(h => h.measuredKw);
      const theo = history.map(h => h.theoreticalKw);
      const calibrated = history.map(h => h.h_x);

      datasets = [
        {
          label: 'Lectura Real Sensor Inversor (kW)',
          data: measured,
          borderColor: '#10b981',
          backgroundColor: '#10b981',
          pointRadius: 6,
          showLine: false,
          yAxisID: 'y'
        },
        {
          label: 'Teórico Físico Base (kW)',
          data: theo,
          borderColor: '#f59e0b',
          borderWidth: 2,
          borderDash: [4, 4],
          fill: false,
          yAxisID: 'y'
        },
        {
          label: 'Estimación Filtrada Kalman EnKF (kW)',
          data: calibrated,
          borderColor: '#38bdf8',
          borderWidth: 2.5,
          fill: false,
          yAxisID: 'y'
        }
      ];

      this.chartToday = new Chart(ctx, {
        type: 'line',
        data: { labels: kalmanLabels, datasets },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { labels: { color: '#cbd5e1' } } },
          scales: {
            x: { ticks: { color: '#94a3b8' } },
            y: { title: { display: true, text: 'Potencia (kW)', color: '#94a3b8' }, min: 0, ticks: { color: '#94a3b8' } }
          }
        }
      });
      return;
    } else if (this.activeChartTab === 'climate_5yr') {
      this.render5YrClimateChart(ctx);
      return;
    } else if (this.activeChartTab === 'pinn_quantiles') {
      this.renderPinnQuantilesChart(ctx);
      return;
    }

    this.chartToday = new Chart(ctx, {
      type: 'line',
      data: { labels, datasets },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        interaction: { mode: 'index', intersect: false },
        plugins: {
          legend: {
            position: 'top',
            labels: { color: '#cbd5e1', font: { family: 'system-ui, sans-serif', size: 12, weight: '500' } }
          },
          tooltip: {
            backgroundColor: '#0f172a',
            titleColor: '#f8fafc',
            bodyColor: '#e2e8f0',
            borderColor: '#334155',
            borderWidth: 1,
            padding: 12,
            callbacks: {
              label: (context) => {
                const label = context.dataset.label || '';
                const val = context.parsed.y;
                if (context.dataset.yAxisID === 'y1') return ` ${label}: ${Math.round(val)}%`;
                return ` ${label}: ${val.toFixed(2)} kW`;
              }
            }
          }
        },
        scales: {
          x: { grid: { color: 'rgba(255, 255, 255, 0.06)' }, ticks: { color: '#94a3b8' } },
          y: {
            type: 'linear',
            display: true,
            position: 'left',
            title: { display: true, text: 'Potencia Eléctrica (kW)', color: '#94a3b8' },
            min: 0,
            max: Math.max(6.0, selectedDay.peakKwClear * 1.2),
            grid: { color: 'rgba(255, 255, 255, 0.08)' },
            ticks: { color: '#94a3b8' }
          },
          y1: {
            type: 'linear',
            display: this.activeChartTab === 'mpc',
            position: 'right',
            title: { display: true, text: 'SOC Batería (%)', color: '#38bdf8' },
            min: 0,
            max: 100,
            grid: { drawOnChartArea: false },
            ticks: { color: '#38bdf8' }
          }
        }
      }
    });
  }

  async render5YrClimateChart(ctx) {
    try {
      const resp = await fetch('/api/weather/historical-5yr-stats');
      if (!resp.ok) return;
      const data = await resp.json();
      const annual = data.annual || [];

      const labels = annual.map(a => `Año ${a.year}`);
      const solarKwh = annual.map(a => a.total_solar_kwh_5kwp);
      const avgTemps = annual.map(a => a.avg_temp_c);
      const maxTemps = annual.map(a => a.max_temp_c);
      const precipMm = annual.map(a => a.total_precip_mm);

      this.chartToday = new Chart(ctx, {
        type: 'bar',
        data: {
          labels,
          datasets: [
            {
              type: 'bar',
              label: '☀️ Generación Solar Anual (kWh / 5kWp)',
              data: solarKwh,
              backgroundColor: 'rgba(245, 158, 11, 0.4)',
              borderColor: '#f59e0b',
              borderWidth: 2,
              borderRadius: 6,
              yAxisID: 'y'
            },
            {
              type: 'line',
              label: '🌡️ Temp Máxima Registrada (°C)',
              data: maxTemps,
              borderColor: '#f43f5e',
              backgroundColor: '#f43f5e',
              borderWidth: 2.5,
              fill: false,
              tension: 0.2,
              yAxisID: 'y1'
            },
            {
              type: 'line',
              label: '🌡️ Temp Media Anual (°C)',
              data: avgTemps,
              borderColor: '#38bdf8',
              backgroundColor: '#38bdf8',
              borderWidth: 2,
              fill: false,
              tension: 0.2,
              yAxisID: 'y1'
            },
            {
              type: 'line',
              label: '🌧️ Lluvia Anual (mm)',
              data: precipMm,
              borderColor: '#10b981',
              backgroundColor: '#10b981',
              borderDash: [4, 4],
              borderWidth: 2,
              fill: false,
              yAxisID: 'y2'
            }
          ]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: { labels: { color: '#cbd5e1' } },
            tooltip: {
              callbacks: {
                footer: () => `Datos Reales ERA5 (49.344 horas) • Tocina (Sevilla)`
              }
            }
          },
          scales: {
            x: { ticks: { color: '#94a3b8' } },
            y: {
              title: { display: true, text: 'Energía Solar Anual (kWh)', color: '#f59e0b' },
              ticks: { color: '#f59e0b' },
              min: 0,
              max: 8000
            },
            y1: {
              position: 'right',
              title: { display: true, text: 'Temperatura (°C)', color: '#f43f5e' },
              ticks: { color: '#f43f5e' },
              min: 0,
              max: 50,
              grid: { drawOnChartArea: false }
            },
            y2: {
              position: 'right',
              display: false,
              min: 0,
              max: 1200
            }
          }
        }
      });
    } catch (e) {
      console.warn('Error rendering 5-year climate chart:', e);
    }
  }

  async renderPinnQuantilesChart(ctx) {
    try {
      const resp = await fetch(`/api/ai/pinn-forecast?day=${this.selectedDayIndex}`);
      if (!resp.ok) return;
      const hours = await resp.json();
      if (!Array.isArray(hours) || !hours.length) return;

      const labels = hours.map(h => h.time_label);
      const p10 = hours.map(h => h.p10_adverse_kw);
      const p50 = hours.map(h => h.p50_expected_kw);
      const p90 = hours.map(h => h.p90_optimal_kw);
      const clearSky = hours.map(h => h.p_total_clear_kw);
      const temps = hours.map(h => h.temp_c);

      this.chartToday = new Chart(ctx, {
        type: 'line',
        data: {
          labels,
          datasets: [
            {
              label: '☀️ Teórico Despejado (Física Pura)',
              data: clearSky,
              borderColor: '#f59e0b',
              borderWidth: 2,
              borderDash: [5, 4],
              fill: false,
              tension: 0.3,
              yAxisID: 'y'
            },
            {
              label: '🟢 p50: Generación Esperada Calibrada PINN (kW)',
              data: p50,
              borderColor: '#10b981',
              backgroundColor: 'rgba(16, 185, 129, 0.25)',
              borderWidth: 3,
              fill: true,
              tension: 0.35,
              yAxisID: 'y'
            },
            {
              label: '🛡️ p90: Escenario Óptimo Despejado (kW)',
              data: p90,
              borderColor: '#38bdf8',
              borderWidth: 1.5,
              borderDash: [3, 3],
              fill: false,
              tension: 0.3,
              yAxisID: 'y'
            },
            {
              label: '⚠️ p10: Escenario Adverso / Nublado (kW)',
              data: p10,
              borderColor: '#f43f5e',
              borderWidth: 1.5,
              borderDash: [3, 3],
              fill: false,
              tension: 0.3,
              yAxisID: 'y'
            },
            {
              label: '🌡️ Temp Ambiente (°C)',
              data: temps,
              borderColor: '#94a3b8',
              borderWidth: 1.2,
              fill: false,
              yAxisID: 'y1'
            }
          ]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          interaction: { mode: 'index', intersect: false },
          plugins: {
            legend: { position: 'top', labels: { color: '#cbd5e1' } },
            tooltip: {
              callbacks: {
                footer: (tooltipItems) => {
                  const idx = tooltipItems[0].dataIndex;
                  const h = hours[idx];
                  return `Elevación Solar: ${h.sun_elevation_deg}° • Nubes: ${h.cloud_cover_pct}%`;
                }
              }
            }
          },
          scales: {
            x: { grid: { color: 'rgba(255, 255, 255, 0.05)' }, ticks: { color: '#94a3b8' } },
            y: {
              title: { display: true, text: 'Potencia Solar (kW)', color: '#10b981' },
              min: 0,
              max: 4.5,
              ticks: { color: '#10b981' }
            },
            y1: {
              position: 'right',
              title: { display: true, text: 'Temperatura (°C)', color: '#94a3b8' },
              min: 15,
              max: 48,
              grid: { drawOnChartArea: false },
              ticks: { color: '#94a3b8' }
            }
          }
        }
      });
    } catch (e) {
      console.warn('Error rendering PINN quantiles chart:', e);
    }
  }

  async loadSankeyData() {
    try {
      const resp = await fetch('/api/ai/sankey-data');
      if (resp.ok) {
        const payload = await resp.json();
        if (this.sankey) {
          this.sankey.updateData(payload);
        }
      }
    } catch (e) {
      console.warn('Error loading Sankey data:', e);
    }
  }

  async loadThermalPrecooling() {
    try {
      const resp = await fetch('/api/ai/thermal-precooling');
      if (resp.ok) {
        const data = await resp.json();
        const recEl = document.getElementById('learning-rec-text');
        if (recEl) {
          if (data.recommend_precooling) {
            recEl.innerHTML = `
              <strong style="color: #10b981;">❄️ Oportunidad de Pre-Refrigeración Activa:</strong> ${data.reason}<br/>
              <span style="color: #38bdf8;">Setpoint óptimo: <strong>${data.optimal_setpoint_c}°C</strong></span> • Ahorro estimado noche: <strong style="color: #f59e0b;">+${data.estimated_night_savings_eur} €</strong>
            `;
          } else {
            recEl.textContent = `Operación normal del inversor y Daikin. ${data.reason}`;
          }
        }
      }
    } catch (e) {
      console.warn('Error loading thermal precooling:', e);
    }
  }

  renderWeekChart() {
    const ctx = document.getElementById('weekChart');
    if (!ctx || !this.daysData.length) return;

    if (this.chartWeek) {
      this.chartWeek.destroy();
    }

    const labels = this.daysData.map((d, i) => 
      i === 0 ? 'Hoy' : new Intl.DateTimeFormat('es-ES', { weekday: 'short', day: 'numeric' }).format(d.date)
    );

    const clearKwhData = this.daysData.map(d => parseFloat(d.kwhClear.toFixed(2)));
    const eastKwhData = this.daysData.map(d => parseFloat(d.eastKwh.toFixed(2)));
    const westKwhData = this.daysData.map(d => parseFloat(d.westKwh.toFixed(2)));

    this.chartWeek = new Chart(ctx, {
      type: 'bar',
      data: {
        labels,
        datasets: [
          {
            type: 'line',
            label: 'Máximo Teórico Clear-Sky (kWh)',
            data: clearKwhData,
            borderColor: '#f59e0b',
            borderWidth: 2.5,
            borderDash: [5, 5],
            pointRadius: 4,
            pointBackgroundColor: '#f59e0b',
            fill: false,
            tension: 0.2
          },
          {
            type: 'bar',
            label: 'String Este 85° (kWh)',
            data: eastKwhData,
            backgroundColor: '#38bdf8',
            stack: 'generacion',
            borderRadius: 4
          },
          {
            type: 'bar',
            label: 'String Oeste 265° (kWh)',
            data: westKwhData,
            backgroundColor: '#fb923c',
            stack: 'generacion',
            borderRadius: 4
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { position: 'top', labels: { color: '#cbd5e1' } },
          tooltip: {
            backgroundColor: '#0f172a',
            titleColor: '#f8fafc',
            callbacks: {
              afterBody: (context) => {
                const dayIndex = context[0].dataIndex;
                const d = this.daysData[dayIndex];
                return [
                  `Total Previsto: ${d.kwhReal.toFixed(2)} kWh`,
                  `Carga VE Omoda 7: ${d.evChargedKwh ? d.evChargedKwh.toFixed(1) : 0} kWh`,
                  `Nubosidad media: ${d.avgCloud}%`,
                  `Clima: ${d.weatherDesc}`
                ];
              }
            }
          }
        },
        scales: {
          x: { grid: { color: 'rgba(255, 255, 255, 0.05)' }, ticks: { color: '#94a3b8' } },
          y: {
            title: { display: true, text: 'Energía Diaria Generada (kWh)', color: '#94a3b8' },
            min: 0,
            grid: { color: 'rgba(255, 255, 255, 0.08)' },
            ticks: { color: '#94a3b8' }
          }
        }
      }
    });
  }

  async loadAnnualForecast(view = '12m') {
    try {
      if (view === '12m') {
        const resp = await fetch('/api/ai/annual-forecast');
        if (resp.ok) {
          this.annualForecastData = await resp.json();
          this.renderAnnualForecastChart(this.annualForecastData, '12m');
          this.updateAnnualScorecardBadges(this.annualForecastData);
        }
      } else if (view === '52w') {
        const resp = await fetch('/api/ai/weeks-forecast');
        if (resp.ok) {
          const weeksData = await resp.json();
          this.renderAnnualForecastChart(weeksData, '52w');
        }
      } else if (view === 'scorecard') {
        const resp = await fetch('/api/ai/accuracy-scorecard');
        if (resp.ok) {
          const scorecard = await resp.json();
          this.renderScorecardTable(scorecard);
        }
      }
    } catch (e) {
      console.warn('Error loading annual forecast:', e);
    }
  }

  switchAnnualForecastView(view) {
    const chartWrapper = document.getElementById('annual-forecast-chart-wrapper');
    const tableWrapper = document.getElementById('ai-scorecard-table-wrapper');

    if (view === 'scorecard') {
      if (chartWrapper) chartWrapper.style.display = 'none';
      if (tableWrapper) tableWrapper.style.display = 'block';
      this.loadAnnualForecast('scorecard');
    } else {
      if (chartWrapper) chartWrapper.style.display = 'block';
      if (tableWrapper) tableWrapper.style.display = 'none';
      this.loadAnnualForecast(view);
    }
  }

  renderAnnualForecastChart(data, view) {
    const ctx = document.getElementById('annualForecastChart');
    if (!ctx) return;

    if (this.annualForecastChart) {
      this.annualForecastChart.destroy();
    }

    if (view === '12m') {
      const monthly = data.monthly || [];
      const labels = monthly.map(m => m.label);
      const solarKwh = monthly.map(m => m.solar_kwh);
      const homeKwh = monthly.map(m => m.home_kwh);
      const surplusKwh = monthly.map(m => m.surplus_kwh);
      const bvCredits = monthly.map(m => m.bv_credit_eur);

      this.annualForecastChart = new Chart(ctx, {
        type: 'bar',
        data: {
          labels,
          datasets: [
            {
              type: 'bar',
              label: '☀️ Generación Solar Proyectada (kWh)',
              data: solarKwh,
              backgroundColor: 'rgba(245, 158, 11, 0.5)',
              borderColor: '#f59e0b',
              borderWidth: 2,
              borderRadius: 6,
              yAxisID: 'y'
            },
            {
              type: 'line',
              label: '🏠 Consumo Hogar Estimado (kWh)',
              data: homeKwh,
              borderColor: '#f43f5e',
              backgroundColor: '#f43f5e',
              borderWidth: 2.5,
              fill: false,
              tension: 0.2,
              yAxisID: 'y'
            },
            {
              type: 'line',
              label: '💶 Excedente Batería Virtual (kWh)',
              data: surplusKwh,
              borderColor: '#c084fc',
              backgroundColor: '#c084fc',
              borderWidth: 2,
              borderDash: [4, 4],
              fill: false,
              tension: 0.2,
              yAxisID: 'y'
            },
            {
              type: 'line',
              label: '💰 Saldo Monedero BV Acumulable (€)',
              data: bvCredits,
              borderColor: '#10b981',
              backgroundColor: '#10b981',
              borderWidth: 2,
              fill: false,
              yAxisID: 'y1'
            }
          ]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: { position: 'top', labels: { color: '#cbd5e1' } },
            tooltip: {
              callbacks: {
                footer: (tooltipItems) => {
                  const idx = tooltipItems[0].dataIndex;
                  const m = monthly[idx];
                  return `Factura Prevista: ${m.bill_eur} € • Temp media: ${m.avg_temp_c}°C`;
                }
              }
            }
          },
          scales: {
            x: { ticks: { color: '#94a3b8' } },
            y: {
              title: { display: true, text: 'Energía (kWh / mes)', color: '#94a3b8' },
              ticks: { color: '#94a3b8' },
              min: 0
            },
            y1: {
              position: 'right',
              title: { display: true, text: 'Saldo Batería Virtual (€)', color: '#10b981' },
              ticks: { color: '#10b981' },
              min: 0,
              grid: { drawOnChartArea: false }
            }
          }
        }
      });
    } else if (view === '52w') {
      const weeks = Array.isArray(data) ? data : [];
      const labels = weeks.map(w => `Sem ${w.week_num} (${w.month_name})`);
      const solarKwh = weeks.map(w => w.solar_kwh);
      const homeKwh = weeks.map(w => w.home_kwh);
      const surplusKwh = weeks.map(w => w.surplus_kwh);

      this.annualForecastChart = new Chart(ctx, {
        type: 'bar',
        data: {
          labels,
          datasets: [
            {
              type: 'bar',
              label: '☀️ Generación Semanal (kWh)',
              data: solarKwh,
              backgroundColor: 'rgba(245, 158, 11, 0.4)',
              borderColor: '#f59e0b',
              borderWidth: 1.5,
              borderRadius: 4
            },
            {
              type: 'line',
              label: '🏠 Consumo Semanal (kWh)',
              data: homeKwh,
              borderColor: '#f43f5e',
              borderWidth: 2,
              fill: false,
              pointRadius: 2
            },
            {
              type: 'line',
              label: '⚡ Excedente Semanal (kWh)',
              data: surplusKwh,
              borderColor: '#c084fc',
              borderWidth: 1.8,
              borderDash: [3, 3],
              fill: false,
              pointRadius: 2
            }
          ]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { position: 'top', labels: { color: '#cbd5e1' } } },
          scales: {
            x: { ticks: { color: '#94a3b8', maxTicksLimit: 14 } },
            y: { title: { display: true, text: 'Energía (kWh / semana)', color: '#94a3b8' }, min: 0 }
          }
        }
      });
    }
  }

  updateAnnualScorecardBadges(forecastData) {
    if (!forecastData || !forecastData.annual_totals) return;
    const totals = forecastData.annual_totals;

    const solEl = document.getElementById('ai-annual-solar-val');
    const surEl = document.getElementById('ai-annual-surplus-val');
    const bvSubEl = document.getElementById('ai-annual-bv-sub');
    const savEl = document.getElementById('ai-annual-savings-val');

    if (solEl) solEl.textContent = `${totals.solar_kwh.toLocaleString('es-ES')} kWh`;
    if (surEl) surEl.textContent = `+${totals.surplus_kwh.toLocaleString('es-ES')} kWh`;
    if (bvSubEl) bvSubEl.textContent = `~${totals.bv_credit_eur} € en monedero virtual`;
    if (savEl) savEl.textContent = `~${totals.annual_savings_vs_grid_eur} €`;
  }

  renderScorecardTable(scorecard) {
    const tbody = document.getElementById('scorecard-table-body');
    if (!tbody) return;

    const latest = scorecard.latest || {};
    const history = scorecard.history || [];

    const accuracyEl = document.getElementById('ai-accuracy-val');
    const accuracySubEl = document.getElementById('ai-accuracy-sub');
    if (accuracyEl && latest.ai_accuracy_score_pct !== undefined) {
      accuracyEl.textContent = `${latest.ai_accuracy_score_pct}%`;
    }
    if (accuracySubEl && latest.solar_error_pct !== undefined) {
      accuracySubEl.textContent = `Error solar: ${latest.solar_error_pct > 0 ? '+' : ''}${latest.solar_error_pct}%`;
    }

    const rows = history.length ? history : [latest];
    tbody.innerHTML = rows.map(r => {
      const err = r.solar_error_pct || 0;
      const isGood = Math.abs(err) < 8.0;
      const errColor = isGood ? 'var(--color-real)' : '#f43f5e';

      return `
        <tr style="border-bottom: 1px solid var(--border-subtle);">
          <td style="padding: 0.5rem 0.75rem; text-align: left; font-family: var(--font-mono);">${r.date || 'Hoy'}</td>
          <td style="padding: 0.5rem 0.75rem; color: var(--color-solar);">${(r.predicted_solar_kwh || 0).toFixed(2)}</td>
          <td style="padding: 0.5rem 0.75rem; font-weight: 700; color: var(--text-primary);">${(r.actual_solar_kwh || 0).toFixed(2)}</td>
          <td style="padding: 0.5rem 0.75rem; color: ${errColor}; font-weight: 700;">${err > 0 ? '+' : ''}${err.toFixed(1)}%</td>
          <td style="padding: 0.5rem 0.75rem; color: var(--color-east);">${(r.ai_correction_factor || 0.97).toFixed(3)}</td>
          <td style="padding: 0.5rem 0.75rem; text-align: center;">
            <span class="badge-tag" style="background: rgba(16, 185, 129, 0.15); color: #10b981; font-size: 0.7rem;">Auto-Afinado</span>
          </td>
        </tr>
      `;
    }).join('');
  }

  renderLiveSensors() {
    const now = new Date();
    const timeEl = document.getElementById('current-time-label');
    if (timeEl) {
      timeEl.textContent = `${now.toLocaleDateString('es-ES', { weekday: 'long', day: 'numeric', month: 'long', year: 'numeric' })} - ${now.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' })}`;
    }
  }
}

document.addEventListener('DOMContentLoaded', () => {
  window.solarApp = new SolarApp();
});
