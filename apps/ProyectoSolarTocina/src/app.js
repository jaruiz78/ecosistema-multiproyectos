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
import { SolarDialogAssistant } from './solar-dialog-assistant.js';
import { ThermalPrecoolingEngine } from './thermal-precooling-engine.js';
import { TariffContractComparator } from './tariff-contract-comparator.js';
import { MobilitySyncAppViajes } from './mobility-sync-appviajes.js';
import { GreenPassportCrypto } from './green-passport-crypto.js';
import { SolarPushNotifications } from './solar-push-notifications.js';
import { HistoricalAnalyticsManager } from './historical-analytics.js';
import { KioskModeManager } from './kiosk-mode.js';
import { TelegramBotManager } from './telegram-manager.js';
import { BackupUiManager } from './backup-ui.js';
import { SoilingUiManager } from './soiling-ui.js';

/**
 * Obtiene siempre la hora, minuto, fecha y hora fraccionaria exacta en horario de España (Europe/Madrid)
 */
export function getMadridTime(d = new Date()) {
  const parts = new Intl.DateTimeFormat('en-GB', {
    timeZone: 'Europe/Madrid',
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  }).formatToParts(d);
  
  const getVal = (type) => parseInt(parts.find(p => p.type === type)?.value || '0', 10);
  const hour = getVal('hour');
  const minute = getVal('minute');
  const second = getVal('second');
  const year = getVal('year');
  const month = getVal('month');
  const day = getVal('day');
  const dateStr = `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
  
  return {
    year,
    month,
    day,
    hour,
    minute,
    second,
    dateStr,
    fractionalHour: hour + (minute / 60.0) + (second / 3600.0)
  };
}

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
    this.dialogAssistant = null;

    // Nuevos Motores Consilium Romano 3.0
    this.thermalPrecooling = new ThermalPrecoolingEngine('thermal-precooling-container');
    this.tariffComparator = new TariffContractComparator('tariff-comparator-container');
    this.mobilitySync = new MobilitySyncAppViajes('mobility-sync-container');
    this.greenPassport = new GreenPassportCrypto('green-passport-crypto-container');
    this.notifications = new SolarPushNotifications();
    this.historicalAnalytics = new HistoricalAnalyticsManager('historical-analytics-container');
    this.kioskMode = new KioskModeManager();
    this.telegramBot = new TelegramBotManager('telegram-bot-container');
    this.backupUi = new BackupUiManager('backup-manager-container');
    this.soilingUi = new SoilingUiManager('soiling-detector-container');
    
    this.forecastData = null;
    this.daysData = [];
    this.marketPrices = [];
    this.mpcResult = null;
    this.annualFinance = null;
    this.sqliteHistory = [];
    this.todayHourlyReal = null;
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
    
    // Carga paralela de meteo, precios de mercado, proyecciones anuales, historial y pre-cooling
    await Promise.all([
      this.loadWeatherData(),
      this.loadMarketPrices(),
      this.fetchTodayHourlyTelemetry(),
      this.fetchModbusTelemetry(),
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
    this.fetchTodayHourlyTelemetry();
    this.fetchLearningInsights();
    setInterval(() => {
      if (!this.sseEventSource || this.sseEventSource.readyState !== EventSource.OPEN) {
        this.fetchModbusTelemetry();
      }
      this.fetchSqliteHistory();
      this.fetchTodayHourlyTelemetry();
      this.fetchLearningInsights();
    }, 4000);
  }

  async fetchTodayHourlyTelemetry() {
    try {
      const resp = await fetch('/api/history/today-hourly');
      if (resp.ok) {
        this.todayHourlyReal = await resp.json();
        if (this.selectedDayIndex === 0) {
          if (this.activeChartTab === 'overview' || this.activeChartTab === 'strings') {
            this.renderHourlyChart();
          }
          this.renderDailyEnergyBalance();
        }
      }
    } catch (e) {}
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
    this.latestTelemetry = data;

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
    const exportSub = exportEl ? exportEl.parentElement.querySelector('div:last-child') : null;
    if (exportEl && data.grid) {
      if (data.grid.grid_export_w > 10) {
        exportEl.textContent = `+${data.grid.grid_export_kw.toFixed(2)} kW`;
        exportEl.style.color = '#10b981';
        if (exportSub) exportSub.textContent = 'Hacia Batería Virtual';
      } else if (data.grid.grid_import_w > 10) {
        exportEl.textContent = `-${data.grid.grid_import_kw.toFixed(2)} kW`;
        exportEl.style.color = '#fb7185';
        if (exportSub) exportSub.textContent = 'De la Red Eléctrica';
      } else {
        exportEl.textContent = '0.00 kW';
        exportEl.style.color = '#10b981';
        if (exportSub) exportSub.textContent = 'Balance Cero';
      }
    }

    const batEl = document.getElementById('live-bat-val');
    const batSub = document.getElementById('live-bat-sub');
    if (batEl && data.battery) {
      batEl.textContent = `${data.battery.soc_percent}% SoC`;
    }
    if (batSub && data.battery) {
      const pW = data.battery.power_w || 0;
      const absW = Math.round(Math.abs(pW));
      if (absW > 20) {
        const isCharging = (data.solar_total_w || 0) > ((data.grid && data.grid.home_load_w) || 0);
        if (isCharging) {
          batSub.textContent = `${data.battery.voltage_v.toFixed(1)} V • Cargando +${absW} W`;
        } else {
          batSub.textContent = `${data.battery.voltage_v.toFixed(1)} V • Descargando -${absW} W`;
        }
      } else {
        batSub.textContent = `${data.battery.voltage_v.toFixed(1)} V (10.36 kWh)`;
      }
    }

    const invEl = document.getElementById('live-inv-val');
    if (invEl && data.inverter) invEl.textContent = `${data.inverter.temperature_c.toFixed(1)} °C`;

    // Actualizar Diagrama Unifilar Interactivo y Simulador What-If
    if (this.powerFlow) {
      this.powerFlow.updateTelemetry(data);
    }
    if (this.whatIf) {
      const solarW = data.solar_total_w !== undefined ? data.solar_total_w : (data.solar_total_kw ? data.solar_total_kw * 1000 : 0);
      const batSoc = data.battery ? data.battery.soc_percent : 100;
      const curHour = getMadridTime().hour;
      const currentPriceEurKwh = (this.marketPrices && this.marketPrices[curHour]) ? this.marketPrices[curHour].priceEurKwh : 0.135;
      this.whatIf.updateLiveTelemetry(solarW, batSoc, currentPriceEurKwh);
    }

    if (this.kioskMode) {
      this.kioskMode.updateTelemetry(data);
    }
    if (this.notifications) {
      this.notifications.evaluateTelemetry(data);
    }

    // Actualizar Semáforo Inteligente de Carga Omoda 7 SHS (100% Solar)
    this.updateOmodaTrafficLight(data);

    // Auto-asimilación continua en el Filtro de Kalman EnKF
    if (this.daysData.length > 0 && data.grid && data.grid.ac_power_kw > 0.1) {
      const curHour = getMadridTime().hour;
      const theoPoint = this.daysData[0].hourly.find(p => p.hour === curHour) || this.daysData[0].hourly[13];
      const theoKw = theoPoint.clearSky.pTotalAC_kW;
      
      const kResult = this.kalmanTwin.assimilate(data.grid.ac_power_kw, theoKw);
      this.renderKalmanMetrics(kResult);
    }
  }

  updateOmodaTrafficLight(telemetry) {
    if (!telemetry) return;
    const solarW = telemetry.solar_total_w || 0;
    const homeLoadW = (telemetry.grid && telemetry.grid.home_load_w) || 0;
    const batSoc = (telemetry.battery && telemetry.battery.soc_percent) || 42;
    const surplusW = Math.max(0, solarW - homeLoadW);
    const surplusKw = surplusW / 1000.0;

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

    if (surplusEl) surplusEl.textContent = `${surplusKw >= 0.05 ? '+' : ''}${surplusKw.toFixed(2)} kW`;

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

    // 1. Estado VERDE: Excedente >= 2.0 kW o Solar >= 3.0 kW con Batería >= 80%
    if (surplusKw >= 2.0 || (solarW >= 3000 && batSoc >= 80)) {
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

      if (titleEl) titleEl.textContent = '¡Momento ideal para recargar el Omoda 7 SHS!';
      if (descEl) descEl.textContent = `Dispones de ${surplusKw.toFixed(2)} kW de excedente solar directo. Enchufando el coche a 2.3 kW (10A Schuko), la recarga será 100% solar y gratuita sin recurrir a la red eléctrica.`;
      if (pctEl) pctEl.textContent = '100% Solar';
      if (pwrEl) pwrEl.textContent = '2.3 kW (10A)';
      if (windowEl) windowEl.textContent = 'Ahora mismo (Pico Solar)';
    } 
    // 2. Estado NARANJA: Excedente entre 0.8 kW y 2.0 kW o Batería >= 60% con Solar >= 1.5 kW
    else if (surplusKw >= 0.8 || (solarW >= 1500 && batSoc >= 60)) {
      badge.textContent = '🟡 CARGA MAYORMENTE SOLAR (VIABLE)';
      badge.style.background = 'rgba(245, 158, 11, 0.2)';
      badge.style.color = '#f59e0b';
      badge.style.border = '1px solid #f59e0b';

      lightRed.style.background = '#334155';
      lightRed.style.boxShadow = 'none';
      lightRed.style.opacity = '0.25';

      lightAmber.style.background = '#f59e0b';
      lightAmber.style.boxShadow = '0 0 16px #f59e0b';
      lightAmber.style.opacity = '1.0';

      lightGreen.style.background = '#334155';
      lightGreen.style.boxShadow = 'none';
      lightGreen.style.opacity = '0.25';

      const solarFrac = Math.min(95, Math.round((solarW / 2300) * 100));
      if (titleEl) titleEl.textContent = 'Carga viable con soporte de batería Fox-ESS';
      if (descEl) descEl.textContent = `La radiación solar cubre aprox. un ${solarFrac}% de los 2.3 kW de carga. La batería de la casa amortigua el resto sin encarecer la factura.`;
      if (pctEl) pctEl.textContent = `~${solarFrac}% Solar`;
      if (pwrEl) pwrEl.textContent = '1.8 - 2.3 kW';
      if (windowEl) windowEl.textContent = '11:30 - 16:30 h';
    } 
    // 3. Estado ROJO: Excedente < 0.8 kW y Batería < 60%
    else {
      badge.textContent = '🔴 NO RECOMENDADO CARGAR AHORA';
      badge.style.background = 'rgba(239, 68, 68, 0.2)';
      badge.style.color = '#ef4444';
      badge.style.border = '1px solid #ef4444';

      lightRed.style.background = '#ef4444';
      lightRed.style.boxShadow = '0 0 16px #ef4444';
      lightRed.style.opacity = '1.0';

      lightAmber.style.background = '#334155';
      lightAmber.style.boxShadow = 'none';
      lightAmber.style.opacity = '0.25';

      lightGreen.style.background = '#334155';
      lightGreen.style.boxShadow = 'none';
      lightGreen.style.opacity = '0.25';

      const gridNeeded = Math.max(0, 2.3 - (solarW / 1000.0));
      if (titleEl) titleEl.textContent = 'Generación solar insuficiente en este momento';
      if (descEl) descEl.textContent = `Con ${(solarW/1000).toFixed(2)} kW de producción solar y batería al ${batSoc.toFixed(0)}%, enchufar el Omoda 7 consumiría ${gridNeeded.toFixed(2)} kW de la red eléctrica. Se recomienda esperar a las 11:30 h.`;
      if (pctEl) pctEl.textContent = solarW > 100 ? `${Math.round((solarW / 2300) * 100)}% Solar` : '0% Solar';
      if (pwrEl) pwrEl.textContent = '0.0 kW (Off)';
      if (windowEl) windowEl.textContent = '11:30 - 16:30 h';
    }
  }

  updateMeteoSyncHeader(cacheMeta) {
    const lastEl = document.getElementById('meteo-last-sync-time');
    const nextEl = document.getElementById('meteo-next-sync-time');
    if (!lastEl || !nextEl) return;

    if (cacheMeta && cacheMeta.fetched_at) {
      const fetchedDate = new Date(cacheMeta.fetched_at);
      const expiresDate = cacheMeta.expires_at ? new Date(cacheMeta.expires_at) : new Date(fetchedDate.getTime() + 3 * 3600 * 1000);
      
      const formatTime = (d) => d.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' });
      lastEl.textContent = formatTime(fetchedDate);
      nextEl.textContent = `Próx: ${formatTime(expiresDate)} (Auto)`;
    } else {
      const now = new Date();
      const next = new Date(now.getTime() + 3 * 3600 * 1000);
      const formatTime = (d) => d.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' });
      lastEl.textContent = formatTime(now);
      nextEl.textContent = `Próx: ${formatTime(next)} (Auto)`;
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
        if (targetId === 'tab-analytics' && this.historicalAnalytics) {
          setTimeout(() => this.historicalAnalytics.resize(), 50);
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

    // Botón Modo Kiosko / Pantalla Completa
    const kioskBtn = document.getElementById('btn-kiosk-mode');
    if (kioskBtn && this.kioskMode) {
      kioskBtn.addEventListener('click', () => {
        this.kioskMode.enter();
      });
    }

    // Botón Instalar PWA
    let deferredPrompt = null;
    const installBtn = document.getElementById('btn-install-pwa');
    window.addEventListener('beforeinstallprompt', (e) => {
      e.preventDefault();
      deferredPrompt = e;
      if (installBtn) {
        installBtn.style.display = 'inline-flex';
        installBtn.addEventListener('click', async () => {
          if (deferredPrompt) {
            deferredPrompt.prompt();
            const { outcome } = await deferredPrompt.userChoice;
            console.log('[PWA] User choice:', outcome);
            deferredPrompt = null;
            installBtn.style.display = 'none';
          }
        });
      }
    });

    // Registro Service Worker PWA
    if ('serviceWorker' in navigator) {
      window.addEventListener('load', () => {
        navigator.serviceWorker.register('./sw.js').catch(err => {
          console.warn('[SW] Registration warn:', err);
        });
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
    if (this.forecastData && this.forecastData.cacheMeta) {
      this.updateMeteoSyncHeader(this.forecastData.cacheMeta);
    }
  }

  recalculate() {
    if (!this.forecastData) return;

    this.daysData = [];
    const hourly = this.forecastData.hourly;
    const daysMap = new Map();

    hourly.forEach(item => {
      // Agrupación de días por fecha local de España (Europe/Madrid)
      const dayKey = item.time ? item.time.slice(0, 10) : new Intl.DateTimeFormat('en-CA', { timeZone: 'Europe/Madrid', year: 'numeric', month: '2-digit', day: '2-digit' }).format(item.date);
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

      const dayDate = items[0].date;
      const rawDayName = new Intl.DateTimeFormat('es-ES', { weekday: 'long' }).format(dayDate);
      const dayName = rawDayName.charAt(0).toUpperCase() + rawDayName.slice(1);

      this.daysData.push({
        index: dayIdx++,
        dateStr: dayKey,
        date: items[0].date,
        dayName,
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
    this.renderDialogAssistant();
    this.renderNewConsiliumModules();
  }

  renderNewConsiliumModules() {
    const selectedDay = this.daysData[this.selectedDayIndex] || this.daysData[0];
    if (this.thermalPrecooling && selectedDay && selectedDay.hourly) {
      this.thermalPrecooling.render(selectedDay.hourly);
    }
    if (this.tariffComparator) {
      this.tariffComparator.render();
    }
    if (this.mobilitySync) {
      this.mobilitySync.render();
    }
    if (this.greenPassport) {
      this.greenPassport.render();
    }
  }

  renderDialogAssistant() {
    const container = document.getElementById('solar-dialog-container');
    if (!container) return;
    if (!this.dialogAssistant) {
      this.dialogAssistant = new SolarDialogAssistant('solar-dialog-container', this);
    } else {
      this.dialogAssistant.render();
      this.dialogAssistant.bindEvents();
    }
  }

  renderDailyEnergyBalance() {
    const selectedDay = this.daysData[this.selectedDayIndex] || this.daysData[0];
    if (!selectedDay) return;

    const isToday = (this.selectedDayIndex === 0);
    const { hour: currentHour, minute: currentMin } = getMadridTime();

    let realHourlyMap = {};
    if (isToday && this.todayHourlyReal && this.todayHourlyReal.hourly) {
      this.todayHourlyReal.hourly.forEach(h => {
        realHourlyMap[h.hour] = h;
      });
    }

    let totalSolarKwh = 0;
    let totalHomeKwh = 0;
    let totalBatChargedKwh = 0;
    let totalBatDischargedKwh = 0;
    let totalExportKwh = 0;
    let totalImportKwh = 0;

    const tbody = document.getElementById('hourly-table-body');
    if (tbody) tbody.innerHTML = '';

    selectedDay.hourly.forEach(p => {
      let solarKw = p.forecast.pTotalAC_kW;
      let isMeasuredReal = false;
      const isCurrentHour = isToday && (p.hour === currentHour);

      if (isToday) {
        if (p.hour < currentHour && realHourlyMap[p.hour] !== undefined) {
          solarKw = realHourlyMap[p.hour].avg_solar_kw;
          isMeasuredReal = true;
        } else if (isCurrentHour) {
          if (this.latestTelemetry && this.latestTelemetry.solar && this.latestTelemetry.online) {
            solarKw = this.latestTelemetry.solar.total_kw;
          } else if (realHourlyMap[p.hour] !== undefined) {
            solarKw = realHourlyMap[p.hour].avg_solar_kw;
          }
          isMeasuredReal = true;
        }
      }

      let homeKw = p.battery.homeLoadW / 1000.0;
      let isMeasuredHome = false;

      if (isToday) {
        if (p.hour < currentHour && realHourlyMap[p.hour] !== undefined) {
          homeKw = realHourlyMap[p.hour].avg_home_kw !== undefined ? realHourlyMap[p.hour].avg_home_kw : realHourlyMap[p.hour].avg_grid_kw;
          isMeasuredHome = true;
        } else if (isCurrentHour) {
          if (this.latestTelemetry && this.latestTelemetry.grid && this.latestTelemetry.online) {
            homeKw = this.latestTelemetry.grid.home_load_kw || this.latestTelemetry.grid.ac_power_kw || (p.battery.homeLoadW / 1000.0);
          } else if (realHourlyMap[p.hour] !== undefined) {
            homeKw = realHourlyMap[p.hour].avg_home_kw !== undefined ? realHourlyMap[p.hour].avg_home_kw : realHourlyMap[p.hour].avg_grid_kw;
          }
          isMeasuredHome = true;
        }
      }

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
        if (isCurrentHour) {
          row.style.background = 'rgba(56, 189, 248, 0.16)';
          row.style.borderLeft = '4px solid #38bdf8';
          row.style.boxShadow = 'inset 0 0 12px rgba(56, 189, 248, 0.1)';
        }

        let stateBadge = `<span style="color: var(--color-real); font-weight: 600;">☀️ 100% Solar</span>`;
        if (isCurrentHour) {
          stateBadge = `<span style="background: #38bdf8; color: #0f172a; font-weight: 800; padding: 2px 6px; border-radius: 4px; font-size: 0.7rem;">⚡ EN CURSO</span>`;
        } else if (solarKw < 0.1 && batPowerKw < 0) {
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

        let hourColHtml = `<span style="color: var(--text-primary); font-weight: 700;">${p.hour.toString().padStart(2, '0')}:00 h</span>`;
        if (isCurrentHour) {
          hourColHtml = `<span style="color: #38bdf8; font-weight: 800;">${p.hour.toString().padStart(2, '0')}:00 h</span> <span style="background: #38bdf8; color: #0f172a; font-size: 0.65rem; padding: 1px 5px; border-radius: 3px; font-weight: 800; margin-left: 4px;">🕒 AHORA (${currentHour.toString().padStart(2, '0')}:${currentMin.toString().padStart(2, '0')})</span>`;
        }

        let solarDisplay = `${solarKw.toFixed(2)}`;
        if (isCurrentHour) {
          solarDisplay += ` <span style="background: #38bdf8; color: #0f172a; font-size: 0.65rem; padding: 1px 4px; border-radius: 3px; font-weight: 800;">⚡ En Vivo</span>`;
        } else if (isMeasuredReal) {
          solarDisplay += ` <span style="background: rgba(16, 185, 129, 0.2); color: #10b981; font-size: 0.65rem; padding: 1px 4px; border-radius: 3px; font-weight: 700;">🟢 Real</span>`;
        } else if (isToday && p.hour > currentHour) {
          solarDisplay += ` <span style="color: #64748b; font-size: 0.65rem;">🔮 Previsto</span>`;
        }

        let homeDisplay = `${homeKw.toFixed(2)}`;
        if (isCurrentHour) {
          homeDisplay += ` <span style="background: #38bdf8; color: #0f172a; font-size: 0.65rem; padding: 1px 4px; border-radius: 3px; font-weight: 800;">⚡ En Vivo</span>`;
        } else if (isMeasuredHome) {
          homeDisplay += ` <span style="background: rgba(244, 63, 94, 0.2); color: #f43f5e; font-size: 0.65rem; padding: 1px 4px; border-radius: 3px; font-weight: 700;">🟢 Real</span>`;
        } else if (isToday && p.hour > currentHour) {
          homeDisplay += ` <span style="color: #64748b; font-size: 0.65rem;">🔮 Previsto</span>`;
        }

        row.innerHTML = `
          <td style="padding: 0.5rem 0.8rem; text-align: left;">${hourColHtml}</td>
          <td style="padding: 0.5rem 0.8rem; color: var(--color-solar-light); font-weight: 600;">${solarDisplay}</td>
          <td style="padding: 0.5rem 0.8rem; color: #f43f5e; font-weight: 600;">${homeDisplay}</td>
          <td style="padding: 0.5rem 0.8rem;">${batFlowHtml}</td>
          <td style="padding: 0.5rem 0.8rem; color: #38bdf8; font-weight: 700;">${Math.round(p.battery.socPercent)}%</td>
          <td style="padding: 0.5rem 0.8rem; color: var(--color-real); font-weight: 600;">${expKw > 0.01 ? expKw.toFixed(2) : '0.00'}</td>
          <td style="padding: 0.5rem 0.8rem; text-align: center;">${stateBadge}</td>
        `;
        tbody.appendChild(row);
      }
    });

    const solEl = document.getElementById('bal-solar-total');
    if (solEl) {
      if (isToday) {
        solEl.innerHTML = `${totalSolarKwh.toFixed(2)} <span style="font-size: 0.75rem; color: #38bdf8;">kWh</span>`;
      } else {
        solEl.textContent = `${totalSolarKwh.toFixed(2)} kWh`;
      }
    }

    const homeEl = document.getElementById('bal-home-total');
    if (homeEl) {
      if (isToday) {
        homeEl.innerHTML = `${totalHomeKwh.toFixed(2)} <span style="font-size: 0.75rem; color: #f43f5e;">kWh</span>`;
      } else {
        homeEl.textContent = `${totalHomeKwh.toFixed(2)} kWh`;
      }
    }

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

    const chartContextKey = `${this.selectedDayIndex}_${this.activeChartTab}`;

    const updateOrCreateChart = (chartType, chartData, chartOptions, plugins = []) => {
      if (this.chartToday && this.chartToday._contextKey === chartContextKey) {
        this.chartToday.data.labels = chartData.labels;
        if (chartData.datasets.length === this.chartToday.data.datasets.length) {
          chartData.datasets.forEach((ds, i) => {
            this.chartToday.data.datasets[i].data = ds.data;
            if (ds.label) this.chartToday.data.datasets[i].label = ds.label;
          });
        } else {
          this.chartToday.data.datasets = chartData.datasets;
        }
        this.chartToday.update('none');
        return;
      }

      if (this.chartToday) {
        this.chartToday.destroy();
        this.chartToday = null;
      }

      this.chartToday = new Chart(ctx, {
        type: chartType,
        data: chartData,
        options: chartOptions,
        plugins: plugins
      });
      this.chartToday._contextKey = chartContextKey;
    };

    const isToday = (this.selectedDayIndex === 0);
    const { hour: currentHour, minute: currentMin, fractionalHour: currentFractionalHour } = getMadridTime();

    const labels = selectedDay.hourly.map(p => `${p.hour.toString().padStart(2, '0')}:00`);
    const clearSkyData = selectedDay.hourly.map(p => p.clearSky.pTotalAC_kW);
    const realForecastData = selectedDay.hourly.map(p => p.forecast.pTotalAC_kW);
    const eastRealData = selectedDay.hourly.map(p => p.forecast.pEast_kW);
    const westRealData = selectedDay.hourly.map(p => p.forecast.pWest_kW);
    const homeLoadData = selectedDay.hourly.map(p => (p.battery.homeLoadW / 1000));

    let realMeasuredSolar = new Array(24).fill(null);
    let realMeasuredEast = new Array(24).fill(null);
    let realMeasuredWest = new Array(24).fill(null);
    let realMeasuredHome = new Array(24).fill(null);
    let forecastRemainingSolar = new Array(24).fill(null);
    let eastForecastRemaining = new Array(24).fill(null);
    let westForecastRemaining = new Array(24).fill(null);
    let forecastRemainingHome = new Array(24).fill(null);

    if (isToday) {
      if (this.todayHourlyReal && this.todayHourlyReal.hourly) {
        this.todayHourlyReal.hourly.forEach(h => {
          if (h.hour <= currentHour) {
            realMeasuredSolar[h.hour] = h.avg_solar_kw;
            realMeasuredEast[h.hour] = h.avg_pv2_kw;
            realMeasuredWest[h.hour] = h.avg_pv1_kw;
            realMeasuredHome[h.hour] = (h.avg_home_kw !== undefined ? h.avg_home_kw : h.avg_grid_kw);
          }
        });
      }

      if (this.latestTelemetry && this.latestTelemetry.online) {
        if (this.latestTelemetry.solar) realMeasuredSolar[currentHour] = this.latestTelemetry.solar.total_kw;
        if (this.latestTelemetry.pv2_east) realMeasuredEast[currentHour] = (this.latestTelemetry.pv2_east.power_w / 1000.0);
        if (this.latestTelemetry.pv1_west) realMeasuredWest[currentHour] = (this.latestTelemetry.pv1_west.power_w / 1000.0);
        if (this.latestTelemetry.grid) {
          realMeasuredHome[currentHour] = this.latestTelemetry.grid.home_load_kw || this.latestTelemetry.grid.ac_power_kw || (selectedDay.hourly[currentHour].battery.homeLoadW / 1000.0);
        }
      }

      for (let h = currentHour; h < 24; h++) {
        forecastRemainingSolar[h] = realForecastData[h];
        eastForecastRemaining[h] = eastRealData[h];
        westForecastRemaining[h] = westRealData[h];
        forecastRemainingHome[h] = homeLoadData[h];
      }
    }

    const self = this;
    const currentTimeMarkerPlugin = {
      id: 'currentTimeMarker',
      afterDraw(chart) {
        if (self.selectedDayIndex !== 0) return;
        const { hour: h, minute: m, fractionalHour } = getMadridTime();
        const xScale = chart.scales.x;
        if (!xScale) return;
        const xPixel = xScale.getPixelForValue(fractionalHour);
        if (isNaN(xPixel) || xPixel < chart.chartArea.left || xPixel > chart.chartArea.right) return;
        const c = chart.ctx;
        c.save();
        c.beginPath();
        c.setLineDash([5, 4]);
        c.lineWidth = 2.5;
        c.strokeStyle = '#38bdf8';
        c.moveTo(xPixel, chart.chartArea.top);
        c.lineTo(xPixel, chart.chartArea.bottom);
        c.stroke();
        c.restore();
      }
    };

    let datasets = [];

    if (this.activeChartTab === 'overview') {
      if (isToday) {
        datasets = [
          {
            label: '⚡ Producción Real Medida (Inversor)',
            data: realMeasuredSolar,
            borderColor: '#10b981',
            backgroundColor: 'rgba(16, 185, 129, 0.28)',
            borderWidth: 2.5,
            fill: true,
            tension: 0.35,
            pointRadius: 3,
            pointBackgroundColor: '#10b981'
          },
          {
            label: '☀️ Pronóstico Restante (EnKF/Meteo)',
            data: forecastRemainingSolar,
            borderColor: '#f59e0b',
            backgroundColor: 'rgba(245, 158, 11, 0.08)',
            borderWidth: 2,
            borderDash: [5, 4],
            fill: true,
            tension: 0.35,
            pointRadius: 0
          },
          {
            label: '🏠 Consumo Hogar (Real + Est)',
            data: realMeasuredHome.map((val, idx) => val !== null ? val : forecastRemainingHome[idx]),
            borderColor: '#ec4899',
            borderWidth: 2,
            fill: false,
            tension: 0.2,
            pointRadius: 2,
            pointBackgroundColor: '#ec4899'
          },
          {
            label: '☀️ Máximo Teórico Clear-Sky',
            data: clearSkyData,
            borderColor: 'rgba(255, 255, 255, 0.2)',
            borderWidth: 1.5,
            borderDash: [3, 3],
            fill: false,
            tension: 0.35,
            pointRadius: 0
          }
        ];
      } else {
        datasets = [
          {
            label: '☀️ Pronóstico Producción Total (kW)',
            data: realForecastData,
            borderColor: '#f59e0b',
            backgroundColor: 'rgba(245, 158, 11, 0.25)',
            borderWidth: 2.5,
            fill: true,
            tension: 0.35,
            pointRadius: 0
          },
          {
            label: '🏠 Consumo Estimado Hogar (kW)',
            data: homeLoadData,
            borderColor: '#ec4899',
            borderWidth: 2,
            fill: false,
            tension: 0.2,
            pointRadius: 0
          },
          {
            label: '☀️ Teórico Despejado (Clear-Sky)',
            data: clearSkyData,
            borderColor: 'rgba(255, 255, 255, 0.2)',
            borderWidth: 1.5,
            borderDash: [3, 3],
            fill: false,
            tension: 0.35,
            pointRadius: 0
          }
        ];
      }
    } else if (this.activeChartTab === 'strings') {
      if (isToday) {
        datasets = [
          {
            label: '🌅 String 2 Oeste 265° Real (kW)',
            data: realMeasuredWest,
            borderColor: '#fb923c',
            backgroundColor: 'rgba(251, 146, 60, 0.25)',
            borderWidth: 2,
            fill: true,
            tension: 0.35,
            pointRadius: 2
          },
          {
            label: '🌅 String 2 Oeste 265° Pronóstico (kW)',
            data: westForecastRemaining,
            borderColor: '#fb923c',
            borderWidth: 1.5,
            borderDash: [4, 4],
            fill: false,
            tension: 0.35,
            pointRadius: 0
          },
          {
            label: '🌄 String 1 Este 85° Real (kW)',
            data: realMeasuredEast,
            borderColor: '#38bdf8',
            backgroundColor: 'rgba(56, 189, 248, 0.25)',
            borderWidth: 2,
            fill: true,
            tension: 0.35,
            pointRadius: 2
          },
          {
            label: '🌄 String 1 Este 85° Pronóstico (kW)',
            data: eastForecastRemaining,
            borderColor: '#38bdf8',
            borderWidth: 1.5,
            borderDash: [4, 4],
            fill: false,
            tension: 0.35,
            pointRadius: 0
          }
        ];
      } else {
        datasets = [
          {
            label: '🌄 String 1 Este (85°) - 3.0 kWp',
            data: eastRealData,
            borderColor: '#38bdf8',
            backgroundColor: 'rgba(56, 189, 248, 0.2)',
            borderWidth: 2,
            fill: true,
            tension: 0.35,
            pointRadius: 0
          },
          {
            label: '🌅 String 2 Oeste (265°) - 2.0 kWp',
            data: westRealData,
            borderColor: '#fb923c',
            backgroundColor: 'rgba(251, 146, 60, 0.2)',
            borderWidth: 2,
            fill: true,
            tension: 0.35,
            pointRadius: 0
          }
        ];
      }
    } else if (this.activeChartTab === 'prices') {
      const prices = this.marketPrices || [];
      const omiePrices = prices.map(p => p.omie_price_ct_kwh || 0);
      const pvpcPrices = prices.map(p => p.pvpc_price_ct_kwh || 0);

      datasets = [
        {
          label: 'Precio Mercado Mayorista OMIE (c€/kWh)',
          data: omiePrices,
          borderColor: '#ec4899',
          backgroundColor: 'rgba(236, 72, 153, 0.2)',
          borderWidth: 2,
          fill: true,
          yAxisID: 'y'
        },
        {
          label: 'Tarifa Regulada PVPC Estimada (c€/kWh)',
          data: pvpcPrices,
          borderColor: '#c084fc',
          borderWidth: 2,
          borderDash: [4, 4],
          fill: false,
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

      updateOrCreateChart('line', { labels, datasets }, {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { labels: { color: '#cbd5e1' } } },
        scales: {
          x: { ticks: { color: '#94a3b8' } },
          y: { title: { display: true, text: 'Precio Mercado (c€/kWh)', color: '#ec4899' }, min: 0, ticks: { color: '#ec4899' } },
          y1: { position: 'right', title: { display: true, text: 'Solar (kW)', color: '#10b981' }, min: 0, grid: { drawOnChartArea: false }, ticks: { color: '#10b981' } }
        }
      }, [currentTimeMarkerPlugin]);
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

      updateOrCreateChart('line', { labels: histLabels.length ? histLabels : labels, datasets }, {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { labels: { color: '#cbd5e1' } } },
        scales: {
          x: { ticks: { color: '#94a3b8', maxTicksLimit: 12 } },
          y: { title: { display: true, text: 'Potencia Real (kW)', color: '#10b981' }, min: 0, ticks: { color: '#10b981' } },
          y1: { position: 'right', title: { display: true, text: 'SoC (%)', color: '#c084fc' }, min: 0, max: 100, grid: { drawOnChartArea: false }, ticks: { color: '#c084fc' } }
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

      updateOrCreateChart('line', { labels: kalmanLabels, datasets }, {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { labels: { color: '#cbd5e1' } } },
        scales: {
          x: { ticks: { color: '#94a3b8' } },
          y: { title: { display: true, text: 'Potencia (kW)', color: '#94a3b8' }, min: 0, ticks: { color: '#94a3b8' } }
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

    updateOrCreateChart('line', { labels, datasets }, {
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
              if (val === null || val === undefined) return '';
              if (context.dataset.yAxisID === 'y1') return ` ${label}: ${Math.round(val)}%`;
              return ` ${label}: ${val.toFixed(2)} kW`;
            }
          }
        }
      },
      plugins: [currentTimeMarkerPlugin]
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
      this.chartToday._contextKey = `climate_5yr`;
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
      this.chartToday._contextKey = `${this.selectedDayIndex}_pinn_quantiles`;
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

    const labels = this.daysData.map((d, i) => 
      i === 0 ? 'Hoy' : new Intl.DateTimeFormat('es-ES', { weekday: 'short', day: 'numeric' }).format(d.date)
    );

    const clearKwhData = this.daysData.map(d => parseFloat(d.kwhClear.toFixed(2)));
    const eastKwhData = this.daysData.map(d => parseFloat(d.eastKwh.toFixed(2)));
    const westKwhData = this.daysData.map(d => parseFloat(d.westKwh.toFixed(2)));

    if (this.chartWeek) {
      this.chartWeek.data.labels = labels;
      this.chartWeek.data.datasets[0].data = clearKwhData;
      this.chartWeek.data.datasets[1].data = eastKwhData;
      this.chartWeek.data.datasets[2].data = westKwhData;
      this.chartWeek.update('none');
      return;
    }

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
      timeEl.textContent = `${now.toLocaleDateString('es-ES', { timeZone: 'Europe/Madrid', weekday: 'long', day: 'numeric', month: 'long', year: 'numeric' })} - ${now.toLocaleTimeString('es-ES', { timeZone: 'Europe/Madrid', hour: '2-digit', minute: '2-digit' })}`;
    }
  }
}

if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', () => {
    window.solarApp = new SolarApp();
  });
} else {
  window.solarApp = new SolarApp();
}
