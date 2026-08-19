/**
 * Simulador Financiero de Batería Virtual (Cloud Battery) & Facturación Anual
 * Basado en las 5 facturas reales del usuario en Tocina + Generación solar Este-Oeste
 */

export class VirtualBatteryManager {
  constructor(config = {}) {
    this.config = {
      feedInPriceEurKwh: config.feedInPriceEurKwh || 0.072600, // Compensación excedentes Naturgy (0.06 €/kWh + IVA)
      gridImportPriceEurKwh: config.gridImportPriceEurKwh || 0.093991, // Compra Valle Naturgy Noche Luz (con imp)
      fixedPowerCostMonthEur: config.fixedPowerCostMonthEur || 32.40, // Término potencia 4.6 kW (P1+P2) + bono social
      ivaRate: 0.21,
      ieeRate: 0.051127
    };

    // Perfil mensual de consumo histórico (kWh/mes) derivado de las 5 facturas reales
    this.historicalMonthlyHomeKwh = [
      715.99, // Ene (Ola de frío)
      588.04, // Feb
      405.99, // Mar
      380.00, // Abr
      390.00, // May
      460.00, // Jun (Comienzo A/C)
      580.00, // Jul (Pico A/C Sevilla)
      590.00, // Ago (Pico A/C Sevilla)
      450.00, // Sep
      390.00, // Oct
      480.00, // Nov
      650.00  // Dic (Invierno)
    ];

    // Generación solar mensual esperada (kWh/mes) para 5.00 kWp (10x Jinko 500W) en Tocina
    this.expectedMonthlySolarKwh = [
      460, // Ene
      540, // Feb
      720, // Mar
      820, // Abr
      920, // May
      960, // Jun
      990, // Jul (Máximo)
      950, // Ago
      810, // Sep
      650, // Oct
      480, // Nov
      410  // Dic
    ];
  }

  /**
   * Simula los 12 meses del año con Batería Virtual
   * @param {number} evMonthlyKwh - Consumo mensual adicional del Omoda 7 SHS (~250-320 kWh/mes)
   */
  simulateAnnualBalance(evMonthlyKwh = 280) {
    let virtualWalletBalanceEur = 0; // Monedero acumulado
    const months = [
      'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
      'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
    ];

    const monthlyBreakdown = [];
    let totalSavingsYearEur = 0;
    let totalOldBillsYearEur = 0;
    let totalNewBillsYearEur = 0;

    for (let m = 0; m < 12; m++) {
      const homeConsumptionKwh = this.historicalMonthlyHomeKwh[m];
      const evConsumptionKwh = evMonthlyKwh;
      const totalDemandKwh = homeConsumptionKwh + evConsumptionKwh;

      const solarGenerationKwh = this.expectedMonthlySolarKwh[m];

      // Estimación de autoconsumo directo + batería Fox-ESS 10 kWh (85% en verano, 70% en invierno)
      const selfConsumptionRate = (m >= 3 && m <= 8) ? 0.88 : 0.72;
      const selfConsumedKwh = Math.min(totalDemandKwh, solarGenerationKwh * selfConsumptionRate);
      
      const gridImportKwh = Math.max(0, totalDemandKwh - selfConsumedKwh);
      const gridExportKwh = Math.max(0, solarGenerationKwh - selfConsumedKwh);

      // Factura antigua (sin solar ni coche en eléctrico)
      const oldEnergyCost = homeConsumptionKwh * this.config.gridImportPriceEurKwh;
      const oldBillPreTax = oldEnergyCost + this.config.fixedPowerCostMonthEur;
      const oldBillEur = oldBillPreTax * (1 + this.config.ieeRate) * (1 + this.config.ivaRate);
      totalOldBillsYearEur += oldBillEur;

      // Factura nueva con Batería Virtual
      const importCostEur = gridImportKwh * this.config.gridImportPriceEurKwh;
      const exportCreditEur = gridExportKwh * this.config.feedInPriceEurKwh;

      const rawBillBeforeWallet = (importCostEur + this.config.fixedPowerCostMonthEur) * (1 + this.config.ieeRate) * (1 + this.config.ivaRate);

      // Aplicar saldo de Batería Virtual y nuevo crédito
      virtualWalletBalanceEur += exportCreditEur;

      let billToPayEur = rawBillBeforeWallet;
      if (virtualWalletBalanceEur >= rawBillBeforeWallet) {
        virtualWalletBalanceEur -= rawBillBeforeWallet;
        billToPayEur = 0.00; // ¡Factura a 0 € gracias a la batería virtual!
      } else {
        billToPayEur -= virtualWalletBalanceEur;
        virtualWalletBalanceEur = 0;
      }

      totalNewBillsYearEur += billToPayEur;
      const gasolineSavingsEur = (evConsumptionKwh / 17.5) * 7.5 * 1.60; // 17.5 kWh/100km, 7.5 l/100km @ 1.60 €/l
      const monthTotalSavings = (oldBillEur - billToPayEur) + gasolineSavingsEur;
      totalSavingsYearEur += monthTotalSavings;

      monthlyBreakdown.push({
        month: months[m],
        solarKwh: solarGenerationKwh,
        homeKwh: homeConsumptionKwh,
        evKwh: evConsumptionKwh,
        gridImportKwh,
        gridExportKwh,
        oldBillEur,
        billToPayEur,
        virtualWalletBalanceEur,
        gasolineSavingsEur,
        netSavingsEur: monthTotalSavings
      });
    }

    return {
      monthlyBreakdown,
      annualSummary: {
        totalOldBillsEur: totalOldBillsYearEur,
        totalNewBillsEur: totalNewBillsYearEur,
        totalElectricitySavingsEur: totalOldBillsYearEur - totalNewBillsYearEur,
        totalGasolineSavingsEur: (evMonthlyKwh * 12 / 17.5) * 7.5 * 1.60,
        totalNetAnnualSavingsEur: totalSavingsYearEur,
        finalVirtualWalletBalanceEur: virtualWalletBalanceEur
      }
    };
  }
}
