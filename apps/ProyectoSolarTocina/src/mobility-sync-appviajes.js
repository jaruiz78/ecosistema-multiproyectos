/**
 * Sincronizador de Rutas Inteligentes EV (Integración AppViajes & Omoda 7 SHS)
 * Conecta el perfil de movilidad del vehículo híbrido enchufable (18.7 kWh) con la generación
 * solar de Tocina, programando la carga a coste 0.00 € antes de cada viaje planificado.
 */

export const APPVIAJES_ROUTES = [
  {
    id: 'sevilla_commute',
    name: 'Tocina ⇄ Sevilla Centro',
    distanceKm: 76, // Ida y vuelta
    type: 'Urbano / Trabajo',
    energyKwh: 14.4,
    evCoveragePercent: 100, // 100% en modo eléctrico puro (autonomía EV 95 km)
    gasolineEquivLitres: 5.6,
    gasolineCostEur: 9.24, // 5.6L * 1.65€
    description: 'Desplazamiento habitual por A-4 y SE-30. 100% cubierto con batería del Omoda 7.'
  },
  {
    id: 'sevilla_aeropuerto',
    name: 'Tocina ⇄ Aeropuerto San Pablo',
    distanceKm: 64,
    type: 'Interurbano',
    energyKwh: 12.1,
    evCoveragePercent: 100,
    gasolineEquivLitres: 4.7,
    gasolineCostEur: 7.75,
    description: 'Ruta directa por A-4. Cero emisiones y cero coste de combustible.'
  },
  {
    id: 'costa_matalascanas',
    name: 'Tocina ⇄ Costa Huelva (Matalascañas)',
    distanceKm: 270,
    type: 'Fin de Semana / Playa',
    energyKwh: 18.7, // Batería completa + modo híbrido
    evCoveragePercent: 40,
    gasolineEquivLitres: 12.5,
    gasolineCostEur: 20.62,
    description: 'Primeros 95 km en eléctrico solar puro + 175 km en modo híbrido eficiente (4.2 L/100km).'
  },
  {
    id: 'cordoba_express',
    name: 'Tocina ⇄ Córdoba Capital',
    distanceKm: 210,
    type: 'Interurbano / Autovía',
    energyKwh: 18.7,
    evCoveragePercent: 48,
    gasolineEquivLitres: 9.8,
    gasolineCostEur: 16.17,
    description: 'Ruta por A-4. Carga completa en casa antes de la salida para maximizar el ahorro.'
  }
];

export class MobilitySyncAppViajes {
  constructor(containerId) {
    this.container = document.getElementById(containerId);
    this.selectedRouteId = 'sevilla_commute';
    this.departureTime = '08:30';
    this.chargingPowerKw = 2.30; // Schuko estándar 10A (o 3.7 kW si Wallbox)

    this.init();
  }

  init() {
    if (!this.container) return;
    this.render();
  }

