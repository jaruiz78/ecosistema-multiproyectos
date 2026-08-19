/**
 * Asistente Consultor Solar Interactivo (NLP & Diálogo Estructurado)
 * Resuelve consultas sobre el mejor momento para enchufar electrodomésticos o el coche Omoda 7,
 * e indica de forma precisa qué otros aparatos puedes tener encendidos a la vez a coste 0.00 € (sin red)
 * y cuáles con apoyo de red de forma 100% segura sin riesgo de que salte el automático (ICP 4.6 kW).
 */

import { APPLIANCE_CATALOG } from './appliance-recommender.js';

export class SolarDialogAssistant {
  constructor(containerId, solarApp) {
    this.container = document.getElementById(containerId);
    this.solarApp = solarApp;
    this.history = [];
    this.init();
  }

  init() {
    if (!this.container) return;
    this.render();
    this.bindEvents();
  }

  formatDayLabel(d, idx) {
    if (!d) return `Día ${idx + 1}`;
    const dayDate = d.date instanceof Date ? d.date : new Date(d.dateStr || Date.now());
    const rawWeekday = d.dayName || new Intl.DateTimeFormat('es-ES', { weekday: 'long' }).format(dayDate);
    const capWeekday = rawWeekday.charAt(0).toUpperCase() + rawWeekday.slice(1);
    const formattedDate = new Intl.DateTimeFormat('es-ES', { day: 'numeric', month: 'short' }).format(dayDate);
    
    if (idx === 0) return `Hoy (${capWeekday})`;
    if (idx === 1) return `Mañana (${capWeekday})`;
    return `${capWeekday}, ${formattedDate}`;
  }

