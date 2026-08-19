/**
 * daikin-iot-ui.js
 * Componente UI para Centro de Control y Automatización IoT Daikin (Faikin ESP32 S21)
 * Ecosistema Solar Tocina - Los Rosales
 */

export class DaikinIoTUiManager {
  constructor(containerId = 'daikin-iot-container') {
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
      const res = await fetch('/api/daikin/iot/status');
      if (res.ok) {
        this.status = await res.json();
      }
    } catch (e) {
      console.warn('[DaikinIoTUi] Error fetching status:', e);
    }
  }

  setupAutoRefresh() {
    if (this.pollInterval) clearInterval(this.pollInterval);
    this.pollInterval = setInterval(async () => {
      await this.fetchStatus();
      this.updateView();
    }, 15000);
  }

  async togglePower(unitId, currentPower) {
    try {
      const targetUnit = this.status?.units?.find(u => u.id === unitId);
      const newPower = !currentPower;
      const targetTemp = targetUnit?.status?.target_temp_c || 24.0;
      const mode = targetUnit?.status?.mode || 'cool';

      const res = await fetch('/api/daikin/iot/control', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          unit_id: unitId,
          power_on: newPower,
          target_temp_c: targetTemp,
          mode: mode
        })
      });
      if (res.ok) {
        await this.fetchStatus();
        this.updateView();
      }
    } catch (e) {
      alert(`Error conmutando Daikin: ${e.message}`);
    }
  }

  async setTemp(unitId, temp) {
    try {
      const targetUnit = this.status?.units?.find(u => u.id === unitId);
      const res = await fetch('/api/daikin/iot/control', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          unit_id: unitId,
          power_on: targetUnit?.status?.power_on || true,
          target_temp_c: parseFloat(temp),
          mode: targetUnit?.status?.mode || 'cool'
        })
      });
      if (res.ok) {
        await this.fetchStatus();
        this.updateView();
      }
    } catch (e) {
      console.error(e);
    }
  }

  async setMode(unitId, mode) {
    try {
      const targetUnit = this.status?.units?.find(u => u.id === unitId);
      const res = await fetch('/api/daikin/iot/control', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          unit_id: unitId,
          power_on: targetUnit?.status?.power_on || true,
          target_temp_c: targetUnit?.status?.target_temp_c || 24.0,
          mode: mode
        })
      });
      if (res.ok) {
        await this.fetchStatus();
        this.updateView();
      }
    } catch (e) {
      console.error(e);
    }
  }

  async setVane(unitId, vane) {
    try {
      const targetUnit = this.status?.units?.find(u => u.id === unitId);
      const res = await fetch('/api/daikin/iot/control', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          unit_id: unitId,
          power_on: targetUnit?.status?.power_on || true,
          target_temp_c: targetUnit?.status?.target_temp_c || 24.0,
          mode: targetUnit?.status?.mode || 'cool',
          fan_direction: vane
        })
      });
      if (res.ok) {
        await this.fetchStatus();
        this.updateView();
      }
    } catch (e) {
      console.error(e);
    }
  }

  async toggleAutomation(enabled) {
    try {
      const res = await fetch('/api/daikin/iot/config', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ auto_automation_enabled: enabled })
      });
      if (res.ok) {
        await this.fetchStatus();
        this.updateView();
      }
    } catch (e) {
      console.error(e);
    }
  }

  async updateIp(unitId, newIp) {
    try {
      const res = await fetch('/api/daikin/iot/config', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ unit_id: unitId, ip: newIp, hardware_type: 'faikin_esp32' })
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
    const units = this.status?.units || [];
    const autoEnabled = this.status?.auto_automation_enabled ?? true;
    const lastAction = this.status?.last_action_taken || 'Preparado para asignación de módulos Faikin ESP32';

    this.container.innerHTML = `
      <div class="section-box" style="border: 1px solid rgba(14, 165, 233, 0.35); box-shadow: 0 0 20px rgba(14, 165, 233, 0.08); margin-bottom: 1.5rem;">
        <div class="section-header" style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 0.75rem;">
          <div>
            <div style="display: flex; align-items: center; gap: 0.5rem;">
              <span style="font-size: 1.4rem;">❄️</span>
              <h2 style="margin: 0; font-size: 1.15rem;">Centro de Climatización IoT Daikin & Módulos Faikin S21</h2>
            </div>
            <div class="sub-desc" style="margin-top: 0.25rem;">
              Automatización estacional inteligente (Pre-Cooling verano a 0.00 € / Pre-Heating invierno con lamas a 60°)
            </div>
          </div>
          <div style="display: flex; align-items: center; gap: 0.6rem;">
            <label style="display: flex; align-items: center; gap: 0.4rem; font-size: 0.8rem; font-weight: 700; color: var(--text-secondary); cursor: pointer;">
              <input type="checkbox" id="daikin-auto-toggle" ${autoEnabled ? 'checked' : ''} style="accent-color: #0ea5e9;">
              <span>Auto-Precooling / Preheating IA</span>
            </label>
            <span class="badge-tag" style="background: rgba(14, 165, 233, 0.15); color: #38bdf8; font-weight: 700;">
              Protocolo S21 Faikin
            </span>
          </div>
        </div>

        <!-- Banner de Última Acción Automática -->
        <div style="background: rgba(14, 165, 233, 0.08); border: 1px solid rgba(14, 165, 233, 0.25); border-radius: var(--radius-md); padding: 0.65rem 0.9rem; margin: 1rem 0; font-size: 0.8rem; color: var(--text-secondary); display: flex; align-items: center; gap: 0.5rem;">
          <span style="color: #38bdf8; font-weight: 800;">🤖 Estado del Automatismo:</span>
          <span id="daikin-last-action-lbl">${lastAction}</span>
        </div>

        <!-- Cuadrícula de Unidades Daikin -->
        <div id="daikin-units-grid" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 1rem;">
          ${this.renderUnitsHtml(units)}
        </div>
      </div>
    `;

    this.attachEventListeners();
  }

  renderUnitsHtml(units) {
    return units.map(u => {
      const st = u.status || {};
      const isOnline = st.online || Boolean(u.ip);
      const isPowerOn = st.power_on;
      const mode = st.mode || 'cool';
      const temp = st.target_temp_c || 24.0;
      const vane = st.fan_direction || 'swing';

      return `
        <div class="card-box" data-unit-id="${u.id}" style="background: var(--bg-card); border: 1px solid ${isPowerOn ? '#0ea5e9' : 'var(--border-subtle)'}; border-radius: var(--radius-md); padding: 1rem; display: flex; flex-direction: column; justify-content: space-between; gap: 0.85rem;">
          
          <!-- Encabezado de la Unidad -->
          <div style="display: flex; justify-content: space-between; align-items: flex-start;">
            <div>
              <div style="font-size: 0.95rem; font-weight: 800; color: var(--text-primary); display: flex; align-items: center; gap: 0.4rem;">
                <span>${u.id === 'daikin_salon' ? '🛋️' : '🛏️'}</span>
                <span>${u.name}</span>
              </div>
              <div style="font-size: 0.72rem; color: var(--text-muted); margin-top: 0.2rem;">
                ${isOnline ? `IP: <strong style="color: #38bdf8;">${u.ip}</strong> (Faikin Online)` : '<span style="color: #f59e0b;">⚪ En Espera de Módulo Físico (Modo Emulado)</span>'}
              </div>
            </div>
            <button class="daikin-power-btn" data-unit-id="${u.id}" data-power="${isPowerOn}" style="background: ${isPowerOn ? '#0ea5e9' : 'rgba(255,255,255,0.08)'}; color: #ffffff; border: none; border-radius: 9999px; padding: 0.4rem 0.85rem; font-size: 0.78rem; font-weight: 800; cursor: pointer; transition: all 0.2s ease;">
              ${isPowerOn ? '🟢 ENCENDIDO' : '⚪ APAGADO'}
            </button>
          </div>

          <!-- Selector de Temperatura y Modo -->
          <div style="background: rgba(0,0,0,0.2); padding: 0.75rem; border-radius: var(--radius-sm); display: flex; flex-direction: column; gap: 0.6rem;">
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span style="font-size: 0.75rem; color: var(--text-secondary); font-weight: 700;">CONSIGNA:</span>
              <div style="display: flex; align-items: center; gap: 0.5rem;">
                <button class="temp-adjust-btn" data-unit-id="${u.id}" data-delta="-0.5" style="background: rgba(255,255,255,0.1); border: 1px solid var(--border-subtle); color: #fff; border-radius: 4px; width: 26px; height: 26px; font-weight: 800; cursor: pointer;">-</button>
                <span style="font-size: 1.15rem; font-weight: 800; color: #38bdf8; font-family: var(--font-mono);">${temp.toFixed(1)} °C</span>
                <button class="temp-adjust-btn" data-unit-id="${u.id}" data-delta="0.5" style="background: rgba(255,255,255,0.1); border: 1px solid var(--border-subtle); color: #fff; border-radius: 4px; width: 26px; height: 26px; font-weight: 800; cursor: pointer;">+</button>
              </div>
            </div>

            <!-- Modos: Frío / Calor / Ventilador / Deshum -->
            <div style="display: flex; gap: 0.35rem; justify-content: space-between;">
              <button class="daikin-mode-btn ${mode === 'cool' ? 'active' : ''}" data-unit-id="${u.id}" data-mode="cool" style="flex: 1; padding: 0.3rem; font-size: 0.72rem; border-radius: 4px; border: 1px solid ${mode === 'cool' ? '#0ea5e9' : 'var(--border-subtle)'}; background: ${mode === 'cool' ? 'rgba(14, 165, 233, 0.25)' : 'transparent'}; color: ${mode === 'cool' ? '#38bdf8' : 'var(--text-muted)'}; font-weight: 700; cursor: pointer;">❄️ Frío</button>
              <button class="daikin-mode-btn ${mode === 'heat' ? 'active' : ''}" data-unit-id="${u.id}" data-mode="heat" style="flex: 1; padding: 0.3rem; font-size: 0.72rem; border-radius: 4px; border: 1px solid ${mode === 'heat' ? '#f59e0b' : 'var(--border-subtle)'}; background: ${mode === 'heat' ? 'rgba(245, 158, 11, 0.25)' : 'transparent'}; color: ${mode === 'heat' ? '#fbbf24' : 'var(--text-muted)'}; font-weight: 700; cursor: pointer;">☀️ Calor</button>
              <button class="daikin-mode-btn ${mode === 'fan' ? 'active' : ''}" data-unit-id="${u.id}" data-mode="fan" style="flex: 1; padding: 0.3rem; font-size: 0.72rem; border-radius: 4px; border: 1px solid ${mode === 'fan' ? '#10b981' : 'var(--border-subtle)'}; background: ${mode === 'fan' ? 'rgba(16, 185, 129, 0.25)' : 'transparent'}; color: ${mode === 'fan' ? '#34d399' : 'var(--text-muted)'}; font-weight: 700; cursor: pointer;">💨 Vent</button>
              <button class="daikin-mode-btn ${mode === 'dry' ? 'active' : ''}" data-unit-id="${u.id}" data-mode="dry" style="flex: 1; padding: 0.3rem; font-size: 0.72rem; border-radius: 4px; border: 1px solid ${mode === 'dry' ? '#c084fc' : 'var(--border-subtle)'}; background: ${mode === 'dry' ? 'rgba(192, 132, 252, 0.25)' : 'transparent'}; color: ${mode === 'dry' ? '#c084fc' : 'var(--text-muted)'}; font-weight: 700; cursor: pointer;">💧 Dry</button>
            </div>

            <!-- Dirección de lamas deflectoras -->
            <div style="display: flex; justify-content: space-between; align-items: center; font-size: 0.72rem; border-top: 1px solid var(--border-subtle); padding-top: 0.4rem;">
              <span style="color: var(--text-muted);">Lamas Deflectoras:</span>
              <select class="daikin-vane-select" data-unit-id="${u.id}" style="background: var(--bg-elevated); color: var(--text-primary); border: 1px solid var(--border-subtle); border-radius: 4px; font-size: 0.72rem; padding: 0.15rem 0.4rem;">
                <option value="swing" ${vane === 'swing' ? 'selected' : ''}>Oscilación Continua</option>
                <option value="floor_60" ${vane === 'floor_60' ? 'selected' : ''}>60° Hacia el Suelo (Invierno)</option>
                <option value="horizontal" ${vane === 'horizontal' ? 'selected' : ''}>Horizontal (Verano / Techo)</option>
              </select>
            </div>
          </div>

          <!-- Asignación Rápida de IP para cuando llegue el módulo de Amazon -->
          <div style="display: flex; align-items: center; justify-content: space-between; font-size: 0.7rem; color: var(--text-muted);">
            <span>IP Local:</span>
            <input type="text" class="daikin-ip-input" data-unit-id="${u.id}" value="${u.ip || ''}" placeholder="Ej: 192.168.1.145" style="background: rgba(0,0,0,0.3); border: 1px solid var(--border-subtle); color: var(--text-primary); border-radius: 4px; padding: 0.2rem 0.4rem; font-size: 0.72rem; width: 130px;">
          </div>

        </div>
      `;
    }).join('');
  }

  attachEventListeners() {
    if (!this.container) return;

    // Toggle Automation Checkbox
    const autoCb = this.container.querySelector('#daikin-auto-toggle');
    if (autoCb) {
      autoCb.addEventListener('change', (e) => {
        this.toggleAutomation(e.target.checked);
      });
    }

    // Power buttons
    this.container.querySelectorAll('.daikin-power-btn').forEach(btn => {
      btn.addEventListener('click', () => {
        const uId = btn.dataset.unitId;
        const curPower = btn.dataset.power === 'true';
        this.togglePower(uId, curPower);
      });
    });

    // Temp adjust buttons
    this.container.querySelectorAll('.temp-adjust-btn').forEach(btn => {
      btn.addEventListener('click', () => {
        const uId = btn.dataset.unitId;
        const delta = parseFloat(btn.dataset.delta);
        const targetUnit = this.status?.units?.find(u => u.id === uId);
        const curTemp = targetUnit?.status?.target_temp_c || 24.0;
        this.setTemp(uId, curTemp + delta);
      });
    });

    // Mode buttons
    this.container.querySelectorAll('.daikin-mode-btn').forEach(btn => {
      btn.addEventListener('click', () => {
        const uId = btn.dataset.unitId;
        const mode = btn.dataset.mode;
        this.setMode(uId, mode);
      });
    });

    // Vane selects
    this.container.querySelectorAll('.daikin-vane-select').forEach(sel => {
      sel.addEventListener('change', (e) => {
        const uId = sel.dataset.unitId;
        this.setVane(uId, e.target.value);
      });
    });

    // IP inputs blur
    this.container.querySelectorAll('.daikin-ip-input').forEach(inp => {
      inp.addEventListener('change', (e) => {
        const uId = inp.dataset.unitId;
        this.updateIp(uId, e.target.value);
      });
    });
  }

  updateView() {
    const units = this.status?.units || [];
    const grid = this.container?.querySelector('#daikin-units-grid');
    if (grid) {
      grid.innerHTML = this.renderUnitsHtml(units);
      this.attachEventListeners();
    }
    const lastActionLbl = this.container?.querySelector('#daikin-last-action-lbl');
    if (lastActionLbl && this.status?.last_action_taken) {
      lastActionLbl.textContent = this.status.last_action_taken;
    }
  }
}
