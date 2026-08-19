/**
 * Gestor Frontend del Bot de Telegram (@SolarTocinaBot)
 * Permite configurar el bot, emparejar el teléfono 653944373, activar/desactivar alertas
 * y enviar mensajes de prueba en tiempo real.
 */

export class TelegramBotManager {
  constructor(containerId) {
    this.container = document.getElementById(containerId);
    this.config = null;
    this.init();
  }

  async init() {
    if (!this.container) return;
    await this.fetchConfig();
    this.render();
  }

  async fetchConfig() {
    try {
      const res = await fetch('/api/telegram/config');
      if (res.ok) {
        this.config = await res.json();
      }
    } catch (e) {
      console.warn('[TelegramManager] Error obteniendo config:', e);
    }
  }

  render() {
    if (!this.container) return;
    const cfg = this.config || {};
    const hasToken = Boolean(cfg.bot_token);
    const hasChat = Boolean(cfg.chat_id);
    const isPaired = hasToken && hasChat;

    this.container.innerHTML = `
      <section class="section-box" style="border: 1px solid rgba(56, 189, 248, 0.4); box-shadow: 0 0 25px rgba(56, 189, 248, 0.08);">
        <div class="section-header">
          <div style="display: flex; align-items: center; gap: 0.75rem; flex-wrap: wrap;">
            <div style="background: #0088cc; width: 36px; height: 36px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 1.2rem; box-shadow: 0 0 10px rgba(0, 136, 204, 0.4);">
              ✈️
            </div>
            <div>
              <h2 style="font-size: 1.1rem; color: var(--text-primary);">🤖 Asistente Bot de Telegram (@SolarTocinaBot)</h2>
              <div class="sub-desc">Notificaciones inteligentes al móvil de José Antonio (<strong>${cfg.user_phone || '653944373'}</strong>)</div>
            </div>
          </div>
          <span class="badge-tag" style="background: ${isPaired ? 'rgba(16, 185, 129, 0.2)' : 'rgba(245, 158, 11, 0.2)'}; color: ${isPaired ? '#10b981' : '#f59e0b'}; font-weight: 700;">
            ${isPaired ? '🟢 Conectado y Listo' : (hasToken ? '🟡 Esperando primer mensaje /start' : '⚪ Sin Configurar')}
          </span>
        </div>

        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 1.25rem; margin-top: 1rem;">
          
          <!-- Columna 1: Configuración de Credenciales -->
          <div style="background: var(--bg-card); padding: 1.1rem; border-radius: var(--radius-sm); border: 1px solid var(--border-subtle); display: flex; flex-direction: column; gap: 0.85rem;">
            <h3 style="font-size: 0.95rem; color: #38bdf8; display: flex; align-items: center; gap: 0.4rem;">
              🔑 Claves de Conexión Telegram API
            </h3>
            
            <div>
              <label style="font-size: 0.8rem; color: var(--text-muted); display: block; margin-bottom: 0.25rem;">
                Bot Token (de @BotFather en Telegram):
              </label>
              <input type="password" id="tg-token-input" value="${cfg.bot_token || ''}" placeholder="123456789:ABCdefGHIjklMNO..." style="width: 100%; background: var(--bg-panel); color: var(--text-primary); border: 1px solid var(--border-subtle); padding: 0.5rem 0.75rem; border-radius: 6px; font-family: var(--font-mono); font-size: 0.85rem;">
              <div style="font-size: 0.7rem; color: var(--text-muted); margin-top: 0.25rem;">
                Crea un bot gratis en Telegram buscando a <a href="https://t.me/BotFather" target="_blank" style="color: #38bdf8; text-decoration: underline;">@BotFather</a> y envía <code>/newbot</code>.
              </div>
            </div>

            <div>
              <label style="font-size: 0.8rem; color: var(--text-muted); display: block; margin-bottom: 0.25rem;">
                Chat ID (o tu número 653944373):
              </label>
              <input type="text" id="tg-chat-input" value="${cfg.chat_id || ''}" placeholder="Se auto-rellena al escribir /start al bot" style="width: 100%; background: var(--bg-panel); color: var(--text-primary); border: 1px solid var(--border-subtle); padding: 0.5rem 0.75rem; border-radius: 6px; font-family: var(--font-mono); font-size: 0.85rem;">
            </div>

            <div style="display: flex; gap: 0.5rem; margin-top: 0.25rem;">
              <button id="tg-save-btn" class="refresh-button" style="flex: 1; justify-content: center; background: #0284c7; color: #fff; font-weight: 700; border: none; padding: 0.6rem;">
                💾 Guardar Configuración
              </button>
              <button id="tg-test-btn" class="refresh-button" style="flex: 1; justify-content: center; background: rgba(16, 185, 129, 0.2); color: #10b981; border: 1px solid rgba(16, 185, 129, 0.4); font-weight: 700; padding: 0.6rem;">
                🚀 Enviar Prueba
              </button>
            </div>
            
            <div id="tg-status-msg" style="font-size: 0.8rem; min-height: 1.2rem;"></div>
          </div>

          <!-- Columna 2: Alertas Automáticas & Comandos -->
          <div style="background: var(--bg-card); padding: 1.1rem; border-radius: var(--radius-sm); border: 1px solid var(--border-subtle); display: flex; flex-direction: column; gap: 0.85rem;">
            <h3 style="font-size: 0.95rem; color: var(--color-real); display: flex; align-items: center; gap: 0.4rem;">
              🔔 Alertas Automáticas Activas
            </h3>

            <div style="display: flex; flex-direction: column; gap: 0.6rem; font-size: 0.85rem;">
              <label style="display: flex; align-items: center; gap: 0.6rem; cursor: pointer;">
                <input type="checkbox" id="tg-morning-chk" ${cfg.morning_brief_enabled ? 'checked' : ''}>
                <span>☀️ <strong>Resumen Matinal (08:00 h):</strong> Previsión de kWh de sol y estado de batería.</span>
              </label>

              <label style="display: flex; align-items: center; gap: 0.6rem; cursor: pointer;">
                <input type="checkbox" id="tg-surplus-chk" ${cfg.surplus_alert_enabled ? 'checked' : ''}>
                <span>⚡ <strong>Alerta Excedente Solar (+2.0 kW):</strong> Aviso para Omoda 7 y electrodomésticos.</span>
              </label>

              <label style="display: flex; align-items: center; gap: 0.6rem; cursor: pointer;">
                <input type="checkbox" id="tg-batfull-chk" ${cfg.battery_full_alert_enabled ? 'checked' : ''}>
                <span>🔋 <strong>Batería al 100%:</strong> Notificación de inicio de vertido a Batería Virtual.</span>
              </label>

              <label style="display: flex; align-items: center; gap: 0.6rem; cursor: pointer;">
                <input type="checkbox" id="tg-sunset-chk" ${cfg.sunset_brief_enabled ? 'checked' : ''}>
                <span>🌙 <strong>Resumen Nocturno (21:30 h):</strong> Balance del día y euros ahorrados.</span>
              </label>
            </div>

            <div style="margin-top: auto; padding-top: 0.75rem; border-top: 1px solid var(--border-subtle); font-size: 0.8rem; color: var(--text-muted);">
              📱 <strong>Comandos que puedes enviarle en cualquier momento:</strong><br>
              <code>/solar</code> · <code>/bateria</code> · <code>/hoy</code> · <code>/ahorro</code> · <code>/omoda7</code> · <code>/daikin</code>
            </div>
          </div>

        </div>
      </section>
    `;

    this.attachEvents();
  }