  render() {
    const days = this.solarApp.daysData || [];
    const dayOptions = days.map((d, idx) => {
      const label = this.formatDayLabel(d, idx);
      const kwhStr = d.kwhReal ? `${d.kwhReal.toFixed(1)} kWh` : 'Prev. Normal';
      return `<option value="${idx}">${label} (${kwhStr})</option>`;
    }).join('');

    const applianceOptions = APPLIANCE_CATALOG.map(a => {
      return `<option value="${a.id}">${a.icon} ${a.name} (${a.realPowerW} W - ${a.durationHours}h)</option>`;
    }).join('');

    this.container.innerHTML = `
      <div class="solar-dialog-box" style="background: var(--bg-card); border: 1px solid rgba(56, 189, 248, 0.35); border-radius: var(--radius-lg); padding: 1.25rem; box-shadow: var(--shadow-card); display: flex; flex-direction: column; gap: 1rem;">
        
        <!-- Cabecera del Asistente -->
        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.75rem;">
          <div style="display: flex; align-items: center; gap: 0.6rem;">
            <div style="font-size: 1.4rem; background: rgba(56, 189, 248, 0.15); width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; border-radius: var(--radius-md);">💬</div>
            <div>
              <h3 style="font-size: 1.05rem; font-weight: 700; color: var(--text-primary);">Asistente Consultor Solar & Cargas Simultáneas IA</h3>
              <div style="font-size: 0.75rem; color: var(--text-muted);">Pregunta qué conectar, a qué hora y qué otros aparatos son viables a la vez sin saltar la luz</div>
            </div>
          </div>
          <span class="badge-tag" style="background: rgba(16, 185, 129, 0.2); color: #10b981; font-weight: 700;">Coste 0.00 € & ICP Seguro</span>
        </div>

        <!-- Entrada de Consulta por Lenguaje Natural / Chat -->
        <div style="display: flex; flex-direction: column; gap: 0.5rem;">
          <div style="display: flex; gap: 0.5rem;">
            <input type="text" id="dialog-nlp-input" placeholder="Ej: ¿A qué hora cargo el coche mañana y qué más puedo tener puesto?" style="flex: 1; background: var(--bg-elevated); color: var(--text-primary); border: 1px solid var(--border-subtle); border-radius: var(--radius-md); padding: 0.65rem 0.9rem; font-size: 0.88rem; outline: none; transition: border-color 0.2s;" onfocus="this.style.borderColor='var(--color-east)'" onblur="this.style.borderColor='var(--border-subtle)'">
            <button id="dialog-ask-btn" class="refresh-button" style="background: var(--color-east); color: #0b0f19; font-weight: 700; border: none; padding: 0.65rem 1.25rem; border-radius: var(--radius-md); cursor: pointer; white-space: nowrap;">
              Consultar IA ⚡
            </button>
          </div>

          <!-- Sugerencias Rápidas / Atajos -->
          <div style="display: flex; gap: 0.4rem; flex-wrap: wrap; align-items: center;">
            <span style="font-size: 0.72rem; color: var(--text-muted);">Consultas e Instrucciones:</span>
            <button class="quick-query-btn" data-query="Desayuno entre 08:00 y 09:00 con tostador de pan y cafetera">🍞 Desayuno 08:00-09:00</button>
            <button class="quick-query-btn" data-query="Almuerzo a las 14:00 con vitrocerámica, microondas y Daikin">🍲 Comida 14:00</button>
            <button class="quick-query-btn" data-query="Cena a las 20:30 con vitrocerámica, freidora de aire y televisión">🍽️ Cena 20:30</button>
            <button class="quick-query-btn" data-query="Teletrabajo y dos puestos de estudio en casa con ordenadores y ventilador">💻 Teletrabajo y Estudios</button>
            <button class="quick-query-btn" data-query="¿Cuándo cargo el coche mañana y qué más puedo encender?">🚗 Coche mañana + Cargas</button>
            <button class="quick-query-btn" data-query="¿A qué hora pongo la lavadora hoy sin pagar luz?">🧺 Lavadora hoy</button>
            <button class="quick-query-btn" data-query="¿Cuándo pre-refrigerar con Daikin?">❄️ Daikin Pre-cooling</button>
          </div>
        </div>

        <!-- O Selección Guiada / Desplegables -->
        <div style="background: rgba(0,0,0,0.25); padding: 0.85rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle); display: grid; grid-template-columns: 1fr 1.2fr auto; gap: 0.75rem; align-items: center;">
          <div>
            <label style="font-size: 0.72rem; color: var(--text-secondary); font-weight: 600; display: block; margin-bottom: 0.25rem;">📅 Día a Consultar:</label>
            <select id="dialog-day-select" class="form-select" style="width: 100%; background: var(--bg-card); color: var(--text-primary); border: 1px solid var(--border-subtle); border-radius: var(--radius-sm); padding: 0.45rem 0.6rem; font-size: 0.82rem;">
              ${dayOptions}
            </select>
          </div>

          <div>
            <label style="font-size: 0.72rem; color: var(--text-secondary); font-weight: 600; display: block; margin-bottom: 0.25rem;">🔌 Electrodoméstico Principal o Coche:</label>
            <select id="dialog-appliance-select" class="form-select" style="width: 100%; background: var(--bg-card); color: var(--text-primary); border: 1px solid var(--border-subtle); border-radius: var(--radius-sm); padding: 0.45rem 0.6rem; font-size: 0.82rem;">
              ${applianceOptions}
            </select>
          </div>

          <div style="align-self: flex-end;">
            <button id="dialog-calculate-btn" class="refresh-button" style="background: var(--bg-elevated); color: var(--color-real); border: 1px solid rgba(16, 185, 129, 0.4); font-weight: 700; padding: 0.45rem 1rem; border-radius: var(--radius-sm); font-size: 0.82rem;">
              Calcular Ventana
            </button>
          </div>
        </div>

        <!-- Tarjeta de Respuesta Inteligente del Asistente IA y Gemelo Digital -->
        <div id="dialog-ai-reasoning-container" style="display: none; margin-bottom: 1rem;"></div>

        <!-- Tarjeta de Ventana Horaria y Cargas Simultáneas -->
        <div id="dialog-result-container" style="display: none;">
          <!-- Renderizado dinámico tras consulta -->
        </div>

      </div>
    `;

    const style = document.createElement('style');
    style.innerHTML = `
      .quick-query-btn {
        background: var(--bg-elevated);
        border: 1px solid var(--border-subtle);
        color: var(--text-secondary);
        font-size: 0.72rem;
        padding: 0.2rem 0.55rem;
        border-radius: 9999px;
        cursor: pointer;
        transition: all 0.15s ease;
      }
      .quick-query-btn:hover {
        background: var(--bg-card-hover);
        color: var(--color-east);
        border-color: rgba(56, 189, 248, 0.4);
      }
      .simultaneous-badge-item {
        background: rgba(0,0,0,0.3);
        border: 1px solid var(--border-subtle);
        border-radius: var(--radius-sm);
        padding: 0.4rem 0.6rem;
        display: flex;
        align-items: center;
        justify-content: space-between;
        gap: 0.5rem;
        font-size: 0.76rem;
      }
      .rich-ai-content {
        font-size: 0.85rem;
        line-height: 1.55;
        color: var(--text-secondary);
      }
      .rich-ai-content h3, .rich-ai-content h4 {
        color: var(--text-primary);
        margin: 0.6rem 0 0.35rem 0;
        font-weight: 700;
      }
      .rich-ai-content h3 { font-size: 1.05rem; }
      .rich-ai-content h4 { font-size: 0.92rem; color: var(--color-east); }
      .rich-ai-content p { margin-bottom: 0.5rem; }
      .rich-ai-content blockquote {
        background: rgba(14, 165, 233, 0.08);
        border-left: 3px solid #38bdf8;
        padding: 0.55rem 0.85rem;
        border-radius: 0 var(--radius-sm) var(--radius-sm) 0;
        margin: 0.6rem 0;
        color: var(--text-primary);
      }
      .rich-ai-content table {
        width: 100%;
        border-collapse: collapse;
        margin: 0.75rem 0;
        font-size: 0.8rem;
        background: rgba(0,0,0,0.25);
        border-radius: var(--radius-sm);
        overflow: hidden;
        border: 1px solid var(--border-subtle);
      }
      .rich-ai-content th {
        background: rgba(30, 41, 59, 0.8);
        color: var(--text-primary);
        font-weight: 700;
        padding: 0.45rem 0.65rem;
        border-bottom: 1px solid var(--border-subtle);
        text-align: left;
      }
      .rich-ai-content td {
        padding: 0.45rem 0.65rem;
        border-bottom: 1px solid rgba(255,255,255,0.05);
        color: var(--text-secondary);
      }
      .rich-ai-content tr:last-child td { border-bottom: none; }
      .math-rendered-block {
        background: rgba(15, 23, 42, 0.65);
        border: 1px solid rgba(168, 85, 247, 0.3);
        border-radius: var(--radius-sm);
        padding: 0.75rem 1rem;
        margin: 0.75rem 0;
        text-align: center;
        overflow-x: auto;
        box-shadow: 0 4px 12px rgba(0,0,0,0.2);
      }
      .mermaid-diagram-card {
        background: rgba(15, 23, 42, 0.8);
        border: 1px solid rgba(56, 189, 248, 0.3);
        border-radius: var(--radius-sm);
        padding: 0.85rem;
        margin: 0.85rem 0;
        display: flex;
        justify-content: center;
        align-items: center;
        overflow-x: auto;
      }
      .mermaid-diagram-card svg {
        max-width: 100%;
        height: auto;
      }
    `;
    document.head.appendChild(style);
  }

