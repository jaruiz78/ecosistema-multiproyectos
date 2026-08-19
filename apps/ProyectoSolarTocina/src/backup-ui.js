/**
 * Gestor de Copias de Seguridad y Rotación de Archivos SQLite
 */

export class BackupUiManager {
  constructor(containerId) {
    this.container = document.getElementById(containerId);
    this.backups = [];
    this.init();
  }

  async init() {
    if (!this.container) return;
    await this.fetchBackups();
    this.render();
  }

  async fetchBackups() {
    try {
      const res = await fetch('/api/backup/list');
      if (res.ok) {
        const data = await res.json();
        this.backups = data.backups || [];
      }
    } catch (e) {
      console.warn('[BackupUi] Error obteniendo backups:', e);
    }
  }

  render() {
    if (!this.container) return;

    this.container.innerHTML = `
      <section class="section-box">
        <div class="section-header">
          <div>
            <h2>💾 Copias de Seguridad & Archivo Automático</h2>
            <div class="sub-desc">Snapshots comprimidos en GZIP de <strong>telemetry_history.db</strong></div>
          </div>
          <button id="btn-create-backup" class="refresh-button" style="background: #10b981; color: #0f172a; font-weight: 700; border: none; padding: 0.45rem 0.9rem;">
            📦 Crear Copia Ahora
          </button>
        </div>

        <div id="backup-msg" style="font-size: 0.8rem; margin: 0.5rem 0; min-height: 1rem;"></div>

        <div style="font-size: 0.8rem; color: var(--text-muted); display: flex; flex-direction: column; gap: 0.35rem;">
          <div>• Política de retención: <strong style="color: var(--text-primary);">Últimos 30 días comprimidos</strong></div>
          <div>• Formato de archivo: <strong style="color: #38bdf8;">SQLite VACUUM + GZIP (.sqlite.gz)</strong></div>
        </div>

        <div style="margin-top: 0.75rem; max-height: 140px; overflow-y: auto; background: var(--bg-card); border-radius: var(--radius-sm); border: 1px solid var(--border-subtle); padding: 0.5rem;">
          ${this.backups.length === 0 ? '<div style="font-size: 0.8rem; color: var(--text-muted); padding: 0.5rem; text-align: center;">No hay copias previas aún. Pulsa "Crear Copia Ahora".</div>' : ''}
          ${this.backups.map(b => `
            <div style="display: flex; justify-content: space-between; font-size: 0.8rem; padding: 0.35rem 0.5rem; border-bottom: 1px solid var(--border-subtle);">
              <span>📄 <strong>${b.name}</strong></span>
              <span style="color: #38bdf8;">${b.size_kb} KB</span>
            </div>
          `).join('')}
        </div>
      </section>
    `;

    const createBtn = document.getElementById('btn-create-backup');
    const msgEl = document.getElementById('backup-msg');
    if (createBtn) {
      createBtn.addEventListener('click', async () => {
        try {
          createBtn.disabled = true;
          createBtn.textContent = 'Creando snapshot...';
          const res = await fetch('/api/backup/create', { method: 'POST' });
          const data = await res.json();
          if (data.success) {
            msgEl.style.color = '#10b981';
            msgEl.textContent = `✅ Backup creado: ${data.backup.file} (${data.backup.size_kb} KB)`;
            await this.fetchBackups();
            this.render();
          } else {
            msgEl.style.color = '#f43f5e';
            msgEl.textContent = `❌ Error: ${data.error}`;
          }
        } catch (e) {
          msgEl.style.color = '#f43f5e';
          msgEl.textContent = `❌ Error: ${e.message}`;
        } finally {
          createBtn.disabled = false;
        }
      });
    }
  }
}
