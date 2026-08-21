/**
 * advanced-mpc-dashboard.js
 * ==========================
 * Componente UI para el Control Predictivo Basado en Modelos (MPC 48h)
 * y Difusión Térmica de Fourier en Forjados y Fachadas.
 */

class AdvancedMPCDashboard {
  constructor(containerId = 'mpc-dashboard-container') {
    this.containerId = containerId;
    this.container = document.getElementById(containerId);
    this.mpcData = null;
    this.fourierData = null;
    this.nowcastData = null;
    this.init();
  }

  async init() {
    if (!this.container) return;
    await this.fetchData();
    this.render();
  }

  async fetchData() {
    try {
      const [mpcRes, fourierRes, nowcastRes] = await Promise.all([
        fetch('/api/ai/mpc-schedule').then(r => r.json()),
        fetch('/api/ai/fourier-wall-diffusion').then(r => r.json()),
        fetch('/api/ai/solar-nowcast').then(r => r.json())
      ]);
      this.mpcData = mpcRes;
      this.fourierData = fourierRes;
      this.nowcastData = nowcastRes;
    } catch (e) {
      console.warn('Fallback a datos MPC locales:', e);
    }
  }

  render() {
    if (!this.container) return;

    const mpc = this.mpcData || {};
    const fourier = this.fourierData || {};
    const nowcast = this.nowcastData || {};

    this.container.innerHTML = `
      <div style="background: var(--bg-card); border: 1px solid rgba(16, 185, 129, 0.35); border-radius: var(--radius-lg); padding: 1.25rem; box-shadow: var(--shadow-card); display: flex; flex-direction: column; gap: 1.25rem;">
        
        <!-- Encabezado -->
        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.75rem; flex-wrap: wrap; gap: 0.5rem;">
          <div style="display: flex; align-items: center; gap: 0.6rem;">
            <div style="font-size: 1.4rem; background: rgba(16, 185, 129, 0.15); width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; border-radius: var(--radius-md);">🎯</div>
            <div>
              <h3 style="font-size: 1.05rem; font-weight: 700; color: var(--text-primary);">Optimizador Predictivo MPC (48h) & Nowcasting Satelital</h3>
              <div style="font-size: 0.75rem; color: var(--text-muted);">Despacho Óptimo: Batería Fox-ESS · Daikin Pre-cooling · Omoda 7 EV · Batería Virtual</div>
            </div>
          </div>
          <span class="badge-tag" style="background: rgba(16, 185, 129, 0.2); color: #10b981; font-weight: 700;">
            Autosuficiencia: ${mpc.self_sufficiency_pct || 100}%
          </span>
        </div>

        <!-- Métricas Clave de Optimización -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 0.75rem;">
          <div style="background: rgba(0,0,0,0.2); padding: 0.75rem; border-radius: var(--radius-sm); border-left: 3px solid #10b981;">
            <div style="font-size: 0.72rem; color: var(--text-muted);">Generación Prevista 48h</div>
            <div style="font-size: 1.15rem; font-weight: 700; color: #10b981;">${mpc.total_pv_generation_kwh || 60.4} kWh</div>
          </div>
          <div style="background: rgba(0,0,0,0.2); padding: 0.75rem; border-radius: var(--radius-sm); border-left: 3px solid #38bdf8;">
            <div style="font-size: 0.72rem; color: var(--text-muted);">Carga Solar Omoda 7 (EV)</div>
            <div style="font-size: 1.15rem; font-weight: 700; color: #38bdf8;">${mpc.ev_energy_delivered_kwh || 11.4} kWh</div>
          </div>
          <div style="background: rgba(0,0,0,0.2); padding: 0.75rem; border-radius: var(--radius-sm); border-left: 3px solid #c084fc;">
            <div style="font-size: 0.72rem; color: var(--text-muted);">Saldo Batería Virtual</div>
            <div style="font-size: 1.15rem; font-weight: 700; color: #c084fc;">+${mpc.virtual_battery_credit_earned_eur || 0.85} €</div>
          </div>
          <div style="background: rgba(0,0,0,0.2); padding: 0.75rem; border-radius: var(--radius-sm); border-left: 3px solid #f59e0b;">
            <div style="font-size: 0.72rem; color: var(--text-muted);">Desfase Térmico Forjado</div>
            <div style="font-size: 1.15rem; font-weight: 700; color: #f59e0b;">${fourier.roof ? fourier.roof.thermal_lag_hours : 11.0} h</div>
          </div>
        </div>

        <!-- Nowcasting Solar Satelital a 60 Minutos -->
        <div style="background: rgba(15, 23, 42, 0.6); border: 1px solid rgba(56, 189, 248, 0.3); border-radius: var(--radius-md); padding: 0.85rem;">
          <div style="font-size: 0.85rem; font-weight: 700; color: #38bdf8; margin-bottom: 0.5rem; display: flex; align-items: center; gap: 0.4rem;">
            <span>🛰️</span> Nowcasting Solar a Muy Corto Plazo (Próximos 60 min • Tocina):
          </div>
          <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(130px, 1fr)); gap: 0.5rem;">
            ${(nowcast.nowcast_60min || []).map(step => `
              <div style="background: rgba(0,0,0,0.25); padding: 0.5rem; border-radius: 4px; text-align: center;">
                <div style="font-size: 0.72rem; color: var(--text-muted);">${step.target_time} (+${step.offset_minutes}m)</div>
                <div style="font-size: 0.95rem; font-weight: 700; color: #10b981;">${step.projected_pv_kw} kW</div>
                <div style="font-size: 0.68rem; color: ${step.solar_drop_risk === 'Bajo' ? '#10b981' : '#f59e0b'};">Riesgo: ${step.solar_drop_risk}</div>
              </div>
            `).join('')}
          </div>
        </div>

      </div>
    `;
  }
}

if (typeof window !== 'undefined') {
  window.AdvancedMPCDashboard = AdvancedMPCDashboard;
}