  attachEvents() {
    const saveBtn = document.getElementById('tg-save-btn');
    const testBtn = document.getElementById('tg-test-btn');
    const msgEl = document.getElementById('tg-status-msg');

    if (saveBtn) {
      saveBtn.addEventListener('click', async () => {
        const token = document.getElementById('tg-token-input').value.trim();
        const chatId = document.getElementById('tg-chat-input').value.trim();
        const morning = document.getElementById('tg-morning-chk').checked;
        const surplus = document.getElementById('tg-surplus-chk').checked;
        const batfull = document.getElementById('tg-batfull-chk').checked;
        const sunset = document.getElementById('tg-sunset-chk').checked;

        try {
          saveBtn.disabled = true;
          saveBtn.textContent = 'Guardando...';
          const res = await fetch('/api/telegram/config', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
              bot_token: token,
              chat_id: chatId,
              morning_brief_enabled: morning,
              surplus_alert_enabled: surplus,
              battery_full_alert_enabled: batfull,
              sunset_brief_enabled: sunset
            })
          });
          const data = await res.json();
          if (data.success) {
            msgEl.style.color = '#10b981';
            msgEl.textContent = '✅ Configuración guardada con éxito.';
            this.config = data.config;
            setTimeout(() => this.render(), 1200);
          } else {
            msgEl.style.color = '#f43f5e';
            msgEl.textContent = '❌ Error: ' + (data.error || 'No se pudo guardar');
          }
        } catch (e) {
          msgEl.style.color = '#f43f5e';
          msgEl.textContent = '❌ Error de red: ' + e.message;
        } finally {
          saveBtn.disabled = false;
        }
      });
    }

    if (testBtn) {
      testBtn.addEventListener('click', async () => {
        const chatId = document.getElementById('tg-chat-input').value.trim();
        try {
          testBtn.disabled = true;
          testBtn.textContent = 'Enviando...';
          const res = await fetch('/api/telegram/test', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ chat_id: chatId })
          });
          const data = await res.json();
          if (data.success) {
            msgEl.style.color = '#10b981';
            msgEl.textContent = '🚀 ¡Mensaje de prueba recibido en tu Telegram!';
          } else {
            msgEl.style.color = '#f43f5e';
            msgEl.textContent = '⚠️ ' + (data.message || 'Verifica el Token o escribe /start al bot primero.');
          }
        } catch (e) {
          msgEl.style.color = '#f43f5e';
          msgEl.textContent = '❌ Error: ' + e.message;
        } finally {
          testBtn.disabled = false;
          testBtn.textContent = '🚀 Enviar Prueba';
        }
      });
    }
  }
}
