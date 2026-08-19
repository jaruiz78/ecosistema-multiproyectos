/**
 * environmental-sensors-ui.js
 * Componente UI para Red de Sensores Ambientales (Temperatura, Humedad, Confort Térmico & Inercia RC)
 * Ecosistema Solar Tocina - Los Rosales
 */

export class EnvironmentalSensorsUiManager {
  constructor(containerId = 'environmental-sensors-container') {
    this.container = document.getElementById(containerId);
    this.status = null;
    this.pollInterval = null;
  }

  async init() {
    if (!this.container) return;
    await this.fetchStatus();
    this.render();
    this.setupAutoRefresh();
  }

  async fetchStatus() {
    try {
      const res = await fetch('/api/environmental-sensors/status');
      if (res.ok) {
        this.status = await res.json();
      }
    } catch (e) {
      console.warn('[EnvironmentalSensorsUi] Error fetching status:', e);
    }
  }

  setupAutoRefresh() {
    if (this.pollInterval) clearInterval(this.pollInterval);
    this.pollInterval = setInterval(async () => {
      await this.fetchStatus();
      this.updateView();
    }, 15000);
  }

  render() {
    if (!this.container) return;
    const sensors = this.status?.sensors || [];
    const aggregated = this.status?.aggregated_indoor || {};

    this.container.innerHTML = `
      <div class="section-box" style="border: 1px solid rgba(16, 185, 129, 0.35); box-shadow: 0 0 20px rgba(16, 185, 129, 0.08); margin-bottom: 1.5rem;">
        <div class="section-header" style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 0.75rem;">
          <div>
            <div style="display: flex; align-items: center; gap: 0.5rem;">
              <span style="font-size: 1.4rem;">🌡️</span>
              <h2 style="margin: 0; font-size: 1.15rem;">Red de Sensores Ambientales & Inercia Térmica del Hogar</h2>
            </div>
            <div class="sub-desc" style="margin-top: 0.25rem;">
              Monitorización de estancias en tiempo real y retroalimentación al modelo térmico RC (Rth / Cth)
            </div>
          </div>
          <div style="display: flex; align-items: center; gap: 0.6rem;">
            <span class="badge-tag" style="background: rgba(16, 185, 129, 0.15); color: #34d399; font-weight: 700;">
              Inercia Térmica: ${aggregated.calculated_rc_thermal_inertia_hours || 4.8} h
            </span>
          </div>
        </div>

        <!-- Banner de Promedio Interior y Confort -->
        <div style="background: rgba(16, 185, 129, 0.08); border: 1px solid rgba(16, 185, 129, 0.25); border-radius: var(--radius-md); padding: 0.75rem 1rem; margin: 1rem 0; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 0.75rem;">
          <div style="display: flex; align-items: center; gap: 1rem;">
            <div>
              <span style="font-size: 0.72rem; color: var(--text-muted); text-transform: uppercase;">Media Interior Ponderada</span>
              <div style="font-size: 1.25rem; font-weight: 800; color: #34d399; font-family: var(--font-mono);">
                ${aggregated.mean_temp_c || 26.4} °C
              </div>
            </div>
            <div>
              <span style="font-size: 0.72rem; color: var(--text-muted); text-transform: uppercase;">Humedad Relativa Media</span>
              <div style="font-size: 1.25rem; font-weight: 800; color: #38bdf8; font-family: var(--font-mono);">
                ${aggregated.mean_humidity_pct || 47.0}%
              </div>
            </div>
          </div>
          <div style="font-size: 0.8rem; color: var(--text-secondary); text-align: right;">
            <span>Índice Confort ISO 7730:</span> <strong style="color: #10b981;">Confort Térmico Óptimo</strong>
          </div>
        </div>

        <!-- Cuadrícula de Sensores -->
        <div id="environmental-sensors-grid" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 1rem;">
          ${this.renderSensorsHtml(sensors)}
        </div>
      </div>
    `;
  }

  renderSensorsHtml(sensors) {
    return sensors.map(s => {
      const r = s.readings || {};
      const isIndoor = s.location.includes('indoor');

      return `
        <div class="card-box" style="background: var(--bg-card); border: 1px solid var(--border-subtle); border-radius: var(--radius-md); padding: 0.9rem; display: flex; flex-direction: column; justify-content: space-between; gap: 0.6rem;">
          <div style="display: flex; justify-content: space-between; align-items: flex-start;">
            <div>
              <div style="font-size: 0.88rem; font-weight: 800; color: var(--text-primary);">${s.name}</div>
              <div style="font-size: 0.7rem; color: var(--text-muted); text-transform: uppercase;">${s.location}</div>
            </div>
            <span style="font-size: 0.7rem; color: #10b981; font-family: var(--font-mono); background: rgba(16, 185, 129, 0.1); padding: 0.1rem 0.35rem; border-radius: 4px;">
              🔋 ${s.battery_pct}%
            </span>
          </div>

          <div style="display: flex; justify-content: space-between; align-items: baseline; background: rgba(0,0,0,0.2); padding: 0.5rem 0.75rem; border-radius: var(--radius-sm);">
            <div>
              <span style="font-size: 1.4rem; font-weight: 800; color: ${isIndoor ? '#34d399' : '#fbbf24'}; font-family: var(--font-mono);">${r.temperature_c.toFixed(1)}</span>
              <span style="font-size: 0.85rem; color: var(--text-muted);">°C</span>
            </div>
            <div style="text-align: right;">
              <span style="font-size: 1.1rem; font-weight: 800; color: #38bdf8; font-family: var(--font-mono);">${r.humidity_pct.toFixed(0)}</span>
              <span style="font-size: 0.8rem; color: var(--text-muted);">% HR</span>
            </div>
          </div>

          <div style="display: flex; justify-content: space-between; font-size: 0.7rem; color: var(--text-muted);">
            <span>Rocío: <strong style="color: #38bdf8;">${r.dew_point_c} °C</strong></span>
            <span>Estado: <strong style="color: #10b981;">${r.comfort_index}</strong></span>
          </div>
        </div>
      `;
    }).join('');
  }

  updateView() {
    const grid = this.container?.querySelector('#environmental-sensors-grid');
    if (grid && this.status?.sensors) {
      grid.innerHTML = this.renderSensorsHtml(this.status.sensors);
    }
  }
}
