/**
 * historical-analytics.js
 * Módulo de Histórico Analítico y Auditoría Energética Multidimensional
 * Tocina (MultiProyectos AI)
 * 
 * Permite consultar y auditar con precisión física:
 * - Máximo posible a producir despejado (Clear-Sky)
 * - Máximo posible según meteorología real
 * - Producido real (Medido Inversor / Asimilado EnKF)
 * - Consumo total del hogar
 * - Consumo de autoconsumo directo solar
 * - Consumo de batería Fox-ESS EP5
 * - Consumo de red eléctrica
 * - Excedentes exportados a Batería Virtual
 * Agrupable interactivamente por Día, Semana, Mes y Año.
 */

export class HistoricalAnalyticsManager {
  constructor(containerId = 'historical-analytics-container') {
    this.container = document.getElementById(containerId);
    this.granularity = 'month'; // 'day' | 'week' | 'month' | 'year'
    this.year = 2026;
    this.month = 8;
    this.dateStr = new Intl.DateTimeFormat('en-CA', { timeZone: 'Europe/Madrid', year: 'numeric', month: '2-digit', day: '2-digit' }).format(new Date());
    
    this.data = null;
    this.genChart = null;
    this.consChart = null;

    if (this.container) {
      this.init();
    }
  }

  async init() {
    this.renderSkeleton();
    await this.fetchAnalyticsData();
    await this.fetchClimateBacktestData();
  }