  render() {
    if (!this.container) return;
    const route = APPVIAJES_ROUTES.find(r => r.id === this.selectedRouteId) || APPVIAJES_ROUTES[0];
    
    // Cálculo de tiempo de carga solar
    const chargeDurationHours = route.energyKwh / this.chargingPowerKw;
    const chargeDurationHoursInt = Math.floor(chargeDurationHours);
    const chargeDurationMins = Math.round((chargeDurationHours - chargeDurationHoursInt) * 60);

    const routesHtml = APPVIAJES_ROUTES.map(r => {
      const isSelected = r.id === this.selectedRouteId;
      return `
        <div class="whatif-card ${isSelected ? 'active' : ''}" data-route-id="${r.id}" style="background: var(--bg-card); border: 1px solid ${isSelected ? 'var(--color-east)' : 'var(--border-subtle)'}; border-radius: var(--radius-md); padding: 0.85rem; cursor: pointer; transition: all 0.2s ease;">
          <div style="display: flex; justify-content: space-between; align-items: flex-start;">
            <strong style="color: var(--text-primary); font-size: 0.88rem;">🚗 ${r.name}</strong>
            <span style="font-size: 0.72rem; color: var(--color-east); font-weight: 700; background: rgba(56, 189, 248, 0.15); padding: 0.15rem 0.4rem; border-radius: var(--radius-sm);">${r.distanceKm} km</span>
          </div>
          <div style="font-size: 0.74rem; color: var(--text-muted); margin-top: 0.3rem;">${r.description}</div>
          <div style="display: flex; justify-content: space-between; margin-top: 0.5rem; font-size: 0.72rem; border-top: 1px solid var(--border-subtle); padding-top: 0.35rem;">
            <span style="color: var(--color-real); font-weight: 700;">Ahorro: +${r.gasolineCostEur.toFixed(2)} €</span>
            <span style="color: #c084fc; font-weight: 700;">${r.energyKwh} kWh</span>
          </div>
        </div>
      `;
    }).join('');

    this.container.innerHTML = `
      <div class="mobility-sync-card" style="background: var(--bg-card); border: 1px solid rgba(56, 189, 248, 0.35); border-radius: var(--radius-lg); padding: 1.25rem; box-shadow: var(--shadow-card); display: flex; flex-direction: column; gap: 1rem;">
        
        <!-- Cabecera -->
        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.75rem;">
          <div style="display: flex; align-items: center; gap: 0.6rem;">
            <div style="font-size: 1.4rem; background: rgba(56, 189, 248, 0.15); width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; border-radius: var(--radius-md);">🧭</div>
            <div>
              <h3 style="font-size: 1.05rem; font-weight: 700; color: var(--text-primary);">Planificador de Rutas & Carga Solar Omoda 7 (AppViajes)</h3>
              <div style="font-size: 0.75rem; color: var(--text-muted);">Programa la recarga solar inteligente para que la batería de 18.7 kWh esté al 100% antes de tu viaje</div>
            </div>
          </div>
          <span class="badge-tag" style="background: rgba(16, 185, 129, 0.2); color: #10b981; font-weight: 700;">+${route.gasolineCostEur.toFixed(2)} € Ahorro Gasolina</span>
        </div>

        <!-- Selector de Rutas -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 0.65rem;">
          ${routesHtml}
        </div>

        <!-- Métricas del Viaje Seleccionado -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(170px, 1fr)); gap: 0.75rem;">
          <div style="background: var(--bg-elevated); padding: 0.8rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
            <div style="font-size: 0.7rem; color: var(--color-solar); font-weight: 700; text-transform: uppercase;">Energía Requerida</div>
            <div style="font-size: 1.3rem; font-weight: 800; color: var(--color-solar-light);">${route.energyKwh} kWh</div>
            <div style="font-size: 0.7rem; color: var(--text-muted);">Tiempo: ${chargeDurationHoursInt}h ${chargeDurationMins}m a 2.3 kW</div>
          </div>

          <div style="background: var(--bg-elevated); padding: 0.8rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
            <div style="font-size: 0.7rem; color: var(--color-real); font-weight: 700; text-transform: uppercase;">Coste Combustible Solar</div>
            <div style="font-size: 1.3rem; font-weight: 800; color: var(--color-real);">0.00 € (100% Gratis)</div>
            <div style="font-size: 0.7rem; color: var(--text-muted);">Ahorras ${route.gasolineCostEur.toFixed(2)} € vs Gasolina</div>
          </div>

          <div style="background: var(--bg-elevated); padding: 0.8rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
            <div style="font-size: 0.7rem; color: #38bdf8; font-weight: 700; text-transform: uppercase;">Cobertura Modo EV</div>
            <div style="font-size: 1.3rem; font-weight: 800; color: #38bdf8;">${route.evCoveragePercent}% Eléctrico</div>
            <div style="font-size: 0.7rem; color: var(--text-muted);">${Math.min(route.distanceKm, 95)} km sin encender térmico</div>
          </div>

          <div style="background: var(--bg-elevated); padding: 0.8rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
            <div style="font-size: 0.7rem; color: #c084fc; font-weight: 700; text-transform: uppercase;">Emisiones Evitadas</div>
            <div style="font-size: 1.3rem; font-weight: 800; color: #c084fc;">-${(route.gasolineEquivLitres * 2.31).toFixed(1)} kg CO₂</div>
            <div style="font-size: 0.7rem; color: var(--text-muted);">Cero impacto ambiental</div>
          </div>
        </div>

        <!-- Recomendación de Despacho de Carga -->
        <div style="background: rgba(56, 189, 248, 0.08); border-left: 3px solid var(--color-east); padding: 0.75rem 1rem; border-radius: 0 var(--radius-sm) var(--radius-sm) 0; font-size: 0.82rem; color: var(--text-primary); line-height: 1.45;">
          <strong>⚡ Estrategia de Carga Solar Recomendada:</strong>
          Para viajar a <strong>${route.name}</strong>, programa la carga del Omoda 7 entre las <strong>12:30 y 16:30 h</strong> de la tarde anterior o durante la franja de máxima radiación. El vehículo absorberá ${route.energyKwh} kWh de excedente solar directo sin pagar luz y sin tocar la batería Fox-ESS de la casa.
        </div>

      </div>
    `;

    this.bindEvents();
  }

  bindEvents() {
    const cards = this.container.querySelectorAll('.whatif-card');
    cards.forEach(card => {
      card.addEventListener('click', () => {
        this.selectedRouteId = card.dataset.routeId;
        this.render();
      });
    });
  }
}
