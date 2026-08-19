/**
 * smart-plugs-ui.js
 * Componente UI para Gestor de Enchufes Inteligentes & Submedición Aislada
 * Ecosistema Solar Tocina - Los Rosales
 */

export class SmartPlugsUiManager {
  constructor(containerId = 'smart-plugs-container') {
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
      const res = await fetch('/api/smart-plugs/status');
      if (res.ok) {
        this.status = await res.json();
      }
    } catch (e) {
      console.warn('[SmartPlugsUi] Error fetching status:', e);
    }
  }

  setupAutoRefresh() {
    if (this.pollInterval) clearInterval(this.pollInterval);
    this.pollInterval = setInterval(async () => {
      await this.fetchStatus();
      this.updateView();
    }, 15000);
  }

  async togglePlug(plugId, currentPower) {
    try {
      const res = await fetch('/api/smart-plugs/toggle', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ plug_id: plugId, power_on: !currentPower })
      });
      if (res.ok) {
        await this.fetchStatus();
        this.updateView();
      }
    } catch (e) {
      alert(`Error conmutando enchufe: ${e.message}`);
    }
  }

  async toggleAutoDispatch(enabled) {
    try {
      const res = await fetch('/api/smart-plugs/config', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ auto_dispatch_enabled: enabled })
      });
      if (res.ok) {
        await this.fetchStatus();
        this.updateView();
      }
    } catch (e) {
      console.error(e);
    }
  }

  async updatePlugIp(plugId, ip) {
    try {
      const res = await fetch('/api/smart-plugs/config', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ plug_id: plugId, ip: ip, hardware_type: 'shelly_plus_1pm' })
      });
      if (res.ok) {
        await this.fetchStatus();
        this.updateView();
      }
    } catch (e) {
      console.error(e);
    }
  }

  render() {
    if (!this.container) return;
    const plugs = this.status?.plugs || [];
    const autoEnabled = this.status?.auto_dispatch_enabled ?? true;
    const summary = this.status?.last_dispatch_summary || 'Enchufes listos para asignación de IPs locales';

    this.container.innerHTML = `
      <div class="section-box" style="border: 1px solid rgba(245, 158, 11, 0.35); box-shadow: 0 0 20px rgba(245, 158, 11, 0.08); margin-bottom: 1.5rem;">
        <div class="section-header" style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 0.75rem;">
          <div>
            <div style="display: flex; align-items: center; gap: 0.5rem;">
              <span style="font-size: 1.4rem;">🔌</span>
              <h2 style="margin: 0; font-size: 1.15rem;">Gestor de Enchufes Inteligentes & Submedición (Shelly / Tuya)</h2>
            </div>
            <div class="sub-desc" style="margin-top: 0.25rem;">
              Encendido autónomo por excedentes solares y aislamiento contable de recargas del Omoda 7 SHS
            </div>
          </div>
          <div style="display: flex; align-items: center; gap: 0.6rem;">
            <label style="display: flex; align-items: center; gap: 0.4rem; font-size: 0.8rem; font-weight: 700; color: var(--text-secondary); cursor: pointer;">
              <input type="checkbox" id="plugs-auto-toggle" ${autoEnabled ? 'checked' : ''} style="accent-color: #f59e0b;">
              <span>Despacho Solar Automático</span>
            </label>
            <span class="badge-tag" style="background: rgba(245, 158, 11, 0.15); color: #fbbf24; font-weight: 700;">
              Shelly Plus 1PM Ready
            </span>
          </div>
        </div>

        <!-- Banner de Estado de Despacho -->
        <div style="background: rgba(245, 158, 11, 0.08); border: 1px solid rgba(245, 158, 11, 0.25); border-radius: var(--radius-md); padding: 0.65rem 0.9rem; margin: 1rem 0; font-size: 0.8rem; color: var(--text-secondary); display: flex; align-items: center; gap: 0.5rem;">
          <span style="color: #fbbf24; font-weight: 800;">⚡ Estado de Despacho:</span>
          <span id="plugs-summary-lbl">${summary}</span>
        </div>

        <!-- Cuadrícula de Enchufes -->
        <div id="smart-plugs-grid" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap: 1rem;">
          ${this.renderPlugsHtml(plugs)}
        </div>
      </div>
    `;

    this.attachEventListeners();
  }

  renderPlugsHtml(plugs) {
    return plugs.map(p => {
      const st = p.state || {};
      const isOnline = st.online || Boolean(p.ip);
      const isPowerOn = st.power_on;
      const rule = p.automation_rule || {};

      return `
        <div class="card-box" data-plug-id="${p.id}" style="background: var(--bg-card); border: 1px solid ${isPowerOn ? '#f59e0b' : 'var(--border-subtle)'}; border-radius: var(--radius-md); padding: 1rem; display: flex; flex-direction: column; justify-content: space-between; gap: 0.75rem;">
          
          <div style="display: flex; justify-content: space-between; align-items: flex-start;">
            <div>
              <div style="font-size: 0.95rem; font-weight: 800; color: var(--text-primary); display: flex; align-items: center; gap: 0.4rem;">
                <span>${p.icon || '🔌'}</span>
                <span>${p.name}</span>
              </div>
              <div style="font-size: 0.72rem; color: var(--text-muted); margin-top: 0.2rem;">
                ${isOnline ? `IP: <strong style="color: #fbbf24;">${p.ip}</strong> (Shelly Online)` : '<span style="color: #f59e0b;">⚪ En Espera de Enchufe Físico</span>'}
              </div>
            </div>
            <button class="plug-power-btn" data-plug-id="${p.id}" data-power="${isPowerOn}" style="background: ${isPowerOn ? '#f59e0b' : 'rgba(255,255,255,0.08)'}; color: ${isPowerOn ? '#000' : '#fff'}; border: none; border-radius: 9999px; padding: 0.4rem 0.85rem; font-size: 0.78rem; font-weight: 800; cursor: pointer; transition: all 0.2s ease;">
              ${isPowerOn ? '🟢 ACTIVADO' : '⚪ APAGADO'}
            </button>
          </div>

          <!-- Métrica de Potencia y Energía Submedida -->
          <div style="background: rgba(0,0,0,0.25); padding: 0.6rem 0.8rem; border-radius: var(--radius-sm); display: flex; justify-content: space-between; align-items: center;">
            <div>
              <div style="font-size: 0.68rem; color: var(--text-muted); text-transform: uppercase;">Potencia Instantánea</div>
              <div style="font-size: 1.1rem; font-weight: 800; color: ${isPowerOn ? '#fbbf24' : 'var(--text-muted)'}; font-family: var(--font-mono);">
                ${st.current_power_w.toFixed(0)} W
              </div>
            </div>
            <div style="text-align: right;">
              <div style="font-size: 0.68rem; color: var(--text-muted); text-transform: uppercase;">Energía Hoy</div>
              <div style="font-size: 0.95rem; font-weight: 800; color: #10b981; font-family: var(--font-mono);">
                ${st.today_energy_kwh.toFixed(2)} kWh
              </div>
            </div>
          </div>

          <!-- Regla de Automatización Solar -->
          <div style="font-size: 0.72rem; color: var(--text-secondary); line-height: 1.35; background: rgba(255,255,255,0.03); padding: 0.5rem; border-radius: 4px; border: 1px solid var(--border-subtle);">
            <strong style="color: #fbbf24;">Regla:</strong> ${rule.description || 'Control manual'}
          </div>

          <!-- Asignación Rápida de IP -->
          <div style="display: flex; align-items: center; justify-content: space-between; font-size: 0.7rem; color: var(--text-muted);">
            <span>IP Local:</span>
            <input type="text" class="plug-ip-input" data-plug-id="${p.id}" value="${p.ip || ''}" placeholder="Ej: 192.168.1.180" style="background: rgba(0,0,0,0.3); border: 1px solid var(--border-subtle); color: var(--text-primary); border-radius: 4px; padding: 0.2rem 0.4rem; font-size: 0.72rem; width: 130px;">
          </div>

        </div>
      `;
    }).join('');
  }

  attachEventListeners() {
    if (!this.container) return;

    const autoCb = this.container.querySelector('#plugs-auto-toggle');
    if (autoCb) {
      autoCb.addEventListener('change', (e) => {
        this.toggleAutoDispatch(e.target.checked);
      });
    }

    this.container.querySelectorAll('.plug-power-btn').forEach(btn => {
      btn.addEventListener('click', () => {
        const pId = btn.dataset.plugId;
        const curPower = btn.dataset.power === 'true';
        this.togglePlug(pId, curPower);
      });
    });

    this.container.querySelectorAll('.plug-ip-input').forEach(inp => {
      inp.addEventListener('change', (e) => {
        const pId = inp.dataset.plugId;
        this.updatePlugIp(pId, e.target.value);
      });
    });
  }

  updateView() {
    const plugs = this.status?.plugs || [];
    const grid = this.container?.querySelector('#smart-plugs-grid');
    if (grid) {
      grid.innerHTML = this.renderPlugsHtml(plugs);
      this.attachEventListeners();
    }
    const summaryLbl = this.container?.querySelector('#plugs-summary-lbl');
    if (summaryLbl && this.status?.last_dispatch_summary) {
      summaryLbl.textContent = this.status.last_dispatch_summary;
    }
  }
}