  renderSkeleton() {
    if (!this.container) return;

    this.container.innerHTML = `
      <div class="analytics-wrapper" style="display: flex; flex-direction: column; gap: 1.5rem;">
        
        <!-- Header & Controles de Agrupación -->
        <div class="analytics-header-card" style="background: rgba(15, 23, 42, 0.7); border: 1px solid var(--border-subtle); border-radius: 12px; padding: 1.25rem; display: flex; flex-wrap: wrap; justify-content: space-between; align-items: center; gap: 1rem;">
          <div>
            <div style="display: flex; align-items: center; gap: 0.5rem; margin-bottom: 0.25rem;">
              <span style="font-size: 1.4rem;">📊</span>
              <h3 style="font-size: 1.2rem; font-weight: 700; color: var(--text-primary); margin: 0;">Histórico & Auditoría Energética Multidimensional</h3>
            </div>
            <p style="font-size: 0.85rem; color: var(--text-secondary); margin: 0;">
              Auditoría comparativa de potencial teórico (Clear-Sky), meteorología real, generación medida y balance de flujos del hogar.
            </p>
          </div>

          <!-- Selector de Granularidad -->
          <div style="display: flex; align-items: center; gap: 0.75rem; flex-wrap: wrap;">
            <div class="granularity-pill-group" style="display: flex; background: rgba(30, 41, 59, 0.8); border: 1px solid var(--border-subtle); border-radius: 8px; padding: 3px;">
              <button class="gran-btn ${this.granularity === 'day' ? 'active' : ''}" data-gran="day" style="padding: 6px 14px; border: none; background: ${this.granularity === 'day' ? 'var(--color-primary, #0284c7)' : 'transparent'}; color: var(--text-primary); border-radius: 6px; font-size: 0.82rem; font-weight: 600; cursor: pointer; transition: all 0.2s;">📅 Día</button>
              <button class="gran-btn ${this.granularity === 'week' ? 'active' : ''}" data-gran="week" style="padding: 6px 14px; border: none; background: ${this.granularity === 'week' ? 'var(--color-primary, #0284c7)' : 'transparent'}; color: var(--text-primary); border-radius: 6px; font-size: 0.82rem; font-weight: 600; cursor: pointer; transition: all 0.2s;">📆 Semana</button>
              <button class="gran-btn ${this.granularity === 'month' ? 'active' : ''}" data-gran="month" style="padding: 6px 14px; border: none; background: ${this.granularity === 'month' ? 'var(--color-primary, #0284c7)' : 'transparent'}; color: var(--text-primary); border-radius: 6px; font-size: 0.82rem; font-weight: 600; cursor: pointer; transition: all 0.2s;">🗓️ Mes</button>
              <button class="gran-btn ${this.granularity === 'year' ? 'active' : ''}" data-gran="year" style="padding: 6px 14px; border: none; background: ${this.granularity === 'year' ? 'var(--color-primary, #0284c7)' : 'transparent'}; color: var(--text-primary); border-radius: 6px; font-size: 0.82rem; font-weight: 600; cursor: pointer; transition: all 0.2s;">📈 Año</button>
            </div>

            <!-- Filtros de Fecha / Año dinámicos -->
            <div id="analytics-filter-controls" style="display: flex; align-items: center; gap: 0.5rem;">
              <!-- Se inyecta según la granularidad -->
            </div>

            <!-- Botones de Exportación -->
            <div style="display: flex; gap: 0.4rem;">
              <button id="btn-export-analytics-csv" title="Descargar informe CSV" style="background: rgba(16, 185, 129, 0.15); border: 1px solid #10b981; color: #10b981; padding: 6px 12px; border-radius: 6px; font-size: 0.8rem; font-weight: 700; cursor: pointer; display: flex; align-items: center; gap: 4px;">
                <span>📥 CSV</span>
              </button>
              <button id="btn-export-analytics-json" title="Descargar informe JSON" style="background: rgba(56, 189, 248, 0.15); border: 1px solid #38bdf8; color: #38bdf8; padding: 6px 12px; border-radius: 6px; font-size: 0.8rem; font-weight: 700; cursor: pointer; display: flex; align-items: center; gap: 4px;">
                <span>JSON</span>
              </button>
            </div>
          </div>
        </div>

        <!-- 4 Tarjetas KPI Resumen del Período -->
        <div id="analytics-kpi-grid" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 1rem;">
          <!-- Cargando KPIs... -->
        </div>

        <!-- 2 Gráficos Analíticos -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(480px, 1fr)); gap: 1.25rem;">
          
          <!-- Gráfico 1: Generación (Teórico Clear-Sky vs Meteo vs Real) -->
          <div style="background: rgba(15, 23, 42, 0.7); border: 1px solid var(--border-subtle); border-radius: 12px; padding: 1.25rem; display: flex; flex-direction: column; gap: 0.75rem;">
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <div>
                <h4 style="font-size: 1rem; font-weight: 700; color: var(--text-primary); margin: 0;">☀️ Generación: Máx. Teórico vs Meteo vs Real</h4>
                <span style="font-size: 0.75rem; color: var(--text-secondary);">Potencial sin nubes vs Afección climática vs Medición real inversor</span>
              </div>
              <span class="badge" style="background: rgba(16, 185, 129, 0.15); color: #10b981; font-size: 0.75rem; padding: 3px 8px; border-radius: 4px; font-weight: 700;">Física + EnKF</span>
            </div>
            <div style="position: relative; height: 300px; width: 100%;">
              <canvas id="analyticsGenChartCanvas"></canvas>
            </div>
          </div>

          <!-- Gráfico 2: Desglose de Consumo del Hogar (Solar + Batería + Red) vs Exportación -->
          <div style="background: rgba(15, 23, 42, 0.7); border: 1px solid var(--border-subtle); border-radius: 12px; padding: 1.25rem; display: flex; flex-direction: column; gap: 0.75rem;">
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <div>
                <h4 style="font-size: 1rem; font-weight: 700; color: var(--text-primary); margin: 0;">🏠 Cobertura de Consumo & Excedentes</h4>
                <span style="font-size: 0.75rem; color: var(--text-secondary);">Origen del consumo del hogar (Solar Directo, Batería EP5, Red) y Exportado</span>
              </div>
              <span class="badge" style="background: rgba(56, 189, 248, 0.15); color: #38bdf8; font-size: 0.75rem; padding: 3px 8px; border-radius: 4px; font-weight: 700;">Flujos Energéticos</span>
            </div>
            <div style="position: relative; height: 300px; width: 100%;">
              <canvas id="analyticsConsChartCanvas"></canvas>
            </div>
          </div>
        </div>

        <!-- Tabla Completa de Auditoría -->
        <div style="background: rgba(15, 23, 42, 0.7); border: 1px solid var(--border-subtle); border-radius: 12px; padding: 1.25rem; overflow-x: auto;">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
            <div>
              <h4 style="font-size: 1rem; font-weight: 700; color: var(--text-primary); margin: 0;">📋 Tabla de Auditoría Energética Detallada</h4>
              <span style="font-size: 0.78rem; color: var(--text-secondary);">Desglose pormenorizado de todas las variables energéticas y tasas de autoconsumo</span>
            </div>
          </div>

          <table style="width: 100%; border-collapse: collapse; font-size: 0.83rem; text-align: right;">
            <thead>
              <tr style="border-bottom: 2px solid rgba(255, 255, 255, 0.1); color: var(--text-secondary); font-size: 0.78rem; text-transform: uppercase;">
                <th style="padding: 0.6rem 0.8rem; text-align: left;">Período / Fecha</th>
                <th style="padding: 0.6rem 0.8rem; color: #f59e0b;" title="Potencia/Energía con cielo 100% despejado">Máx. Clear-Sky</th>
                <th style="padding: 0.6rem 0.8rem; color: #38bdf8;" title="Potencia/Energía según radiación y nubosidad real">Máx. Meteo</th>
                <th style="padding: 0.6rem 0.8rem; color: #10b981; font-weight: 700;" title="Generación fotovoltaica real">Real Solar</th>
                <th style="padding: 0.6rem 0.8rem; color: #f43f5e;" title="Consumo eléctrico total del hogar">Consumo Total</th>
                <th style="padding: 0.6rem 0.8rem; color: #10b981;" title="Solar consumida instantáneamente en casa">Solar Directo</th>
                <th style="padding: 0.6rem 0.8rem; color: #c084fc;" title="Energía descargada de baterías Fox-ESS">De Batería</th>
                <th style="padding: 0.6rem 0.8rem; color: #fb7185;" title="Energía importada de la red eléctrica">De Red</th>
                <th style="padding: 0.6rem 0.8rem; color: #ec4899;" title="Excedente exportado a Batería Virtual">Excedente BV</th>
                <th style="padding: 0.6rem 0.8rem; text-align: center; color: #38bdf8;" title="Porcentaje de consumo cubierto con solar + batería">Autosuficiencia</th>
              </tr>
            </thead>
            <tbody id="analytics-table-body">
              <!-- Filas de datos -->
            </tbody>
            <tfoot id="analytics-table-footer" style="border-top: 2px solid rgba(255, 255, 255, 0.15); font-weight: 700; background: rgba(30, 41, 59, 0.5);">
              <!-- Fila de totales -->
            </tfoot>
          </table>
        </div>

        <!-- SECCIÓN: AUDITORÍA MULTIDECENAL CRUZADA CON CLIMA DE TOCINA (2014 - 2026) -->
        <div id="climate-backtest-container" style="background: rgba(15, 23, 42, 0.7); border: 1px solid var(--border-subtle); border-radius: 12px; padding: 1.25rem; display: flex; flex-direction: column; gap: 1.25rem;">
          <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 0.75rem;">
            <div>
              <div style="display: flex; align-items: center; gap: 0.5rem;">
                <span style="font-size: 1.3rem;">🌍</span>
                <h4 style="font-size: 1.1rem; font-weight: 700; color: var(--text-primary); margin: 0;">Auditoría Multidecenal Clima-Facturas (2014 - 2026)</h4>
              </div>
              <span style="font-size: 0.78rem; color: var(--text-secondary);">Cruce de 136 recibos de Endesa + 7 de El Corte Inglés con el archivo climático de Tocina (ERA5) y simulación con Batería Virtual</span>
            </div>
            <span class="badge" style="background: rgba(56, 189, 248, 0.15); color: #38bdf8; font-size: 0.78rem; padding: 4px 10px; border-radius: 6px; font-weight: 700;">143 Meses Reales</span>
          </div>

          <!-- Tarjetas KPI Clima-Facturas -->
          <div id="climate-kpi-grid" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 0.85rem;">
            <div style="background: rgba(30, 41, 59, 0.6); padding: 1rem; border-radius: 8px; border: 1px solid var(--border-subtle);">
              <div style="font-size: 0.72rem; color: #f43f5e; font-weight: 700; text-transform: uppercase;">Pagado Real Histórico</div>
              <div style="font-size: 1.35rem; font-weight: 800; color: #f43f5e;" id="cb-total-paid">12.965,41 €</div>
              <div style="font-size: 0.7rem; color: var(--text-secondary);">136 Endesa + 7 ECI</div>
            </div>
            <div style="background: rgba(30, 41, 59, 0.6); padding: 1rem; border-radius: 8px; border: 1px solid var(--border-subtle);">
              <div style="font-size: 0.72rem; color: #10b981; font-weight: 700; text-transform: uppercase;">Con Solar + Fox-ESS + BV</div>
              <div style="font-size: 1.35rem; font-weight: 800; color: #10b981;" id="cb-solar-cost">0,00 €</div>
              <div style="font-size: 0.7rem; color: var(--text-secondary);">Factura 0 € con monedero</div>
            </div>
            <div style="background: rgba(30, 41, 59, 0.6); padding: 1rem; border-radius: 8px; border: 1px solid var(--border-subtle);">
              <div style="font-size: 0.72rem; color: #38bdf8; font-weight: 700; text-transform: uppercase;">Ahorro Total Demostrado</div>
              <div style="font-size: 1.35rem; font-weight: 800; color: #38bdf8;" id="cb-total-savings">12.070,29 €</div>
              <div style="font-size: 0.7rem; color: var(--text-secondary);">12 años simulados</div>
            </div>
            <div style="background: rgba(30, 41, 59, 0.6); padding: 1rem; border-radius: 8px; border: 1px solid var(--border-subtle);">
              <div style="font-size: 0.72rem; color: #c084fc; font-weight: 700; text-transform: uppercase;">Firma Térmica Vivienda</div>
              <div style="font-size: 1.35rem; font-weight: 800; color: #c084fc;" id="cb-thermal-sens">1,88 kWh/HDD</div>
              <div style="font-size: 0.7rem; color: var(--text-secondary);">Base load: 240 kWh/mes</div>
            </div>
          </div>

          <!-- Gráfica Histórica 2014-2026 -->
          <div style="position: relative; height: 320px; width: 100%;">
            <canvas id="climateBacktestChartCanvas"></canvas>
          </div>

          <!-- Desglose de Hitos Históricos y Tabla Desplegable -->
          <div style="max-height: 280px; overflow-y: auto; border: 1px solid var(--border-subtle); border-radius: 8px;">
            <table style="width: 100%; border-collapse: collapse; font-size: 0.8rem; text-align: right;">
              <thead style="background: rgba(15, 23, 42, 0.95); position: sticky; top: 0; color: var(--text-secondary);">
                <tr>
                  <th style="padding: 0.5rem 0.7rem; text-align: left;">Fecha</th>
                  <th style="padding: 0.5rem 0.7rem; text-align: left;">Proveedor / Hito</th>
                  <th style="padding: 0.5rem 0.7rem; text-align: center;">Temp Tocina</th>
                  <th style="padding: 0.5rem 0.7rem; color: #f43f5e;">Factura Real</th>
                  <th style="padding: 0.5rem 0.7rem; color: #10b981;">Gen. Solar Sim</th>
                  <th style="padding: 0.5rem 0.7rem; color: #38bdf8;">Factura Solar</th>
                  <th style="padding: 0.5rem 0.7rem; color: #c084fc; text-align: center;">Monedero BV</th>
                </tr>
              </thead>
              <tbody id="climate-backtest-tbody">
                <!-- Rellenado dinámicamente -->
              </tbody>
            </table>
          </div>

        </div>

      </div>
    `;

    this.bindEvents();
    this.renderFilterControls();
  }

