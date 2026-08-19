/**
 * virtual-battery-naturgy-ui.js
 * Componente UI para Centro de Control y Monedero de Batería Virtual Naturgy Clientes S.A.U.
 * Ecosistema Solar Tocina - Los Rosales
 */

export class NaturgyVirtualBatteryUiManager {
  constructor(containerId = 'naturgy-virtual-battery-container') {
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
      const res = await fetch('/api/naturgy/virtual-battery/status');
      if (res.ok) {
        this.status = await res.json();
      }
    } catch (e) {
      console.warn('[NaturgyVBUi] Error fetching status:', e);
    }
  }

  setupAutoRefresh() {
    if (this.pollInterval) clearInterval(this.pollInterval);
    this.pollInterval = setInterval(async () => {
      await this.fetchStatus();
      this.updateView();
    }, 20000);
  }

  async toggleActiveStatus(isActive) {
    try {
      const res = await fetch('/api/naturgy/virtual-battery/toggle-active', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ is_active: isActive })
      });
      if (res.ok) {
        await this.fetchStatus();
        this.render();
      }
    } catch (e) {
      alert(`Error conmutando estado de Batería Virtual: ${e.message}`);
    }
  }

  render() {
    if (!this.container) return;
    const isAct = this.status?.is_active ?? false;
    const contract = this.status?.contract_info || {};
    const proj = this.status?.projection || {};
    const months = proj.projection_months || [];
    const balance = isAct ? (this.status?.config?.current_wallet_balance_eur || 0.0) : (this.status?.config?.virtual_simulated_wallet_eur || 52.30);

    this.container.innerHTML = `
      <div class="section-box" style="border: 1px solid rgba(192, 132, 252, 0.4); box-shadow: 0 0 25px rgba(192, 132, 252, 0.1); margin-bottom: 1.5rem;">
        
        <!-- Cabecera y Conmutador de Estado (Standby vs Activa) -->
        <div class="section-header" style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1rem;">
          <div>
            <div style="display: flex; align-items: center; gap: 0.5rem;">
              <span style="font-size: 1.5rem;">🏦</span>
              <h2 style="margin: 0; font-size: 1.25rem;">Batería Virtual Naturgy Clientes S.A.U.</h2>
            </div>
            <div class="sub-desc" style="margin-top: 0.25rem;">
              Contrato <strong>Noche Luz ECO 2.0TD</strong> | Compensación de excedentes a <strong style="color: #c084fc;">0,0726 €/kWh</strong> | Caducidad saldo: <strong>5 Años (60 meses)</strong>
            </div>
          </div>

          <!-- Switch Master de Activación -->
          <div style="display: flex; align-items: center; gap: 0.75rem; background: rgba(0,0,0,0.3); padding: 0.6rem 1rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
            <span style="font-size: 0.8rem; font-weight: 700; color: ${isAct ? '#10b981' : '#f59e0b'};">
              ${isAct ? '🟢 ACTIVA (Facturación Real)' : '🟡 CONTRATADA EN ESPERA (Standby)'}
            </span>
            <button id="btn-toggle-vb-active" style="background: ${isAct ? '#10b981' : 'linear-gradient(135deg, #a855f7, #7e22ce)'}; color: #ffffff; border: none; border-radius: 9999px; padding: 0.45rem 1rem; font-size: 0.8rem; font-weight: 800; cursor: pointer; box-shadow: 0 2px 8px rgba(168, 85, 247, 0.35);">
              ${isAct ? 'Pausar a Standby' : '🚀 Activar Ahora'}
            </button>
          </div>
        </div>

        <!-- Tarjeta de Saldo y Resumen Financiero -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 1rem; margin: 1.25rem 0;">
          
          <div style="background: var(--bg-elevated); border: 1px solid rgba(192, 132, 252, 0.3); border-radius: var(--radius-md); padding: 1rem;">
            <div style="font-size: 0.72rem; color: #c084fc; font-weight: 700; text-transform: uppercase;">Saldo Monedero Virtual</div>
            <div style="font-size: 1.8rem; font-weight: 800; color: #c084fc; font-family: var(--font-mono); margin: 0.2rem 0;">
              ${balance.toFixed(2)} €
            </div>
            <div style="font-size: 0.72rem; color: var(--text-muted);">
              ${isAct ? 'Saldo real disponible para descontar' : 'Saldo virtual acumulado en simulación'}
            </div>
          </div>

          <div style="background: var(--bg-elevated); border: 1px solid var(--border-subtle); border-radius: var(--radius-md); padding: 1rem;">
            <div style="font-size: 0.72rem; color: #10b981; font-weight: 700; text-transform: uppercase;">Meses a Factura Cero (0,00 €)</div>
            <div style="font-size: 1.8rem; font-weight: 800; color: #10b981; font-family: var(--font-mono); margin: 0.2rem 0;">
              ${proj.zero_bill_months_count || 3} meses / año
            </div>
            <div style="font-size: 0.72rem; color: var(--text-muted);">
              Término fijo y consumo cubiertos íntegramente
            </div>
          </div>

          <div style="background: var(--bg-elevated); border: 1px solid var(--border-subtle); border-radius: var(--radius-md); padding: 1rem;">
            <div style="font-size: 0.72rem; color: var(--text-secondary); font-weight: 700; text-transform: uppercase;">Pago Medio Anual al Banco</div>
            <div style="font-size: 1.8rem; font-weight: 800; color: var(--text-primary); font-family: var(--font-mono); margin: 0.2rem 0;">
              ${proj.monthly_average_paid_eur || 19.97} € / mes
            </div>
            <div style="font-size: 0.72rem; color: var(--text-muted);">
              Todos los gastos totales e impuestos incluidos
            </div>
          </div>

        </div>

        <!-- Tabla Desglosada Mes a Mes -->
        <div style="background: rgba(0,0,0,0.25); border: 1px solid var(--border-subtle); border-radius: var(--radius-md); padding: 1rem; overflow-x: auto;">
          <div style="font-size: 0.85rem; font-weight: 800; color: var(--text-primary); margin-bottom: 0.75rem; display: flex; justify-content: space-between;">
            <span>📅 Proyección Anual de Compensación de Batería Virtual</span>
            <span style="font-size: 0.75rem; color: #c084fc;">Regla FIFO: Caducidad a 5 Años</span>
          </div>

          <table style="width: 100%; border-collapse: collapse; font-size: 0.76rem; text-align: left;">
            <thead>
              <tr style="border-bottom: 1px solid var(--border-subtle); color: var(--text-muted);">
                <th style="padding: 0.5rem;">Mes</th>
                <th style="padding: 0.5rem;">Consumo</th>
                <th style="padding: 0.5rem;">Solar</th>
                <th style="padding: 0.5rem;">Excedente Vertido</th>
                <th style="padding: 0.5rem;">Crédito Generado</th>
                <th style="padding: 0.5rem;">Factura Bruta</th>
                <th style="padding: 0.5rem; text-align: right;">Pago Final Banco</th>
              </tr>
            </thead>
            <tbody>
              ${months.map(m => `
                <tr style="border-bottom: 1px solid rgba(255,255,255,0.03); ${m.is_zero_bill ? 'background: rgba(16, 185, 129, 0.06);' : ''}">
                  <td style="padding: 0.45rem; font-weight: 700; color: ${m.is_zero_bill ? '#10b981' : 'var(--text-primary)'};">
                    ${m.month_name} ${m.is_zero_bill ? '🏆 (0 €)' : ''}
                  </td>
                  <td style="padding: 0.45rem;">${m.home_consumption_kwh} kWh</td>
                  <td style="padding: 0.45rem; color: #fbbf24;">${m.solar_generation_kwh} kWh</td>
                  <td style="padding: 0.45rem; color: #38bdf8;">+${m.grid_export_kwh} kWh</td>
                  <td style="padding: 0.45rem; color: #c084fc; font-weight: 700;">+${m.export_credit_eur.toFixed(2)} €</td>
                  <td style="padding: 0.45rem; color: var(--text-muted);">${m.raw_bill_eur.toFixed(2)} €</td>
                  <td style="padding: 0.45rem; text-align: right; font-weight: 800; font-family: var(--font-mono); color: ${m.is_zero_bill ? '#10b981' : 'var(--text-primary)'};">
                    ${m.final_bill_eur.toFixed(2)} €
                  </td>
                </tr>
              `).join('')}
            </tbody>
          </table>
        </div>

      </div>
    `;

    this.attachEventListeners();
  }

  attachEventListeners() {
    const btn = this.container?.querySelector('#btn-toggle-vb-active');
    if (btn) {
      btn.addEventListener('click', () => {
        const isAct = this.status?.is_active ?? false;
        this.toggleActiveStatus(!isAct);
      });
    }
  }

  updateView() {
    this.render();
  }
}
