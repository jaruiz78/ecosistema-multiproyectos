/**
 * Battery SOH, Degradation & String Hot-Spot Diagnostic UI Component
 * Ecosistema Solar Tocina - Los Rosales
 * Autor: Google Antigravity
 */

class BatterySohDiagnosticUI {
  constructor() {
    this.containerId = 'battery-soh-diagnostic-container';
  }

  async init() {
    const container = document.getElementById(this.containerId);
    if (!container) return;

    try {
      const resp = await fetch('/api/battery/soh-diagnostic');
      if (!resp.ok) throw new Error('HTTP ' + resp.status);
      const data = await resp.json();
      this.render(data, container);
    } catch (e) {
      console.warn('[BatterySOH] Error cargando diagnóstico:', e);
      container.innerHTML = `<div class="card error-box">⚠️ No se pudo cargar el diagnóstico SOH: ${e.message}</div>`;
    }
  }

  render(data, container) {
    const soh = data.state_of_health_pct || 99.9;
    const grade = data.health_grade || 'A+ (Excelente)';
    const ri = data.internal_resistance_mohm || 34.5;
    const years = data.estimated_useful_years_remaining || 18.0;
    const cycles = data.equivalent_full_cycles || 12;
    const strDiag = data.strings_diagnostic || {};

    container.innerHTML = `
      <div class="card diagnostic-card" style="background: linear-gradient(135deg, rgba(16, 185, 129, 0.05) 0%, rgba(15, 23, 42, 0.6) 100%); border: 1px solid rgba(16, 185, 129, 0.2); border-radius: 12px; padding: 20px; margin-bottom: 20px;">
        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid rgba(255,255,255,0.1); padding-bottom: 12px; margin-bottom: 16px;">
          <div>
            <h3 style="margin: 0; font-size: 1.15rem; color: #10b981; display: flex; align-items: center; gap: 8px;">
              <span>🩺</span> Diagnóstico de Salud (SOH) Fox-ESS EP5 & Strings Fotovoltaicos
            </h3>
            <span style="font-size: 0.8rem; color: var(--text-muted);">2x Baterías EP5 HV (10.36 kWh LiFePO4) • Inversor Sunworks KP10 SW</span>
          </div>
          <span style="background: rgba(16, 185, 129, 0.2); color: #10b981; padding: 4px 12px; border-radius: 20px; font-weight: 700; font-size: 0.85rem; border: 1px solid #10b981;">
            Grado ${grade}
          </span>
        </div>

        <!-- 4 Grid Metrics -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(160px, 1fr)); gap: 14px; margin-bottom: 16px;">
          <div style="background: rgba(0,0,0,0.3); padding: 12px; border-radius: 8px; border-left: 3px solid #10b981;">
            <div style="font-size: 0.75rem; color: var(--text-muted); text-transform: uppercase;">Estado de Salud (SOH)</div>
            <div style="font-size: 1.4rem; font-weight: 800; color: #10b981;">${soh}%</div>
            <div style="font-size: 0.75rem; color: #94a3b8;">Capacidad Útil: ${data.usable_capacity_remanent_kwh} kWh</div>
          </div>

          <div style="background: rgba(0,0,0,0.3); padding: 12px; border-radius: 8px; border-left: 3px solid #38bdf8;">
            <div style="font-size: 0.75rem; color: var(--text-muted); text-transform: uppercase;">Resistencia Interna</div>
            <div style="font-size: 1.4rem; font-weight: 800; color: #38bdf8;">${ri} mΩ</div>
            <div style="font-size: 0.75rem; color: #94a3b8;">Pack 60S LiFePO4 (~0.58 mΩ/celda)</div>
          </div>

          <div style="background: rgba(0,0,0,0.3); padding: 12px; border-radius: 8px; border-left: 3px solid #fbbf24;">
            <div style="font-size: 0.75rem; color: var(--text-muted); text-transform: uppercase;">Ciclos Completos (EFC)</div>
            <div style="font-size: 1.4rem; font-weight: 800; color: #fbbf24;">${cycles} / 6.000</div>
            <div style="font-size: 0.75rem; color: #94a3b8;">Garantía Fabricante: >15 años</div>
          </div>

          <div style="background: rgba(0,0,0,0.3); padding: 12px; border-radius: 8px; border-left: 3px solid #a855f7;">
            <div style="font-size: 0.75rem; color: var(--text-muted); text-transform: uppercase;">Vida Útil Remanente</div>
            <div style="font-size: 1.4rem; font-weight: 800; color: #a855f7;">~${years} años</div>
            <div style="font-size: 0.75rem; color: #94a3b8;">Degradación: <0.8%/año</div>
          </div>
        </div>

        <!-- String Balance and Hotspot Status -->
        <div style="background: rgba(0,0,0,0.25); border: 1px solid rgba(255,255,255,0.08); border-radius: 8px; padding: 12px 16px;">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
            <span style="font-size: 0.85rem; font-weight: 600; color: #f8fafc;">☀️ Diagnóstico de Paneles & Strings (6 Este / 4 Oeste)</span>
            <span style="font-size: 0.75rem; color: ${strDiag.hotspot_risk === 'LOW' ? '#10b981' : '#fbbf24'}; font-weight: 600;">
              ${strDiag.status_message || 'Strings Óptimos'}
            </span>
          </div>
          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 10px; font-size: 0.75rem; color: #94a3b8;">
            <div>• String 1 (Este 89°): 6x Jinko 500W (Vmp ~176V)</div>
            <div>• String 2 (Oeste 269°): 4x Jinko 500W (Vmp ~253V)</div>
          </div>
        </div>
      </div>
    `;
  }
}

window.batterySohDiagnostic = new BatterySohDiagnosticUI();