  renderFilterControls() {
    const filterContainer = document.getElementById('analytics-filter-controls');
    if (!filterContainer) return;

    if (this.granularity === 'day') {
      filterContainer.innerHTML = `
        <input type="date" id="analytics-date-picker" value="${this.dateStr}" style="background: rgba(30, 41, 59, 0.9); border: 1px solid var(--border-subtle); color: var(--text-primary); padding: 5px 10px; border-radius: 6px; font-size: 0.82rem; cursor: pointer;">
      `;
      const dp = document.getElementById('analytics-date-picker');
      if (dp) {
        dp.addEventListener('change', (e) => {
          this.dateStr = e.target.value;
          this.fetchAnalyticsData();
        });
      }
    } else if (this.granularity === 'week' || this.granularity === 'month') {
      filterContainer.innerHTML = `
        <select id="analytics-year-select" style="background: rgba(30, 41, 59, 0.9); border: 1px solid var(--border-subtle); color: var(--text-primary); padding: 5px 10px; border-radius: 6px; font-size: 0.82rem; cursor: pointer;">
          <option value="2026" ${this.year === 2026 ? 'selected' : ''}>Año 2026</option>
          <option value="2025" ${this.year === 2025 ? 'selected' : ''}>Año 2025</option>
          <option value="2024" ${this.year === 2024 ? 'selected' : ''}>Año 2024</option>
          <option value="2023" ${this.year === 2023 ? 'selected' : ''}>Año 2023</option>
          <option value="2022" ${this.year === 2022 ? 'selected' : ''}>Año 2022</option>
        </select>
      `;
      const ys = document.getElementById('analytics-year-select');
      if (ys) {
        ys.addEventListener('change', (e) => {
          this.year = parseInt(e.target.value);
          this.fetchAnalyticsData();
        });
      }
    } else if (this.granularity === 'year') {
      filterContainer.innerHTML = `
        <span style="font-size: 0.82rem; color: var(--text-secondary); font-weight: 600;">Histórico 2022 - 2026</span>
      `;
    }
  }

