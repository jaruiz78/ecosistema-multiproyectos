/**
 * Sistema de Notificaciones Inteligentes de Oportunidad Solar (Local Web Push & Toast)
 * Detecta picos de excedente solar, batería llena y horas óptimas de consumo en local
 * sin necesidad de servidores externos ni costes de mensajería.
 */

export class SolarPushNotifications {
  constructor(toastContainerId = 'solar-toast-container') {
    this.containerId = toastContainerId;
    this.enabled = true;
    this.lastNotifTime = 0;
    this.cooldownMs = 60000; // 1 minuto entre avisos para evitar spam
    this.lastSolarKw = 0;
    this.lastBatSoc = 0;

    this.init();
  }

  init() {
    let container = document.getElementById(this.containerId);
    if (!container) {
      container = document.createElement('div');
      container.id = this.containerId;
      container.style.cssText = `
        position: fixed;
        bottom: 1.5rem;
        right: 1.5rem;
        z-index: 9999;
        display: flex;
        flex-direction: column;
        gap: 0.5rem;
        max-width: 380px;
        pointer-events: none;
      `;
      document.body.appendChild(container);
    }
  }

  evaluateTelemetry(data) {
    if (!this.enabled) return;
    const now = Date.now();
    if (now - this.lastNotifTime < this.cooldownMs) return;

    const solarW = data.solar_total_w || 0;
    const solarKw = solarW / 1000.0;
    const batSoc = data.battery ? data.battery.soc_percent : 100;
    const gridExportW = data.grid ? (data.grid.export_power_kw ? data.grid.export_power_kw * 1000 : 0) : 0;
    const surplusKw = gridExportW / 1000.0;

    // 1. Alerta de Batería Llena + Excedente Coche
    if (batSoc >= 99 && solarKw > 2.0 && this.lastBatSoc < 99) {
      this.showToast('🔋 Batería Fox-ESS al 100%', `Generando ${solarKw.toFixed(1)} kW de sol. Es el momento perfecto para enchufar el Omoda 7 a coste 0.00 €.`, 'info');
      this.lastNotifTime = now;
    }
    // 2. Alerta de Excedente Solar Alto (> 2.5 kW)
    else if (surplusKw > 2.5 && this.lastSolarKw <= 2.5) {
      this.showToast('☀️ ¡Gran Excedente Solar!', `Tienes +${surplusKw.toFixed(1)} kW libres vertiendo a red. Puedes poner lavadora o lavavajillas gratis.`, 'success');
      this.lastNotifTime = now;
    }

    this.lastSolarKw = solarKw;
    this.lastBatSoc = batSoc;
  }

  showToast(title, message, type = 'info') {
    const container = document.getElementById(this.containerId);
    if (!container) return;

    const toast = document.createElement('div');
    toast.style.cssText = `
      background: rgba(15, 23, 42, 0.95);
      border: 1px solid ${type === 'success' ? 'rgba(16, 185, 129, 0.5)' : 'rgba(56, 189, 248, 0.5)'};
      border-left: 4px solid ${type === 'success' ? 'var(--color-real)' : 'var(--color-east)'};
      border-radius: var(--radius-md);
      padding: 0.85rem 1.1rem;
      box-shadow: 0 10px 25px rgba(0,0,0,0.5);
      color: var(--text-primary);
      font-size: 0.82rem;
      display: flex;
      flex-direction: column;
      gap: 0.25rem;
      pointer-events: auto;
      animation: slideInToast 0.3s ease-out;
      transition: opacity 0.3s ease, transform 0.3s ease;
    `;

    toast.innerHTML = `
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <strong style="color: ${type === 'success' ? 'var(--color-real)' : 'var(--color-east)'}; font-size: 0.88rem;">${title}</strong>
        <button style="background: transparent; border: none; color: var(--text-muted); cursor: pointer; font-size: 1rem;" onclick="this.parentElement.parentElement.remove()">✕</button>
      </div>
      <div style="color: var(--text-secondary); line-height: 1.35;">${message}</div>
    `;

    container.appendChild(toast);

    // Desaparición automática tras 7 segundos
    setTimeout(() => {
      toast.style.opacity = '0';
      toast.style.transform = 'translateY(10px)';
      setTimeout(() => toast.remove(), 300);
    }, 7000);
  }
}
