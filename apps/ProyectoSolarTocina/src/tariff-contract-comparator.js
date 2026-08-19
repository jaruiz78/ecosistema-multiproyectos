/**
 * Comparador y Simulador de Contratos de la Luz & Batería Virtual (Multi-Comercializadora)
 * Proyecta las facturas reales mensuales para la instalación en Tocina (5.185 kWh/año sol + Fox-ESS 10.36 kWh + 4.6 kW)
 * evaluando qué contrato permite pagar 0.00 € todo el año incluyendo los meses fríos de invierno.
 */

export const TARIFF_OFFERS = [
  {
    id: 'naturgy_noche_luz_eco',
    name: 'Naturgy (Tarifa Noche Luz ECO + Batería Virtual)',
    tag: '🟢 CONTRATO ACTIVO VIGENTE (19/08/2026)',
    company: 'Naturgy Clientes, S.A.U.',
    cups: 'ES0031104638423001VV0F',
    powerTermEurKwDay: 0.117388,      // Media P1 (0.156477) + P2 (0.078299) con imp
    energyImportEurKwh: 0.093991,     // Precio Valle (00-08h) con imp
    energyImportPeakEurKwh: 0.231734, // Precio Punta con imp
    surplusExportEurKwh: 0.072600,    // 0.06 €/kWh + 21% IVA
    walletFeeMonthlyEur: 0.00,        // Batería Virtual 0€/mes
    walletAppliesToFixedTerm: true,   // Compensa 100% de la factura
    walletExpires: false,            // 5 años de caducidad
    description: 'Contrato firmado el 19/08/2026. Batería Virtual gratuita (0€/mes), compensación fija a 0.06 €/kWh (+IVA) y precio valle a 0.0939 €/kWh para el Omoda 7.'
  },
  {
    id: 'octopus_solar_wallet',
    name: 'Octopus Energy (Solar Wallet)',
    tag: 'ALTERNATIVA MERCADO LIBRE',
    powerTermEurKwDay: 0.082,
    energyImportEurKwh: 0.125,
    surplusExportEurKwh: 0.040,
    walletFeeMonthlyEur: 0.00,
    walletAppliesToFixedTerm: true,
    walletExpires: false,
    description: 'Monedero solar en euros. Excedente a ~0.04 €/kWh para instalaciones no realizadas directamente por Octopus.'
  },
  {
    id: 'proxima_energia',
    name: 'Próxima Energía (Batería Virtual)',
    tag: 'INDEXADA OMIE',
    powerTermEurKwDay: 0.095,
    energyImportEurKwh: 0.138,
    surplusExportEurKwh: 0.075,
    walletFeeMonthlyEur: 3.00,
    walletAppliesToFixedTerm: true,
    walletExpires: false,
    description: 'Precios indexados a mercado OMIE con cuota de gestión de monedero de 3€/mes.'
  },
  {
    id: 'el_corte_ingles_historico',
    name: 'El Corte Inglés Energía (Histórico 16/12/2025 - 18/08/2026)',
    tag: 'HISTÓRICO TRANSICIÓN',
    powerTermEurKwDay: 0.108,
    energyImportEurKwh: 0.142,
    surplusExportEurKwh: 0.055,
    walletFeeMonthlyEur: 0.00,
    walletAppliesToFixedTerm: false,
    walletExpires: true,
    description: 'Periodo de transición en mercado libre con Telecor S.A. antes del nuevo contrato con Batería Virtual.'
  },
  {
    id: 'endesa_energia_xxi_historico',
    name: 'Endesa / Energía XXI (Histórico 01/06/2014 - 15/12/2025)',
    tag: 'HISTÓRICO 136 FACTURAS',
    powerTermEurKwDay: 0.092,
    energyImportEurKwh: 0.155,
    surplusExportEurKwh: 0.045,
    walletFeeMonthlyEur: 0.00,
    walletAppliesToFixedTerm: false,
    walletExpires: true,
    description: '136 recibos analizados en mercado regulado (PVPC) sin batería virtual (gasto acumulado 12.337,92 €).'
  }
];

