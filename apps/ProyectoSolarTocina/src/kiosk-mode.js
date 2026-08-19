/**
 * Modo Kiosko / Pantalla Completa (Dashboard de Pared / Tablet)
 * Diseñado para tablets o pantallas fijas en cocina/salón:
 * - Números digitales gigantes de alto contraste
 * - Semáforo solar en tiempo real (Coste 0 € vs Batería vs Red)
 * - Nivel de Baterías Fox-ESS EP5
 * - Reloj en tiempo real y consumo de climatización Daikin
 */

export class KioskModeManager {
  constructor() {
    this.overlay = null;
    this.isActive = false;
    this.createOverlayDOM();
  }

  createOverlayDOM() {
    if (document.getElementById('kiosk-overlay')) return;

    this.overlay = document.createElement('div');
    this.overlay.id = 'kiosk-overlay';
    this.overlay.style.cssText = `
      display: none;
      position: fixed;
      inset: 0;
      z-index: 99999;
      background: #090d16;
      color: #f8fafc;
      font-family: system-ui, -apple-system, sans-serif;
      padding: 2rem;
      box-sizing: border-box;
      flex-direction: column;
      justify-content: space-between;
      overflow: hidden;
    `;

    this.overlay.innerHTML = `
      <!-- Top Bar -->
      <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid rgba(255,255,255,0.1); padding-bottom: 1rem;">
        <div style="display: flex; align-items: center; gap: 1rem;">
          <span style="font-size: 2.2rem;">☀️</span>
          <div>
            <div style="font-size: 1.5rem; font-weight: 800; color: #38bdf8;">SOLAR TOCINA · LOS ROSALES</div>
            <div style="font-size: 0.95rem; color: #94a3b8;">10x Jinko 500W (5 kWp) · Inversor Sunworks KP10 · 2x Fox-ESS EP5 (10.36 kWh)</div>
          </div>
        </div>
        <div style="display: flex; align-items: center; gap: 1.5rem;">
          <div id="kiosk-clock" style="font-size: 2rem; font-weight: 700; font-family: monospace; color: #f1f5f9;">00:00:00</div>
          <button id="kiosk-exit-btn" style="background: rgba(244,63,94,0.2); border: 1px solid #f43f5e; color: #f43f5e; font-size: 1rem; font-weight: 700; padding: 0.5rem 1.2rem; border-radius: 8px; cursor: pointer;">
            ✕ Salir Modo Kiosko
          </button>
        </div>
      </div>

      <!-- Main Central Grid -->
      <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 1.5rem; margin: 1.5rem 0; flex: 1;">
        
        <!-- Tarjeta 1: Solar Total -->
        <div style="background: rgba(30, 41, 59, 0.6); border: 2px solid rgba(245, 158, 11, 0.5); border-radius: 16px; padding: 1.5rem; display: flex; flex-direction: column; justify-content: space-between; box-shadow: 0 0 30px rgba(245, 158, 11, 0.1);">
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <span style="font-size: 1.1rem; color: #fbbf24; font-weight: 700; text-transform: uppercase;">☀️ Solar Generando</span>
            <span style="font-size: 1.8rem;">⚡</span>
          </div>
          <div style="text-align: center; margin: 1rem 0;">
            <div id="kiosk-solar-val" style="font-size: 4.5rem; font-weight: 900; color: #fbbf24; line-height: 1;">0.00</div>
            <div style="font-size: 1.4rem; color: #94a3b8; margin-top: 0.25rem;">kW (10x 500W)</div>
          </div>
          <div style="display: flex; justify-content: space-between; font-size: 0.95rem; color: #cbd5e1; border-top: 1px solid rgba(255,255,255,0.1); padding-top: 0.75rem;">
            <span>PV1 Oeste: <strong id="kiosk-pv1-val">0 W</strong></span>
            <span>PV2 Este: <strong id="kiosk-pv2-val">0 W</strong></span>
          </div>
        </div>

        <!-- Tarjeta 2: Batería Fox-ESS -->
        <div style="background: rgba(30, 41, 59, 0.6); border: 2px solid rgba(16, 185, 129, 0.5); border-radius: 16px; padding: 1.5rem; display: flex; flex-direction: column; justify-content: space-between; box-shadow: 0 0 30px rgba(16, 185, 129, 0.1);">
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <span style="font-size: 1.1rem; color: #10b981; font-weight: 700; text-transform: uppercase;">🔋 Batería Fox-ESS</span>
            <span style="font-size: 1.8rem;">📦</span>
          </div>
          <div style="text-align: center; margin: 1rem 0;">
            <div id="kiosk-bat-val" style="font-size: 4.5rem; font-weight: 900; color: #10b981; line-height: 1;">50%</div>
            <div id="kiosk-bat-sub" style="font-size: 1.3rem; color: #94a3b8; margin-top: 0.25rem;">195.6 V (10.36 kWh)</div>
          </div>
          <div style="background: rgba(255,255,255,0.1); border-radius: 8px; height: 16px; overflow: hidden;">
            <div id="kiosk-bat-bar" style="background: #10b981; width: 50%; height: 100%; transition: width 0.5s ease;"></div>
          </div>
        </div>

        <!-- Tarjeta 3: Consumo Vivienda -->
        <div style="background: rgba(30, 41, 59, 0.6); border: 2px solid rgba(244, 63, 94, 0.5); border-radius: 16px; padding: 1.5rem; display: flex; flex-direction: column; justify-content: space-between; box-shadow: 0 0 30px rgba(244, 63, 94, 0.1);">
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <span style="font-size: 1.1rem; color: #f43f5e; font-weight: 700; text-transform: uppercase;">🏠 Consumo Casa</span>
            <span style="font-size: 1.8rem;">💡</span>
          </div>
          <div style="text-align: center; margin: 1rem 0;">
            <div id="kiosk-home-val" style="font-size: 4.5rem; font-weight: 900; color: #f43f5e; line-height: 1;">0.45</div>
            <div style="font-size: 1.4rem; color: #94a3b8; margin-top: 0.25rem;">kW (Demanda Real)</div>
          </div>
          <div style="display: flex; justify-content: space-between; font-size: 0.95rem; color: #cbd5e1; border-top: 1px solid rgba(255,255,255,0.1); padding-top: 0.75rem;">
            <span>Clima Daikin: <strong id="kiosk-daikin-w">~0 W</strong></span>
            <span>Standby base: <strong>~280 W</strong></span>
          </div>
        </div>

        <!-- Tarjeta 4: Red & Batería Virtual -->
        <div style="background: rgba(30, 41, 59, 0.6); border: 2px solid rgba(56, 189, 248, 0.5); border-radius: 16px; padding: 1.5rem; display: flex; flex-direction: column; justify-content: space-between; box-shadow: 0 0 30px rgba(56, 189, 248, 0.1);">
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <span style="font-size: 1.1rem; color: #38bdf8; font-weight: 700; text-transform: uppercase;">🌐 Red / Excedentes BV</span>
            <span style="font-size: 1.8rem;">💶</span>
          </div>
          <div style="text-align: center; margin: 1rem 0;">
            <div id="kiosk-grid-val" style="font-size: 4.5rem; font-weight: 900; color: #38bdf8; line-height: 1;">+0.00</div>
            <div id="kiosk-grid-desc" style="font-size: 1.3rem; color: #94a3b8; margin-top: 0.25rem;">kW Exportando a BV</div>
          </div>
          <div style="display: flex; justify-content: space-between; font-size: 0.95rem; color: #cbd5e1; border-top: 1px solid rgba(255,255,255,0.1); padding-top: 0.75rem;">
            <span>Importación: <strong id="kiosk-grid-in">0.00 kW</strong></span>
            <span>Coste: <strong style="color: #10b981;">0.00 €/h</strong></span>
          </div>
        </div>

      </div>

      <!-- Bottom Status Banner / Semáforo de Oportunidad -->
      <div id="kiosk-traffic-banner" style="background: rgba(16, 185, 129, 0.15); border: 2px solid #10b981; border-radius: 14px; padding: 1.25rem 2rem; display: flex; justify-content: space-between; align-items: center;">
        <div style="display: flex; align-items: center; gap: 1.25rem;">
          <span id="kiosk-traffic-icon" style="font-size: 2.2rem;">🟢</span>
          <div>
            <div id="kiosk-traffic-title" style="font-size: 1.4rem; font-weight: 800; color: #10b981;">AUTOSUFICIENCIA TOTAL (100% SOLAR)</div>
            <div id="kiosk-traffic-msg" style="font-size: 1rem; color: #cbd5e1;">Tu vivienda funciona a coste cero alimentada directamente por las placas y batería Fox-ESS.</div>
          </div>
        </div>
        <div style="text-align: right;">
          <div style="font-size: 0.95rem; color: #94a3b8;">Ahorro hoy estimado</div>
          <div id="kiosk-savings-val" style="font-size: 1.8rem; font-weight: 800; color: #38bdf8;">~5.44 €/día</div>
        </div>
      </div>
    `;

    document.body.appendChild(this.overlay);

    // Reloj
    setInterval(() => {
      const clk = document.getElementById('kiosk-clock');
      if (clk) {
        clk.textContent = new Date().toLocaleTimeString('es-ES', { timeZone: 'Europe/Madrid', hour12: false });
      }
    }, 1000);

    // Botón Salir
    const exitBtn = document.getElementById('kiosk-exit-btn');
    if (exitBtn) {
      exitBtn.addEventListener('click', () => this.exit());
    }

    // Tecla Escape para salir
    window.addEventListener('keydown', (e) => {
      if (e.key === 'Escape' && this.isActive) {
        this.exit();
      }
    });
  }

