/**
 * proactive-advisor-ui.js
 * =======================
 * Componente UI para visualización en tiempo real del Asistente Proactivo de Hogar.
 */

class ProactiveAdvisorUI {
  constructor(containerId = 'proactive-advisor-container') {
    this.containerId = containerId;
    this.container = document.getElementById(containerId);
    this.alerts = [];
    this.init();
  }

  async init() {
    if (!this.container) return;
    await this.fetchAlerts();
    this.render();
  }

  async fetchAlerts() {
    try {
      const res = await fetch('/api/ai/proactive-alerts');
      const data = await res.json();
      this.alerts = data.alerts || [];
    } catch (e) {
      console.warn('Fallback a alertas locales:', e);
    }
  }

  render() {
    if (!this.container) return;

    this.container.innerHTML = `
      <div style="background: var(--bg-card); border: 1px solid rgba(245, 158, 11, 0.35); border-radius: var(--radius-lg); padding: 1.25rem; box-shadow: var(--shadow-card); display: flex; flex-direction: column; gap: 1rem;">
        
        <!-- Encabezado -->
        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.75rem;">
          <div style="display: flex; align-items: center; gap: 0.6rem;">
            <div style="font-size: 1.4rem; background: rgba(245, 158, 11, 0.15); width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; border-radius: var(--radius-md);">🔔</div>
            <div>
              <h3 style="font-size: 1.05rem; font-weight: 700; color: var(--text-primary);">Asistente Proactivo de Hogar & Recomendaciones en Vivo</h3>
              <div style="font-size: 0.75rem; color: var(--text-muted);">Alertas Accionables: Excedente Solar · Free-Cooling · Persianas · Batería Fox-ESS</div>
            </div>
          </div>
          <span class="badge-tag" style="background: rgba(245, 158, 11, 0.2); color: #f59e0b; font-weight: 700;">
            ${this.alerts.length} Avisos Activos
          </span>
        </div>

        <!-- Lista de Alertas -->
        <div style="display: flex; flex-direction: column; gap: 0.65rem;">
          ${this.alerts.length === 0 ? `
            <div style="color: var(--text-muted); font-size: 0.85rem; padding: 0.5rem;">No hay alertas pendientes en este momento. Sistema en equilibrio óptimo.</div>
          ` : this.alerts.map(a => `
            <div style="background: rgba(0,0,0,0.22); border-left: 4px solid ${a.priority === 'HIGH' ? '#10b981' : '#38bdf8'}; border-radius: var(--radius-sm); padding: 0.85rem; display: flex; flex-direction: column; gap: 0.35rem;">
              <div style="display: flex; justify-content: space-between; align-items: center;">
                <div style="font-size: 0.9rem; font-weight: 700; color: var(--text-primary); display: flex; align-items: center; gap: 0.4rem;">
                  <span>${a.emoji}</span> <span>${a.title}</span>
                </div>
                <span style="font-size: 0.7rem; color: var(--text-muted);">${a.timestamp}</span>
              </div>
              <div style="font-size: 0.82rem; color: var(--text-secondary); line-height: 1.4;">
                ${a.message}
              </div>
            </div>
          `).join('')}
        </div>

      </div>
    `;
  }
}

if (typeof window !== 'undefined') {
  window.ProactiveAdvisorUI = ProactiveAdvisorUI;
}