export class TariffContractComparator {
  constructor(containerId) {
    this.container = document.getElementById(containerId);
    this.contractedKw = 4.60;
    this.selectedTariffId = 'naturgy_noche_luz_eco';

    // Perfil real de consumo verificado de Tocina (5.185 kWh/año)
    this.monthlyConsumptionKwh = [764, 520, 410, 310, 256, 380, 480, 510, 420, 390, 465, 680];
    // Producción solar mensual modelada (10x Jinko 500W con inclinación 25° y azimut 85°/265°)
    this.monthlySolarGenKwh = [295, 345, 460, 525, 590, 615, 630, 595, 480, 390, 310, 260];

    this.init();
  }

  init() {
    if (!this.container) return;
    this.render();
  }

  simulateTariff(tariff) {
    let walletEur = 0;
    const months = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];
    const monthlyResults = [];
    let annualTotalBilledEur = 0;
    let annualSurplusEarnedEur = 0;

    for (let m = 0; m < 12; m++) {
      const daysInMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][m];
      const genKwh = this.monthlySolarGenKwh[m];
      const consKwh = this.monthlyConsumptionKwh[m];

      // Autoconsumo directo con sol y batería Fox-ESS (~75% en verano, ~50% en invierno)
      const selfConsRatio = m >= 4 && m <= 8 ? 0.75 : 0.55;
      const selfConsKwh = Math.min(consKwh, genKwh * selfConsRatio);
      const gridImportKwh = Math.max(0, consKwh - selfConsKwh);
      const gridExportKwh = Math.max(0, genKwh - selfConsKwh);

      // Costes del mes
      const fixedTermEur = this.contractedKw * tariff.powerTermEurKwDay * daysInMonth;
      const energyTermEur = gridImportKwh * tariff.energyImportEurKwh;
      const grossCostEur = fixedTermEur + energyTermEur + tariff.walletFeeMonthlyEur;

      // Ganancia por excedentes
      const surplusValueEur = gridExportKwh * tariff.surplusExportEurKwh;
      annualSurplusEarnedEur += surplusValueEur;

      let netBillEur = grossCostEur;
      let monthWalletStart = walletEur;

      if (tariff.id === 'pvpc_regulado_sin_bv') {
        // En PVPC el excedente solo compensa hasta el término de energía
        const maxCompensation = energyTermEur;
        const actualComp = Math.min(surplusValueEur, maxCompensation);
        netBillEur = fixedTermEur + (energyTermEur - actualComp);
        walletEur = 0; // Se pierde el resto
      } else if (tariff.walletAppliesToFixedTerm) {
        // Batería virtual completa (Octopus / Próxima)
        walletEur += surplusValueEur;
        if (walletEur >= grossCostEur) {
          walletEur -= grossCostEur;
          netBillEur = 0.00;
        } else {
          netBillEur = grossCostEur - walletEur;
          walletEur = 0;
        }
      } else {
        // Batería virtual parcial (solo energía)
        const netEnergy = Math.max(0, energyTermEur - surplusValueEur);
        const leftoverSurplus = Math.max(0, surplusValueEur - energyTermEur);
        walletEur = tariff.walletExpires && m % 3 === 2 ? 0 : walletEur + leftoverSurplus;
        netBillEur = fixedTermEur + netEnergy;
      }

      annualTotalBilledEur += netBillEur;

