/**
 * ICP Contracted Power Optimizer UI Component
 * Ecosistema Solar Tocina - Los Rosales
 * Autor: Google Antigravity
 */

class IcpPowerOptimizerUI {
  constructor() {
    this.containerId = 'icp-power-optimizer-container';
  }

  async init() {
    const container = document.getElementById(this.containerId);
    if (!container) return;

    try {
      const resp = await fetch('/api/finance/icp-optimizer');
      if (!resp.ok) throw new Error('HTTP ' + resp.status);
      const data = await resp.json();
      this.render(data, container);
    } catch (e) {
      console.warn('[IcpOptimizer] Error cargando optimizador ICP:', e);
      container.innerHTML = `<div class="card error-box">⚠️ Error cargando optimizador de potencia: ${e.message}</div>`;
    }
  }

  render(data, container) {
    const stats = data.peak_statistics || {};
    const scenarios = data.scenarios || [];
    const opt = data.optimal_recommendation || {};

    let scenariosHtml = '';
    scenarios.forEach(sc => {
      const isCurrent = sc.kw === 4.60;
      scenariosHtml += `
        <div style="background: rgba(0,0,0,0.3); border: 1px solid ${isCurrent ? '#38bdf8' : 'rgba(255,255,255,0.08)'}; border-radius: 8px; padding: 12px; position: relative;">
          ${isCurrent ? '<span style="position: absolute; top: -10px; right: 10px; background: #38bdf8; color: #0f172a; font-size: 0.65rem; font-weight: 800; padding: 2px 8px; border-radius: 10px;">CONTRATO ACTUAL</span>' : ''}
          <div style="font-weight: 700; font-size: 1rem; color: #f8fafc; margin-bottom: 4px;">${sc.name}</div>
          <div style="font-size: 0.8rem; color: var(--text-muted); margin-bottom: 8px;">Término Fijo: <strong style="color: #f8fafc;">${sc.fixed_cost_year} €/año</strong></div>
          <div style="font-size: 0.75rem; color: ${sc.color}; font-weight: 600; margin-bottom: 4px;">Riesgo: ${sc.risk_level}</div>
          <div style="font-size: 0.75rem; color: #94a3b8; line-height: 1.3;">${sc.recommendation}</div>
          ${sc.annual_savings_eur > 0 ? `<div style="margin-top: 8px; font-size: 0.75rem; color: #10b981; font-weight: 700;">Ahorro: +${sc.annual_savings_eur} €/año</div>` : ''}
        </div>
      `;
    });

    container.innerHTML = `
      <div class="card icp-optimizer-card" style="background: linear-gradient(135deg, rgba(56, 189, 248, 0.05) 0%, rgba(15, 23, 42, 0.6) 100%); border: 1px solid rgba(56, 189, 248, 0.2); border-radius: 12px; padding: 20px; margin-bottom: 20px;">
        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid rgba(255,255,255,0.1); padding-bottom: 12px; margin-bottom: 16px;">
          <div>
            <h3 style="margin: 0; font-size: 1.15rem; color: #38bdf8; display: flex; align-items: center; gap: 8px;">
              <span>⚡</span> Optimizador de Potencia Contratada & ICP (4.60 kW)
            </h3>
            <span style="font-size: 0.8rem; color: var(--text-muted);">Auditoría de Curva de Demanda Cuarto-Horaria & Respaldo Fox-ESS</span>
          </div>
          <span style="background: rgba(56, 189, 248, 0.15); color: #38bdf8; padding: 4px 12px; border-radius: 20px; font-weight: 700; font-size: 0.85rem; border: 1px solid #38bdf8;">
            Pico Máx: ${stats.max_peak_observed_kw || 3.58} kW
          </span>
        </div>

        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 12px; margin-bottom: 16px;">
          ${scenariosHtml}
        </div>

        <div style="background: rgba(56, 189, 248, 0.08); border-left: 4px solid #38bdf8; border-radius: 6px; padding: 12px 16px; font-size: 0.82rem; color: #cbd5e1; line-height: 1.4;">
          <strong>💡 Dictamen de la IA:</strong> ${opt.rationale || 'Mantener 4.60 kW es óptimo para máxima simultaneidad con Omoda 7 y Daikin.'}
        </div>
      </div>
    `;
  }
}

window.icpPowerOptimizer = new IcpPowerOptimizerUI();
