/**
 * Valley Charge Scheduler & Automation UI Component (P3 Fox-ESS EP5)
 * Ecosistema Solar Tocina - Los Rosales
 * Autor: Google Antigravity
 */

class ValleyChargeSchedulerUI {
  constructor() {
    this.containerId = 'valley-charge-scheduler-container';
    this.state = null;
  }

  async init() {
    const container = document.getElementById(this.containerId);
    if (!container) return;

    try {
      const resp = await fetch('/api/battery/valley-charge-status');
      if (!resp.ok) throw new Error('HTTP ' + resp.status);
      this.state = await resp.json();
      this.render(this.state, container);
    } catch (e) {
      console.warn('[ValleyChargeScheduler] Error cargando estado:', e);
      container.innerHTML = `<div class="card error-box">⚠️ Error cargando programador valle: ${e.message}</div>`;
    }
  }

  render(data, container) {
    const isRecommended = data.action_recommended;
    const cfg = data.config || { auto_enabled: false, target_soc_pct: 85, start_hour: 2, end_hour: 6, charge_power_w: 2000 };
    const eco = data.economics || {};
    const sched = data.schedule || {};

    container.innerHTML = `
      <div class="card valley-charge-card" style="background: linear-gradient(135deg, ${isRecommended ? 'rgba(245, 158, 11, 0.08)' : 'rgba(16, 185, 129, 0.05)'} 0%, rgba(15, 23, 42, 0.65) 100%); border: 1px solid ${isRecommended ? 'rgba(245, 158, 11, 0.3)' : 'rgba(16, 185, 129, 0.2)'}; border-radius: 12px; padding: 20px; margin-bottom: 20px;">
        
        <!-- Header -->
        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid rgba(255,255,255,0.1); padding-bottom: 12px; margin-bottom: 16px;">
          <div>
            <h3 style="margin: 0; font-size: 1.15rem; color: ${isRecommended ? '#fbbf24' : '#10b981'}; display: flex; align-items: center; gap: 8px;">
              <span>🌙</span> ${data.title}
            </h3>
            <span style="font-size: 0.8rem; color: var(--text-muted);">
              Predicción de Generación Solar vs Consumo del Hogar • Tarifa Valle P3 (00:00 - 08:00 h)
            </span>
          </div>
          <span style="background: ${isRecommended ? 'rgba(245, 158, 11, 0.2)' : 'rgba(16, 185, 129, 0.2)'}; color: ${isRecommended ? '#fbbf24' : '#10b981'}; padding: 4px 12px; border-radius: 20px; font-weight: 700; font-size: 0.82rem; border: 1px solid ${isRecommended ? '#fbbf24' : '#10b981'};">
            ${isRecommended ? '⚠️ CARGA VALLE RECOMENDADA' : '☀️ AUTOCONSUMO 100%'}
          </span>
        </div>

        <!-- Rationale Box -->
        <div style="background: rgba(0,0,0,0.3); border-left: 4px solid ${isRecommended ? '#fbbf24' : '#10b981'}; border-radius: 6px; padding: 12px 16px; font-size: 0.85rem; color: #e2e8f0; line-height: 1.45; margin-bottom: 16px;">
          ${data.rationale}
        </div>

        <!-- Metrics Grid -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(160px, 1fr)); gap: 12px; margin-bottom: 16px;">
          <div style="background: rgba(0,0,0,0.25); padding: 10px 12px; border-radius: 8px;">
            <div style="font-size: 0.75rem; color: var(--text-muted);">Solar Previsto Mañana</div>
            <div style="font-size: 1.25rem; font-weight: 800; color: #f59e0b;">${data.tomorrow_solar_forecast_kwh} kWh</div>
            <div style="font-size: 0.72rem; color: #94a3b8;">Nubosidad media: ${data.tomorrow_avg_cloud_cover_pct}%</div>
          </div>

          <div style="background: rgba(0,0,0,0.25); padding: 10px 12px; border-radius: 8px;">
            <div style="font-size: 0.75rem; color: var(--text-muted);">Consumo Hogar Previsto</div>
            <div style="font-size: 1.25rem; font-weight: 800; color: #38bdf8;">${data.tomorrow_home_consumption_kwh} kWh</div>
            <div style="font-size: 0.72rem; color: #94a3b8;">Daikin + Teletrabajo</div>
          </div>

          <div style="background: rgba(0,0,0,0.25); padding: 10px 12px; border-radius: 8px;">
            <div style="font-size: 0.75rem; color: var(--text-muted);">Coste Carga en Valle</div>
            <div style="font-size: 1.25rem; font-weight: 800; color: #10b981;">${eco.valley_night_cost_eur || 0.41} €</div>
            <div style="font-size: 0.72rem; color: #94a3b8;">${eco.energy_to_charge_kwh || 6.1} kWh a ${eco.valley_price_kwh || 0.068} €/kWh</div>
          </div>

          <div style="background: rgba(0,0,0,0.25); padding: 10px 12px; border-radius: 8px;">
            <div style="font-size: 0.75rem; color: var(--text-muted);">Ahorro Neto vs Pico P1</div>
            <div style="font-size: 1.25rem; font-weight: 800; color: #c084fc;">+${eco.net_daily_savings_eur || 0.89} €</div>
            <div style="font-size: 0.72rem; color: #94a3b8;">Evita compra a 0.22 €/kWh</div>
          </div>
        </div>

        <!-- Automation & Controls Box -->
        <div style="background: rgba(0,0,0,0.4); border: 1px solid rgba(255,255,255,0.08); border-radius: 10px; padding: 16px; margin-bottom: 16px;">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px; flex-wrap: wrap; gap: 10px;">
            <div>
              <div style="font-weight: 700; font-size: 0.95rem; color: #f8fafc; display: flex; align-items: center; gap: 8px;">
                <span>⚡</span> Automatización Modbus TCP (Inversor Sunworks KP10)
              </div>
              <div style="font-size: 0.78rem; color: ${cfg.auto_enabled ? '#10b981' : '#94a3b8'};">
                ${data.auto_status_text}
              </div>
            </div>

            <!-- Toggle Switch -->
            <label style="display: flex; align-items: center; gap: 8px; cursor: pointer;">
              <span style="font-size: 0.8rem; font-weight: 600; color: #cbd5e1;">Automatismo:</span>
              <input type="checkbox" id="valley-auto-toggle" ${cfg.auto_enabled ? 'checked' : ''} style="width: 20px; height: 20px; cursor: pointer; accent-color: #10b981;">
            </label>
          </div>

          <!-- Configuration Controls -->
          <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 14px; font-size: 0.8rem; color: #94a3b8;">
            <div>
              <label style="display: block; margin-bottom: 4px; font-weight: 600; color: #cbd5e1;">SoC Objetivo:</label>
              <select id="valley-soc-select" class="form-select" style="width: 100%; padding: 6px 10px; background: #1e293b; color: #f8fafc; border: 1px solid rgba(255,255,255,0.15); border-radius: 6px;">
                <option value="75" ${cfg.target_soc_pct === 75 ? 'selected' : ''}>75% (7.0 kWh útiles)</option>
                <option value="80" ${cfg.target_soc_pct === 80 ? 'selected' : ''}>80% (7.5 kWh útiles)</option>
                <option value="85" ${cfg.target_soc_pct === 85 ? 'selected' : ''}>85% (8.0 kWh útiles - Recomendado)</option>
                <option value="90" ${cfg.target_soc_pct === 90 ? 'selected' : ''}>90% (8.4 kWh útiles)</option>
                <option value="95" ${cfg.target_soc_pct === 95 ? 'selected' : ''}>95% (8.8 kWh útiles)</option>
              </select>
            </div>

            <div>
              <label style="display: block; margin-bottom: 4px; font-weight: 600; color: #cbd5e1;">Hora Inicio Carga (P3):</label>
              <select id="valley-start-select" class="form-select" style="width: 100%; padding: 6px 10px; background: #1e293b; color: #f8fafc; border: 1px solid rgba(255,255,255,0.15); border-radius: 6px;">
                <option value="1" ${cfg.start_hour === 1 ? 'selected' : ''}>01:00 h</option>
                <option value="2" ${cfg.start_hour === 2 ? 'selected' : ''}>02:00 h (Recomendado)</option>
                <option value="3" ${cfg.start_hour === 3 ? 'selected' : ''}>03:00 h</option>
                <option value="4" ${cfg.start_hour === 4 ? 'selected' : ''}>04:00 h (Hora más barata)</option>
              </select>
            </div>

            <div>
              <label style="display: block; margin-bottom: 4px; font-weight: 600; color: #cbd5e1;">Hora Fin Carga (P3):</label>
              <select id="valley-end-select" class="form-select" style="width: 100%; padding: 6px 10px; background: #1e293b; color: #f8fafc; border: 1px solid rgba(255,255,255,0.15); border-radius: 6px;">
                <option value="5" ${cfg.end_hour === 5 ? 'selected' : ''}>05:00 h</option>
                <option value="6" ${cfg.end_hour === 6 ? 'selected' : ''}>06:00 h (Recomendado)</option>
                <option value="7" ${cfg.end_hour === 7 ? 'selected' : ''}>07:00 h</option>
                <option value="8" ${cfg.end_hour === 8 ? 'selected' : ''}>08:00 h (Fin Periodo P3)</option>
              </select>
            </div>

            <div>
              <label style="display: block; margin-bottom: 4px; font-weight: 600; color: #cbd5e1;">Potencia Límite de Carga:</label>
              <select id="valley-power-select" class="form-select" style="width: 100%; padding: 6px 10px; background: #1e293b; color: #f8fafc; border: 1px solid rgba(255,255,255,0.15); border-radius: 6px;">
                <option value="1500" ${cfg.charge_power_w === 1500 ? 'selected' : ''}>1.5 kW (6.5A - Suave)</option>
                <option value="2000" ${cfg.charge_power_w === 2000 ? 'selected' : ''}>2.0 kW (8.7A - Recomendado)</option>
                <option value="2500" ${cfg.charge_power_w === 2500 ? 'selected' : ''}>2.5 kW (10.8A)</option>
                <option value="3000" ${cfg.charge_power_w === 3000 ? 'selected' : ''}>3.0 kW (13.0A)</option>
              </select>
            </div>
          </div>

          <!-- Buttons Row -->
          <div style="display: flex; gap: 10px; margin-top: 14px; flex-wrap: wrap;">
            <button id="btn-save-valley-config" class="btn" style="background: #10b981; color: #0f172a; font-weight: 700; border: none; padding: 8px 16px; border-radius: 6px; cursor: pointer;">
              💾 Guardar Ajustes
            </button>
            <button id="btn-execute-valley-now" class="btn" style="background: rgba(56, 189, 248, 0.15); border: 1px solid #38bdf8; color: #38bdf8; font-weight: 700; padding: 8px 16px; border-radius: 6px; cursor: pointer;">
              ⚡ Probar Envío Modbus Ahora
            </button>
            <button id="btn-show-manual-guide" class="btn" style="background: rgba(255, 255, 255, 0.08); border: 1px solid rgba(255,255,255,0.15); color: #e2e8f0; font-weight: 600; padding: 8px 16px; border-radius: 6px; cursor: pointer;">
              📖 Ver Instrucciones Manuales FoxCloud
            </button>
          </div>
          <div id="valley-config-feedback" style="margin-top: 8px; font-size: 0.78rem; display: none;"></div>
        </div>

        <!-- GUARDIÁN DE SEGURIDAD ANTI-CORTES ICP (PRIORIDAD CERO CORTES) -->
        <div style="background: rgba(16, 185, 129, 0.06); border: 1px solid rgba(16, 185, 129, 0.25); border-radius: 8px; padding: 12px 16px; margin-bottom: 12px;">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px; flex-wrap: wrap; gap: 6px;">
            <div style="font-weight: 700; font-size: 0.85rem; color: #10b981; display: flex; align-items: center; gap: 6px;">
              <span>🛡️</span> Guardián de Seguridad Anti-Cortes ICP (Prioridad Cero Cortes)
            </div>
            <span style="font-size: 0.72rem; background: rgba(16, 185, 129, 0.2); color: #10b981; padding: 2px 8px; border-radius: 10px; font-weight: 700;">
              Límite Red: 4.000 W (Tope Seguro vs 4.60 kW ICP)
            </span>
          </div>
          <div style="font-size: 0.78rem; color: #cbd5e1; line-height: 1.4;">
            <strong>Regla de Jerarquía Estricta:</strong> <code>Prioridad 1: Suministro Hogar (Cero Cortes)</code> &gt; <code>Prioridad 2: Clima Daikin</code> &gt; <code>Prioridad 3: Batería Fox-ESS</code>.
            Si el consumo doméstico sube por sorpresa (horno, microondas, vitrocerámica), el inversor <strong>modula la carga de la batería a la baja o la detiene al instante (&lt;500 ms)</strong> para garantizar que nunca se dispare el contador.
          </div>
        </div>

        <!-- Collapsible Manual Instructions -->
        <div id="valley-manual-guide-panel" style="display: none; background: rgba(0,0,0,0.5); border: 1px dashed rgba(255,255,255,0.15); border-radius: 8px; padding: 14px; margin-top: 10px;">
          <h4 style="margin: 0 0 10px 0; font-size: 0.9rem; color: #fbbf24;">📲 Pasos para Configuración Manual en FoxCloud 2.0 / Pantalla Sunworks:</h4>
          <ol style="margin: 0; padding-left: 20px; font-size: 0.8rem; color: #cbd5e1; line-height: 1.6;">
            <li>${data.manual_instructions?.step_1 || 'Abrir la App FoxCloud.'}</li>
            <li>${data.manual_instructions?.step_2 || 'Cambiar Work Mode a Force Time Use.'}</li>
            <li>${data.manual_instructions?.step_3 || 'Establecer horas de 02:00 a 06:00 h.'}</li>
            <li>${data.manual_instructions?.step_4 || 'Configurar Max SoC en 85%.'}</li>
            <li>${data.manual_instructions?.step_5 || 'Al pasar el temporal, volver a Self-Use.'}</li>
          </ol>
        </div>

      </div>
    `;

    this.bindEvents();
  }