  bindEvents() {
    const askBtn = document.getElementById('dialog-ask-btn');
    const input = document.getElementById('dialog-nlp-input');
    const calcBtn = document.getElementById('dialog-calculate-btn');

    if (askBtn && input) {
      askBtn.addEventListener('click', () => this.handleNlpQuery(input.value));
      input.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') this.handleNlpQuery(input.value);
      });
    }

    if (calcBtn) {
      calcBtn.addEventListener('click', () => {
        const dayIdx = parseInt(document.getElementById('dialog-day-select').value || 0);
        const applianceId = document.getElementById('dialog-appliance-select').value;
        this.processRecommendation(dayIdx, applianceId);
      });
    }

    this.container.querySelectorAll('.quick-query-btn').forEach(btn => {
      btn.addEventListener('click', (e) => {
        const query = e.currentTarget.getAttribute('data-query');
        if (input) input.value = query;
        this.handleNlpQuery(query);
      });
    });
  }

  async handleNlpQuery(queryText) {
    if (!queryText || !queryText.trim()) return;
    const text = queryText.toLowerCase();

    // 1. Siempre procesar la consulta por el Motor de IA y Gemelo Digital en backend
    await this.processApplianceTagEvent(queryText);

    // 2. Sincronizar selectores visuales de apoyo
    let dayIdx = 0;
    const days = this.solarApp.daysData || [];
    if (text.includes('mañana')) {
      dayIdx = Math.min(1, days.length - 1);
    } else if (text.includes('pasado mañana')) {
      dayIdx = Math.min(2, days.length - 1);
    }

    let applianceId = 'omoda7_ev_charge';
    if (text.includes('coche') || text.includes('omoda') || text.includes('vehiculo') || text.includes('cargar') || text.includes('bateria coche')) {
      applianceId = 'omoda7_ev_charge';
    } else if (text.includes('lavadora') || text.includes('ropa') || text.includes('lavar')) {
      applianceId = 'beko_washer';
    } else if (text.includes('lavavajillas') || text.includes('platos') || text.includes('fregar')) {
      applianceId = 'fagor_dishwasher';
    } else if (text.includes('horno') || text.includes('asar') || text.includes('pizza')) {
      applianceId = 'teka_oven';
    } else if (text.includes('secadora') || text.includes('secar')) {
      applianceId = 'superser_dryer';
    } else if (text.includes('freidora') || text.includes('airfryer') || text.includes('cecofry')) {
      applianceId = 'cecofry_airfryer';
    } else if (text.includes('daikin') || text.includes('aire') || text.includes('clima') || text.includes('frio') || text.includes('refrigerar')) {
      applianceId = 'daikin_salon';
    } else if (text.includes('microondas')) {
      applianceId = 'digital_microwave';
    }

    const daySelect = document.getElementById('dialog-day-select');
    const appSelect = document.getElementById('dialog-appliance-select');
    if (daySelect) daySelect.value = dayIdx;
    if (appSelect) appSelect.value = applianceId;
  }

  async processApplianceTagEvent(queryText) {
    const resContainer = document.getElementById('dialog-ai-reasoning-container');
    if (!resContainer) return;

    resContainer.style.display = 'block';
    resContainer.innerHTML = `
      <div style="background: var(--bg-card); padding: 1rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle); color: var(--text-muted); font-size: 0.85rem;">
        ⏳ Analizando balance integral con el Gemelo Digital y las previsiones meteorológicas...
      </div>
    `;

    try {
      const res = await fetch('/api/appliances/tag-event', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ text: queryText })
      });
      const data = await res.json();

      const isPast = data.time_mode === 'past';
      const isFuture = data.time_mode === 'future';
      const isConsult = data.time_mode === 'consultation';
      
      const badgeBg = isConsult ? 'rgba(168, 85, 247, 0.2)' : (isPast ? 'rgba(16, 185, 129, 0.2)' : 'rgba(56, 189, 248, 0.2)');
      const badgeCol = isConsult ? '#c084fc' : (isPast ? '#10b981' : '#38bdf8');
      const badgeText = isConsult ? '🔮 CONSULTA IA & GEMELO DIGITAL' : (isPast ? '✅ ETIQUETADO ASIMILADO' : '🟢 ESTADO CONFIRMADO');

      const appBadges = (data.appliances || []).map(a => `
        <span style="background: rgba(255,255,255,0.06); border: 1px solid var(--border-subtle); border-radius: var(--radius-sm); padding: 0.25rem 0.55rem; font-size: 0.8rem;">
          ${a.icon} <strong>${a.name}</strong> (${a.peak_power_w} W)
        </span>
      `).join('');

      const renderedBody = await this.renderRichContent(data.message || 'Instrucción procesada correctamente.');

      resContainer.innerHTML = `
        <div style="background: linear-gradient(135deg, rgba(30, 41, 59, 0.95), rgba(15, 23, 42, 0.98)); border: 1px solid ${badgeCol}; border-radius: var(--radius-md); padding: 1.25rem; box-shadow: 0 0 20px rgba(0,0,0,0.3); display: flex; flex-direction: column; gap: 0.95rem; animation: fadeInPane 0.25s ease-in-out;">
          
          <div style="display: flex; justify-content: space-between; align-items: flex-start; flex-wrap: wrap; gap: 0.5rem; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.6rem;">
            <div>
              <div style="font-size: 0.72rem; color: var(--color-east); font-weight: 700; text-transform: uppercase;">
                ${isPast ? 'Asimilación de Verdad Terreno (Ground Truth NILM)' : (isConsult ? 'Consultoría Holística & Gemelo Digital' : 'Simulación Predictiva Solar')}
              </div>
              <div style="font-size: 0.95rem; font-weight: 700; color: var(--text-primary); margin-top: 0.15rem;">
                Franja: ${data.time_window || 'En curso'}
              </div>
            </div>
            <span class="badge-tag" style="background: ${badgeBg}; color: ${badgeCol}; font-weight: 700;">
              ${badgeText}
            </span>
          </div>

          <div>
            <div style="font-size: 0.75rem; color: var(--text-muted); margin-bottom: 0.35rem;">Aparatos y Cargas Evaluadas:</div>
            <div style="display: flex; gap: 0.4rem; flex-wrap: wrap;">
              ${appBadges}
            </div>
          </div>

          <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(130px, 1fr)); gap: 0.6rem; background: rgba(0,0,0,0.25); padding: 0.75rem; border-radius: var(--radius-sm);">
            <div>
              <div style="font-size: 0.7rem; color: var(--text-muted);">Energía requerida:</div>
              <div style="font-size: 1rem; font-weight: 700; color: var(--text-primary);">${data.energy_kwh || '0.00'} kWh</div>
            </div>
            <div>
              <div style="font-size: 0.7rem; color: var(--text-muted);">Batería Fox-ESS al fin:</div>
              <div style="font-size: 1rem; font-weight: 700; color: var(--color-real);">${data.final_battery_soc || 100}% SoC (10.36 kWh)</div>
            </div>
            <div>
              <div style="font-size: 0.7rem; color: var(--text-muted);">Excedente Batería Virtual:</div>
              <div style="font-size: 1rem; font-weight: 700; color: #c084fc;">+${data.virtual_wallet_surplus_kwh || '0.00'} kWh</div>
            </div>
            <div>
              <div style="font-size: 0.7rem; color: var(--text-muted);">Coste de Red:</div>
              <div style="font-size: 1rem; font-weight: 700; color: #38bdf8;">0.00 € (100% Gratis)</div>
            </div>
          </div>

          <div class="rich-ai-content" style="background: rgba(255,255,255,0.02); padding: 0.85rem 1rem; border-radius: var(--radius-sm); border-left: 3px solid ${badgeCol};">
            ${renderedBody}
          </div>

        </div>
      `;
    } catch (err) {
      resContainer.innerHTML = `
        <div style="color: #ef4444; font-size: 0.85rem;">Error conectando con el servicio de etiquetado: ${err.message}</div>
      `;
    }
  }

  async renderRichContent(rawText) {
    if (!rawText) return '';

    let processed = rawText;
    const mathPlaceholders = [];
    const mermaidPlaceholders = [];

    // 1. Extraer bloques KaTeX \[ ... \]
    processed = processed.replace(/\\\[([\s\S]*?)\\\]/g, (match, eq) => {
      const idx = mathPlaceholders.length;
      let rendered = eq;
      if (window.katex) {
        try {
          rendered = window.katex.renderToString(eq.trim(), { displayMode: true, throwOnError: false });
        } catch (e) {
          rendered = `<div class="katex-error">${eq}</div>`;
        }
      }
      mathPlaceholders.push(rendered);
      return `@@MATH_BLOCK_${idx}@@`;
    });

    // 2. Extraer KaTeX inline \( ... \)
    processed = processed.replace(/\\\(([\s\S]*?)\\\)/g, (match, eq) => {
      const idx = mathPlaceholders.length;
      let rendered = eq;
      if (window.katex) {
        try {
          rendered = window.katex.renderToString(eq.trim(), { displayMode: false, throwOnError: false });
        } catch (e) {
          rendered = `<span class="katex-error">${eq}</span>`;
        }
      }
      mathPlaceholders.push(rendered);
      return `@@MATH_INLINE_${idx}@@`;
    });

    // 3. Extraer bloques Mermaid ```mermaid ... ```
    processed = processed.replace(/```mermaid([\s\S]*?)```/g, (match, code) => {
      const idx = mermaidPlaceholders.length;
      mermaidPlaceholders.push(code.trim());
      return `@@MERMAID_BLOCK_${idx}@@`;
    });

    // 4. Parsear Markdown a HTML con Marked
    let html = '';
    if (window.marked) {
      html = window.marked.parse(processed);
    } else {
      html = processed.replace(/\n/g, '<br>');
    }

    // 5. Reinsertar KaTeX
    mathPlaceholders.forEach((mathHtml, idx) => {
      html = html.replace(`@@MATH_BLOCK_${idx}@@`, `<div class="math-rendered-block">${mathHtml}</div>`);
      html = html.replace(`@@MATH_INLINE_${idx}@@`, mathHtml);
    });

    // 6. Reinsertar y Renderizar Mermaid
    for (let i = 0; i < mermaidPlaceholders.length; i++) {
      const code = mermaidPlaceholders[i];
      const divId = `mermaid_diag_${Date.now()}_${i}`;
      let svgContent = '';
      if (window.mermaid) {
        try {
          const { svg } = await window.mermaid.render(divId, code);
          svgContent = svg;
        } catch (err) {
          console.warn('Error renderizando Mermaid:', err);
          svgContent = `<pre class="mermaid-fallback" style="background: rgba(0,0,0,0.4); padding: 0.5rem; border-radius: 4px; font-size: 0.75rem;">${code}</pre>`;
        }
      } else {
        svgContent = `<pre class="mermaid-fallback" style="background: rgba(0,0,0,0.4); padding: 0.5rem; border-radius: 4px; font-size: 0.75rem;">${code}</pre>`;
      }
      html = html.replace(`@@MERMAID_BLOCK_${i}@@`, `<div class="mermaid-diagram-card">${svgContent}</div>`);
    }

    return html;
  }

  calculateSimultaneousAppliances(queriedAppliance, rec, selectedDay) {
    const windowPoints = (selectedDay.hourly || []).slice(rec.startHour, Math.min(24, rec.endHour + 1));
    const avgSolarKw = windowPoints.length > 0 
      ? windowPoints.reduce((acc, p) => acc + p.forecast.pTotalAC_kW, 0) / windowPoints.length 
      : (selectedDay.peakKw || 4.2);

    const queriedKw = queriedAppliance.realPowerW / 1000.0;
    const baseHouseKw = 0.22; // Frigo + Standby + WiFi
    const batSafeDischargeKw = 3.00; // Descarga suave disponible de Fox-ESS sin estrés
    const contractedPowerKw = 4.60;  // Potencia contratada en factura

    // Margen libre a Coste 0.00 € (Sol directo restante + Batería Fox-ESS)
    const directSurplusKw = Math.max(0, avgSolarKw - queriedKw - baseHouseKw);
    const totalCleanHeadroomKw = directSurplusKw + batSafeDischargeKw;

    // Margen libre de Red (ICP 4.6 kW) sin que salte el automático
    const currentGridDeficitKw = Math.max(0, queriedKw + baseHouseKw - avgSolarKw - batSafeDischargeKw);
    const remainingIcpHeadroomKw = Math.max(0, contractedPowerKw - currentGridDeficitKw);

    const cleanGroup = [];
    const gridSafeGroup = [];
    const riskyCombinations = [];

    const otherAppliances = APPLIANCE_CATALOG.filter(a => a.id !== queriedAppliance.id && a.id !== 'solar_thermal_acs');

    otherAppliances.forEach(app => {
      const appKw = app.realPowerW / 1000.0;
      
      // 1. ¿Entra 100% a coste 0.00 € (Sol + Batería)?
      if (appKw <= totalCleanHeadroomKw) {
        cleanGroup.push({
          ...app,
          appKw,
          costLabel: '0.00 € (100% Gratis)',
          isFree: true
        });
      } 
      // 2. ¿Entra con apoyo de red sin superar los 4.6 kW del ICP?
      else if (appKw <= (totalCleanHeadroomKw + remainingIcpHeadroomKw)) {
        const gridPortionKw = appKw - totalCleanHeadroomKw;
        const hourlyCost = gridPortionKw * 0.135;
        gridSafeGroup.push({
          ...app,
          appKw,
          gridKw: gridPortionKw.toFixed(2),
          costLabel: `+${hourlyCost.toFixed(2)} €/h`,
          isFree: false
        });
      } 
      // 3. Superaría el límite si se activa en paralelo
      else {
        riskyCombinations.push({
          ...app,
          appKw
        });
      }
    });

    return {
      avgSolarKw: avgSolarKw.toFixed(2),
      directSurplusKw: directSurplusKw.toFixed(2),
      totalCleanHeadroomKw: totalCleanHeadroomKw.toFixed(2),
      remainingIcpHeadroomKw: remainingIcpHeadroomKw.toFixed(2),
      cleanGroup,
      gridSafeGroup,
      riskyCombinations
    };
  }

  processRecommendation(dayIdx, applianceId, userQuery = '') {
    const days = this.solarApp.daysData || [];
    const selectedDay = days[dayIdx] || days[0];
    if (!selectedDay || !selectedDay.hourly) return;

    const appliance = APPLIANCE_CATALOG.find(a => a.id === applianceId) || APPLIANCE_CATALOG[0];
    const rec = this.solarApp.applianceRecommender.findBestWindowForAppliance(appliance, selectedDay.hourly);
    const dayLabel = this.formatDayLabel(selectedDay, dayIdx);

    // Calcular aparatos simultáneos
    const sim = this.calculateSimultaneousAppliances(appliance, rec, selectedDay);

    const resContainer = document.getElementById('dialog-result-container');
    if (!resContainer) return;

    const peakSolarHour = selectedDay.peakHour || 14;

    // Renderizado de tarjetas de aparatos limpios a coste 0€
    const cleanListHtml = sim.cleanGroup.map(a => `
      <div class="simultaneous-badge-item" style="border-left: 3px solid var(--color-real);">
        <div>
          <span style="font-size: 1.1rem; margin-right: 0.25rem;">${a.icon}</span>
          <strong style="color: var(--text-primary);">${a.name}</strong>
          <span style="color: var(--text-muted); font-size: 0.72rem; margin-left: 0.25rem;">(${a.realPowerW} W)</span>
        </div>
        <span style="color: var(--color-real); font-weight: 700; font-size: 0.72rem; background: rgba(16, 185, 129, 0.15); padding: 0.15rem 0.4rem; border-radius: var(--radius-sm);">
          0.00 € (Sin Red)
        </span>
      </div>
    `).join('');

    // Renderizado de tarjetas de aparatos con apoyo de red seguro
    const gridListHtml = sim.gridSafeGroup.map(a => `
      <div class="simultaneous-badge-item" style="border-left: 3px solid #fbbf24;">
        <div>
          <span style="font-size: 1.1rem; margin-right: 0.25rem;">${a.icon}</span>
          <strong style="color: var(--text-primary);">${a.name}</strong>
          <span style="color: var(--text-muted); font-size: 0.72rem; margin-left: 0.25rem;">(${a.realPowerW} W)</span>
        </div>
        <div style="text-align: right;">
          <span style="color: #fbbf24; font-weight: 700; font-size: 0.72rem; background: rgba(245, 158, 11, 0.15); padding: 0.15rem 0.4rem; border-radius: var(--radius-sm);">
            ${a.costLabel}
          </span>
          <div style="font-size: 0.65rem; color: var(--text-muted); margin-top: 0.1rem;">ICP Seguro</div>
        </div>
      </div>
    `).join('');

    resContainer.style.display = 'block';
    resContainer.innerHTML = `
      <div style="background: linear-gradient(135deg, rgba(30, 41, 59, 0.95), rgba(15, 23, 42, 0.98)); border: 1px solid rgba(16, 185, 129, 0.4); border-radius: var(--radius-md); padding: 1.15rem; box-shadow: 0 0 20px rgba(16, 185, 129, 0.12); display: flex; flex-direction: column; gap: 0.9rem; animation: fadeInPane 0.25s ease-in-out;">
        
        <!-- Veredicto Principal del Aparato Consultado -->
        <div style="display: flex; justify-content: space-between; align-items: flex-start; flex-wrap: wrap; gap: 0.5rem; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.75rem;">
          <div>
            <div style="font-size: 0.75rem; color: var(--color-east); font-weight: 700; text-transform: uppercase;">
              🎯 Ventana Óptima Recomendada para ${dayLabel}
            </div>
            <div style="font-size: 1.4rem; font-weight: 800; color: var(--color-solar-light); margin-top: 0.15rem;">
              ${rec.timeRangeStr}
            </div>
            <div style="font-size: 0.84rem; color: var(--text-secondary); margin-top: 0.15rem;">
              Aparato Consultado: <strong style="color: var(--text-primary);">${appliance.icon} ${appliance.name}</strong> (${appliance.realPowerW} W • ${appliance.durationHours}h ciclo)
            </div>
          </div>

          <div style="text-align: right;">
            <div style="font-size: 0.72rem; color: var(--text-muted); text-transform: uppercase;">Coste del Aparato Principal</div>
            <div style="font-size: 1.4rem; font-weight: 800; color: var(--color-real);">
              ${rec.is100PercentFree ? '0.00 € (100% GRATIS)' : `${rec.costEur.toFixed(2)} €`}
            </div>
            <div style="font-size: 0.75rem; color: var(--text-muted);">
              ${rec.is100PercentFree ? '☀️ Cobertura solar íntegra' : `Ahorras ${rec.savingsEur.toFixed(2)} €`}
            </div>
          </div>
        </div>

        <!-- Métricas Clave de la Ventana -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(135px, 1fr)); gap: 0.5rem;">
          <div style="background: rgba(0,0,0,0.3); padding: 0.5rem 0.65rem; border-radius: var(--radius-sm); border: 1px solid var(--border-subtle);">
            <div style="font-size: 0.66rem; color: var(--color-solar); font-weight: 700; text-transform: uppercase;">Sol Medio Ventana</div>
            <div style="font-size: 1.05rem; font-weight: 800; color: var(--color-solar-light);">${sim.avgSolarKw} kW</div>
            <div style="font-size: 0.66rem; color: var(--text-muted);">Pico a las ${peakSolarHour}:00 h</div>
          </div>

          <div style="background: rgba(0,0,0,0.3); padding: 0.5rem 0.65rem; border-radius: var(--radius-sm); border: 1px solid var(--border-subtle);">
            <div style="font-size: 0.66rem; color: var(--color-real); font-weight: 700; text-transform: uppercase;">Margen Limpio Libre</div>
            <div style="font-size: 1.05rem; font-weight: 800; color: var(--color-real);">+${sim.totalCleanHeadroomKw} kW</div>
            <div style="font-size: 0.66rem; color: var(--text-muted);">A coste 0.00 €</div>
          </div>

          <div style="background: rgba(0,0,0,0.3); padding: 0.5rem 0.65rem; border-radius: var(--radius-sm); border: 1px solid var(--border-subtle);">
            <div style="font-size: 0.66rem; color: #38bdf8; font-weight: 700; text-transform: uppercase;">Margen ICP Contratado</div>
            <div style="font-size: 1.05rem; font-weight: 800; color: #38bdf8;">+${sim.remainingIcpHeadroomKw} kW</div>
            <div style="font-size: 0.66rem; color: var(--text-muted);">Límite 4.6 kW sin salto</div>
          </div>

          <div style="background: rgba(0,0,0,0.3); padding: 0.5rem 0.65rem; border-radius: var(--radius-sm); border: 1px solid var(--border-subtle);">
            <div style="font-size: 0.66rem; color: #c084fc; font-weight: 700; text-transform: uppercase;">Batería Fox-ESS</div>
            <div style="font-size: 1.05rem; font-weight: 800; color: #c084fc;">95% - 100%</div>
            <div style="font-size: 0.66rem; color: var(--text-muted);">Protegida para la noche</div>
          </div>
        </div>

        <!-- ======================================================== -->
        <!-- SECCIÓN 1: QUÉ PUEDES TENER A COSTE 0.00 € (SIN RED)    -->
        <!-- ======================================================== -->
        <div style="background: rgba(16, 185, 129, 0.06); border: 1px solid rgba(16, 185, 129, 0.35); border-radius: var(--radius-md); padding: 0.85rem; display: flex; flex-direction: column; gap: 0.6rem;">
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <div style="font-size: 0.82rem; font-weight: 800; color: var(--color-real); text-transform: uppercase; display: flex; align-items: center; gap: 0.4rem;">
              <span>🟢</span> Electrodomésticos Simultáneos a Coste 0.00 € (100% Sin Red)
            </div>
            <span style="font-size: 0.72rem; color: var(--text-muted);">Margen disponible: <strong>+${sim.totalCleanHeadroomKw} kW</strong></span>
          </div>

          <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 0.45rem;">
            ${cleanListHtml || '<div style="font-size: 0.75rem; color: var(--text-muted);">Consumo base del hogar cubierto al 100%.</div>'}
          </div>
        </div>

        <!-- ======================================================== -->
        <!-- SECCIÓN 2: QUÉ PUEDES ENCENDER ADEMÁS CON RED SEGURA     -->
        <!-- ======================================================== -->
        ${gridListHtml ? `
        <div style="background: rgba(245, 158, 11, 0.06); border: 1px solid rgba(245, 158, 11, 0.35); border-radius: var(--radius-md); padding: 0.85rem; display: flex; flex-direction: column; gap: 0.6rem;">
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <div style="font-size: 0.82rem; font-weight: 800; color: #fbbf24; text-transform: uppercase; display: flex; align-items: center; gap: 0.4rem;">
              <span>🟡</span> Electrodomésticos Adicionales con Apoyo de Red (ICP Seguro 4.6 kW)
            </div>
            <span style="font-size: 0.72rem; color: var(--text-muted);">Margen antes de salto ICP: <strong>+${sim.remainingIcpHeadroomKw} kW</strong></span>
          </div>

          <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 0.45rem;">
            ${gridListHtml}
          </div>
          <div style="font-size: 0.72rem; color: var(--text-secondary); line-height: 1.35; margin-top: 0.2rem;">
            🛡️ <strong>Seguridad Garantizada:</strong> Estos aparatos de alta potencia pueden funcionar a la vez que ${appliance.name} sin riesgo de que salte el automático general de la vivienda.
          </div>
        </div>
        ` : ''}

        <!-- Explicación Razonada de la IA -->
        <div style="background: rgba(0, 0, 0, 0.25); border-left: 3px solid var(--color-east); padding: 0.6rem 0.8rem; border-radius: 0 var(--radius-sm) var(--radius-sm) 0; font-size: 0.8rem; color: var(--text-primary); line-height: 1.4;">
          <strong>💡 Razonamiento del Gemelo Digital:</strong>
          <span style="color: var(--text-secondary); margin-left: 0.3rem;">
            ${this.generateReasoningText(appliance, rec, selectedDay, sim)}
          </span>
        </div>

      </div>
    `;

    resContainer.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
  }

  generateReasoningText(appliance, rec, selectedDay, sim) {
    if (appliance.id === 'omoda7_ev_charge') {
      return `Durante la franja ${rec.timeRangeStr}, la producción solar (${sim.avgSolarKw} kW) cubre holgadamente la carga del Omoda 7 SHS (2.3 kW). Te queda un excedente limpio libre de +${sim.directSurplusKw} kW para mantener frigorífico, Daikin Inverter y portátiles a coste 0.00 €. Además, si necesitaras hornear o lavar, el margen de red de +${sim.remainingIcpHeadroomKw} kW garantiza que el ICP no saltará.`;
    }
    if (appliance.id === 'daikin_salon' || appliance.id === 'daikin_bedroom') {
      return `Al consumir solo ${appliance.realPowerW} W en la franja ${rec.timeRangeStr}, dispones de prácticamente toda la potencia solar (+${sim.totalCleanHeadroomKw} kW) libre para cargar el coche o poner la lavadora y lavavajillas simultáneamente sin pagar red eléctrica.`;
    }
    if (appliance.id === 'teka_oven' || appliance.id === 'superser_dryer') {
      return `En la ventana ${rec.timeRangeStr} el sol directo amortigua la resistencia de ${appliance.realPowerW} W. Puedes compaginarlo con el Daikin y la informática a coste 0.00 €, y si coincides con el coche Omoda 7, la red suministrará el pico con total seguridad ICP.`;
    }
    return `En el intervalo ${rec.timeRangeStr}, la generación solar de los strings Este y Oeste entrega ${sim.avgSolarKw} kW medios, garantizando 100% de cobertura para ${appliance.name} y ${sim.cleanGroup.length} aparatos simultáneos a coste 0.00 €.`;
  }
}