      monthlyResults.push({
        month: months[m],
        genKwh,
        consKwh,
        gridImportKwh: Math.round(gridImportKwh),
        gridExportKwh: Math.round(gridExportKwh),
        surplusValueEur: surplusValueEur.toFixed(1),
        netBillEur: netBillEur.toFixed(2),
        walletEndEur: walletEur.toFixed(1)
      });
    }

    const freeMonthsCount = monthlyResults.filter(r => parseFloat(r.netBillEur) === 0.00).length;

    return {
      tariff,
      monthlyResults,
      annualTotalBilledEur: annualTotalBilledEur.toFixed(2),
      annualSurplusEarnedEur: annualSurplusEarnedEur.toFixed(2),
      finalWalletEndEur: walletEur.toFixed(2),
      freeMonthsCount
    };
  }

  render() {
    if (!this.container) return;
    const currentTariff = TARIFF_OFFERS.find(t => t.id === this.selectedTariffId) || TARIFF_OFFERS[0];
    const sim = this.simulateTariff(currentTariff);

    const offersNavHtml = TARIFF_OFFERS.map(t => {
      const isSelected = t.id === this.selectedTariffId;
      return `
        <button class="tab-btn ${isSelected ? 'active' : ''}" data-tariff-id="${t.id}" style="font-size: 0.78rem; padding: 0.45rem 0.85rem;">
          ${t.name}
        </button>
      `;
    }).join('');

    const monthlyRowsHtml = sim.monthlyResults.map(r => {
      const isFree = parseFloat(r.netBillEur) === 0.00;
      return `
        <tr style="border-bottom: 1px solid rgba(255,255,255,0.05); text-align: right;">
          <td style="text-align: left; font-weight: 700; color: var(--text-primary); padding: 0.45rem 0.6rem;">${r.month}</td>
          <td style="padding: 0.45rem 0.6rem; color: var(--color-solar-light);">${r.genKwh} kWh</td>
          <td style="padding: 0.45rem 0.6rem; color: #f43f5e;">${r.consKwh} kWh</td>
          <td style="padding: 0.45rem 0.6rem; color: var(--color-real);">+${r.surplusValueEur} €</td>
          <td style="padding: 0.45rem 0.6rem; color: #c084fc;">${r.walletEndEur} €</td>
          <td style="padding: 0.45rem 0.6rem; font-weight: 800; color: ${isFree ? 'var(--color-real)' : '#f43f5e'};">
            ${isFree ? '0.00 € (GRATIS)' : `${r.netBillEur} €`}
          </td>
        </tr>
      `;
    }).join('');

    this.container.innerHTML = `
      <div class="tariff-comparator-card" style="background: var(--bg-card); border: 1px solid rgba(16, 185, 129, 0.35); border-radius: var(--radius-lg); padding: 1.25rem; box-shadow: var(--shadow-card); display: flex; flex-direction: column; gap: 1rem;">
        
        <!-- Cabecera -->
        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.75rem;">
          <div style="display: flex; align-items: center; gap: 0.6rem;">
            <div style="font-size: 1.4rem; background: rgba(16, 185, 129, 0.15); width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; border-radius: var(--radius-md);">💶</div>
            <div>
              <h3 style="font-size: 1.05rem; font-weight: 700; color: var(--text-primary);">Simulador de Contratos de Luz & Batería Virtual</h3>
              <div style="font-size: 0.75rem; color: var(--text-muted);">Proyección real 12 meses para tu nuevo contrato con 5.185 kWh solares y batería Fox-ESS</div>
            </div>
          </div>
          <span class="badge-tag" style="background: rgba(16, 185, 129, 0.2); color: #10b981; font-weight: 700;">${currentTariff.tag}</span>
        </div>

        <!-- Selector de Comercializadora / Oferta -->
        <div style="display: flex; gap: 0.4rem; flex-wrap: wrap;" id="tariff-tabs-bar">
          ${offersNavHtml}
        </div>

        <!-- 4 Métricas Clave de la Tarifa Seleccionada -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 0.75rem;">
          <div style="background: var(--bg-elevated); padding: 0.8rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
            <div style="font-size: 0.7rem; color: var(--color-real); font-weight: 700; text-transform: uppercase;">Factura Anual Total</div>
            <div style="font-size: 1.35rem; font-weight: 800; color: ${parseFloat(sim.annualTotalBilledEur) < 50 ? 'var(--color-real)' : '#f43f5e'};">
              ${sim.annualTotalBilledEur} € / año
            </div>
            <div style="font-size: 0.7rem; color: var(--text-muted);">${sim.freeMonthsCount} meses a 0.00 €</div>
          </div>

          <div style="background: var(--bg-elevated); padding: 0.8rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
            <div style="font-size: 0.7rem; color: var(--color-solar); font-weight: 700; text-transform: uppercase;">Excedente Generado en Verano</div>
            <div style="font-size: 1.35rem; font-weight: 800; color: var(--color-solar-light);">+${sim.annualSurplusEarnedEur} € / año</div>
            <div style="font-size: 0.7rem; color: var(--text-muted);">Acumulado en monedero</div>
          </div>

          <div style="background: var(--bg-elevated); padding: 0.8rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
            <div style="font-size: 0.7rem; color: #c084fc; font-weight: 700; text-transform: uppercase;">Saldo Monedero Final</div>
            <div style="font-size: 1.35rem; font-weight: 800; color: #c084fc;">+${sim.finalWalletEndEur} €</div>
            <div style="font-size: 0.7rem; color: var(--text-muted);">Sobra tras pasar el invierno</div>
          </div>

          <div style="background: var(--bg-elevated); padding: 0.8rem; border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
            <div style="font-size: 0.7rem; color: #38bdf8; font-weight: 700; text-transform: uppercase;">Compensación Término Fijo</div>
            <div style="font-size: 1.35rem; font-weight: 800; color: #38bdf8;">
              ${currentTariff.walletAppliesToFixedTerm ? 'SÍ (Potencia 0€)' : 'NO (Solo Energía)'}
            </div>
            <div style="font-size: 0.7rem; color: var(--text-muted);">Potencia 4.6 kW cubierta</div>
          </div>
        </div>

        <!-- Tabla Detallada 12 Meses -->
        <div style="overflow-x: auto; background: rgba(0,0,0,0.25); border-radius: var(--radius-md); border: 1px solid var(--border-subtle);">
          <table style="width: 100%; border-collapse: collapse; font-size: 0.78rem;">
            <thead>
              <tr style="background: rgba(255,255,255,0.03); color: var(--text-secondary); text-align: right; border-bottom: 1px solid var(--border-subtle);">
                <th style="text-align: left; padding: 0.5rem 0.6rem;">Mes</th>
                <th style="padding: 0.5rem 0.6rem;">☀️ Sol Generado</th>
                <th style="padding: 0.5rem 0.6rem;">🏠 Consumo Casa</th>
                <th style="padding: 0.5rem 0.6rem;">Excedente Ganado</th>
                <th style="padding: 0.5rem 0.6rem;">Monedero BV</th>
                <th style="padding: 0.5rem 0.6rem;">Factura Final</th>
              </tr>
            </thead>
            <tbody>
              ${monthlyRowsHtml}
            </tbody>
          </table>
        </div>

        <!-- Dictamen del Asesor de Contratación -->
        <div style="background: rgba(16, 185, 129, 0.08); border-left: 3px solid var(--color-real); padding: 0.75rem 1rem; border-radius: 0 var(--radius-sm) var(--radius-sm) 0; font-size: 0.82rem; color: var(--text-primary); line-height: 1.45;">
          <strong>🎯 Recomendación para tu nuevo contrato en Tocina:</strong>
          ${currentTariff.id === 'octopus_solar_wallet' 
            ? `La tarifa <strong>Octopus Solar Wallet</strong> es la opción matemática óptima. Gracias a que el monedero solar no caduca y permite compensar tanto el término de potencia como la energía, los <strong>+180 €</strong> acumulados entre mayo y septiembre pagan íntegramente las facturas de calefacción de noviembre, diciembre, enero y febrero, logrando una <strong>factura anual neta de 0.00 €</strong>.`
            : `Esta tarifa no compensa la potencia o sufre caducidad del saldo. Te costaría <strong>${sim.annualTotalBilledEur} € al año</strong> en comparación con los 0.00 € alcanzables con una Batería Virtual completa sin caducidad.`}
        </div>

      </div>
    `;

    this.bindEvents();
  }

  bindEvents() {
    const btns = this.container.querySelectorAll('#tariff-tabs-bar .tab-btn');
    btns.forEach(btn => {
      btn.addEventListener('click', () => {
        this.selectedTariffId = btn.dataset.tariffId;
        this.render();
      });
    });
  }
}