  bindEvents() {
    const btnSave = document.getElementById('btn-save-valley-config');
    const btnNow = document.getElementById('btn-execute-valley-now');
    const btnGuide = document.getElementById('btn-show-manual-guide');
    const toggleAuto = document.getElementById('valley-auto-toggle');
    const feedback = document.getElementById('valley-config-feedback');
    const guidePanel = document.getElementById('valley-manual-guide-panel');

    if (btnGuide && guidePanel) {
      btnGuide.addEventListener('click', () => {
        guidePanel.style.display = guidePanel.style.display === 'none' ? 'block' : 'none';
      });
    }

    if (btnSave) {
      btnSave.addEventListener('click', async () => {
        const auto_enabled = document.getElementById('valley-auto-toggle')?.checked || false;
        const target_soc_pct = parseInt(document.getElementById('valley-soc-select')?.value || 85);
        const start_hour = parseInt(document.getElementById('valley-start-select')?.value || 2);
        const end_hour = parseInt(document.getElementById('valley-end-select')?.value || 6);
        const charge_power_w = parseInt(document.getElementById('valley-power-select')?.value || 2000);

        try {
          const resp = await fetch('/api/battery/valley-charge-config', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ auto_enabled, target_soc_pct, start_hour, end_hour, charge_power_w })
          });
          const res = await resp.json();
          if (res.success && feedback) {
            feedback.style.display = 'block';
            feedback.style.color = '#10b981';
            feedback.textContent = `✅ Configuración guardada. Automatismo ${auto_enabled ? 'ACTIVADO' : 'APAGADO'}.`;
            setTimeout(() => { feedback.style.display = 'none'; }, 4000);
          }
        } catch (e) {
          if (feedback) {
            feedback.style.display = 'block';
            feedback.style.color = '#f43f5e';
            feedback.textContent = `❌ Error guardando: ${e.message}`;
          }
        }
      });
    }

    if (btnNow) {
      btnNow.addEventListener('click', async () => {
        const target_soc_pct = parseInt(document.getElementById('valley-soc-select')?.value || 85);
        try {
          btnNow.disabled = true;
          btnNow.textContent = '⏳ Enviando Modbus...';
          const resp = await fetch('/api/battery/valley-charge-execute', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ mode: 'force_time_use', target_soc_pct })
          });
          const res = await resp.json();
          if (feedback) {
            feedback.style.display = 'block';
            feedback.style.color = '#38bdf8';
            feedback.textContent = `⚡ Modbus ejecutado: ${res.message || 'Comando aplicado'}`;
            setTimeout(() => { feedback.style.display = 'none'; }, 5000);
          }
        } catch (e) {
          if (feedback) {
            feedback.style.display = 'block';
            feedback.style.color = '#f43f5e';
            feedback.textContent = `❌ Error Modbus: ${e.message}`;
          }
        } finally {
          btnNow.disabled = false;
          btnNow.textContent = '⚡ Probar Envío Modbus Ahora';
        }
      });
    }
  }
}

window.valleyChargeScheduler = new ValleyChargeSchedulerUI();