  enter() {
    this.isActive = true;
    if (this.overlay) {
      this.overlay.style.display = 'flex';
      if (document.documentElement.requestFullscreen) {
        document.documentElement.requestFullscreen().catch(() => {});
      }
    }
  }

  exit() {
    this.isActive = false;
    if (this.overlay) {
      this.overlay.style.display = 'none';
      if (document.fullscreenElement && document.exitFullscreen) {
        document.exitFullscreen().catch(() => {});
      }
    }
  }

  updateTelemetry(data) {
    if (!this.isActive || !data) return;

    const solarKw = data.solar_total_kw || 0.0;
    const pv1W = (data.pv1_west ? data.pv1_west.power_w : (data.pv1_east ? data.pv1_east.power_w : 0));
    const pv2W = (data.pv2_west ? data.pv2_west.power_w : (data.pv2_east ? data.pv2_east.power_w : 0));
    const batSoc = data.battery ? data.battery.soc_percent : 50;
    const batV = data.battery ? data.battery.voltage_v : 195.0;
    const homeKw = data.grid ? (data.grid.home_load_kw || (data.grid.home_load_w / 1000.0) || 0.45) : 0.45;
    const gridKw = data.grid ? (data.grid.ac_power_kw || 0.0) : 0.0;

    const sEl = document.getElementById('kiosk-solar-val');
    if (sEl) sEl.textContent = solarKw.toFixed(2);

    const pv1El = document.getElementById('kiosk-pv1-val');
    if (pv1El) pv1El.textContent = `${pv1W} W`;

    const pv2El = document.getElementById('kiosk-pv2-val');
    if (pv2El) pv2El.textContent = `${pv2W} W`;

    const bEl = document.getElementById('kiosk-bat-val');
    if (bEl) bEl.textContent = `${batSoc}%`;

    const bSub = document.getElementById('kiosk-bat-sub');
    if (bSub) bSub.textContent = `${batV.toFixed(1)} V (10.36 kWh)`;

    const bBar = document.getElementById('kiosk-bat-bar');
    if (bBar) bBar.style.width = `${batSoc}%`;

    const hEl = document.getElementById('kiosk-home-val');
    if (hEl) hEl.textContent = homeKw.toFixed(2);

    const gEl = document.getElementById('kiosk-grid-val');
    const gDesc = document.getElementById('kiosk-grid-desc');
    if (gEl && gDesc) {
      if (solarKw > homeKw) {
        const surplus = solarKw - homeKw;
        gEl.textContent = `+${surplus.toFixed(2)}`;
        gEl.style.color = '#38bdf8';
        gDesc.textContent = 'kW Exportando a BV';
      } else {
        gEl.textContent = `0.00`;
        gEl.style.color = '#10b981';
        gDesc.textContent = 'kW Batería Suministrando';
      }
    }

    // Semáforo de Oportunidad
    const banner = document.getElementById('kiosk-traffic-banner');
    const icon = document.getElementById('kiosk-traffic-icon');
    const title = document.getElementById('kiosk-traffic-title');
    const msg = document.getElementById('kiosk-traffic-msg');

    if (banner && icon && title && msg) {
      if (solarKw > homeKw + 1.8 && batSoc >= 90) {
        banner.style.background = 'rgba(16, 185, 129, 0.2)';
        banner.style.borderColor = '#10b981';
        icon.textContent = '⚡';
        title.style.color = '#10b981';
        title.textContent = 'GRAN EXCEDENTE SOLAR (+2.0 kW) · ¡OPORTUNIDAD!';
        msg.textContent = 'Enchufa el Omoda 7 SHS, pon la lavadora o activa el aire Daikin a coste 0.00 €.';
      } else if (solarKw >= homeKw) {
        banner.style.background = 'rgba(56, 189, 248, 0.15)';
        banner.style.borderColor = '#38bdf8';
        icon.textContent = '🟢';
        title.style.color = '#38bdf8';
        title.textContent = 'AUTOSUFICIENCIA PLENA (100% SOLAR)';
        msg.textContent = 'Las placas solares cubren todo el consumo de la vivienda y cargan la batería.';
      } else {
        banner.style.background = 'rgba(139, 92, 246, 0.15)';
        banner.style.borderColor = '#8b5cf6';
        icon.textContent = '🔋';
        title.style.color = '#c084fc';
        title.textContent = 'ALIMENTADO POR BATERÍA FOX-ESS EP5';
        msg.textContent = 'Consumo cubierto por la energía solar almacenada durante el día. Cero red.';
      }
    }
  }
}