  bindEvents() {
    // Selector de granularidad
    const granBtns = this.container.querySelectorAll('.gran-btn');
    granBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        granBtns.forEach(b => {
          b.classList.remove('active');
          b.style.background = 'transparent';
        });
        btn.classList.add('active');
        btn.style.background = 'var(--color-primary, #0284c7)';
        this.granularity = btn.dataset.gran;
        this.renderFilterControls();
        this.fetchAnalyticsData();
      });
    });

    // Exportación CSV
    const btnCsv = document.getElementById('btn-export-analytics-csv');
    if (btnCsv) {
      btnCsv.addEventListener('click', () => this.downloadCSV());
    }

    // Exportación JSON
    const btnJson = document.getElementById('btn-export-analytics-json');
    if (btnJson) {
      btnJson.addEventListener('click', () => this.downloadJSON());
    }
  }

  async fetchAnalyticsData() {
    try {
      let url = `/api/history/analytics?granularity=${this.granularity}&year=${this.year}&month=${this.month}&date=${this.dateStr}`;
      const resp = await fetch(url);
      if (!resp.ok) return;
      this.data = await resp.json();
      this.renderView();
    } catch (e) {
      console.error('[HistoricalAnalytics] Error al cargar analítica:', e);
    }
  }

  renderView() {
    if (!this.data) return;

    this.renderKpis();
    this.renderCharts();
    this.renderTable();
  }

  renderKpis() {
    const kpiContainer = document.getElementById('analytics-kpi-grid');
    if (!kpiContainer || !this.data.summary) return;

    const s = this.data.summary;
    const unit = this.granularity === 'day' ? 'kWh' : 'kWh';

    kpiContainer.innerHTML = `
      <!-- KPI 1: Generación Solar -->
      <div style="background: rgba(15, 23, 42, 0.8); border: 1px solid var(--border-subtle); border-radius: 10px; padding: 1rem; display: flex; flex-direction: column; gap: 0.4rem;">
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span style="font-size: 0.8rem; color: var(--text-secondary); text-transform: uppercase; font-weight: 700;">☀️ Generación Solar</span>
          <span style="font-size: 0.75rem; color: #10b981; font-weight: 700; background: rgba(16, 185, 129, 0.15); padding: 2px 6px; border-radius: 4px;">${s.avg_performance_percent}% Rend.</span>
        </div>
        <div style="font-size: 1.6rem; font-weight: 800; color: #10b981;">
          ${s.total_real_solar_kwh.toLocaleString('es-ES')} <small style="font-size: 0.9rem; font-weight: 600; color: var(--text-secondary);">${unit}</small>
        </div>
        <div style="font-size: 0.78rem; color: var(--text-secondary); display: flex; justify-content: space-between;">
          <span>Máx. Meteo: <strong style="color: #38bdf8;">${s.total_meteo_max_kwh.toLocaleString('es-ES')}</strong></span>
          <span>Clear-Sky: <strong style="color: #f59e0b;">${s.total_clear_sky_kwh.toLocaleString('es-ES')}</strong></span>
        </div>
      </div>

      <!-- KPI 2: Consumo del Hogar -->
      <div style="background: rgba(15, 23, 42, 0.8); border: 1px solid var(--border-subtle); border-radius: 10px; padding: 1rem; display: flex; flex-direction: column; gap: 0.4rem;">
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span style="font-size: 0.8rem; color: var(--text-secondary); text-transform: uppercase; font-weight: 700;">🏠 Consumo Total Hogar</span>
          <span style="font-size: 0.75rem; color: #f43f5e; font-weight: 700; background: rgba(244, 63, 94, 0.15); padding: 2px 6px; border-radius: 4px;">Demanda Real</span>
        </div>
        <div style="font-size: 1.6rem; font-weight: 800; color: var(--text-primary);">
          ${s.total_home_kwh.toLocaleString('es-ES')} <small style="font-size: 0.9rem; font-weight: 600; color: var(--text-secondary);">${unit}</small>
        </div>
        <div style="font-size: 0.78rem; color: var(--text-secondary); display: flex; justify-content: space-between;">
          <span>Solar Directo: <strong style="color: #10b981;">${s.total_direct_solar_kwh.toLocaleString('es-ES')}</strong></span>
          <span>Batería EP5: <strong style="color: #c084fc;">${s.total_battery_home_kwh.toLocaleString('es-ES')}</strong></span>
          <span>Red: <strong style="color: #fb7185;">${s.total_grid_import_kwh.toLocaleString('es-ES')}</strong></span>
        </div>
      </div>

      <!-- KPI 3: Autosuficiencia & Excedentes -->
      <div style="background: rgba(15, 23, 42, 0.8); border: 1px solid var(--border-subtle); border-radius: 10px; padding: 1rem; display: flex; flex-direction: column; gap: 0.4rem;">
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span style="font-size: 0.8rem; color: var(--text-secondary); text-transform: uppercase; font-weight: 700;">🌱 Autosuficiencia Limpia</span>
          <span style="font-size: 0.75rem; color: #38bdf8; font-weight: 700; background: rgba(56, 189, 248, 0.15); padding: 2px 6px; border-radius: 4px;">Solar + Fox-ESS</span>
        </div>
        <div style="font-size: 1.6rem; font-weight: 800; color: #38bdf8;">
          ${s.avg_autonomy_percent}% <small style="font-size: 0.85rem; font-weight: 600; color: var(--text-secondary);">cobertura</small>
        </div>
        <div style="font-size: 0.78rem; color: var(--text-secondary); display: flex; justify-content: space-between;">
          <span>Excedentes BV: <strong style="color: #ec4899;">${s.total_grid_export_kwh.toLocaleString('es-ES')} kWh</strong></span>
          <span>Importado: <strong style="color: #fb7185;">${s.total_grid_import_kwh.toLocaleString('es-ES')} kWh</strong></span>
        </div>
      </div>

      <!-- KPI 4: Ahorro & Sostenibilidad -->
      <div style="background: rgba(15, 23, 42, 0.8); border: 1px solid var(--border-subtle); border-radius: 10px; padding: 1rem; display: flex; flex-direction: column; gap: 0.4rem;">
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span style="font-size: 0.8rem; color: var(--text-secondary); text-transform: uppercase; font-weight: 700;">💶 Ahorro Económico & CO2</span>
          <span style="font-size: 0.75rem; color: #eab308; font-weight: 700; background: rgba(234, 179, 8, 0.15); padding: 2px 6px; border-radius: 4px;">Tasa Real</span>
        </div>
        <div style="font-size: 1.6rem; font-weight: 800; color: #eab308;">
          ${s.total_savings_eur.toLocaleString('es-ES', { minimumFractionDigits: 2 })} <small style="font-size: 0.9rem; font-weight: 600; color: var(--text-secondary);">€</small>
        </div>
        <div style="font-size: 0.78rem; color: var(--text-secondary); display: flex; justify-content: space-between;">
          <span>CO₂ Evitado: <strong style="color: #10b981;">${s.total_co2_kg.toLocaleString('es-ES')} kg</strong></span>
          <span>Monedero BV: <strong style="color: #ec4899;">+${(s.total_grid_export_kwh * 0.095).toFixed(1)} €</strong></span>
        </div>
      </div>
    `;
  }

  renderCharts() {
    const items = this.data.items || [];
    if (!items.length) return;

    const labels = items.map(it => it.label);
    const clearSkyData = items.map(it => it.clear_sky_kwh);
    const meteoMaxData = items.map(it => it.meteo_max_kwh);
    const realSolarData = items.map(it => it.real_solar_kwh);

    const directSolarData = items.map(it => it.direct_solar_kwh);
    const batteryData = items.map(it => it.battery_home_kwh);
    const gridImportData = items.map(it => it.grid_import_kwh);
    const gridExportData = items.map(it => it.grid_export_kwh);

    // 1. Gráfico de Generación
    const genCtx = document.getElementById('analyticsGenChartCanvas');
    if (genCtx) {
      if (this.genChart) {
        this.genChart.data.labels = labels;
        this.genChart.data.datasets[0].data = clearSkyData;
        this.genChart.data.datasets[1].data = meteoMaxData;
        this.genChart.data.datasets[2].data = realSolarData;
        this.genChart.update('none');
      } else {
        this.genChart = new Chart(genCtx, {
          type: 'bar',
          data: {
            labels,
            datasets: [
              {
                type: 'line',
                label: 'Máximo Clear-Sky (Sin nubes)',
                data: clearSkyData,
                borderColor: '#f59e0b',
                borderWidth: 2,
                borderDash: [5, 4],
                pointRadius: 2,
                fill: false,
                tension: 0.3
              },
              {
                type: 'line',
                label: 'Máximo Meteo-Ajustado',
                data: meteoMaxData,
                borderColor: '#38bdf8',
                borderWidth: 2,
                pointRadius: 2,
                fill: false,
                tension: 0.3
              },
              {
                type: 'bar',
                label: 'Generación Solar Real (kW / kWh)',
                data: realSolarData,
                backgroundColor: 'rgba(16, 185, 129, 0.75)',
                borderColor: '#10b981',
                borderWidth: 1,
                borderRadius: 4
              }
            ]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: { mode: 'index', intersect: false },
            plugins: {
              legend: { labels: { color: '#cbd5e1', font: { size: 11 } } },
              tooltip: {
                backgroundColor: '#0f172a',
                padding: 10,
                callbacks: {
                  label: (ctx) => ` ${ctx.dataset.label}: ${ctx.parsed.y.toFixed(2)} kWh`
                }
              }
            },
            scales: {
              x: { ticks: { color: '#94a3b8', maxTicksLimit: 12 }, grid: { color: 'rgba(255,255,255,0.05)' } },
              y: { title: { display: true, text: 'Energía (kWh)', color: '#94a3b8' }, ticks: { color: '#94a3b8' }, grid: { color: 'rgba(255,255,255,0.06)' }, min: 0 }
            }
          }
        });
      }
    }

    // 2. Gráfico de Consumo Desglosado Apilado
    const consCtx = document.getElementById('analyticsConsChartCanvas');
    if (consCtx) {
      if (this.consChart) {
        this.consChart.data.labels = labels;
        this.consChart.data.datasets[0].data = directSolarData;
        this.consChart.data.datasets[1].data = batteryData;
        this.consChart.data.datasets[2].data = gridImportData;
        this.consChart.data.datasets[3].data = gridExportData;
        this.consChart.update('none');
      } else {
        this.consChart = new Chart(consCtx, {
          type: 'bar',
          data: {
            labels,
            datasets: [
              {
                label: 'Solar Directo (Casa)',
                data: directSolarData,
                backgroundColor: '#10b981',
                stack: 'Consumo',
                borderRadius: 2
              },
              {
                label: 'Batería Fox-ESS (Casa)',
                data: batteryData,
                backgroundColor: '#8b5cf6',
                stack: 'Consumo',
                borderRadius: 2
              },
              {
                label: 'Importación Red Eléctrica',
                data: gridImportData,
                backgroundColor: '#f43f5e',
                stack: 'Consumo',
                borderRadius: 2
              },
              {
                type: 'line',
                label: 'Excedente a Batería Virtual',
                data: gridExportData,
                borderColor: '#ec4899',
                backgroundColor: 'rgba(236, 72, 153, 0.15)',
                borderWidth: 2,
                pointRadius: 2,
                fill: false,
                tension: 0.2
              }
            ]
          },
          options: {
          responsive: true,
          maintainAspectRatio: false,
          interaction: { mode: 'index', intersect: false },
          plugins: {
            legend: { labels: { color: '#cbd5e1', font: { size: 11 } } },
            tooltip: {
              backgroundColor: '#0f172a',
              padding: 10,
              callbacks: {
                label: (ctx) => ` ${ctx.dataset.label}: ${ctx.parsed.y.toFixed(2)} kWh`
              }
            }
          },
          scales: {
            x: { stacked: true, ticks: { color: '#94a3b8', maxTicksLimit: 12 }, grid: { color: 'rgba(255,255,255,0.05)' } },
            y: { stacked: true, title: { display: true, text: 'Energía Hogar (kWh)', color: '#94a3b8' }, ticks: { color: '#94a3b8' }, grid: { color: 'rgba(255,255,255,0.06)' }, min: 0 }
          }
        }
      });
    }
  }

  renderTable() {
    const tbody = document.getElementById('analytics-table-body');
    const tfoot = document.getElementById('analytics-table-footer');
    if (!tbody || !this.data.items) return;

    tbody.innerHTML = '';
    const items = this.data.items;

    items.forEach(it => {
      const tr = document.createElement('tr');
      tr.style.borderBottom = '1px solid rgba(255, 255, 255, 0.05)';

      const autonomyColor = it.autonomy_percent >= 90 ? '#10b981' : (it.autonomy_percent >= 60 ? '#38bdf8' : '#eab308');

      tr.innerHTML = `
        <td style="padding: 0.55rem 0.8rem; text-align: left; font-weight: 700; color: var(--text-primary);">${it.label}</td>
        <td style="padding: 0.55rem 0.8rem; color: #f59e0b;">${it.clear_sky_kwh.toFixed(2)}</td>
        <td style="padding: 0.55rem 0.8rem; color: #38bdf8;">${it.meteo_max_kwh.toFixed(2)}</td>
        <td style="padding: 0.55rem 0.8rem; color: #10b981; font-weight: 700;">${it.real_solar_kwh.toFixed(2)}</td>
        <td style="padding: 0.55rem 0.8rem; color: #f43f5e; font-weight: 600;">${it.total_home_kwh.toFixed(2)}</td>
        <td style="padding: 0.55rem 0.8rem; color: #10b981;">${it.direct_solar_kwh.toFixed(2)}</td>
        <td style="padding: 0.55rem 0.8rem; color: #c084fc;">${it.battery_home_kwh.toFixed(2)}</td>
        <td style="padding: 0.55rem 0.8rem; color: #fb7185;">${it.grid_import_kwh.toFixed(2)}</td>
        <td style="padding: 0.55rem 0.8rem; color: #ec4899; font-weight: 600;">${it.grid_export_kwh.toFixed(2)}</td>
        <td style="padding: 0.55rem 0.8rem; text-align: center;">
          <span style="background: rgba(56, 189, 248, 0.15); color: ${autonomyColor}; font-weight: 700; padding: 2px 6px; border-radius: 4px; font-size: 0.75rem;">
            ${it.autonomy_percent.toFixed(1)}%
          </span>
        </td>
      `;
      tbody.appendChild(tr);
    });

    // Pie de tabla con totales
    if (tfoot && this.data.summary) {
      const s = this.data.summary;
      tfoot.innerHTML = `
        <tr>
          <td style="padding: 0.75rem 0.8rem; text-align: left; color: var(--text-primary); font-weight: 800;">TOTAL RESUMEN</td>
          <td style="padding: 0.75rem 0.8rem; color: #f59e0b;">${s.total_clear_sky_kwh.toLocaleString('es-ES')}</td>
          <td style="padding: 0.75rem 0.8rem; color: #38bdf8;">${s.total_meteo_max_kwh.toLocaleString('es-ES')}</td>
          <td style="padding: 0.75rem 0.8rem; color: #10b981; font-weight: 800;">${s.total_real_solar_kwh.toLocaleString('es-ES')}</td>
          <td style="padding: 0.75rem 0.8rem; color: #f43f5e; font-weight: 800;">${s.total_home_kwh.toLocaleString('es-ES')}</td>
          <td style="padding: 0.75rem 0.8rem; color: #10b981;">${s.total_direct_solar_kwh.toLocaleString('es-ES')}</td>
          <td style="padding: 0.75rem 0.8rem; color: #c084fc;">${s.total_battery_home_kwh.toLocaleString('es-ES')}</td>
          <td style="padding: 0.75rem 0.8rem; color: #fb7185;">${s.total_grid_import_kwh.toLocaleString('es-ES')}</td>
          <td style="padding: 0.75rem 0.8rem; color: #ec4899; font-weight: 800;">${s.total_grid_export_kwh.toLocaleString('es-ES')}</td>
          <td style="padding: 0.75rem 0.8rem; text-align: center;">
            <span style="background: rgba(16, 185, 129, 0.2); color: #10b981; font-weight: 800; padding: 3px 8px; border-radius: 4px; font-size: 0.8rem;">
              ${s.avg_autonomy_percent}%
            </span>
          </td>
        </tr>
      `;
    }
  }

  resize() {
    if (this.genChart) this.genChart.resize();
    if (this.consChart) this.consChart.resize();
  }

  downloadCSV() {
    if (!this.data || !this.data.items) return;

    const headers = [
      "Periodo", "Max_Clear_Sky_kWh", "Max_Meteo_kWh", "Real_Solar_kWh",
      "Consumo_Total_kWh", "Solar_Directo_kWh", "De_Bateria_kWh",
      "De_Red_kWh", "Excedente_BV_kWh", "Autosuficiencia_Pct", "Ahorro_EUR", "CO2_Evitado_kg"
    ];

    const rows = this.data.items.map(it => [
      `"${it.label}"`,
      it.clear_sky_kwh,
      it.meteo_max_kwh,
      it.real_solar_kwh,
      it.total_home_kwh,
      it.direct_solar_kwh,
      it.battery_home_kwh,
      it.grid_import_kwh,
      it.grid_export_kwh,
      it.autonomy_percent,
      it.savings_eur,
      it.co2_saved_kg
    ]);

    const csvContent = "data:text/csv;charset=utf-8," + [headers.join(","), ...rows.map(r => r.join(","))].join("\n");
    const encodedUri = encodeURI(csvContent);
    const link = document.createElement("a");
    link.setAttribute("href", encodedUri);
    link.setAttribute("download", `auditoria_solar_tocina_${this.granularity}_${this.year}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  downloadJSON() {
    if (!this.data) return;
    const dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(this.data, null, 2));
    const link = document.createElement("a");
    link.setAttribute("href", dataStr);
    link.setAttribute("download", `auditoria_solar_tocina_${this.granularity}_${this.year}.json`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  async fetchClimateBacktestData() {
    try {
      const resp = await fetch('/api/history/climate-backtest');
      if (!resp.ok) return;
      this.climateData = await resp.json();
      this.renderClimateBacktest();
    } catch (e) {
      console.warn('Error fetching climate backtest:', e);
    }
  }

  renderClimateBacktest() {
    if (!this.climateData || !this.climateData.records) return;
    const s = this.climateData.summary;
    
    const paidEl = document.getElementById('cb-total-paid');
    const solarEl = document.getElementById('cb-solar-cost');
    const savEl = document.getElementById('cb-total-savings');
    const thermEl = document.getElementById('cb-thermal-sens');
    
    if (paidEl) paidEl.textContent = `${s.total_historic_paid_eur.toLocaleString('es-ES', { minimumFractionDigits: 2 })} €`;
    if (solarEl) solarEl.textContent = `${s.total_sim_paid_with_solar_eur.toFixed(2)} €`;
    if (savEl) savEl.textContent = `${s.total_historic_savings_eur.toLocaleString('es-ES', { minimumFractionDigits: 2 })} €`;
    if (thermEl) thermEl.textContent = `${s.thermal_sensitivity_kwh_per_hdd} kWh/HDD`;

    // Render Table Body
    const tbody = document.getElementById('climate-backtest-tbody');
    if (tbody) {
      tbody.innerHTML = '';
      // Mostramos los meses en orden cronológico inverso
      const recs = [...this.climateData.records].reverse();
      recs.forEach(r => {
        const tr = document.createElement('tr');
        tr.style.borderBottom = '1px solid rgba(255, 255, 255, 0.05)';
        
        const monthNames = ['', 'Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];
        const dateLabel = `${monthNames[r.month]} ${r.year}`;
        const isCrisis = r.event && r.event.includes('🔥');
        const eventBadge = r.event ? `<span style="font-size: 0.72rem; padding: 2px 6px; border-radius: 4px; background: ${isCrisis ? 'rgba(239, 68, 68, 0.2)' : 'rgba(56, 189, 248, 0.15)'}; color: ${isCrisis ? '#ef4444' : '#38bdf8'}; margin-left: 6px;">${r.event}</span>` : '';

        tr.innerHTML = `
          <td style="padding: 0.45rem 0.7rem; text-align: left; font-weight: 700; color: var(--text-primary);">${dateLabel}</td>
          <td style="padding: 0.45rem 0.7rem; text-align: left; color: var(--text-secondary); font-size: 0.75rem;">${r.provider} ${eventBadge}</td>
          <td style="padding: 0.45rem 0.7rem; text-align: center; color: #fbbf24;">${r.temp_mean_c.toFixed(1)} °C</td>
          <td style="padding: 0.45rem 0.7rem; color: #f43f5e; font-weight: 700;">${r.actual_paid_eur.toFixed(2)} €</td>
          <td style="padding: 0.45rem 0.7rem; color: #10b981;">${r.sim_solar_kwh.toFixed(0)} kWh</td>
          <td style="padding: 0.45rem 0.7rem; color: #38bdf8; font-weight: 700;">${r.sim_new_bill_eur.toFixed(2)} €</td>
          <td style="padding: 0.45rem 0.7rem; text-align: center; color: #c084fc; font-weight: 600;">+${r.virtual_wallet_eur.toFixed(2)} €</td>
        `;
        tbody.appendChild(tr);
      });
    }

    // Render Chart
    this.renderClimateBacktestChart();
  }

  renderClimateBacktestChart() {
    const canvas = document.getElementById('climateBacktestChartCanvas');
    if (!canvas || typeof Chart === 'undefined') return;

    const recs = this.climateData.records;
    const monthNames = ['', 'Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];
    const labels = recs.map(r => `${monthNames[r.month]} '${String(r.year).slice(-2)}`);
    const actualPaid = recs.map(r => r.actual_paid_eur);
    const solarPaid = recs.map(r => r.sim_new_bill_eur);
    const temperatures = recs.map(r => r.temp_mean_c);

    if (this.climateChart) {
      this.climateChart.data.labels = labels;
      this.climateChart.data.datasets[0].data = actualPaid;
      this.climateChart.data.datasets[1].data = solarPaid;
      this.climateChart.data.datasets[2].data = temperatures;
      this.climateChart.update('none');
      return;
    }

    this.climateChart = new Chart(canvas, {
      type: 'line',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Factura Real Pagada (€)',
            data: actualPaid,
            borderColor: '#f43f5e',
            backgroundColor: 'rgba(244, 63, 94, 0.1)',
            borderWidth: 2,
            fill: true,
            tension: 0.25,
            yAxisID: 'y'
          },
          {
            label: 'Factura con Solar + Fox-ESS + BV (€)',
            data: solarPaid,
            borderColor: '#10b981',
            borderWidth: 2.5,
            pointRadius: 0,
            fill: false,
            yAxisID: 'y'
          },
          {
            label: 'Temp. Media Tocina (°C)',
            data: temperatures,
            borderColor: '#fbbf24',
            borderWidth: 1.5,
            borderDash: [4, 4],
            pointRadius: 0,
            fill: false,
            yAxisID: 'yTemp'
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        interaction: { mode: 'index', intersect: false },
        plugins: {
          legend: { labels: { color: '#cbd5e1', font: { size: 11 } } },
          tooltip: {
            callbacks: {
              label: (ctx) => {
                if (ctx.dataset.yAxisID === 'yTemp') return ` ${ctx.dataset.label}: ${ctx.raw.toFixed(1)} °C`;
                return ` ${ctx.dataset.label}: ${ctx.raw.toFixed(2)} €`;
              }
            }
          }
        },
        scales: {
          x: {
            ticks: { color: '#94a3b8', maxRotation: 45, autoSkip: true, maxTicksLimit: 24 },
            grid: { color: 'rgba(255,255,255,0.05)' }
          },
          y: {
            title: { display: true, text: 'Importe Factura (€)', color: '#f43f5e' },
            ticks: { color: '#94a3b8' },
            grid: { color: 'rgba(255,255,255,0.07)' },
            beginAtZero: true
          },
          yTemp: {
            position: 'right',
            title: { display: true, text: 'Temperatura (°C)', color: '#fbbf24' },
            ticks: { color: '#fbbf24' },
            grid: { drawOnChartArea: false }
          }
        }
      }
    });
  }
}
